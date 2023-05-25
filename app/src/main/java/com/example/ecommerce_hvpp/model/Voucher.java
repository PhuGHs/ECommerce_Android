package com.example.ecommerce_hvpp.model;

public class Voucher {
    private String applyFor, condition, id, name, voucherName;
    private double discountedValue;
    private long startDate, endDate;

    public Voucher(String applyFor, String condition, String id, String name, String voucherName, double discountedValue, long startDate, long endDate) {
        this.applyFor = applyFor;
        this.condition = condition;
        this.id = id;
        this.name = name;
        this.voucherName = voucherName;
        this.discountedValue = discountedValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getApplyFor() {
        return applyFor;
    }

    public void setApplyFor(String applyFor) {
        this.applyFor = applyFor;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

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

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public double getDiscountedValue() {
        return discountedValue;
    }

    public void setDiscountedValue(double discountedValue) {
        this.discountedValue = discountedValue;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
