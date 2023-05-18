package com.example.ecommerce_hvpp.viewmodel.admin_customer_management;

import com.example.ecommerce_hvpp.repositories.AdminCustomerManagementRepository;

public class AdminCustomerManagementViewModel {
    AdminCustomerManagementRepository repo;
    public AdminCustomerManagementViewModel() {
        repo = new AdminCustomerManagementRepository();
    }


}
