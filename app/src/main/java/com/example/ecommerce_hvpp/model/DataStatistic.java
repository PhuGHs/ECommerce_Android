package com.example.ecommerce_hvpp.model;

public class DataStatistic {
    private String date;
    private int quantity;

    public DataStatistic(String date, int quantity) {
        this.date = date;
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
