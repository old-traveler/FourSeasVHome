package com.hjq.demo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.bumptech.glide.Glide;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.HouseInfo;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.util.FileProviderCompat;
import com.hjq.dialog.ToastDialog;
import com.hjq.dialog.WaitDialog;
import com.hjq.toast.ToastUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import java.io.File;

import static com.hjq.demo.ui.activity.AddHotelActivity.GALLERY_REQUSET_CODE;

public class PublishActivity extends MyActivity {

  public static final int REQUEST_PANORAMA = 103;

  @BindView(R.id.tb_about_title)
  TitleBar tbAboutTitle;
  @BindView(R.id.iv_panorama)
  ImageView ivPanorama;
  @BindView(R.id.tv_panorama_tip)
  TextView tvPanoramaTip;
  @BindView(R.id.tv_desc_image)
  TextView tvDescImage;
  @BindView(R.id.iv_desc)
  ImageView ivDesc;
  @BindView(R.id.iv_desc_tip)
  TextView ivDescTip;
  @BindView(R.id.et_title)
  EditText etTitle;
  @BindView(R.id.et_price)
  EditText etPrice;
  @BindView(R.id.et_count)
  EditText etCount;
  private String headImageFilePath;
  private String panoramaPath;
  private String hotelId;

  public static void start(Context context, String hotelId) {
    Intent intent = new Intent(context, PublishActivity.class);
    context.startActivity(intent.putExtra("hotelId", hotelId));
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_publish;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_about_title;
  }

  private void routeToGallery(int code) {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(intent, code);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == GALLERY_REQUSET_CODE
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null) {
      routeToCrop(data.getData());
    } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
      ivDescTip.setVisibility(View.GONE);
      Glide.with(this).load(UCrop.getOutput(data))
          .into(ivDesc);
    } else if (resultCode == RESULT_OK && requestCode == REQUEST_PANORAMA && data != null){
      panoramaPath = FileProviderCompat.getRealPathFromURI(this,data.getData());
      Glide.with(this)
          .load(data.getData())
          .into(ivPanorama);
    }
  }

  private void routeToCrop(Uri uri) {
    headImageFilePath = String.format("head_image%d.jpg", System.currentTimeMillis());
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

  @Override
  protected void initView() {
    tbAboutTitle.setRightTitle("发布");
    tbAboutTitle.setOnTitleBarListener(new OnTitleBarListener() {
      @Override
      public void onLeftClick(View v) {
        finish();
      }

      @Override
      public void onTitleClick(View v) {

      }

      @Override
      public void onRightClick(View v) {
        publish();
      }
    });
  }

  private void publish() {
    if (TextUtils.isEmpty(panoramaPath)){
      ToastUtils.show("全景");
    }else if (TextUtils.isEmpty(headImageFilePath)){
      ToastUtils.show("图片");
    }else if (TextUtils.isEmpty(etTitle.getText().toString())){
      ToastUtils.show("名字");
    }else if (TextUtils.isEmpty(etPrice.getText().toString())){
      ToastUtils.show("价格");
    }else if (TextUtils.isEmpty(etCount.getText().toString())){
      ToastUtils.show("数量");
    }else {
      final BaseDialog dialog = new WaitDialog.Builder(this)
          .setMessage("加载中...") // 消息文本可以不用填写
          .show();
      final BmobFile panorama = new BmobFile(new File(panoramaPath));
      final BmobFile descImage = new BmobFile(new File(headImageFilePath));
      panorama.upload(new UploadFileListener() {
        @Override
        public void done(BmobException e) {
          if (e == null){
            descImage.upload(new UploadFileListener() {
              @Override
              public void done(BmobException e) {
                if (e == null){
                  HouseInfo info = new HouseInfo();
                  info.setArea(18);
                  info.setBelongTo(hotelId);
                  info.setInfoImage(descImage.getFileUrl());
                  info.setName(etTitle.getText().toString());
                  info.setPanoramaImage(panorama.getFileUrl());
                  info.setPeopleCount(2);
                  info.setNumber(Integer.parseInt(etCount.getText().toString()));
                  info.setPrice(Integer.parseInt(etPrice.getText().toString()));
                  info.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                      dialog.dismiss();
                      if (e == null){
                        new ToastDialog.Builder(PublishActivity.this)
                            .setType(ToastDialog.Type.FINISH)
                            .setMessage("完成")
                            .show();
                      }else {
                        ToastUtils.show(e.getMessage());
                      }
                    }
                  });
                }else {
                  dialog.dismiss();
                  ToastUtils.show(e.getMessage());
                }
              }
            });
          }else {
            dialog.dismiss();
            ToastUtils.show(e.getMessage());
          }
        }
      });
    }

  }

  @Override
  public boolean isStatusBarEnabled() {
    return !super.isStatusBarEnabled();
  }

  @Override
  protected void initData() {
    hotelId = getIntent().getStringExtra("hotelId");
  }

  @OnClick({ R.id.iv_panorama, R.id.iv_desc })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_panorama:
        routeToGallery(REQUEST_PANORAMA);
        break;
      case R.id.iv_desc:
        routeToGallery(GALLERY_REQUSET_CODE);
        break;
    }
  }
}
