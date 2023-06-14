package com.example.ecommerce_hvpp.model;

import java.util.Date;

public class OrderHistory {
    private String customerId;
    private String id;
    private Date createdDate;
    private String phoneNumber;
    private String address;
    private String recipientName;
    private double totalPrice;
    private Date updateDate;
    private boolean status;
    private User user;

    public OrderHistory() {}

    public OrderHistory(String customerId, String id, Date createDate, String phoneNumber, String address, String recipientName) {
        this.customerId = customerId;
        this.id = id;
        this.createdDate = createDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.recipientName = recipientName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createDate) {
        this.createdDate = createDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
