package com.example.ecommerce_hvpp.repositories.customerRepositories;

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
    private MutableLiveData<Long> _mldQuantity = new MutableLiveData<>();
    private final String TAG = "OrderHistoryRepository";
    private String imagepath;
    private String name;
    private double price;
    private long count = 0;
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
                            Log.d(TAG, "get id document: " + snapshot.getId());
                            Timestamp time_order = snapshot.getTimestamp("time_create");
                            long all_quantity = getQuantityofItem(snapshot.getId()).getValue();
                            String sum_of_order = snapshot.getString("value");
                            Log.d(TAG,  "Tong san pham: " + all_quantity);
                            orderhistories.add(new OrderHistoryItem(all_quantity, sum_of_order, time_order.getSeconds()*1000));
                        }
                    }
                    _mldListOrderHistory.setValue(orderhistories);
                })
                .addOnFailureListener(e -> {
                    _mldListOrderHistory.setValue(null);
                });
        return _mldListOrderHistory;
    }
    public void setQuantityofItem(String document_id){
        db = FirebaseFirestore.getInstance();
        db.collection("Order").document(document_id).collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String quantity = document.getString("quantity");
                                count = count + Long.valueOf(quantity);
                            }
                            _mldQuantity.setValue(count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public LiveData<Long> getQuantityofItem(String document_id){
        firebaseHelper.getCollection("Order")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getId().equals(document_id)){
                            CollectionReference collectionReference = snapshot.getReference().collection("products");
                            collectionReference.get()
                                    .addOnSuccessListener(nestedTask ->{
                                        for(QueryDocumentSnapshot nestedDocument : queryDocumentSnapshots){
                                            String quantity = nestedDocument.getString("quantity");
                                            count = count + Long.valueOf(quantity);
                                        }
                                    });
                        }
                    }
                    _mldQuantity.setValue(count);
                })
                .addOnFailureListener(e -> {
                    _mldQuantity.setValue(null);
                });
        return _mldQuantity;
    }

}
