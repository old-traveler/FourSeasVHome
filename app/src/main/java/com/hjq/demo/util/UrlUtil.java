package com.hjq.demo.util;

public class UrlUtil {


  public static String getUriNewStaticMap(double longitude, double latitude, int width,
      int height) {
    return "http://api.map.baidu.com/staticimage?center="
        + longitude
        + ","
        + latitude
        + "&width="
        + width / 2
        + "&height="
        + height / 2
        + "&zoom=17";
  }

}
