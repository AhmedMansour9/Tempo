package com.tempomena.Model;

/**
 * Created by HP on 04/06/2018.
 */

public class Retrivedata {
    private String img1;
    private String img2;
    private String img3;
   private String img4;
   private String name;
   private String discrption;
    private String govern;
    private String discount;
   private String phone;
    private long cit_id;
    private String date;
   private String token;
   private String key;
   private Boolean Admin;
   private  Boolean Statues;
    private  String Social_id;
    private  String Sub_id;
    private String type_service;
    private String currency;
    private String sub_name;
    private String cat_name;
    private String user_name;

    public Retrivedata(){}

    public Retrivedata(String img1, String img2, String img3, String img4, String name, String discrption, String govern, String discount, String phone, long cit_id, String date, String token, String key, Boolean admin, Boolean statues, String social_id, String sub_id, String type_service, String currency, String sub_name, String cat_name, String user_name) {
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.name = name;
        this.discrption = discrption;
        this.govern = govern;
        this.discount = discount;
        this.phone = phone;
        this.cit_id = cit_id;
        this.date = date;
        this.token = token;
        this.key = key;
        Admin = admin;
        Statues = statues;
        Social_id = social_id;
        Sub_id = sub_id;
        this.type_service = type_service;
        this.currency = currency;
        this.sub_name = sub_name;
        this.cat_name = cat_name;
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscrption() {
        return discrption;
    }

    public void setDiscrption(String discrption) {
        this.discrption = discrption;
    }

    public String getGovern() {
        return govern;
    }

    public void setGovern(String govern) {
        this.govern = govern;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCit_id() {
        return cit_id;
    }

    public void setCit_id(long cit_id) {
        this.cit_id = cit_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }

    public Boolean getStatues() {
        return Statues;
    }

    public void setStatues(Boolean statues) {
        Statues = statues;
    }

    public String getSocial_id() {
        return Social_id;
    }

    public void setSocial_id(String social_id) {
        Social_id = social_id;
    }

    public String getSub_id() {
        return Sub_id;
    }

    public void setSub_id(String sub_id) {
        Sub_id = sub_id;
    }

    public String getType_service() {
        return type_service;
    }

    public void setType_service(String type_service) {
        this.type_service = type_service;
    }
}
