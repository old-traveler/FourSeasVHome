package com.hjq.demo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hjq.demo.R;
import com.hjq.demo.bean.Order;

public class OrderViewHolder extends BaseViewHolder<Order> {
  @BindView(R.id.tv_live_time)
  TextView tvLiveTime;
  @BindView(R.id.tv_order_time)
  TextView tvOrderTime;
  @BindView(R.id.tv_title)
  TextView tvTitle;

  public OrderViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this, view);
  }

  @Override
  public void loadItemData(Context context, Order data, int position) {
    tvLiveTime.setText(data.getTime());
    tvOrderTime.setText(data.getCreatedAt());
    tvTitle.setText(data.getName());
  }
}
