package com.example.connectwave.Model;

public class users {
    public String uid;
    public String name;
    public String email;
    public String imageurl;
    public String status;

    public users() {

    }

    public users(String uid, String name, String email, String imageurl, String status) {
        this.uid = uid;
        this.status = status;
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}