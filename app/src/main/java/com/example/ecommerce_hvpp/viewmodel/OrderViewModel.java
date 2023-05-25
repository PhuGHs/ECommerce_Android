package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.repositories.OrderRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class OrderViewModel extends ViewModel {
    OrderRepository repo = new OrderRepository();
    private FirebaseAuth firebaseAuth;
    public LiveData<List<Order>> showOrderList(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getAllOrders(fbUser.getUid());
    }
}
