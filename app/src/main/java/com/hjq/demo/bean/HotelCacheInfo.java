package com.hjq.demo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class HotelCacheInfo extends HotelInfo {

  @Id
  private String objectId;

  private String name;

  private float score;

  private String address;

  private String city;

  private String image;

  private String latlon;

  private String type;

  private String contact;

  private String price;

  private String desc;

  @Generated(hash = 521354562)
  public HotelCacheInfo(String objectId, String name, float score, String address,
      String city, String image, String latlon, String type, String contact,
      String price, String desc) {
    this.objectId = objectId;
    this.name = name;
    this.score = score;
    this.address = address;
    this.city = city;
    this.image = image;
    this.latlon = latlon;
    this.type = type;
    this.contact = contact;
    this.price = price;
    this.desc = desc;
  }

  @Generated(hash = 1420593768)
  public HotelCacheInfo() {
  }

  public String getObjectId() {
    return this.objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getScore() {
    return this.score;
  }

  public void setScore(float score) {
    this.score = score;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getLatlon() {
    return this.latlon;
  }

  public void setLatlon(String latlon) {
    this.latlon = latlon;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContact() {
    return this.contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getPrice() {
    return this.price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getDesc() {
    return this.desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public HotelCacheInfo(HotelInfo info) {
     this(info.getObjectId(), info.getName(), info.getScore(),
        info.getAddress(), info.getCity(), info.getImage(), info.getLatlon(), info.getType(),
        info.getContact(), info.getPrice(), info.getDesc());
  }

  public HotelInfo toHotelInfo(){
    HotelInfo hotelInfo = new HotelInfo();
    hotelInfo.setObjectId(objectId);
    hotelInfo.setName(name);
    hotelInfo.setScore(score);
    hotelInfo.setAddress(address);
    hotelInfo.setCity(city);
    hotelInfo.setImage(image);
    hotelInfo.setLatlon(latlon);
    hotelInfo.setType(type);
    hotelInfo.setContact(contact);
    hotelInfo.setPrice(price);
    hotelInfo.setDesc(desc);
    return hotelInfo;
  }
}
