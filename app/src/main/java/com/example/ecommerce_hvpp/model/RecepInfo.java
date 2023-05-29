package com.example.ecommerce_hvpp.model;

public class RecepInfo {
    private String name = "";
    private String phonenumber = "";
    private String address = "";
    private boolean isApplied = false;
    private String customer_ID = "";
    public RecepInfo(){

    }
    public RecepInfo(String name, String phonenumber, String address, boolean isApplied){
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
        this.isApplied = isApplied;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public boolean getisApplied() {
        return isApplied;
    }
    public String getCustomer_ID() {
        return customer_ID;
    }
}
