package com.example.ecommerce_hvpp.model;

import java.util.Date;

public class OrderHistory {
    private String customer_id;
    private int id;
    private Date time_create;
    private String phone;
    private String address;
    private String name;
    private User user;

    public OrderHistory() {}

    public OrderHistory(String customer_id, int id, Date time_create, String phone, String address, String name) {
        this.customer_id = customer_id;
        this.id = id;
        this.time_create = time_create;
        this.phone = phone;
        this.address = address;
        this.name = name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime_create() {
        return time_create;
    }

    public void setTime_create(Date time_create) {
        this.time_create = time_create;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
