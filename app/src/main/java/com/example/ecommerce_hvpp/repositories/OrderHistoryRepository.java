package com.example.ecommerce_hvpp.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
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
    private List<OrderHistorySubItem> _mldListSubOrderHistory = new ArrayList<>();
    private final String TAG = "OrderHistoryRepository";
    private String imagepath;
    private String name;
    private double price;
    private long ID_of_order;
    private long count = 0;
    public OrderHistoryRepository(){
        _mldListOrderHistory = new MutableLiveData<>();
        _mldListSubOrderHistory = new ArrayList<>();
        firebaseHelper = FirebaseHelper.getInstance();
    }
    public List<OrderHistorySubItem> getAll_Items(long ID) {
        firebaseHelper.getCollection("OrderDetail").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getLong("order_id").equals(ID)){
                            long quantity = snapshot.getLong("quantity");
                            firebaseHelper.getCollection("Product").document(snapshot.getString("product_id")).get()
                                    .addOnSuccessListener(documentSnapshots -> {
                                        if (documentSnapshots.exists()){
                                            imagepath = documentSnapshots.getString("url_main");
                                            name = documentSnapshots.getString("name");
                                            price = documentSnapshots.getDouble("price");
                                        }
                                    });
                            Log.d(TAG, "lay thanh cong");
                            _mldListSubOrderHistory.add(new OrderHistorySubItem(imagepath, name, quantity, quantity*price));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "lay items that bai");
                });
        return _mldListSubOrderHistory;
    }
    public long getID_ofOrder(String UID){
        firebaseHelper.getCollection("Order").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customer_id").equals(UID)){
                            ID_of_order = snapshot.getLong("id");
                            Log.d(TAG, "Lay id thanh cong " + ID_of_order);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Khong co id ");
                });
        return ID_of_order;
    }
    public LiveData<List<OrderHistoryItem>> getAll_OrderHistories(String UID) {
        firebaseHelper.getCollection("Order").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OrderHistoryItem> orderhistories = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customer_id").equals(UID)){
                            Timestamp time_order = snapshot.getTimestamp("time_create");
                            long all_quantity = setQuantityofItem(snapshot.getLong("id"));
                            String sum_of_order = snapshot.getString("value");
                            List<OrderHistorySubItem> list_items = getAll_Items(snapshot.getLong("id"));
                            orderhistories.add(new OrderHistoryItem(all_quantity, sum_of_order, time_order.getSeconds()*1000, list_items));
                        }
                    }
                    _mldListOrderHistory.setValue(orderhistories);
                })
                .addOnFailureListener(e -> {
                    _mldListOrderHistory.setValue(null);
                });
        return _mldListOrderHistory;
    }
    public long setQuantityofItem(long order_id){
        firebaseHelper.getCollection("OrderDetail").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getLong("order_id").equals(order_id)){
                            long quantity = snapshot.getLong("quantity");
                            count = count + quantity;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    count = 0;
                });
        return count;
    }
}
