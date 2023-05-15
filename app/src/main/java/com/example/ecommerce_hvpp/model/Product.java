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
    private double MarkAvg;

    public Product(String ID, String name, String color, String club, String nation, String season, double price, int quantity, double markAvg) {
        this.ID = ID;
        Name = name;
        Color = color;
        Club = club;
        Nation = nation;
        Season = season;
        Price = price;
        Quantity = quantity;
        MarkAvg = markAvg;
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

    public double getMarkAvg() {
        return MarkAvg;
    }
}
