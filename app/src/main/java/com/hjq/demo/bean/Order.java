package com.hjq.demo.bean;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {

  private User user;

  private int price;

  private String name;

  private HouseInfo houseInfo;

  private String time;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HouseInfo getHouseInfo() {
    return houseInfo;
  }

  public void setHouseInfo(HouseInfo houseInfo) {
    this.houseInfo = houseInfo;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
