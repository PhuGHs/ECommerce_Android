package com.example.ecommerce_hvpp.model;

public class Revenue {
    private String product_id;
    private long quantity;

    public Revenue(String product_id, long quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = this.quantity + quantity;
    }
    public void resetQuantity(){
        this.quantity = 0;
    }
}
