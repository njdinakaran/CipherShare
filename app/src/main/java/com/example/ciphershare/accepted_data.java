package com.example.ciphershare;

public class accepted_data {
    String username,userrecent,useremail;

    public accepted_data(String username, String userrecent,String useremail) {
        this.username = username;
        this.userrecent = userrecent;
        this.useremail =useremail;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserrecent() {
        return userrecent;
    }

    public void setUserrecent(String userrecent) {
        this.userrecent = userrecent;
    }
}
