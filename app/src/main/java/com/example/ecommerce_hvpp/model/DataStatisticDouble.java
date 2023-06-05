package com.example.ecommerce_hvpp.model;

public class DataStatisticDouble {
    private double dayQuantity;
    private String dayPercent;
    private int dayColor;
    private double monthQuantity;
    private String monthPercent;
    private int monthColor;

    public DataStatisticDouble() {}

    public DataStatisticDouble(double dayQuantity, String dayPercent, int dayColor, double monthQuantity, String monthPercent, int monthColor) {
        this.dayQuantity = dayQuantity;
        this.dayPercent = dayPercent;
        this.dayColor = dayColor;
        this.monthQuantity = monthQuantity;
        this.monthPercent = monthPercent;
        this.monthColor = monthColor;
    }

    public double getDayQuantity() {
        return dayQuantity;
    }

    public void setDayQuantity(double dayQuantity) {
        this.dayQuantity = dayQuantity;
    }

    public String getDayPercent() {
        return dayPercent;
    }

    public void setDayPercent(String dayPercent) {
        this.dayPercent = dayPercent;
    }

    public int getDayColor() {
        return dayColor;
    }

    public void setDayColor(int dayColor) {
        this.dayColor = dayColor;
    }

    public double getMonthQuantity() {
        return monthQuantity;
    }

    public void setMonthQuantity(double monthQuantity) {
        this.monthQuantity = monthQuantity;
    }

    public String getMonthPercent() {
        return monthPercent;
    }

    public void setMonthPercent(String monthPercent) {
        this.monthPercent = monthPercent;
    }

    public int getMonthColor() {
        return monthColor;
    }

    public void setMonthColor(int monthColor) {
        this.monthColor = monthColor;
    }
}
