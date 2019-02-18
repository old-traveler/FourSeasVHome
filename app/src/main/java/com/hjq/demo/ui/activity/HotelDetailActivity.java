package com.hjq.demo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.bumptech.glide.Glide;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.HotelCacheInfo;
import com.hjq.demo.bean.HotelInfo;
import com.hjq.demo.bean.HouseInfo;
import com.hjq.demo.bean.MyPoint;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.gen.HotelCacheInfoDao;
import com.hjq.demo.helper.DaoHelper;
import com.hjq.demo.ui.adapter.BaseRecycleAdapter;
import com.hjq.demo.ui.adapter.HouseViewHolder;
import com.hjq.demo.ui.adapter.OnItemClickListener;
import com.hjq.demo.util.UrlUtil;
import com.hjq.demo.widget.XCollapsingToolbarLayout;
import com.hjq.dialog.WaitDialog;
import com.hjq.toast.ToastUtils;
import java.math.BigDecimal;
import java.util.List;

public class HotelDetailActivity extends MyActivity {

  @BindView(R.id.iv_detail)
  ImageView ivDetail;
  @BindView(R.id.t_test_title)
  Toolbar tTestTitle;
  @BindView(R.id.tb_test_a_bar)
  TitleBar tbTestABar;
  @BindView(R.id.ctl_test_bar)
  XCollapsingToolbarLayout ctlTestBar;
  @BindView(R.id.abl_test_bar)
  AppBarLayout ablTestBar;
  @BindView(R.id.tv_score)
  TextView tvScore;
  @BindView(R.id.tv_score_tip)
  TextView tvScoreTip;
  @BindView(R.id.tv_type)
  TextView tvType;
  @BindView(R.id.iv_map)
  ImageView ivMap;
  @BindView(R.id.tv_address)
  TextView tvAddress;
  @BindView(R.id.tv_phone)
  TextView tvPhone;
  @BindView(R.id.tv_desc)
  TextView tvDesc;
  @BindView(R.id.rv_house)
  RecyclerView rvHouse;
  private HotelInfo hotelInfo;
  private BaseRecycleAdapter<HouseInfo, HouseViewHolder> adapter;
  private HotelCacheInfo info = null;

  /** π */
  private static double PI = 3.1415926535897932384626;
  /** 参数 */
  private static double X_PI = 3.14159265358979324 * 3000.0 / 180.0;

  public static void start(Context context, HotelInfo hotelInfo) {
    Intent intent = new Intent(context, HotelDetailActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("info", hotelInfo);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  public boolean isStatusBarEnabled() {
    return !super.isStatusBarEnabled();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_hotel_detail;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_test_a_bar;
  }

  @Override
  protected void initView() {
    rvHouse.setLayoutManager(new LinearLayoutManager(this));
    rvHouse.setAdapter(
        adapter = new BaseRecycleAdapter<>(R.layout.item_house_info, HouseViewHolder.class));
    adapter.setOnItemClickListener(new OnItemClickListener<HouseInfo>() {
      @Override
      public void onItemClick(HouseInfo itemData, View view, int position) {
        HouseDetailActivity.start(HotelDetailActivity.this, itemData);
      }
    });
    tbTestABar.getRightView().setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        PublishActivity.start(HotelDetailActivity.this, hotelInfo.getObjectId());
        return true;
      }
    });
    tbTestABar.setOnTitleBarListener(new OnTitleBarListener() {
      @Override
      public void onLeftClick(View v) {
        finish();
      }

      @Override
      public void onTitleClick(View v) {

      }

      @Override
      public void onRightClick(View v) {
        if (info != null){
          DaoHelper.getDefault().getDaoSession().getHotelCacheInfoDao().delete(info);
          info = null;
        }else {
          info = new HotelCacheInfo(hotelInfo);
          DaoHelper.getDefault().getDaoSession().getHotelCacheInfoDao().insert(info);
        }
        tbTestABar.setRightIcon(info == null ? R.mipmap.ic_unfollow : R.mipmap.ic_followed);
      }
    });
    tbTestABar.getTitleView().setTextColor(getResources().getColor(R.color.white));
    tbTestABar.setLeftIcon(R.mipmap.bar_icon_back_white);
    ctlTestBar.setOnScrimsListener(new XCollapsingToolbarLayout.OnScrimsListener() {
      @Override
      public void onScrimsStateChange(boolean shown) {
        if (shown) {
          tbTestABar.setLeftIcon(R.mipmap.bar_icon_back_black);
          tbTestABar.getTitleView().setTextColor(getResources().getColor(R.color.black));
        } else {
          tbTestABar.setLeftIcon(R.mipmap.bar_icon_back_white);
          tbTestABar.getTitleView().setTextColor(getResources().getColor(R.color.white));
        }
      }
    });
  }

  @Override
  protected void initData() {
    hotelInfo = (HotelInfo) getIntent().getExtras().getSerializable("info");
    info = DaoHelper.getDefault()
        .getDaoSession()
        .getHotelCacheInfoDao()
        .queryBuilder()
        .where(HotelCacheInfoDao.Properties.ObjectId.eq(hotelInfo.getObjectId()))
        .unique();

    tbTestABar.setRightIcon(info == null ? R.mipmap.ic_unfollow : R.mipmap.ic_followed);

    Glide.with(this)
        .load(hotelInfo.getImage())
        .into(ivDetail);
    tvType.setText(hotelInfo.getType());
    tvAddress.setText("地址：" + hotelInfo.getAddress());
    tvDesc.setText("介绍：" + hotelInfo.getDesc());
    tvPhone.setText("电话：" + hotelInfo.getContact());
    String score = "<font><big>" + hotelInfo.getScore() + "</big></font> 分";
    tvScore.setText(Html.fromHtml(score));
    String[] latlon = hotelInfo.getLatlon().split("-");
    MyPoint baiduPoint =
        bd_encrypt(new MyPoint(Double.parseDouble(latlon[0]), Double.parseDouble(latlon[1])));
    Glide.with(this)
        .load(UrlUtil.getUriNewStaticMap(baiduPoint.getLat(), baiduPoint.getLon(), 800, 400))
        .into(ivMap);
    tbTestABar.setTitle(hotelInfo.getName());
    fetchHouseData();
  }

  private void fetchHouseData() {
    final BaseDialog dialog = new WaitDialog.Builder(this)
        .setMessage("加载中...") // 消息文本可以不用填写
        .show();
    BmobQuery<HouseInfo> query = new BmobQuery<>();
    query.addWhereEqualTo("belongTo", hotelInfo.getObjectId());
    query.findObjects(new FindListener<HouseInfo>() {
      @Override
      public void done(List<HouseInfo> list, BmobException e) {
        dialog.dismiss();
        if (e == null) {
          adapter.setDataList(list);
        } else {
          ToastUtils.show(e.getMessage());
        }
      }
    });
  }

  public static MyPoint bd_encrypt(MyPoint lngLat_gd) {
    double x = lngLat_gd.getLon(), y = lngLat_gd.getLat();
    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
    return new MyPoint(dataDigit(6, z * Math.cos(theta) + 0.0065),
        dataDigit(6, z * Math.sin(theta) + 0.006));
  }

  public static double dataDigit(int digit, double in) {
    return new BigDecimal(in).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
  }
}
