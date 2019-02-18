package com.hjq.demo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.hjq.demo.R;
import com.hjq.demo.bean.HouseInfo;

public class HouseViewHolder extends BaseViewHolder<HouseInfo> {

  @BindView(R.id.iv_hotel)
  ImageView ivHouse;
  @BindView(R.id.tv_hotel_title)
  TextView tvHouseTitle;
  @BindView(R.id.tv_score)
  TextView tvNumber;
  @BindView(R.id.tv_type)
  TextView tvPeopleCount;
  @BindView(R.id.tv_price)
  TextView tvPrice;
  @BindView(R.id.tv_address)
  TextView tvArea;

  public HouseViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this,view);
  }

  @Override
  public void loadItemData(Context context, HouseInfo data, int position) {
    Glide.with(context)
        .load(data.getInfoImage())
        .into(ivHouse);
    tvHouseTitle.setText(data.getName());
    tvNumber.setText(String.format("仅剩%d间",data.getNumber()));
    tvPeopleCount.setText(String.format("可住%d人",data.getPeopleCount()));
    tvPrice.setText(String.format("¥ %d",data.getPrice()));
    tvArea.setText(String.format("房间面积：%d平米",data.getArea()));
  }
}
