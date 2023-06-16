package com.example.ecommerce_hvpp.model;


import static com.example.ecommerce_hvpp.util.constant.CLUB;
import static com.example.ecommerce_hvpp.util.constant.NATION;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String id;
    private String name;
    private String club = "";
    private String nation = "";
    private String season;
    private String description;
    private double price;
    private double pointAvg;
    private long size_m, size_l, size_xl;
    private String url_main, url_sub1, url_sub2, url_thumb;
    private String status = "available";
    private long timeAdded = System.currentTimeMillis();

    public Product(){

    }

    public Product(String id, String name, String club, String nation, String season, String description, double price, double pointAvg, long size_m, long size_l, long size_xl, String url_main, String url_sub1, String url_sub2, String url_thumb, String status, long timeAdded) {
        this.id = id;
        this.name = name;
        this.club = club;
        this.nation = nation;
        this.season = season;
        this.description = description;
        this.price = price;
        this.pointAvg = pointAvg;
        this.size_m = size_m;
        this.size_l = size_l;
        this.size_xl = size_xl;
        this.url_main = url_main;
        this.url_sub1 = url_sub1;
        this.url_sub2 = url_sub2;
        this.url_thumb = url_thumb;
        this.status = status;
        this.timeAdded = timeAdded;
    }

    public Product(String id, String name, String season, double price, String description, int size_xl, int size_l, int size_m) {
        this.size_l = size_l;
        this.id = id;
        this.name = name;
        this.season = season;
        this.price = price;
        this.description = description;
        this.size_xl = size_xl;
        this.size_m = size_m;
    }

    public Product(String name, String season, double price, String description, int size_xl, int size_l, int size_m) {
        this.size_l = size_l;
        this.name = name;
        this.season = season;
        this.price = price;
        this.description = description;
        this.size_xl = size_xl;
        this.size_m = size_m;
    }


    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        club = in.readString();
        nation = in.readString();
        season = in.readString();
        description = in.readString();
        price = in.readDouble();
        pointAvg = in.readDouble();
        size_m = in.readLong();
        size_l = in.readLong();
        size_xl = in.readLong();
        url_main = in.readString();
        url_sub1 = in.readString();
        url_sub2 = in.readString();
        url_thumb = in.readString();
        status = in.readString();
        timeAdded = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(club);
        dest.writeString(nation);
        dest.writeString(season);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeDouble(pointAvg);
        dest.writeLong(size_m);
        dest.writeLong(size_l);
        dest.writeLong(size_xl);
        dest.writeString(url_main);
        dest.writeString(url_sub1);
        dest.writeString(url_sub2);
        dest.writeString(url_thumb);
        dest.writeString(status);
        dest.writeLong(timeAdded);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public double getPointAvg() {
        return pointAvg;
    }

    public void setPointAvg(double point) {
        this.pointAvg = point;
    }

    public long getSizeM() {
        return size_m;
    }

    public void setSizeM(long sizeM) {
        this.size_m = sizeM;
    }

    public long getSizeL() {
        return size_l;
    }

    public void setSizeL(long sizeL) {
        this.size_l = sizeL;
    }

    public long getSizeXL() {
        return size_xl;
    }
    public long getSize(String size){
        if (size.equals("M")) return size_m;
        if (size.equals("L")) return size_l;
        return size_xl;
    }

    public void setSizeXL(long sizeXL) {
        this.size_xl = sizeXL;
    }

    public String getUrlmain() {
        return url_main;
    }

    public void setUrlmain(String urlmain) {
        this.url_main = urlmain;
    }

    public String getUrlsub1() {
        return url_sub1;
    }

    public void setUrlsub1(String urlsub1) {
        this.url_sub1 = urlsub1;
    }

    public String getUrlsub2() {
        return url_sub2;
    }

    public void setUrlsub2(String urlsub2) {
        this.url_sub2 = urlsub2;
    }

    public String getUrlthumb() {
        return url_thumb;
    }

    public void setUrlthumb(String urlthumb) {
        this.url_thumb = urlthumb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }

    public long getSize_xl() {
        return size_xl;
    }

    public void setSize_xl(int size_xl) {
        this.size_xl = size_xl;
    }

    public long getSize_l() {
        return size_l;
    }

    public void setSize_l(long size_l) {
        this.size_l = size_l;
    }

    public long getSize_m() {
        return size_m;
    }

    public void setSize_m(long size_m) {
        this.size_m = size_m;
    }

    public int getType() {
        if (!this.getClub().equals(""))
            return CLUB;
        return NATION;
    }
}
