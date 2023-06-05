package com.example.ecommerce_hvpp.model;

public class DataStatisticFloat {
    private float dayQuantity;
    private String dayPercent;
    private float dayColor;
    private float monthQuantity;
    private String monthPercent;
    private float monthColor;

    public DataStatisticFloat() {}

    public DataStatisticFloat(float dayQuantity, String dayPercent, float dayColor, float monthQuantity, String monthPercent, float monthColor) {
        this.dayQuantity = dayQuantity;
        this.dayPercent = dayPercent;
        this.dayColor = dayColor;
        this.monthQuantity = monthQuantity;
        this.monthPercent = monthPercent;
        this.monthColor = monthColor;
    }

    public float getDayQuantity() {
        return dayQuantity;
    }

    public void setDayQuantity(float dayQuantity) {
        this.dayQuantity = dayQuantity;
    }

    public String getDayPercent() {
        return dayPercent;
    }

    public void setDayPercent(String dayPercent) {
        this.dayPercent = dayPercent;
    }

    public float getDayColor() {
        return dayColor;
    }

    public void setDayColor(float dayColor) {
        this.dayColor = dayColor;
    }

    public float getMonthQuantity() {
        return monthQuantity;
    }

    public void setMonthQuantity(float monthQuantity) {
        this.monthQuantity = monthQuantity;
    }

    public String getMonthPercent() {
        return monthPercent;
    }

    public void setMonthPercent(String monthPercent) {
        this.monthPercent = monthPercent;
    }

    public float getMonthColor() {
        return monthColor;
    }

    public void setMonthColor(float monthColor) {
        this.monthColor = monthColor;
    }
}
