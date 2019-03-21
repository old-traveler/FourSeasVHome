package com.hjq.demo.common;

import android.content.Context;
import android.support.multidex.MultiDex;

import cn.bmob.v3.Bmob;
import com.hjq.demo.helper.ActivityStackManager;
import com.hjq.demo.helper.DaoHelper;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;


public class MyApplication extends UIApplication {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        // 初始化吐司工具类
        ToastUtils.init(this);

        // 友盟统计
        UmengHelper.init(this);

        // Activity 栈管理
        ActivityStackManager.init(this);
        Bmob.initialize(this, "c5d560e9c55e92d7d32de36dd65abe97");
        DaoHelper.getDefault();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
        MultiDex.install(this);
    }
}