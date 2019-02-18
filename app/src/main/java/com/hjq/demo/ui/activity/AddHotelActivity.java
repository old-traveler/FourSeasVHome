package com.hjq.demo.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.bumptech.glide.Glide;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.HotelInfo;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.util.SpCacheHelper;
import com.hjq.dialog.MenuDialog;
import com.hjq.dialog.ToastDialog;
import com.hjq.dialog.WaitDialog;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddHotelActivity extends MyActivity {

  public static final int GALLERY_REQUSET_CODE = 100;
  public static final int CROP_REQUEST_CODE = 101;
  @BindView(R.id.tb_title)
  TitleBar titleBar;
  @BindView(R.id.iv_hotel)
  ImageView ivHotel;
  @BindView(R.id.tv_add_image)
  TextView tvAddImage;
  @BindView(R.id.et_name)
  EditText etName;
  @BindView(R.id.et_contract)
  EditText etContract;
  @BindView(R.id.et_detail)
  EditText etDetail;
  @BindView(R.id.tv_type)
  TextView tvType;
  @BindView(R.id.tv_address)
  TextView tvAddress;

  private String headImageFilePath;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_add_hotel;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_title;
  }

  @Override
  protected void initView() {
    titleBar.setRightTitle("发布");
    titleBar.setOnTitleBarListener(new OnTitleBarListener() {
      @Override
      public void onLeftClick(View v) {
        finish();
      }

      @Override
      public void onTitleClick(View v) {

      }

      @Override
      public void onRightClick(View v) {
        addHotel();

      }
    });
    tvAddress.setText(SpCacheHelper.getString("address"));
  }

  private void addHotel() {
    if (TextUtils.isEmpty(headImageFilePath)){
      ToastUtils.show("请先选择照片");
    }else if (TextUtils.isEmpty(etName.getText().toString())){
      ToastUtils.show("填写名称");
    }else if (TextUtils.isEmpty(etContract.getText().toString())){
      ToastUtils.show("电话");
    }else if (TextUtils.isEmpty(etDetail.getText().toString())){
      ToastUtils.show("介绍");
    }else if (tvType.getText().toString().length() > 3){
      ToastUtils.show("类型");
    }else {
      final BmobFile bmobFile = new BmobFile(new File(headImageFilePath));
      final BaseDialog dialog = new WaitDialog.Builder(this)
          .setMessage("加载中...") // 消息文本可以不用填写
          .show();
      bmobFile.upload(new UploadFileListener() {
        @Override
        public void done(BmobException e) {
          if (e == null){
            HotelInfo hotelInfo = new HotelInfo();
            hotelInfo.setAddress(tvAddress.getText().toString());
            hotelInfo.setImage(bmobFile.getFileUrl());
            hotelInfo.setCity(SpCacheHelper.getString("city"));
            hotelInfo.setContact(etContract.getText().toString());
            hotelInfo.setDesc(etDetail.getText().toString());
            hotelInfo.setType(tvType.getText().toString());
            hotelInfo.setName(etName.getText().toString());
            hotelInfo.setPrice("¥ 99 - 280");
            hotelInfo.setLatlon(SpCacheHelper.getString("latlon"));
            hotelInfo.save(new SaveListener<String>() {
              @Override
              public void done(String s, BmobException e) {
                dialog.dismiss();
                if (e != null){
                  ToastUtils.show(e.getMessage());
                }else {
                  new ToastDialog.Builder(AddHotelActivity.this)
                      .setType(ToastDialog.Type.FINISH)
                      .setMessage("完成")
                      .show();
                }
              }
            });
          }else {
            dialog.dismiss();
            ToastUtils.show(e.getMessage());
            Log.d("hyc",e.getMessage());
          }
        }
      });
    }

  }

  @Override
  protected void initData() {

  }

  @OnClick({R.id.iv_hotel,R.id.tv_type})
  public void onViewClicked(View view) {
    switch (view.getId()){
      case R.id.iv_hotel:
        XXPermissions.with(this)
            .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
            .permission(Permission.CAMERA,Permission.WRITE_EXTERNAL_STORAGE,Permission.READ_EXTERNAL_STORAGE) //不指定权限则自动获取清单中的危险权限
            .request(new OnPermission() {

              @Override
              public void hasPermission(List<String> granted, boolean isAll) {
                if (isAll) {
                  routeToGallery();
                }else {
                  toast("获取权限成功，部分权限未正常授予");
                }
              }

              @Override
              public void noPermission(List<String> denied, boolean quick) {
                if(quick) {
                  toast("被永久拒绝授权，请手动授予权限");
                  //如果是被永久拒绝就跳转到应用权限系统设置页面
                  XXPermissions.gotoPermissionSettings(getContext());
                }else {
                  toast("获取权限失败");
                }
              }
            });
        break;

      case R.id.tv_type:
        List<String> data = new ArrayList<>();
        data.add("经济型");
        data.add("豪华型");
        data.add("舒适型");
        data.add("居家型");
        new MenuDialog.Builder(this)
            .setCancel(null) // 设置 null 表示不显示取消按钮
            //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
            .setList(data)
            .setListener(new MenuDialog.OnListener() {

              @Override
              public void select(Dialog dialog, int position, String text) {
                tvType.setText(text);
              }

              @Override
              public void cancel(Dialog dialog) {

              }
            })
            .setGravity(Gravity.CENTER)
            .setAnimStyle(BaseDialog.AnimStyle.SCALE)
            .show();
        break;
    }

  }

  private void routeToGallery() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(intent, GALLERY_REQUSET_CODE);
  }


  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == GALLERY_REQUSET_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
      routeToCrop(data.getData());
    }else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
      tvAddImage.setVisibility(View.GONE);
      Glide.with(this).load(UCrop.getOutput(data))
          .into(ivHotel);
    }
  }

  private void routeToCrop(Uri uri) {
    headImageFilePath = String.format("head_image%d.jpg",System.currentTimeMillis());
    File file = new File(getCacheDir(), headImageFilePath);
    headImageFilePath = file.getAbsolutePath();
    Uri destinationUri = Uri.fromFile(file);
    UCrop.Options options = new UCrop.Options();
    options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
    options.withAspectRatio(9, 5);
    options.withMaxResultSize(1080, 500);
    UCrop.of(uri, destinationUri)
        .withOptions(options)
        .start(this);
  }





}
