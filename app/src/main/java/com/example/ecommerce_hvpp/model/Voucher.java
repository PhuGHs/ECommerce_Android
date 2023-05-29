package com.example.ecommerce_hvpp.model;

public class Voucher {
    private String name;
    private String id;
    private long value;
    private long date_end;
    public Voucher(){

    }
    public Voucher(String name, String code, long discount_value, long end_date){
        this.name = name;
        this.id = code;
        this.value = discount_value;
        this.date_end = end_date;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public long getValue() {
        return value;
    }

    public long getDate_end() {
        return date_end;
    }
}
