package com.example.ecommerce_hvpp.model;


public class Product {
    private String ID;
    private String Name;
    private String Club;
    private String Nation;
    private String Season;
    private double Price;
    private long Quantity;
    private double PointAvg;
    private String URLmain;
    private String URLsub1;
    private String URLsub2;
    private String URLthumb;

    public Product(String ID, String name, String club, String nation, String season, double price, long quantity, double markAvg, String URLmain, String URLsub1, String URLsub2, String URLthumb) {
        this.ID = ID;
        Name = name;
        Club = club;
        Nation = nation;
        Season = season;
        Price = price;
        Quantity = quantity;
        PointAvg = markAvg;
        this.URLmain = URLmain;
        this.URLsub1 = URLsub1;
        this.URLsub2 = URLsub2;
        this.URLthumb = URLthumb;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getClub() {
        return Club;
    }

    public void setClub(String club) {
        Club = club;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String nation) {
        Nation = nation;
    }

    public String getSeason() {
        return Season;
    }

    public void setSeason(String season) {
        Season = season;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public long getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getPointAvg() {
        return PointAvg;
    }

    public void setPointAvg(double pointAvg) {
        PointAvg = pointAvg;
    }

    public String getURLmain() {
        return URLmain;
    }

    public void setURLmain(String URLmain) {
        this.URLmain = URLmain;
    }

    public String getURLsub1() {
        return URLsub1;
    }

    public void setURLsub1(String URLsub1) {
        this.URLsub1 = URLsub1;
    }

    public String getURLsub2() {
        return URLsub2;
    }

    public void setURLsub2(String URLsub2) {
        this.URLsub2 = URLsub2;
    }

    public String getURLthumb() {
        return URLthumb;
    }

    public void setURLthumb(String URLthumb) {
        this.URLthumb = URLthumb;
    }
}
