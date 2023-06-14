package com.example.ecommerce_hvpp.model;

import java.util.List;

public class OrderHistoryItem {
    private long Quantity_of_product;
    private double Sum_of_order;
    private long DayCreate_subItem;
    private String ID_of_Order;
    public OrderHistoryItem(){

    }
    public OrderHistoryItem(String iD_of_Order ,long quantity_of_product, double sum_of_order, long dayCreate_subItem){
        this.ID_of_Order = iD_of_Order;
        this.Quantity_of_product = quantity_of_product;
        this.Sum_of_order = sum_of_order;
        this.DayCreate_subItem = dayCreate_subItem;
    }

    public long getQuantity_of_product() {
        return Quantity_of_product;
    }

    public double getSum_of_order() {
        return Sum_of_order;
    }

    public long getDayCreate_subItem() {
        return DayCreate_subItem;
    }

    public String getID_of_Order() {
        return ID_of_Order;
    }
}
