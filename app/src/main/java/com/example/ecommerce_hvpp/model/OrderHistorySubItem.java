package com.example.ecommerce_hvpp.model;

public class OrderHistorySubItem {
    private String ImagePath_subItem;
    private String Name_subItem;
    private long Quantity_subItem;
    private double Sum_subItem;
    public OrderHistorySubItem(){

    }
    public OrderHistorySubItem(String imagePath_subItem, String name_subItem, long quantity_subItem, double sum_subItem){
        this.ImagePath_subItem = imagePath_subItem;
        this.Name_subItem = name_subItem;
        this.Quantity_subItem = quantity_subItem;
        this.Sum_subItem = sum_subItem;
    }

    public String getImagePath_subItem() {
        return ImagePath_subItem;
    }

    public String getName_subItem() {
        return Name_subItem;
    }



    public long getQuantity_subItem() {
        return Quantity_subItem;
    }

    public double getSum_subItem() {
        return Sum_subItem;
    }
}
