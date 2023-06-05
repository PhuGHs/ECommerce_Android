package com.example.ecommerce_hvpp.viewmodel.Customer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.repositories.customerRepositories.OrderHistoryRepository;
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
}