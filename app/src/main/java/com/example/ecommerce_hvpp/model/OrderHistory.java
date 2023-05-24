package com.example.ecommerce_hvpp.model;

import java.util.Date;

public class OrderHistory {
    private String CustomerID;
    private int ID;
    private Date TimeCreate;
    private Customer customer;

    public OrderHistory() {}

    public OrderHistory(String customerID, int ID, Date timeCreate) {
        CustomerID = customerID;
        this.ID = ID;
        TimeCreate = timeCreate;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getTimeCreate() {
        return TimeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        TimeCreate = timeCreate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
