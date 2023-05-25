package com.example.ecommerce_hvpp.model;


import android.util.Log;

import androidx.lifecycle.Observer;

import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

public class Cart {
    private Product product;
    private long quantity;
    private String size;

    public Cart(Product product, long quantity, String size) {
        this.product = product;
        this.quantity = quantity;
        this.size = size;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
