package com.hjq.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.hjq.demo.common.MyApplication;

public class SpCacheHelper {

  private static SharedPreferences.Editor getEditor() {
    return getSharedPreferences().edit();
  }

  private static SharedPreferences getSharedPreferences() {
    return MyApplication.getContext().getSharedPreferences("helper", Context.MODE_PRIVATE);
  }


  public static void deleteClassFromSp(String key){
    getEditor().clear().commit();
  }

  public static <T> T getClassFromSp(String key, Class<T> cls) {
    try {
      String json = getSharedPreferences().getString(key, "");
      return new Gson().fromJson(json, cls);
    } catch (Exception e) {
      return null;
    }
  }

  public static void putBoolean(String key, boolean data) {
    getEditor().putBoolean(key, data).commit();
  }

  public static void putString(String key, String data) {
    getEditor().putString(key, data).commit();
  }

  public static void putLong(String key, long data){
    getEditor().putLong(key,data).commit();
  }

  public static long getLong(String key){
    return getSharedPreferences().getLong(key,0);
  }

  public static String getString(String key){
    return getSharedPreferences().getString(key,null);
  }

  public static boolean getBoolean(String key) {
    return getSharedPreferences().getBoolean(key, false);
  }
}
