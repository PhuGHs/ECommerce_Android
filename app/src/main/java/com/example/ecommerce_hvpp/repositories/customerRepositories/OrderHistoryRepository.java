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
    private MutableLiveData<List<OrderHistoryItem>> _mldListOrderHistory = new MutableLiveData<>();
    private MutableLiveData<List<OrderHistorySubItem>> _mldListSubOrderHistory = new MutableLiveData<>();
    private MutableLiveData<Order> orderInfo = new MutableLiveData<>();
    private MutableLiveData<Integer> num_of_completeorder = new MutableLiveData<>();
    private MutableLiveData<Double> total_moneypaid = new MutableLiveData<>();
    private final String TAG = "OrderHistoryRepository";
    private long count = 0;
    private long all_quantity = 0;
    public OrderHistoryRepository(){
        _mldListOrderHistory = new MutableLiveData<>();
        _mldListSubOrderHistory = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
    }
    public LiveData<List<OrderHistoryItem>> getAll_OrderHistories(String UID) {
        firebaseHelper.getCollection("users").document(UID).collection("orderhistory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OrderHistoryItem> orderhistories = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Timestamp time_order = snapshot.getTimestamp("createdDate");
                        long all_quantity = snapshot.getLong("totalQuantity");
                        double sum_of_order = snapshot.getDouble("totalPrice");
                        Log.d(TAG,  "Tong san pham: " + all_quantity);
                        orderhistories.add(new OrderHistoryItem(snapshot.getId(), all_quantity, sum_of_order, time_order.getSeconds()*1000));
                    }
                    _mldListOrderHistory.setValue(orderhistories);
                })
                .addOnFailureListener(e -> {
                    _mldListOrderHistory.setValue(null);
                });
        return _mldListOrderHistory;
    }
    public LiveData<List<OrderHistorySubItem>> getAll_ItemsOfOrder(String UID, String orderId){
        firebaseHelper.getCollection("users").document(UID).collection("bought_items")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<OrderHistorySubItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : documentSnapshot){
                        if (snapshot.getString("orderId").equals(orderId)){
                            String productid = snapshot.getString("productId");
                            String image = snapshot.getString("image");
                            String name = snapshot.getString("name");
                            double price = snapshot.getDouble("price");
                            long quantity = snapshot.getLong("quantity");
                            boolean isreviewed = snapshot.getBoolean("isReviewed");
                            items.add(new OrderHistorySubItem(productid, image, name, Long.toString(quantity), price, isreviewed));
                        }
                    }
                    _mldListSubOrderHistory.setValue(items);
                })
                .addOnFailureListener(e -> {
                    _mldListSubOrderHistory.setValue(null);
                });
        return _mldListSubOrderHistory;
    }
    public LiveData<Order> getOrderInfo(String UID, String orderId){
        firebaseHelper.getCollection("users").document(UID).collection("orderhistory").document(orderId)
                .get()
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
    public LiveData<Integer> getNum_of_completeOrder(String UID){
        firebaseHelper.getCollection("users").document(UID).collection("orderhistory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = 0;
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        count++;
                    }
                    num_of_completeorder.setValue(count);
                    Log.d(TAG, "num order " + count);
                })
                .addOnFailureListener(e -> {
                    num_of_completeorder.setValue(0);
                    Log.d(TAG, "khong lay duoc so luong order");
                });
        return num_of_completeorder;
    }
    public LiveData<Double> getTotal_of_MoneyPaid(String UID){
        firebaseHelper.getCollection("users").document(UID).collection("orderhistory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    double total_sum = 0;
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        double price = snapshot.getDouble("totalPrice");
                        total_sum = total_sum + price;
                    }
                    total_moneypaid.setValue(total_sum);
                    Log.d(TAG, "total sum " + total_sum);
                })
                .addOnFailureListener(e -> {
                    total_moneypaid.setValue(null);
                    Log.d(TAG, "khong lay duoc tong tien order");
                });
        return total_moneypaid;
    }
}
