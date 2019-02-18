package com.hjq.demo.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  private String sex;

}
