package com.example.ecommerce_hvpp.model;


public class Product {
    private String ID;
    private String Name;
    private String Color;
    private String Club;
    private String Nation;
    private String Season;
    private double Price;
    private int Quantity;
    private double Point;
    private String URLmain, URLsub1, URLsub2, URLthumb;

    public Product(String ID, String name, String color, String club, String nation, String season, double price, int quantity, double markAvg) {
        this.ID = ID;
        Name = name;
        Color = color;
        Club = club;
        Nation = nation;
        Season = season;
        Price = price;
        Quantity = quantity;
        Point = markAvg;
    }

    public Product() {
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getColor() {
        return Color;
    }

    public String getClub() {
        return Club;
    }

    public String getNation() {
        return Nation;
    }

    public String getSeason() {
        return Season;
    }

    public double getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getPoint() {
        return Point;
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

    public void setPoint(double point) {
        Point = point;
    }
}
