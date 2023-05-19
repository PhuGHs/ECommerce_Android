package com.example.ecommerce_hvpp.viewmodel.admin_product_management;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProductManagementRepository;
import com.example.ecommerce_hvpp.util.Resource;

public class AdminProductDetailsViewModel extends ViewModel {
    private AdminProductManagementRepository repo;
    private MutableLiveData<Boolean> isEditMode = new MutableLiveData<>();
    public AdminProductDetailsViewModel() {
        repo = new AdminProductManagementRepository();
    }

    public LiveData<Resource<Product>> getProduct(String Id) {
        return repo.getProduct(Id);
    }
    public void addProduct(Product pd) {
        repo.addProduct(pd);
    }

    public void editProduct() {

    }

    public LiveData<Boolean> getIsEditMode() {
        return isEditMode;
    }
    public void setIsEditMode(Boolean isEditMode) {
        this.isEditMode.setValue(isEditMode);
    }
}
