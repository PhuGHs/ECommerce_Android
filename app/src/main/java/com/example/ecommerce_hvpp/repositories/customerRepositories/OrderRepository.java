package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                            String status = snapshot.getString("status");

                            Log.d(TAG, "Them thanh cong " + id);
                            orders.add(new Order(id, title, status));
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
    public void confirmOrder(String UID, String order_id){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        firebaseHelper.getCollection("Order").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customerId").equals(UID)){
                            Map<String, Object> orderhistory = new HashMap<>();
                            orderhistory.put("address", snapshot.getString("address"));
                            orderhistory.put("createdDate", snapshot.getTimestamp("createdDate"));
                            orderhistory.put("deliveryMethod", snapshot.getString("deliverMethod"));
                            orderhistory.put("name", snapshot.getString("name"));
                            orderhistory.put("orderId", snapshot.getString("id"));
                            orderhistory.put("phoneNumber", snapshot.getString("phoneNumber"));
                            orderhistory.put("receiveDate", snapshot.getTimestamp("receiveDate"));
                            orderhistory.put("recipientName", snapshot.getString("recipientName"));
                            orderhistory.put("totalPrice", snapshot.getLong("totalPrice"));
                            orderhistory.put("totalQuantity", snapshot.getLong("totalQuantity"));

                            Log.d(TAG, "Them thanh cong " + order_id);
                            fs.collection("users").document(UID).collection("orderhistory").document(snapshot.getString("id")).set(orderhistory);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    return;
                });
    }
    public void confirmItemsOfOrder(String UID, String order_id){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        firebaseHelper.getCollection("Order")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    for (DocumentSnapshot snapshot : documentSnapshot){
                        if (snapshot.getString("id").equals(order_id)){
                            firebaseHelper.getCollection("Order").document(snapshot.getId()).collection("items")
                                    .get()
                                    .addOnSuccessListener(documentSnapshot1 -> {
                                        for (QueryDocumentSnapshot snapshot1 : documentSnapshot1){
                                            Map<String, Object> item = new HashMap<>();
                                            long quantity = snapshot1.getLong("quantity");
                                            String product_id = snapshot1.getString("product_id");
                                            String image = MainActivity.PDviewModel.listAllProduct.get(product_id).getUrlthumb();
                                            String name = MainActivity.PDviewModel.listAllProduct.get(product_id).getName();
                                            double price = MainActivity.PDviewModel.listAllProduct.get(product_id).getPrice() * quantity;

                                            item.put("image", image);
                                            item.put("isReviewed", false);
                                            item.put("name", name);
                                            item.put("orderId", order_id);
                                            item.put("price", price);
                                            item.put("productId", product_id);
                                            item.put("quantity", quantity);

                                            Log.d(TAG, "get items order successfully");
                                            fs.collection("users").document(UID).collection("bought_items").document().set(item);
                                        }
                                    })
                                    .addOnFailureListener(e1 -> {
                                        return;
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    return;
                });
    }

}
