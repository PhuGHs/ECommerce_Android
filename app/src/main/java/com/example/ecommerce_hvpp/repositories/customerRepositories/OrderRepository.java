package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<List<Order>> _mldListOrder = new MutableLiveData<>();
    private MutableLiveData<List<OrderHistorySubItem>> _mldListItemsOrder = new MutableLiveData<>();
    private MutableLiveData<Order> orderprogressInfo = new MutableLiveData<>();
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
                        if (snapshot.getString("customerId").equals(UID)){
                            String title = snapshot.getString("name");
                            String id = snapshot.getString("id");
                            Date date_end = snapshot.getDate("receiveDate");
                            int day_remaining = getDayRemaining(date_end);

                            Log.d(TAG, "Them thanh cong " + id);
                            orders.add(new Order(id, title, day_remaining));
                        }
                    }
                    _mldListOrder.setValue(orders);
                })
                .addOnFailureListener(e -> {
                    _mldListOrder.setValue(null);
                });
        return _mldListOrder;
    }
    public LiveData<List<OrderHistorySubItem>> getItemsofOrder(String order_id){
        firebaseHelper.getCollection("Order")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    for (DocumentSnapshot snapshot : documentSnapshot){
                        if (snapshot.getString("id").equals(order_id)){
                            firebaseHelper.getCollection("Order").document(snapshot.getId()).collection("items")
                                    .get()
                                    .addOnSuccessListener(documentSnapshot1 -> {
                                        List<OrderHistorySubItem> items = new ArrayList<>();
                                        for (QueryDocumentSnapshot snapshot1 : documentSnapshot1){
                                            long quantity = snapshot1.getLong("quantity");
                                            String product_id = snapshot1.getString("product_id");
                                            String image = MainActivity.PDviewModel.listAllProduct.get(product_id).getUrlthumb();
                                            String name = MainActivity.PDviewModel.listAllProduct.get(product_id).getName();
                                            double price = MainActivity.PDviewModel.listAllProduct.get(product_id).getPrice() * quantity;
                                            items.add(new OrderHistorySubItem(image, name, Long.toString(quantity), price));
                                            Log.d(TAG, "get items order successfully");
                                        }
                                        _mldListItemsOrder.setValue(items);
                                    })
                                    .addOnFailureListener(e1 -> {
                                        _mldListItemsOrder.setValue(null);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    _mldListItemsOrder.setValue(null);
                });
        return _mldListItemsOrder;
    }
    public LiveData<Order> getOrderprogressInfo(String order_id){
        firebaseHelper.getCollection("Order")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    for (DocumentSnapshot snapshot : documentSnapshot){
                        if (snapshot.getString("id").equals(order_id)){
                            String address = snapshot.getString("address");
                            String deliveryMethod = snapshot.getString("deliverMethod");
                            Timestamp startDate = snapshot.getTimestamp("createdDate");
                            Date date_end = snapshot.getDate("receiveDate");
                            int day_remaining = getDayRemaining(date_end);
                            orderprogressInfo.setValue(new Order(address, startDate.getSeconds()*1000, deliveryMethod, day_remaining));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    orderprogressInfo.setValue(null);
                });
        return orderprogressInfo;
    }
    public int getDayRemaining(Date date_end){
        Date now = new Date();
        difference = date_end.getTime() - now.getTime();
        daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
        return daysBetween;
    }

}
