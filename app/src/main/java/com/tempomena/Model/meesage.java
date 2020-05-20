package com.tempomena.Model;

public class meesage {

    public  meesage (){}
    String Msg,from,to,date,from_token,to_token,recieved_from,send_to;


    public meesage(String msg, String from, String to, String date, String from_token, String to_token, String recieved_from, String send_to) {
        Msg = msg;
        this.from = from;
        this.to = to;
        this.date = date;
        this.from_token = from_token;
        this.to_token = to_token;
        this.recieved_from = recieved_from;
        this.send_to = send_to;
    }

    public String getRecieved_from() {
        return recieved_from;
    }

    public void setRecieved_from(String recieved_from) {
        this.recieved_from = recieved_from;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
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
