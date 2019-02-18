package com.hjq.demo.bean;

import cn.bmob.v3.BmobObject;


public class HouseInfo extends BmobObject {

  private String belongTo;

  private String name;

  private int price;

  private int number;

  private int peopleCount;

  private String infoImage;

  private String panoramaImage;

  private int area;

  public String getBelongTo() {
    return belongTo;
  }

  public void setBelongTo(String belongTo) {
    this.belongTo = belongTo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getPeopleCount() {
    return peopleCount;
  }

  public void setPeopleCount(int peopleCount) {
    this.peopleCount = peopleCount;
  }

  public String getInfoImage() {
    return infoImage;
  }

  public void setInfoImage(String infoImage) {
    this.infoImage = infoImage;
  }

  public String getPanoramaImage() {
    return panoramaImage;
  }

  public void setPanoramaImage(String panoramaImage) {
    this.panoramaImage = panoramaImage;
  }

  public int getArea() {
    return area;
  }

  public void setArea(int area) {
    this.area = area;
  }
}
