package com.hjq.demo.bean;

public class MyPoint {

  private double lat;

  private double lon;

  public MyPoint(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }
}
