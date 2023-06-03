package com.example.ecommerce_hvpp.viewmodel.Customer;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.repositories.customerRepositories.OrderHistoryRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class OrderHistoryViewModel extends ViewModel {
    OrderHistoryRepository repo = new OrderHistoryRepository();
    private FirebaseAuth firebaseAuth;
    public LiveData<List<OrderHistoryItem>> showOrderHistoryList(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getAll_OrderHistories(fbUser.getUid());
    }
    public LiveData<Order> showOrderInfo(String order_id){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getOrderInfo(fbUser.getUid(), order_id);
    }
    public LiveData<List<OrderHistorySubItem>> showItemsofOrder(String order_id){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getAll_ItemsOfOrder(fbUser.getUid(), order_id);
    }
    public LiveData<Integer> showNumofOrder(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getNum_of_completeOrder(fbUser.getUid());
    }
    public LiveData<Double> showTotalSum(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getTotal_of_MoneyPaid(fbUser.getUid());
    }
}
