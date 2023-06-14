package com.example.ecommerce_hvpp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Order implements Parcelable {
    private String id, address, customerId, deliveryMethod, paymentMethod, recipientName, note, phone_number;
    private String status;
    private String title;
    private int remaining_day;
    private long createdDate, receiveDate;
    private double totalPrice;
    private List<Voucher> voucherList;
    private List<OrderDetail> items;
    public Order(){

    }

    protected Order(Parcel in) {
        id = in.readString();
        address = in.readString();
        customerId = in.readString();
        deliveryMethod = in.readString();
        paymentMethod = in.readString();
        recipientName = in.readString();
        note = in.readString();
        phone_number = in.readString();
        createdDate = in.readLong();
        receiveDate = in.readLong();
        totalPrice = in.readFloat();
        voucherList = in.createTypedArrayList(Voucher.CREATOR);
        items = in.createTypedArrayList(OrderDetail.CREATOR);
    }

    public Order(String id, String address, String customerId, String deliveryMethod, String paymentMethod, String recipientName, String note, String phone_number, String status, long createdDate, long receiveDate, double totalPrice, List<Voucher> voucherList, List<OrderDetail> items) {
        this.id = id;
        this.address = address;
        this.customerId = customerId;
        this.deliveryMethod = deliveryMethod;
        this.paymentMethod = paymentMethod;
        this.recipientName = recipientName;
        this.note = note;
        this.phone_number = phone_number;
        this.status = status;
        this.createdDate = createdDate;
        this.receiveDate = receiveDate;
        this.totalPrice = totalPrice;
        this.voucherList = voucherList;
        this.items = items;
    }

    public Order(String id, String address, String customerId, String deliveryMethod, String paymentMethod, String recipientName, String note, String phone_number, String status, long createdDate, long receiveDate, double totalPrice, List<Voucher> voucherList) {
        this.id = id;
        this.address = address;
        this.customerId = customerId;
        this.deliveryMethod = deliveryMethod;
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

    public Order(String id, String title, int day_remaining) {
        this.id = id;
        this.title = title;
        this.remaining_day = day_remaining;
    }
    public Order(String id, String deliveryMethod, long receiveDate, String recipientName, String phone_number, String address){
        this.id = id;
        this.deliveryMethod = deliveryMethod;
        this.receiveDate = receiveDate;
        this.recipientName = recipientName;
        this.phone_number = phone_number;
        this.address = address;
    }
    public Order(String address, long createdDate, String deliveryMethod, int remaining_day){
        this.address = address;
        this.createdDate = createdDate;
        this.deliveryMethod = deliveryMethod;
        this.remaining_day = remaining_day;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(address);
        dest.writeString(customerId);
        dest.writeString(deliveryMethod);
        dest.writeString(paymentMethod);
        dest.writeString(recipientName);
        dest.writeString(note);
        dest.writeString(phone_number);
        dest.writeLong(createdDate);
        dest.writeLong(receiveDate);
        dest.writeDouble(totalPrice);
        dest.writeTypedList(voucherList);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public float getTotalDiscount() {
        float x = 0;
        for(Voucher voucher : voucherList) {
            x += voucher.getDiscountedValue();
        }
        return x;
    }

    public float getSubtotal() {
        float x = 0;
        for (OrderDetail orderDetail : items) {
            x += orderDetail.getTotal();
        }
        return x;
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
        return deliveryMethod;
    }

    public void setDeliverMethod(String deliverMethod) {
        this.deliveryMethod = deliverMethod;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<OrderDetail> getItems() {
        return items;
    }

    public void setItems(List<OrderDetail> items) {
        this.items = items;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public String getTitle() {
        return title;
    }

    public int getRemaining_day() {
        return remaining_day;
    }
}
