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
import com.hjq.demo.bean.HotelCacheInfo;
import com.hjq.demo.bean.HotelInfo;

public class HotelCacheViewHolder extends BaseViewHolder<HotelCacheInfo> {
  @BindView(R.id.iv_hotel)
  ImageView ivHotel;
  @BindView(R.id.tv_hotel_title)
  TextView tvHotelTitle;
  @BindView(R.id.tv_score)
  TextView tvScore;
  @BindView(R.id.tv_type)
  TextView tvType;
  @BindView(R.id.tv_price)
  TextView tvPrice;
  @BindView(R.id.tv_address)
  TextView tvAddress;

  public HotelCacheViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this,view);
  }

  @Override
  public void loadItemData(Context context, HotelCacheInfo data, int position) {
    Glide.with(context)
        .load(data.getImage())
        .into(ivHotel);
    tvHotelTitle.setText(data.getName());
    tvScore.setText(data.getScore()+"分");
    tvType.setText(data.getType());
    tvPrice.setText(data.getPrice());
    tvAddress.setText(data.getAddress());
  }
}

