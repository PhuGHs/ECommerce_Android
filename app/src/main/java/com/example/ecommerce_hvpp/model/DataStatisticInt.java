package com.example.ecommerce_hvpp.model;

public class DataStatisticInt {
    private int dayQuantity;
    private String dayPercent;
    private int dayColor;
    private int monthQuantity;
    private String monthPercent;
    private int monthColor;

    public DataStatisticInt() {}

    public DataStatisticInt(int dayQuantity, String dayPercent, int dayColor, int monthQuantity, String monthPercent, int monthColor) {
        this.dayQuantity = dayQuantity;
        this.dayPercent = dayPercent;
        this.dayColor = dayColor;
        this.monthQuantity = monthQuantity;
        this.monthPercent = monthPercent;
        this.monthColor = monthColor;
    }

    public int getDayQuantity() {
        return dayQuantity;
    }

    public void setDayQuantity(int dayQuantity) {
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

    public int getMonthQuantity() {
        return monthQuantity;
    }

    public void setMonthQuantity(int monthQuantity) {
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
