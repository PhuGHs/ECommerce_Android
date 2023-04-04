package com.example.ecommerce_hvpp.model;

public class User {
    private String username = "";
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
