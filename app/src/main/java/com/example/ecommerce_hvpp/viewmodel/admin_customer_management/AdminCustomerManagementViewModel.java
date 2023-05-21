package com.example.ecommerce_hvpp.viewmodel.admin_customer_management;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.repositories.AdminCustomerManagementRepository;
import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class AdminCustomerManagementViewModel extends ViewModel {
    AdminProfileRepository repo;

    public AdminCustomerManagementViewModel() {
        repo = new AdminProfileRepository();
    }

}
