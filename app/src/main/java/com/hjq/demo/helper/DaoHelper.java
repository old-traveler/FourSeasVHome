package com.hjq.demo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.hjq.demo.common.MyApplication;
import com.hjq.demo.gen.DaoMaster;
import com.hjq.demo.gen.DaoSession;

public class DaoHelper {
  private static DaoHelper instance;

  private DaoMaster.DevOpenHelper mHelper;
  private SQLiteDatabase db;
  private DaoMaster mDaoMaster;
  private DaoSession mDaoSession;

  private DaoHelper(Context context) {
    mHelper = new DaoMaster.DevOpenHelper(context, "sihai-db", null);
    db = mHelper.getWritableDatabase();
    // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
    mDaoMaster = new DaoMaster(db);
    mDaoSession = mDaoMaster.newSession();
  }

  public static synchronized DaoHelper getDefault() {
    if (instance == null) {
      instance = new DaoHelper(MyApplication.getContext());
    }
    return instance;
  }

  public DaoSession getDaoSession() {
    return mDaoSession;
  }

  public SQLiteDatabase getDb() {
    return db;
  }
}
