package com.hjq.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.hjq.demo.R;
import com.hjq.demo.bean.HotelInfo;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.activity.HotelDetailActivity;
import com.hjq.demo.ui.adapter.BaseRecycleAdapter;
import com.hjq.demo.ui.adapter.HotelViewHolder;
import com.hjq.demo.ui.adapter.OnItemClickListener;
import com.hjq.demo.util.SpCacheHelper;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目自定义控件展示
 */
public class TestFragmentB extends MyLazyFragment {

  @BindView(R.id.rv_hot)
  RecyclerView rvHot;
  @BindView(R.id.srf_hot)
  SmartRefreshLayout srfHot;
  private BaseRecycleAdapter<HotelInfo, HotelViewHolder> adapter;
  private int page = 0;

  public static TestFragmentB newInstance() {
    return new TestFragmentB();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_test_b;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_test_b_title;
  }

  @Override
  public boolean isStatusBarEnabled() {
    // 使用沉浸式状态栏
    return !super.isStatusBarEnabled();
  }

  @Override
  protected void initView() {
    adapter = new BaseRecycleAdapter<>(R.layout.item_hotel,HotelViewHolder.class);
    rvHot.setLayoutManager(new LinearLayoutManager(getContext()));
    rvHot.setAdapter(adapter);
    srfHot.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 0;
        fetHotData();
        srfHot.setEnableLoadMore(true);
      }
    });

    srfHot.setOnLoadMoreListener(new OnLoadMoreListener() {
      @Override
      public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        fetHotData();
      }
    });
    adapter.setOnItemClickListener(new OnItemClickListener<HotelInfo>() {
      @Override
      public void onItemClick(HotelInfo itemData, View view, int position) {
        HotelDetailActivity.start(getContext(),itemData);
      }
    });

  }

  private void fetHotData() {
    BmobQuery<HotelInfo> query = new BmobQuery<>();
    query.addWhereEqualTo("city", SpCacheHelper.getString("city"));
    query.addWhereGreaterThanOrEqualTo("score",4.8);
    query.setLimit(20);
    query.setSkip(20*page);
    query.findObjects(new FindListener<HotelInfo>() {
      @Override
      public void done(List<HotelInfo> list, BmobException e) {
        if (e == null){
          if (list.size() < 20){
            srfHot.setEnableLoadMore(false);
          }
          if (page == 0){
            adapter.setDataList(list);
          }else {
            adapter.appendDataToList(list);
          }
          page ++;

        }else {
          ToastUtils.show(e.getMessage());
        }
        if (srfHot.getState() == RefreshState.Refreshing){
          srfHot.finishRefresh();
        }else if (srfHot.getState() == RefreshState.Loading){
          srfHot.finishLoadMore();
        }
      }
    });
  }

  @Override
  protected void initData() {
    srfHot.autoRefresh();
  }

}