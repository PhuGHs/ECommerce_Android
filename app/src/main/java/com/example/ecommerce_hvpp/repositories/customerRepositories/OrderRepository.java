package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<List<Order>> _mldListOrder = new MutableLiveData<>();
    private DatabaseReference ref;
    long difference;
    int daysBetween;
    private final String TAG = "OrderRepository";
    public OrderRepository(){
        _mldListOrder = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
        ref = firebaseHelper.getDatabaseReference("Order");
    }
    public LiveData<List<Order>> getAllOrders(String UID) {
        firebaseHelper.getCollection("Order").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> orders = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customer_id").equals(UID)){
                            String title = snapshot.getString("name");
                            long id = snapshot.getLong("id");
                            Date date_end = snapshot.getDate("delivery_date");
                            int day_remaining = getDayRemaining(date_end);

                            Log.d(TAG, "Them thanh cong " + id);
                            orders.add(new Order(Long.toString(id), title, day_remaining));
                        }
                    }
                    _mldListOrder.setValue(orders);
                })
                .addOnFailureListener(e -> {
                    _mldListOrder.setValue(null);
                });
        return _mldListOrder;
    }
    public int getDayRemaining(Date date_end){
        Date now = new Date();
        difference = date_end.getTime() - now.getTime();
        daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
        return daysBetween;
    }
}
