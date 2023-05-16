package com.example.ecommerce_hvpp.repositories.adminRepositories;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AdminProductManagementRepository {
    private FirebaseHelper fbHelper;
    private List<Product> products;

    public List<Product> getAllProductWithNoCriteria() {
        products = new ArrayList<>();
        products.add(new Product("P001", "Real Madrid Home", "white", "Real Madrid", "", "1999/2000", 17.99, 9, 5));
        products.add(new Product("P002", "Real Madrid Away", "white", "Real Madrid", "", "1999/2000", 17.99, 9, 5));
        products.add(new Product("P003", "AC Milan Home", "white", "AC Milan", "", "1999/2000", 17.99, 9, 5));
        products.add(new Product("P004", "Arsenal Home", "white", "Arsenal", "", "1999/2000", 17.99, 9, 5));
        products.add(new Product("P005", "MU Away", "white", "Manchester United", "", "1999/2000", 17.99, 9, 5));
        products.add(new Product("P005", "MU Away", "white", "Manchester United", "", "1999/2000", 17.9, 9, 5));

        return products;
    }
}

