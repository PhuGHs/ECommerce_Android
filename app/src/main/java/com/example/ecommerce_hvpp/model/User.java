package com.example.ecommerce_hvpp.model;

public class User {
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    private boolean isAdmin;
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
    private String address = "";

    public String getAddress() {
        return address;
    }
    public String datebirth = "";

    public String getDatebirth() {
        return datebirth;
    }

    public String getImagePath() {
        return imagePath;
    }

    public User(String username, String email, String password, String imagePath) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
    }
}
