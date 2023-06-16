package com.example.ecommerce_hvpp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDetail implements Parcelable {
    public OrderDetail(String id, String image, String name, double price, int quantity, String size) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String size) {
        this.product_id = product_id;
    }

    private String id;
    private String image;
    private String name;
    private double price;
    private int quantity;
    private String size;
    private String product_id;

    public double getTotal() {
        return quantity*price;
    }

    protected OrderDetail(Parcel in) {
        id = in.readString();
        image = in.readString();
        name = in.readString();
        price = in.readFloat();
        quantity = in.readInt();
        size = in.readString();
        product_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(size);
        dest.writeString(product_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel in) {
            return new OrderDetail(in);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };
}
