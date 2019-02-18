package com.hjq.demo.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.User;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.activity.LoginActivity;
import com.hjq.demo.ui.activity.MyFollowActivity;
import com.hjq.dialog.MenuDialog;
import com.hjq.dialog.MessageDialog;
import com.hjq.toast.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Arrays;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目界面跳转示例
 */
public class TestFragmentD extends MyLazyFragment {

  @BindView(R.id.tb_test_d_title)
  TitleBar tbTestDTitle;
  @BindView(R.id.profile_image)
  CircleImageView profileImage;
  @BindView(R.id.tv_phone)
  TextView tvPhone;
  @BindView(R.id.tv_name)
  TextView tvName;
  @BindView(R.id.fl_name)
  FrameLayout flName;
  @BindView(R.id.tv_sex)
  TextView tvSex;
  @BindView(R.id.fl_sex)
  FrameLayout flSex;
  @BindView(R.id.fl_follow)
  FrameLayout flFollow;
  @BindView(R.id.tv_login_out)
  TextView tvLoginOut;
  private User curUser;

  public static TestFragmentD newInstance() {
    return new TestFragmentD();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_test_d;
  }

  @Override
  protected int getTitleBarId() {
    return R.id.tb_test_d_title;
  }

  @Override
  protected void initView() {

  }

  @Override
  protected void initData() {
    curUser = User.getCurrentUser(User.class);
    tvPhone.setText(curUser.getUsername().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
    if (TextUtils.isEmpty(curUser.getName())) {
      tvName.setText(tvPhone.getText());
    } else {
      tvName.setText(curUser.getName());
    }
    if (!TextUtils.isEmpty(curUser.getSex())) {
      tvSex.setText(curUser.getSex());
    }
  }

  @Override
  public boolean isStatusBarEnabled() {
    // 使用沉浸式状态栏
    return !super.isStatusBarEnabled();
  }

  @OnClick({ R.id.fl_name, R.id.fl_sex, R.id.fl_follow, R.id.tv_login_out })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.fl_name:
        showEditDialog();
        break;
      case R.id.fl_sex:
        showSexSelectDialog();
        break;
      case R.id.fl_follow:
        startActivity(MyFollowActivity.class);
        break;
      case R.id.tv_login_out:

        new MessageDialog.Builder(getFragmentActivity())
            .setTitle("退出登录") // 标题可以不用填写
            .setMessage("是否继续？")
            .setConfirm("确定")
            .setCancel("取消") // 设置 null 表示不显示取消按钮
            //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
            .setListener(new MessageDialog.OnListener() {

              @Override
              public void confirm(Dialog dialog) {
                User.logOut();
                startActivity(LoginActivity.class);
                finish();
              }

              @Override
              public void cancel(Dialog dialog) {

              }
            })
            .show();

        break;
    }
  }

  private void showSexSelectDialog() {
    new MenuDialog.Builder(getFragmentActivity())
        .setCancel(null) // 设置 null 表示不显示取消按钮
        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
        .setList(Arrays.asList(new String[] { "男", "女" }))
        .setListener(new MenuDialog.OnListener() {

          @Override
          public void select(Dialog dialog, int position, String text) {
            curUser.setSex(text);
            curUser.update(new UpdateListener() {
              @Override
              public void done(BmobException e) {
                if (e == null) {
                  tvSex.setText(curUser.getSex());
                } else {
                  ToastUtils.show(e.getMessage());
                }
              }
            });
          }

          @Override
          public void cancel(Dialog dialog) {
          }
        })
        .setGravity(Gravity.CENTER)
        .setAnimStyle(BaseDialog.AnimStyle.SCALE)
        .show();
  }

  private void showEditDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("请输入");    //设置对话框标题
    builder.setIcon(R.mipmap.ic_logo);
    final EditText editText = new EditText(getContext());
    builder.setView(editText);
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        curUser.setName(editText.getText().toString());
        curUser.update(new UpdateListener() {
          @Override
          public void done(BmobException e) {
            if (e == null) {
              tvName.setText(curUser.getName());
            } else {
              ToastUtils.show(e.getMessage());
            }
          }
        });
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    builder.create().show();
  }
}