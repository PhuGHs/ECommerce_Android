package com.example.ecommerce_hvpp.viewmodel.admin_customer_management;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;

public class AdminCustomerManagementViewModel extends ViewModel {
    AdminProfileRepository repo;

    public AdminCustomerManagementViewModel() {
        repo = new AdminProfileRepository();
    }

}
