package com.example.ecommerce_hvpp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Voucher implements Parcelable {
    private String applyFor, id, voucherName;
    private double discountedValue, condition;
    private long startDate, endDate;
    private boolean isUsed;
    public Voucher(){

    }

    protected Voucher(Parcel in) {
        applyFor = in.readString();
        condition = in.readDouble();
        id = in.readString();
        voucherName = in.readString();
        discountedValue = in.readDouble();
        startDate = in.readLong();
        endDate = in.readLong();
    }

    public Voucher(String applyFor, double condition, String id, String voucherName, double discountedValue, long startDate, long endDate) {
        this.applyFor = applyFor;
        this.condition = condition;
        this.id = id;
        this.voucherName = voucherName;
        this.discountedValue = discountedValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Voucher(String name, String code, long value, long endDate, boolean isUsed) {
        this.voucherName = name;
        this.id = code;
        this.discountedValue = value;
        this.endDate = endDate;
        this.isUsed = isUsed;
    }
    public Voucher(String applyFor, double condition, String name, String code, long value, long startDate, long endDate, boolean isUsed) {
        this.applyFor = applyFor;
        this.condition = condition;
        this.voucherName = name;
        this.id = code;
        this.discountedValue = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isUsed = isUsed;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applyFor);
        dest.writeDouble(condition);
        dest.writeString(id);
        dest.writeString(voucherName);
        dest.writeDouble(discountedValue);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };

    public String getApplyFor() {
        return applyFor;
    }

    public void setApplyFor(String applyFor) {
        this.applyFor = applyFor;
    }

    public double getCondition() {
        return condition;
    }

    public void setCondition(double condition) {
        this.condition = condition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isUsed() {
        return isUsed;
    }
}
