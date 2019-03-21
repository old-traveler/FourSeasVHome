package com.hjq.demo.ui.fragment;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.HotelInfo;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.activity.AddHotelActivity;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.ui.activity.HotelDetailActivity;
import com.hjq.demo.ui.activity.SearchActivity;
import com.hjq.demo.ui.adapter.BaseRecycleAdapter;
import com.hjq.demo.ui.adapter.HotelViewHolder;
import com.hjq.demo.ui.adapter.OnItemClickListener;
import com.hjq.demo.util.SpCacheHelper;
import com.hjq.demo.widget.XCollapsingToolbarLayout;

import butterknife.BindView;
import com.hjq.dialog.WaitDialog;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import java.util.HashMap;
import java.util.List;

public class TestFragmentA extends MyLazyFragment
    implements XCollapsingToolbarLayout.OnScrimsListener, AMapLocationListener {

  @BindView(R.id.abl_test_bar)
  AppBarLayout mAppBarLayout;
  @BindView(R.id.ctl_test_bar)
  XCollapsingToolbarLayout mCollapsingToolbarLayout;
  @BindView(R.id.t_test_title)
  Toolbar mToolbar;
  @BindView(R.id.tb_test_a_bar)
  TitleBar mTitleBar;
  @BindView(R.id.rv_hotel)
  RecyclerView rvHotel;

  @BindView(R.id.tv_test_address)
  TextView mAddressView;
  @BindView(R.id.tv_test_search)
  TextView mSearchView;
  @BindView(R.id.slider)
  SliderLayout sliderLayout;

  public AMapLocationClient mlocationClient;
  //声明mLocationOption对象
  public AMapLocationClientOption mLocationOption = null;

  public static TestFragmentA newInstance() {
    return new TestFragmentA();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_test_a;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_test_a_bar;
  }

  @Override
  protected void initView() {
    // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
    ImmersionBar.setTitleBar(getFragmentActivity(), mToolbar);
    mSearchView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(SearchActivity.class);
      }
    });
    //设置渐变监听
    mCollapsingToolbarLayout.setOnScrimsListener(this);

    HashMap<String, String> url_maps = new HashMap<String, String>();
    url_maps.put("VR看房 -体验不一样的真实感",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549886413403&di=7b271070b11fece7cc89a7fa9d49d76f&imgtype=0&src=http%3A%2F%2Ff.expoon.com%2Fnews%2F2017%2F04%2F14%2F432024.jpg");
    url_maps.put("舒不舒服，\"看\"的出来",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549886551274&di=0719823f0d0b0e1d3e412138ff5935f6&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180706%2F3b0d7efb118747a2aa22addde4a58c0e.jpeg");
    url_maps.put("住的好，才能玩的开心",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549886676622&di=14d2e0bfbf1c0f5c1e7d04b25922c293&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180912%2F6961cdc508464b0996db16998d77443e.jpeg");
    for (String s : url_maps.keySet()) {
      TextSliderView textSliderView = new TextSliderView(getContext());
      textSliderView
          .description(s)
          .image(url_maps.get(s))
          .setScaleType(BaseSliderView.ScaleType.Fit);

      sliderLayout.addSlider(textSliderView);
    }
    XXPermissions.with(getFragmentActivity())
        .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
        .permission(Permission.ACCESS_FINE_LOCATION) //不指定权限则自动获取清单中的危险权限
        .request(new OnPermission() {

          @Override
          public void hasPermission(List<String> granted, boolean isAll) {
            if (isAll) {
              initLocation();
            } else {
              toast("获取权限成功，部分权限未正常授予");
            }
          }

          @Override
          public void noPermission(List<String> denied, boolean quick) {
            if (quick) {
              toast("被永久拒绝授权，请手动授予权限");
              //如果是被永久拒绝就跳转到应用权限系统设置页面
              XXPermissions.gotoPermissionSettings(getFragmentActivity());
            } else {
              toast("获取权限失败");
            }
          }
        });

    mAddressView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), AddHotelActivity.class));
      }
    });
  }

  private void initLocation() {
    mlocationClient = new AMapLocationClient(getContext());
    //初始化定位参数
    mLocationOption = new AMapLocationClientOption();
    //设置定位监听
    mlocationClient.setLocationListener(this);
    //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    mLocationOption.setOnceLocation(true);
    //设置定位参数
    mlocationClient.setLocationOption(mLocationOption);
    mlocationClient.startLocation();
  }

  @Override
  public void onLocationChanged(AMapLocation amapLocation) {
    if (amapLocation != null) {
      if (amapLocation.getErrorCode() == 0) {
        //定位成功回调信息，设置相关消息
        mAddressView.setText(amapLocation.getCity());
        SpCacheHelper.putString("city", amapLocation.getCity());
        SpCacheHelper.putString("address", amapLocation.getAddress());
        SpCacheHelper.putString("latlon",
            amapLocation.getLatitude() + "-" + amapLocation.getLongitude());
        fetchHomePageData(amapLocation.getCity());
      } else {
        ToastUtils.show("定位失败");
      }
    }
  }

  private void fetchHomePageData(String city) {
    final BaseDialog dialog = new WaitDialog.Builder(getActivity())
        .setMessage("加载中...") // 消息文本可以不用填写
        .show();
    BmobQuery<HotelInfo> query = new BmobQuery<>();
    query.addWhereEqualTo("city", city);
    query.setLimit(50);
    query.findObjects(new FindListener<HotelInfo>() {
      @Override
      public void done(List<HotelInfo> list, BmobException e) {
        dialog.dismiss();
        if (e == null) {
          initHotelInfo(list);
        } else {
          ToastUtils.show(e.getMessage());
        }
      }
    });
  }

  private void initHotelInfo(List<HotelInfo> hotelInfos) {
    rvHotel.setLayoutManager(new LinearLayoutManager(getContext()));
    BaseRecycleAdapter<HotelInfo, HotelViewHolder> adapter =
        new BaseRecycleAdapter<>(hotelInfos, R.layout.item_hotel, HotelViewHolder.class);
    rvHotel.setAdapter(adapter);
    adapter.setOnItemClickListener(new OnItemClickListener<HotelInfo>() {
      @Override
      public void onItemClick(HotelInfo itemData, View view, int position) {
        HotelDetailActivity.start(getActivity(),itemData);
      }
    });
  }

  @Override
  protected void initData() {

  }

  @Override
  public boolean isStatusBarEnabled() {
    // 使用沉浸式状态栏
    return !super.isStatusBarEnabled();
  }

  @Override
  public boolean statusBarDarkFont() {
    return mCollapsingToolbarLayout.isScrimsShown();
  }

  /**
   * {@link XCollapsingToolbarLayout.OnScrimsListener}
   */
  @Override
  public void onScrimsStateChange(boolean shown) {
    // CollapsingToolbarLayout 发生了渐变
    if (shown) {
      mAddressView.setTextColor(getResources().getColor(R.color.black));
      mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_gray);
      getStatusBarConfig().statusBarDarkFont(true).init();
    } else {
      mAddressView.setTextColor(getResources().getColor(R.color.white));
      mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
      getStatusBarConfig().statusBarDarkFont(false).init();
    }
  }
}