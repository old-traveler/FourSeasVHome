package com.hjq.demo.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.bean.User;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.helper.EditTextInputHelper;
import com.hjq.dialog.WaitDialog;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 登录界面
 */
public class LoginActivity extends MyActivity
        implements View.OnClickListener {

    @BindView(R.id.et_login_phone)
    EditText mPhoneView;
    @BindView(R.id.et_login_password)
    EditText mPasswordView;
    @BindView(R.id.btn_login_commit)
    Button mCommitView;

    private EditTextInputHelper mEditTextInputHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_login_title;
    }

    @Override
    protected void initView() {
        mCommitView.setOnClickListener(this);
        mEditTextInputHelper = new EditTextInputHelper(mCommitView);
        mEditTextInputHelper.addViews(mPhoneView, mPasswordView);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivity(RegisterActivity.class);

//        startActivityForResult(new Intent(this, RegisterActivity.class), new ActivityCallback() {
//
//            @Override
//            public void onActivityResult(int resultCode, @Nullable Intent data) {
//                toast(String.valueOf(resultCode));
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        mEditTextInputHelper.removeViews();
        super.onDestroy();
    }

    @Override
    public boolean isSupportSwipeBack() {
        //不使用侧滑功能
        return !super.isSupportSwipeBack();
    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if (v == mCommitView) {
            if (mPhoneView.getText().toString().length() != 11) {
                ToastUtils.show(getResources().getString(R.string.phone_input_error));
            }else {

                final BaseDialog baseDialog = new WaitDialog.Builder(this)
                    .setMessage("加载中...") // 消息文本可以不用填写
                    .show();
                User user = new User();
                user.setUsername(mPhoneView.getText().toString());
                user.setPassword(mPasswordView.getText().toString());
                user.login(new SaveListener<User>() {

                    @Override
                    public void done(User user, BmobException e) {
                        baseDialog.dismiss();
                        if (e == null){
                            startActivityFinish(HomeActivity.class);
                        }else {
                            ToastUtils.show(e.getMessage());
                        }
                    }
                });
            }
            
        }
    }
}