package com.hjq.demo.bean;

import cn.bmob.v3.BmobObject;

public class HotelInfo extends BmobObject {


  private String name;

  private float score;

  private String address;

  private String city;

  private String image;

  private String latlon;

  private String type;

  private String contact;

  private String price;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getScore() {
    return score;
  }

  public void setScore(float score) {
    this.score = score;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getLatlon() {
    return latlon;
  }

  public void setLatlon(String latlon) {
    this.latlon = latlon;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  private String desc;



  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }


}
