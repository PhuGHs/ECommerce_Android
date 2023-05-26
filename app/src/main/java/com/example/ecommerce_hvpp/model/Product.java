package com.example.ecommerce_hvpp.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {
    private String id = "";
    private String name = "";
    private String club = "";
    private String nation = "";
    private String season = "";
    private String description = "";
    private double price;
    private int size_xl;
    private int size_l;
    private int size_m;
    private double point;
    private String url_main = "", url_sub1 = "", url_sub2 = "", url_thumb = "";

    private String status = "";

    public Product(String id, String name, String club, String nation, String season, String description, double price, int size_xl, int size_l, int size_m, double point, String url_main, String url_sub1, String url_sub2, String url_thumb, String status) {
        this.id = id;
        this.name = name;
        this.club = club;
        this.nation = nation;
        this.season = season;
        this.description = description;
        this.price = price;
        this.size_xl = size_xl;
        this.size_l = size_l;
        this.size_m = size_m;
        this.point = point;
        this.url_main = url_main;
        this.url_sub1 = url_sub1;
        this.url_sub2 = url_sub2;
        this.url_thumb = url_thumb;
        this.status = status;
    }

    public Product() {
    }

    public Product(String Id, String name, String season, String price, String description, int XL, int L, int M) {
        this.id = Id;
        this.name = name;
        this.season = season;
        this.price = Double.parseDouble(price);
        this.description = description;
        this.size_xl = XL;
        this.size_l = L;
        this.size_m = M;
    }

    public Product(String name, String season, String price, String description, int XL, int L, int M) {
        this.name = name;
        this.season = season;
        this.price = Double.parseDouble(price);
        this.description = description;
        this.size_xl = XL;
        this.size_l = L;
        this.size_m = M;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        club = in.readString();
        nation = in.readString();
        season = in.readString();
        description = in.readString();
        price = in.readDouble();
        size_xl = in.readInt();
        size_l = in.readInt();
        size_m = in.readInt();
        point = in.readDouble();
        url_main = in.readString();
        url_sub1 = in.readString();
        url_sub2 = in.readString();
        url_thumb = in.readString();
        status = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }


    public int getSize_xl() {
        return size_xl;
    }

    public void setSize_xl(int size_xl) {
        this.size_xl = size_xl;
    }

    public int getSize_l() {
        return size_l;
    }

    public void setSize_l(int size_l) {
        this.size_l = size_l;
    }

    public int getSize_m() {
        return size_m;
    }

    public void setSize_m(int size_m) {
        this.size_m = size_m;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl_main() {
        return url_main;
    }

    public void setUrl_main(String url_main) {
        this.url_main = url_main;
    }

    public String getUrl_sub1() {
        return url_sub1;
    }

    public void setUrl_sub1(String url_sub1) {
        this.url_sub1 = url_sub1;
    }

    public String getUrl_sub2() {
        return url_sub2;
    }

    public void setUrl_sub2(String url_sub2) {
        this.url_sub2 = url_sub2;
    }

    public String getUrl_thumb() {
        return url_thumb;
    }

    public void setUrl_thumb(String url_thumb) {
        this.url_thumb = url_thumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(club);
        parcel.writeString(nation);
        parcel.writeString(season);
        parcel.writeString(description);
        parcel.writeDouble(price);
        parcel.writeInt(size_xl);
        parcel.writeInt(size_l);
        parcel.writeInt(size_m);
        parcel.writeDouble(point);
        parcel.writeString(url_main);
        parcel.writeString(url_sub1);
        parcel.writeString(url_sub2);
        parcel.writeString(url_thumb);
        parcel.writeString(status);
    }
}
