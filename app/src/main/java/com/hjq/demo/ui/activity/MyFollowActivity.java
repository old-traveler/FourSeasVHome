package com.hjq.demo.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.bean.HotelCacheInfo;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.helper.DaoHelper;
import com.hjq.demo.ui.adapter.BaseRecycleAdapter;
import com.hjq.demo.ui.adapter.HotelCacheViewHolder;
import com.hjq.demo.ui.adapter.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

public class MyFollowActivity extends MyActivity {

  @BindView(R.id.tb_follow)
  TitleBar tbFollow;
  @BindView(R.id.rv_follow)
  RecyclerView rvFollow;
  @BindView(R.id.srf_follow)
  SmartRefreshLayout srfFollow;
  private BaseRecycleAdapter<HotelCacheInfo, HotelCacheViewHolder> adapter;
  private int page = 0;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_my_follow;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_follow;
  }

  @Override
  protected void initView() {
    rvFollow.setLayoutManager(new LinearLayoutManager(this));
    rvFollow.setAdapter(
        adapter = new BaseRecycleAdapter<>(R.layout.item_hotel, HotelCacheViewHolder.class));
    srfFollow.setOnLoadMoreListener(new OnLoadMoreListener() {
      @Override
      public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        fetchData();
      }
    });
    srfFollow.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 0;
        srfFollow.setEnableLoadMore(true);
        fetchData();
      }
    });
    adapter.setOnItemClickListener(new OnItemClickListener<HotelCacheInfo>() {
      @Override
      public void onItemClick(HotelCacheInfo itemData, View view, int position) {
        HotelDetailActivity.start(MyFollowActivity.this,itemData.toHotelInfo());
      }
    });
  }

  private void fetchData() {
    List<HotelCacheInfo> cacheInfos = DaoHelper.getDefault()
        .getDaoSession()
        .getHotelCacheInfoDao()
        .queryBuilder()
        .limit(20)
        .offset(20 * page)
        .list();
    if (page == 0){
      adapter.setDataList(cacheInfos);
      srfFollow.finishRefresh();
    }else {
      adapter.appendDataToList(cacheInfos);
      srfFollow.finishLoadMore();
    }

    if (cacheInfos == null || cacheInfos.size() < 20){
      srfFollow.setEnableLoadMore(false);
    }
    page++;
  }

  @Override
  protected void initData() {
    srfFollow.autoRefresh();
  }
}
