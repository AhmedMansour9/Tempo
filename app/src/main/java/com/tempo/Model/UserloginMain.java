package com.tempo.Model;

public class UserloginMain
{

    public String username;
    public String id;
    public String token;
    public String email;

    public UserloginMain() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    // Send User data to users firebase
    public UserloginMain(String username, String email, String id,String token) {
        this.id=id;
        this.username = username;
        this.token = token;
        this.email = email;
    }

    public void setEmail(String Email) {
        this.email=Email;
    }
    public String emaill() {
        return email;
    }
}
