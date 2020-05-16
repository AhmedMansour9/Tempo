package com.tempomena.Model;

import android.support.annotation.NonNull;

import com.tempomena.Activites.ProductList;

/**
 * Created by kunda on 10/2/2017.
 */

public class Category {
    private String cat_en;
    private String cat_ar;
    private String key;

    public Category() {

    }

    public Category(String cat_en, String cat_ar, String key) {
        this.cat_en = cat_en;
        this.cat_ar = cat_ar;
        this.key = key;
    }

    public String getCat_en() {
        return cat_en;
    }

    public void setCat_en(String cart_en) {
        this.cat_en = cart_en;
    }

    public String getCat_ar() {
        return cat_ar;
    }

    public void setCat_ar(String cat_ar) {
        this.cat_ar = cat_ar;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public String toString() {
        if(ProductList.Language.equals("en")){
            return cat_en;
        }else {
            return cat_ar;

        }
    }
}
