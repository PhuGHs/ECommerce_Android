package com.example.ecommerce_hvpp.model;


import android.util.Log;

import androidx.lifecycle.Observer;

import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

public class Cart {
    private String product_id;
    private long quantity;
    private String size;
    private ProductViewModel viewModel = new ProductViewModel();
    public Cart(String product_id, String size, long quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.size = size;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
