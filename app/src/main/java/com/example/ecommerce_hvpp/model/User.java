package com.example.ecommerce_hvpp.model;

public class User {
    public User(String id, String email, String password, String username) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        imagePath = "https://img.icons8.com/?size=512&id=AjTPlMclWxjF&format=png";
    }

    public User() {

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDatebirth(String datebirth) {
        this.datebirth = datebirth;
    }

    private boolean isAdmin;
    private String datebirth = "";
    private String id;
    private String address = "";
    private long sumRevenue;
    private String username = "";

    public User(boolean isAdmin, String username, String email, String password, String datebirth, String address, String imagePath) {
        this.isAdmin = isAdmin;
        this.username = username;
        this.email = email;
        this.password = password;
        this.datebirth = datebirth;
        this.address = address;
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return username;
    }
    private String email = "";
    public String getEmail() {
        return email;
    }
    private String password = "";
    public String getPassword() {
        return password;
    }
    private String imagePath = "";

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAddress() {
        return address;
    }

    public String getDatebirth() {
        return datebirth;
    }

    public String getImagePath() {
        return imagePath;
    }

//    public User(String username, String email, String password, String imagePath) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.imagePath = imagePath;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSumRevenue() {
        return sumRevenue;
    }

    public void setSumRevenue(long sumRevenue) {
        this.sumRevenue = sumRevenue;
    }
}
