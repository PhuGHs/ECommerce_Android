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

    public User(boolean isAdmin, String username, String email, String password, String imagePath) {
        this.isAdmin = isAdmin;
        this.username = username;
        this.email = email;
        this.password = password;
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
