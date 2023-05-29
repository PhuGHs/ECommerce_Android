package com.example.ecommerce_hvpp.model;

public class Order {
    private long ID;
    private String Title;
    private int Remaining_day;
    private String Method_of_delivery;
    public Order(){

    }
    public Order(long id, String title, int remaining_day){
        this.ID = id;
        this.Title = title;
        this.Remaining_day = remaining_day;
    }

    public long getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public int getRemaining_day() {
        return Remaining_day;
    }

    public String getMethod_of_delivery() {
        return Method_of_delivery;
    }
}
