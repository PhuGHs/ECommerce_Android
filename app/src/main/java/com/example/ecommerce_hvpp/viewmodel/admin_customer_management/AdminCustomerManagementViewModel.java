package com.example.ecommerce_hvpp.viewmodel.admin_customer_management;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.repositories.AdminCustomerManagementRepository;

public class AdminCustomerManagementViewModel extends ViewModel {
    AdminCustomerManagementRepository repo;
    public AdminCustomerManagementViewModel() {
        repo = new AdminCustomerManagementRepository();
    }
}
