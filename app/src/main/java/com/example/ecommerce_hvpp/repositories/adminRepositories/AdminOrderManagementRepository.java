package com.example.ecommerce_hvpp.repositories.adminRepositories;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;

public class AdminOrderManagementRepository {
    private final FirebaseHelper fbHelper;

    public AdminOrderManagementRepository() {
        fbHelper = FirebaseHelper.getInstance();
    }
}
