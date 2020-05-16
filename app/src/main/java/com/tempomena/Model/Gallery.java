package com.tempomena.Model;

public class Gallery {

    String img,linl;

    public Gallery(){}

    public Gallery(String img, String linl) {
        this.img = img;
        this.linl = linl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLinl() {
        return linl;
    }

    public void setLinl(String linl) {
        this.linl = linl;
    }
}
