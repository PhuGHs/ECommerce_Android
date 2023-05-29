package com.example.ecommerce_hvpp.model;

import java.io.Serializable;
import java.util.Date;

public class Promotion implements Serializable {
    private String name;
    private String id;
    private int value;
    private Date date_begin;
    private Date date_end;
    private String apply_for;
    private int condition; // min money

    public Promotion() {}

    public Promotion(String name, String id, int value, int condition, Date date_begin, Date date_end, String apply_for) {
        this.name = name;
        this.id = id;
        this.value = value;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.apply_for = apply_for;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDate_begin() {
        return date_begin;
    }

    public void setDate_begin(Date date_begin) {
        this.date_begin = date_begin;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public String getApply_for() {
        return apply_for;
    }

    public void setApply_for(String apply_for) {
        this.apply_for = apply_for;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
