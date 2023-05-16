package com.example.ecommerce_hvpp.viewmodel.admin_product_management;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProductManagementRepository;

import java.util.List;

public class AdminProductManagementViewModel extends ViewModel {
    private AdminProductManagementRepository repo;

    public AdminProductManagementViewModel() {
        repo = new AdminProductManagementRepository();
    }

    public List<Product> getAllProductWithNoCriteria() {
        return repo.getAllProductWithNoCriteria();
    }
}
