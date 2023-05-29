package com.example.ecommerce_hvpp.model;

import java.util.List;

public class OrderHistoryItem {
    private long Quantity_of_product;
    private String Sum_of_order;
    private long DayCreate_subItem;
    public OrderHistoryItem(){

    }
    public OrderHistoryItem(long quantity_of_product, String sum_of_order, long dayCreate_subItem){
        this.Quantity_of_product = quantity_of_product;
        this.Sum_of_order = sum_of_order;
        this.DayCreate_subItem = dayCreate_subItem;
    }

    public long getQuantity_of_product() {
        return Quantity_of_product;
    }

    public String getSum_of_order() {
        return Sum_of_order;
    }

    public long getDayCreate_subItem() {
        return DayCreate_subItem;
    }
}
