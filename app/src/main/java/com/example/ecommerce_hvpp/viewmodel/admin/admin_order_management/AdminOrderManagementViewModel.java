package com.example.ecommerce_hvpp.viewmodel.admin.admin_order_management;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminOrderManagementRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class AdminOrderManagementViewModel extends ViewModel {
    private AdminOrderManagementRepository repo;
    private MutableLiveData<Resource<String>> combinedLiveData = new MutableLiveData<>();
    public AdminOrderManagementViewModel() {
        repo = new AdminOrderManagementRepository();
    }
    public LiveData<Resource<List<Order>>> getOrders() {
        combinedLiveData.setValue(Resource.loading(null));
        return repo.getOrders();
    }

    public LiveData<Resource<List<Order>>> getOrdersByStatus(String status) {
        return repo.getOrdersByStatus(status);
    }

    public void updateOrder(String Id, String status) {
        repo.updateOrder(Id, status);
    }
//    public void addDummyOrder() {
//        repo.createDummyOrder();
//    }
}
