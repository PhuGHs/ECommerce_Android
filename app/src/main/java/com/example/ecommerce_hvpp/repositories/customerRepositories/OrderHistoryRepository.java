package com.example.ecommerce_hvpp.repositories.customerRepositories;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderDetail;
import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderHistoryRepository {
    private FirebaseHelper firebaseHelper;
    private FirebaseFirestore db;
    private MutableLiveData<List<OrderHistoryItem>> _mldListOrderHistory = new MutableLiveData<>();
    private MutableLiveData<List<OrderHistorySubItem>> _mldListSubOrderHistory = new MutableLiveData<>();
    private MutableLiveData<Order> orderInfo = new MutableLiveData<>();
    private final String TAG = "OrderHistoryRepository";
    private long count = 0;
    private long all_quantity = 0;
    public OrderHistoryRepository(){
        _mldListOrderHistory = new MutableLiveData<>();
        _mldListSubOrderHistory = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
    }
    public LiveData<List<OrderHistoryItem>> getAll_OrderHistories(String UID) {
        firebaseHelper.getCollection("Order").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OrderHistoryItem> orderhistories = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customer_id").equals(UID)){
                            Timestamp time_order = snapshot.getTimestamp("createdDate");
                            long all_quantity = snapshot.getLong("totalQuantity");
                            double sum_of_order = snapshot.getDouble("totalPrice");
                            Log.d(TAG,  "Tong san pham: " + all_quantity);
                            orderhistories.add(new OrderHistoryItem(snapshot.getId(), all_quantity, sum_of_order, time_order.getSeconds()*1000));
                        }
                    }
                    _mldListOrderHistory.setValue(orderhistories);
                })
                .addOnFailureListener(e -> {
                    _mldListOrderHistory.setValue(null);
                });
        return _mldListOrderHistory;
    }
    public LiveData<Order> getOrderInfo(String UID){
        firebaseHelper.getCollection("Order").document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        Timestamp createdDate = documentSnapshot.getTimestamp("createdDate");
                        String recepientName = documentSnapshot.getString("recipientName");
                        String phonenumber = documentSnapshot.getString("phoneNumber");
                        String address = documentSnapshot.getString("address");
                        String deliverymethod = documentSnapshot.getString("deliveryMethod");
                        Order orderinfo = new Order(UID, deliverymethod, createdDate.getSeconds()*1000, recepientName, phonenumber, address);
                        orderInfo.setValue(orderinfo);
                        Log.e("Phuc", orderinfo.getId());
                    } else {
                        Log.d(TAG, "user not found");
                    }
                });
        return orderInfo;
    }
}
