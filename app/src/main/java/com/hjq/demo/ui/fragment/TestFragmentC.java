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
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.bean.Order;
import com.hjq.demo.bean.User;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.activity.OrderDetailActivity;
import com.hjq.demo.ui.adapter.BaseRecycleAdapter;
import com.hjq.demo.ui.adapter.OnItemClickListener;
import com.hjq.demo.ui.adapter.OrderViewHolder;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

public class TestFragmentC extends MyLazyFragment {

  @BindView(R.id.tb_order)
  TitleBar tbOrder;
  @BindView(R.id.rv_order)
  RecyclerView rvOrder;
  @BindView(R.id.srf_order)
  SmartRefreshLayout srfOrder;
  private int page = 0;

  private BaseRecycleAdapter<Order, OrderViewHolder> adapter;

  public static TestFragmentC newInstance() {
    return new TestFragmentC();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_test_c;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_order;
  }

  @Override
  protected void initView() {
    rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
    rvOrder.setAdapter(adapter = new BaseRecycleAdapter<>(R.layout.item_order,OrderViewHolder.class));
    srfOrder.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 0;
        fetchOrderData();
        srfOrder.setEnableLoadMore(true);
      }
    });

    srfOrder.setOnLoadMoreListener(new OnLoadMoreListener() {
      @Override
      public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        fetchOrderData();
      }
    });
    adapter.setOnItemClickListener(new OnItemClickListener<Order>() {
      @Override
      public void onItemClick(Order itemData, View view, int position) {
        OrderDetailActivity.start(getActivity(),itemData);
      }
    });

  }

  private void fetchOrderData() {
    BmobQuery<Order> query = new BmobQuery<>();
    query.addWhereEqualTo("user", User.getCurrentUser(User.class));
    query.setLimit(20);
    query.setSkip(page * 20);
    query.findObjects(new FindListener<Order>() {
      @Override
      public void done(List<Order> list, BmobException e) {
        if (e == null){
          if (page == 0){
            adapter.setDataList(list);
          }else {
            adapter.appendDataToList(list);
          }
          if (list.size() < 20){
            srfOrder.setEnableLoadMore(false);
          }
          page++;
        }else {
          ToastUtils.show(e.getMessage());
        }
        if (srfOrder.getState().equals(RefreshState.Refreshing)){
          srfOrder.finishRefresh();
        }else if (srfOrder.getState().equals(RefreshState.Loading)){
          srfOrder.finishLoadMore();
        }
      }
    });


  }

  @Override
  protected void initData() {
    srfOrder.autoRefresh();
  }

  @Override
  public boolean isStatusBarEnabled() {
    // 使用沉浸式状态栏
    return !super.isStatusBarEnabled();
  }



}