package com.tempomena.Model;

public class meesage {

    public  meesage (){}
    String Msg,from,to,date,from_token,to_token;

    public meesage(String msg, String from, String to, String date, String from_token, String to_token) {
        Msg = msg;
        this.from = from;
        this.to = to;
        this.date = date;
        this.from_token = from_token;
        this.to_token = to_token;
    }

    public String getFrom_token() {
        return from_token;
    }

    public void setFrom_token(String from_token) {
        this.from_token = from_token;
    }

    public String getTo_token() {
        return to_token;
    }

    public void setTo_token(String to_token) {
        this.to_token = to_token;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
