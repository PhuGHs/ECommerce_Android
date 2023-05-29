package com.example.ecommerce_hvpp.model;

public class Customer {
    private String CitizenID;
    private String Dob;
    private String Email;
    private String ID;
    private String Name;
    private String Phone;
    private int SumRevenue;
    private String imagePath;
    private String address;
    public Customer() {}

    public Customer(String citizenID, String dob, String email, String ID, String name, String phone, int sumRevenue, String imagePath, String address) {
        this.CitizenID = citizenID;
        this.Dob = dob;
        this.Email = email;
        this.ID = ID;
        this.Name = name;
        this.Phone = phone;
        this.SumRevenue = sumRevenue;
        this.imagePath = imagePath;
        this.address = address;
    }

    public String getCitizenID() {
        return CitizenID;
    }

    public void setCitizenID(String citizenID) {
        this.CitizenID = citizenID;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        this.Dob = dob;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public int getSumRevenue() {
        return SumRevenue;
    }

    public void setSumRevenue(int sumRevenue) {
        this.SumRevenue = sumRevenue;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
