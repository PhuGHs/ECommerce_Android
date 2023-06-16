package com.example.ecommerce_hvpp.model;

public class OrderHistorySubItem {
    private String ProductID;
    private String OrderID;
    private String ImagePath_subItem;
    private String Name_subItem;
    private String Quantity_subItem;
    private double Sum_subItem;
    private boolean isReviewed;
    public OrderHistorySubItem(){

    }
    public OrderHistorySubItem(String imagePath_subItem, String name_subItem, String quantity_subItem, double sum_subItem){
        this.ImagePath_subItem = imagePath_subItem;
        this.Name_subItem = name_subItem;
        this.Quantity_subItem = quantity_subItem;
        this.Sum_subItem = sum_subItem;
    }
    public OrderHistorySubItem(String productID, String imagePath_subItem, String name_subItem, String quantity_subItem, double sum_subItem, boolean isReviewed){
        this.ProductID = productID;
        this.ImagePath_subItem = imagePath_subItem;
        this.Name_subItem = name_subItem;
        this.Quantity_subItem = quantity_subItem;
        this.Sum_subItem = sum_subItem;
        this.isReviewed = isReviewed;
    }
    public OrderHistorySubItem(String imagePath_subItem, String name_subItem, String quantity_subItem, double sum_subItem, String orderID){
        this.ImagePath_subItem = imagePath_subItem;
        this.Name_subItem = name_subItem;
        this.Quantity_subItem = quantity_subItem;
        this.Sum_subItem = sum_subItem;
        this.OrderID = orderID;
    }
    public String getImagePath_subItem() {
        return ImagePath_subItem;
    }

    public String getName_subItem() {
        return Name_subItem;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getQuantity_subItem() {
        return Quantity_subItem;
    }

    public double getSum_subItem() {
        return Sum_subItem;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public String getProductID() {
        return ProductID;
    }
}
