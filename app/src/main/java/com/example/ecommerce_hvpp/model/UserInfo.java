package com.example.ecommerce_hvpp.model;

public class UserInfo {
    private String name = "";
    private String datebirth = "";
    private String address = "";
    private String email = "";
    private String imagelink = "";
    public UserInfo(String name, String datebirth, String address, String email, String imagelink){
        this.name = name;
        this.datebirth = datebirth;
        this.address = address;
        this.email = email;
        this.imagelink = imagelink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(String datebirth) {
        this.datebirth = datebirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
}
