package com.example.ecommerce_hvpp.model;

public class RecepInfo {
    private String name = "";
    private String phonenumber = "";
    private String address = "";
    private boolean isApplied = false;
    private String customer_ID = "";
    private String recep_ID = "";
    public RecepInfo(){

    }
    public RecepInfo(String recep_ID, String name, String phonenumber, String address, boolean isApplied){
        this.recep_ID = recep_ID;
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

    public String getRecep_ID() {
        return recep_ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }
}
