package com.hjq.demo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.bean.HouseInfo;
import com.hjq.demo.bean.Order;
import com.hjq.demo.common.MyActivity;
import com.hjq.toast.ToastUtils;

public class OrderDetailActivity extends MyActivity {

  @BindView(R.id.tb_order)
  TitleBar tbOrder;
  @BindView(R.id.tv_house_name)
  TextView tvHouseName;
  @BindView(R.id.tv_pay_price)
  TextView tvPayPrice;
  @BindView(R.id.tv_time)
  TextView tvTime;
  @BindView(R.id.tv_order_time)
  TextView tvOrderTime;

  private Order order;

  public static void start(Context context, Order order){
    Intent intent = new Intent(context,OrderDetailActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("order",order);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_order_detail;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_order;
  }

  @Override
  protected void initView() {

  }

  @Override
  protected void initData() {
    order = (Order) getIntent().getExtras().getSerializable("order");
    tvHouseName.setText("预定房间："+order.getName());
    tvOrderTime.setText("下单时间："+order.getCreatedAt());
    tvTime.setText("入住时间："+order.getTime());
    tvPayPrice.setText("支付金额："+order.getPrice());
    tvHouseName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BmobQuery<HouseInfo> query = new BmobQuery<>();
        query.getObject(order.getHouseInfo().getObjectId(), new QueryListener<HouseInfo>() {
          @Override
          public void done(HouseInfo info, BmobException e) {
            if (e== null && info!=null){
              HouseDetailActivity.start(OrderDetailActivity.this,info);
            }else {
              ToastUtils.show(e.getMessage());
            }
          }
        });
      }
    });
  }


}
