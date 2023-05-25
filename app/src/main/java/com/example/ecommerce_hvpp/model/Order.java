package com.example.ecommerce_hvpp.model;

import com.example.ecommerce_hvpp.util.OrderStatus;

import java.util.List;

public class Order {
    private String id, address, customerId, deliverMethod, paymentMethod, recipientName, note, phone_number;
    private OrderStatus status;
    private long createdDate, receiveDate;
    private double totalPrice;
    private List<Voucher> voucherList;


    public Order(String id, String address, String customerId, String deliverMethod, String paymentMethod, String recipientName, String note, String phone_number, OrderStatus status, long createdDate, long receiveDate, double totalPrice, List<Voucher> voucherList) {
        this.id = id;
        this.address = address;
        this.customerId = customerId;
        this.deliverMethod = deliverMethod;
        this.paymentMethod = paymentMethod;
        this.recipientName = recipientName;
        this.note = note;
        this.phone_number = phone_number;
        this.status = status;
        this.createdDate = createdDate;
        this.receiveDate = receiveDate;
        this.totalPrice = totalPrice;
        this.voucherList = voucherList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDeliverMethod() {
        return deliverMethod;
    }

    public void setDeliverMethod(String deliverMethod) {
        this.deliverMethod = deliverMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(long receiveDate) {
        this.receiveDate = receiveDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Voucher> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
