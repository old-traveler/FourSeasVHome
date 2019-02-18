package com.hjq.demo.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.HouseInfo;
import com.hjq.demo.bean.Order;
import com.hjq.demo.bean.User;
import com.hjq.demo.common.MyActivity;
import com.hjq.dialog.MessageDialog;
import com.hjq.dialog.PayPasswordDialog;
import com.hjq.dialog.ToastDialog;
import com.hjq.dialog.WaitDialog;
import com.hjq.toast.ToastUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.icu.text.DateTimePatternGenerator.DAY;

public class HouseDetailActivity extends MyActivity {

  private int distanceDay;

  @BindView(R.id.tb_house)
  TitleBar tbHouse;
  @BindView(R.id.vr_panorama)
  VrPanoramaView vrPanorama;
  @BindView(R.id.tv_number)
  TextView tvNumber;
  @BindView(R.id.tv_live_count)
  TextView tvLiveCount;
  @BindView(R.id.tv_area)
  TextView tvArea;
  @BindView(R.id.tv_live_time)
  TextView tvLiveTime;
  @BindView(R.id.tv_price)
  TextView tvPrice;
  @BindView(R.id.btn_reserve)
  Button btnReserve;

  private HouseInfo info;

  public static void start(Context context, HouseInfo info) {
    Intent intent = new Intent(context, HouseDetailActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("info", info);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_house_detail;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_house;
  }

  @Override
  protected void initView() {

  }

  @Override
  protected void initData() {
    info = (HouseInfo) getIntent().getExtras().getSerializable("info");
    tvArea.setText(String.format("面积：%d平米", info.getArea()));
    tvLiveCount.setText(String.format("可住人数：%d人", info.getPeopleCount()));
    tvNumber.setText(String.format("剩余间数：%d间", info.getNumber()));
    tbHouse.setTitle(info.getName());
    final BaseDialog dialog = new WaitDialog.Builder(this)
        .setMessage("加载中...") // 消息文本可以不用填写
        .show();
    Glide.with(this)
        .asBitmap()
        .load(info.getPanoramaImage())
        .into(new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(@NonNull Bitmap resource,
              @Nullable Transition<? super Bitmap> transition) {
            VrPanoramaView.Options options = new VrPanoramaView.Options();
            options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            vrPanorama.loadImageFromBitmap(resource, options);
          }
        });

    vrPanorama.setEventListener(new VrPanoramaEventListener() {

      @Override
      public void onLoadError(String errorMessage) {
        super.onLoadError(errorMessage);
        dialog.dismiss();
        ToastUtils.show("图片加载失败：" + errorMessage);
      }

      @Override
      public void onLoadSuccess() {
        super.onLoadSuccess();
        dialog.dismiss();
      }
    });
  }


  @OnClick({ R.id.tv_live_time, R.id.btn_reserve })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_live_time:
        showDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
            showDatePickerDialog(HouseDetailActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                  @Override
                  public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth1) {
                    String time = String.format("%d-%02d-%02d～%d-%02d-%02d",year,month+1,dayOfMonth,year1,month1+1,dayOfMonth1);
                    tvLiveTime.setText(time);
                    String[] timeA = time.split("～");
                    Date date = stringToDate(timeA[0],"yyyy-MM-dd");
                    Date endDate = stringToDate(timeA[1],"yyyy-MM-dd");

                    distanceDay = (int) ((endDate.getTime() - date.getTime()) / (24*60*60*1000L));
                  }
                });
          }
        });
        break;
      case R.id.btn_reserve:
        if (distanceDay < 1){
          ToastUtils.show("至少预定一天");
        }else {
          new MessageDialog.Builder(this)
              .setTitle("提示") // 标题可以不用填写
              .setMessage("您将预定一间"+info.getName()+" 共"+distanceDay+"天时间")
              .setConfirm("确定")
              .setCancel("取消") // 设置 null 表示不显示取消按钮
              //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
              .setListener(new MessageDialog.OnListener() {

                @Override
                public void confirm(Dialog dialog) {
                  payTheHouse();
                }

                @Override
                public void cancel(Dialog dialog) {

                }
              })
              .show();
        }
        break;
    }
  }

  private void payTheHouse() {
    new PayPasswordDialog.Builder(this)
        .setTitle("请输入支付密码")
        .setSubTitle("预定房间")
        .setMoney(String.format("￥ %d.00",info.getPrice()*distanceDay))
        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
        .setListener(new PayPasswordDialog.OnListener() {

          @Override
          public void complete(Dialog dialog, String password) {
            if (password.equals("123456")){
              final BaseDialog baseDialog = new WaitDialog.Builder(HouseDetailActivity.this)
                  .setMessage("加载中...") // 消息文本可以不用填写
                  .show();
              final Order order = new Order();
              order.setHouseInfo(info);
              order.setName(info.getName());
              order.setPrice(info.getPrice()*distanceDay);
              order.setTime(tvLiveTime.getText().toString());
              order.setUser(User.getCurrentUser(User.class));
              order.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                  baseDialog.dismiss();
                  if (e == null){
                    OrderDetailActivity.start(HouseDetailActivity.this,order);
                    finish();
                  }else {
                    ToastUtils.show(e.getMessage());
                  }
                }
              });
            }else{
              new ToastDialog.Builder(HouseDetailActivity.this)
                  .setType(ToastDialog.Type.ERROR)
                  .setMessage("密码错误")
                  .show();
            }
          }

          @Override
          public void cancel(Dialog dialog) {

          }
        })
        .show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  public static void showDatePickerDialog(Context context,
      DatePickerDialog.OnDateSetListener listener) {
    Calendar calendar = Calendar.getInstance();
    new DatePickerDialog(context
        , listener
        , calendar.get(Calendar.YEAR)
        , calendar.get(Calendar.MONTH)
        , calendar.get(Calendar.DAY_OF_MONTH)).show();
  }

  public static Date stringToDate(String strTime, String formatType) {
    SimpleDateFormat formatter = new SimpleDateFormat(formatType);
    Date date = null;
    try {
      date = formatter.parse(strTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }


}
