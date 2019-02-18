package com.hjq.demo.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.HotelInfo;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.ui.adapter.BaseRecycleAdapter;
import com.hjq.demo.ui.adapter.HotelViewHolder;
import com.hjq.demo.ui.adapter.OnItemClickListener;
import com.hjq.dialog.WaitDialog;
import com.hjq.toast.ToastUtils;
import java.util.List;

public class SearchActivity extends MyActivity {

  @BindView(R.id.et_search_input)
  EditText etSearchInput;
  @BindView(R.id.rv_search)
  RecyclerView rvSearch;
  private BaseRecycleAdapter<HotelInfo, HotelViewHolder> adapter;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_search;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_search;
  }

  @Override
  protected void initView() {
    rvSearch.setLayoutManager(new LinearLayoutManager(this));
    rvSearch.setAdapter(adapter = new BaseRecycleAdapter<>(R.layout.item_hotel,HotelViewHolder.class));
    adapter.setOnItemClickListener(new OnItemClickListener<HotelInfo>() {
      @Override
      public void onItemClick(HotelInfo itemData, View view, int position) {
        HotelDetailActivity.start(getContext(),itemData);
        finish();
      }
    });
  }

  @Override
  public void onRightClick(View v) {
    super.onRightClick(v);
    if (TextUtils.isEmpty(etSearchInput.getText().toString())){
      ToastUtils.show("请先输入关键字");
    }else {
      search();
    }
  }

  private void search(){
    final BaseDialog dialog = new WaitDialog.Builder(this)
        .setMessage("加载中...") // 消息文本可以不用填写
        .show();
    BmobQuery<HotelInfo> query = new BmobQuery<>();
    query.addWhereEqualTo("name",etSearchInput.getText().toString());
    query.findObjects(new FindListener<HotelInfo>() {
      @Override
      public void done(List<HotelInfo> list, BmobException e) {
        dialog.dismiss();
        if (e == null){
          adapter.setDataList(list);
          if (list.size() ==0){
            ToastUtils.show("未有符合条件的旅馆");
          }
        }else {
          ToastUtils.show(e.getMessage());
        }
      }
    });
  }

  @Override
  protected void initData() {
  }


}
