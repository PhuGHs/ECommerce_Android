package com.example.ecommerce_hvpp.viewmodel.Customer;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void getFirstItem(Context view, String orderId , ImageView image_item, TextView name_item_tv, TextView quantity_item_tv, TextView price_item_tv){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        repo.getFirst_Item(view, fbUser.getUid(), orderId ,image_item, name_item_tv, quantity_item_tv, price_item_tv);
    }
    public LiveData<OrderHistorySubItem> getFirstItem(String order_id){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getFirstItem(fbUser.getUid(), order_id);
    }
}
