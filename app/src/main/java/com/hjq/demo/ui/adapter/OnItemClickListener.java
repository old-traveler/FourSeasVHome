package com.hjq.demo.ui.adapter;

import android.view.View;

public interface OnItemClickListener<D> {

  void onItemClick(D itemData, View view, int position);

}
