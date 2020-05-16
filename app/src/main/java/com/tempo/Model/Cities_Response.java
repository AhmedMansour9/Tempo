package com.tempo.Model;

import android.support.annotation.NonNull;

import com.tempo.Activites.Home;

public class Cities_Response {

    private String id;
    private String name;
    private String name_ar;
    private String cu;

    public Cities_Response() {
    }
    public Cities_Response(String id, String name,String name_ar,String cu) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.cu=cu;

    }

    public String getCu() {
        return cu;
    }

    public void setCu(String cu) {
        this.cu = cu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        if(Home.Language.equals("en")){
            return name;
        }else {
            return  name_ar;
        }
    }
}
