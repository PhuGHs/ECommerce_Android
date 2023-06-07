package com.example.ecommerce_hvpp.viewmodel.Customer;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.repositories.customerRepositories.OrderRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OrderViewModel extends ViewModel {
    OrderRepository repo = new OrderRepository();
    private FirebaseHelper firebaseHelper;
    private HashMap<String, Order> listAllOrder;
    private FirebaseAuth firebaseAuth;
    long difference;
    int daysBetween;
    public OrderViewModel(){
        firebaseHelper = FirebaseHelper.getInstance();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> getALlOrder()); // wait get all product
        future.join();
    }
    public void getALlOrder(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        listAllOrder = new HashMap<>();

        firebaseHelper.getCollection("Order").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customerId").equals(fbUser.getUid())){
                            String title = snapshot.getString("name");
                            String id = snapshot.getString("id");
                            Date date_end = snapshot.getDate("receiveDate");
                            int day_remaining = getDayRemaining(date_end);

                            listAllOrder.put(id, new Order(id, title, day_remaining));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "get all order for hashmap failed");
                });
    }
    public LiveData<List<Order>> showOrderList(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getAllOrders(fbUser.getUid());
    }
    public LiveData<List<OrderHistorySubItem>> showOrderDetail(String order_id){
        return repo.getItemsofOrder(order_id);
    }
    public LiveData<Order> showOrderProgressInfo(String order_id){
        return repo.getOrderprogressInfo(order_id);
    }
    public List<Order> getOrderFound(String queryName){
        List<Order> listFound = new ArrayList<>();
        Set<String> keySet = listAllOrder.keySet();

        for (String key : keySet){
            if (listAllOrder.get(key).getTitle().contains(queryName)){
                listFound.add(listAllOrder.get(key));
            }
        }
        return listFound;
    }
    public int getDayRemaining(Date date_end){
        Date now = new Date();
        difference = date_end.getTime() - now.getTime();
        daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
        return daysBetween;
    }
}
