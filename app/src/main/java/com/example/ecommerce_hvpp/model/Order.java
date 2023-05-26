package com.example.ecommerce_hvpp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ecommerce_hvpp.util.OrderStatus;

import java.util.List;

public class Order implements Parcelable {
    private String id, address, customerId, deliverMethod, paymentMethod, recipientName, note, phone_number;
    private OrderStatus status;
    private long createdDate, receiveDate;
    private float totalPrice;
    private List<Voucher> voucherList;
    private List<OrderDetail> items;

    protected Order(Parcel in) {
        id = in.readString();
        address = in.readString();
        customerId = in.readString();
        deliverMethod = in.readString();
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

    public Order(String id, String address, String customerId, String deliverMethod, String paymentMethod, String recipientName, String note, String phone_number, OrderStatus status, long createdDate, long receiveDate, float totalPrice, List<Voucher> voucherList, List<OrderDetail> items) {
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
        this.items = items;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(address);
        dest.writeString(customerId);
        dest.writeString(deliverMethod);
        dest.writeString(paymentMethod);
        dest.writeString(recipientName);
        dest.writeString(note);
        dest.writeString(phone_number);
        dest.writeLong(createdDate);
        dest.writeLong(receiveDate);
        dest.writeFloat(totalPrice);
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
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

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
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
}
