package com.example.ecommerce_hvpp.viewmodel.admin.admin_product_management;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProductManagementRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class AdminProductManagementViewModel extends ViewModel {
    private AdminProductManagementRepository repo;

    public AdminProductManagementViewModel() {
        repo = new AdminProductManagementRepository();
    }

    public LiveData<Resource<List<Product>>> getAllProductWithNoCriteria() {
        return repo.getAllProductWithNoCriteria();
    }

    public void deleteProductWithId(String Id, String status) {
        repo.updateProductStatus(Id, status);
    }
}
