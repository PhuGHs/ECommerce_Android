package com.example.ecommerce_hvpp.model;

import java.sql.Timestamp;
import java.util.Date;

public class Feedback {
    private String CustomerID;
    private String ProductID;
    private long Point;
    private long Date;
    private String Comment;

    public Feedback(String customerID, String productID, long point, long date, String comment) {
        CustomerID = customerID;
        ProductID = productID;
        Point = point;
        Date = date;
        Comment = comment;
    }
    public Feedback(){

    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public long getPoint() {
        return Point;
    }

    public void setPoint(long point) {
        Point = point;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
