package com.example.ecommerce_hvpp.repositories.adminRepositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderDetail;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminOrderManagementRepository {
    private final FirebaseHelper fbHelper;
    private MutableLiveData<Resource<List<Order>>> _mldGetOrders;
    private MutableLiveData<Resource<List<Order>>> _mldGetOrdersByStatus;
    public AdminOrderManagementRepository() {
        fbHelper = FirebaseHelper.getInstance();
        _mldGetOrders = new MutableLiveData<>();
    }

    public LiveData<Resource<List<Order>>> getOrders() {
        _mldGetOrders.setValue(Resource.loading(null));
        CollectionReference collectionRef = fbHelper.getCollection("Order");
        collectionRef.get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if(querySnapshot != null) {
                            List<Order> orders = new ArrayList<>();
                            Order order = null;
                            for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                String documentId = documentSnapshot.getId();
                                String address = documentSnapshot.getString("address");
                                String customerId = documentSnapshot.getString("customerId");
                                String deliveryMethod = documentSnapshot.getString("deliveryMethod");
                                String paymentMethod = documentSnapshot.getString("paymentMethod");
                                String recipientName = documentSnapshot.getString("recipientName");
                                String note = documentSnapshot.getString("note");
                                String phoneNumber = documentSnapshot.getString("phoneNumber");
                                String orderStatus = documentSnapshot.getString("status");
                                long createdDate = documentSnapshot.getTimestamp("createdDate").toDate().getTime();
                                long updateDate = documentSnapshot.getTimestamp("updateDate").toDate().getTime();
                                long receiveDate = documentSnapshot.getTimestamp("receiveDate").toDate().getTime();
                                double totalPrice = documentSnapshot.getDouble("totalPrice");
                                List<Voucher> vouchers = new ArrayList<>();
                                List<Map<String, Object>> voucherList = (List<Map<String, Object>>) documentSnapshot.get("voucherList");
                                if (voucherList != null) {
                                    for (Map<String, Object> voucherMap : voucherList) {
                                        // Create a new Voucher object using the voucherMap
                                        // and add it to the vouchers list
                                        Voucher voucher = new Voucher(voucherMap.get("applyFor").toString(), Double.parseDouble(voucherMap.get("condition").toString()), voucherMap.get("Id").toString(), voucherMap.get("voucherName").toString(),  Double.parseDouble(voucherMap.get("discountedValue").toString()));
                                        vouchers.add(voucher);
                                    }
                                }
                                order = new Order(documentId, address, customerId, deliveryMethod, paymentMethod, recipientName, note, phoneNumber, orderStatus, createdDate, receiveDate, totalPrice, vouchers);
                                List<OrderDetail> orderDetails = new ArrayList<>();
                                CollectionReference nestedCol = documentSnapshot.getReference().collection("items");
                                nestedCol.get()
                                        .addOnCompleteListener(nestedTask -> {
                                            if(nestedTask.isSuccessful()) {
                                                QuerySnapshot nestedShot = nestedTask.getResult();
                                                if(nestedShot != null) {
                                                    for(DocumentSnapshot nestedDocument : nestedShot.getDocuments()) {
                                                        String id = nestedDocument.getId();
                                                        String image = nestedDocument.getString("image");
                                                        Double price = nestedDocument.getDouble("price");
                                                        String name = nestedDocument.getString("name");
                                                        long quantity = nestedDocument.getLong("quantity");
                                                        String size = nestedDocument.getString("size");

                                                        orderDetails.add(new OrderDetail(id, image, name, price, (int) quantity, size));
                                                    }
                                                }
                                            }
                                            else {
                                                _mldGetOrders.setValue(Resource.error(nestedTask.getException().getMessage(), null));
                                            }
                                        });
                                order.setItems(orderDetails);
                            }
                            orders.add(order);
                            _mldGetOrders.setValue(Resource.success(orders));
                        }
                    } else {
                        _mldGetOrders.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
        return _mldGetOrders;
    }

    public LiveData<Resource<List<Order>>> getOrdersByStatus(String status) {
        _mldGetOrdersByStatus = new MutableLiveData<>();
        _mldGetOrdersByStatus.setValue(Resource.loading(null));
        fbHelper.getCollection("Order").whereEqualTo("status", status).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Order> orders = new ArrayList<>();
                        Order order = null;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String documentId = documentSnapshot.getId();
                            String address = documentSnapshot.getString("address");
                            String customerId = documentSnapshot.getString("customerId");
                            String deliveryMethod = documentSnapshot.getString("deliveryMethod");
                            String paymentMethod = documentSnapshot.getString("paymentMethod");
                            String recipientName = documentSnapshot.getString("recipientName");
                            String note = documentSnapshot.getString("note");
                            String phoneNumber = documentSnapshot.getString("phoneNumber");
                            String orderStatus = documentSnapshot.getString("status");
                            long createdDate = documentSnapshot.getTimestamp("createdDate").toDate().getTime();
                            long updateDate = documentSnapshot.getTimestamp("updateDate").toDate().getTime();
                            long receiveDate = documentSnapshot.getTimestamp("receiveDate").toDate().getTime();
                            double totalPrice = documentSnapshot.getDouble("totalPrice");
                            List<Voucher> vouchers = new ArrayList<>();
                            List<Map<String, Object>> voucherList = (List<Map<String, Object>>) documentSnapshot.get("voucherList");
                            if (voucherList != null) {
                                for (Map<String, Object> voucherMap : voucherList) {
                                    // Create a new Voucher object using the voucherMap
                                    // and add it to the vouchers list
                                    Voucher voucher = new Voucher(voucherMap.get("applyFor").toString(), Double.parseDouble(voucherMap.get("condition").toString()), voucherMap.get("Id").toString(), voucherMap.get("voucherName").toString(),  Double.parseDouble(voucherMap.get("discountedValue").toString()));
                                    vouchers.add(voucher);
                                }
                            }
                            order = new Order(documentId, address, customerId, deliveryMethod, paymentMethod, recipientName, note, phoneNumber, orderStatus, createdDate, receiveDate, totalPrice, vouchers);
                            List<OrderDetail> orderDetails = new ArrayList<>();
                            CollectionReference nestedCol = documentSnapshot.getReference().collection("items");
                            nestedCol.get()
                                    .addOnCompleteListener(nestedTask -> {
                                        if(nestedTask.isSuccessful()) {
                                            QuerySnapshot nestedShot = nestedTask.getResult();
                                            if(nestedShot != null) {
                                                for(DocumentSnapshot nestedDocument : nestedShot.getDocuments()) {
                                                    String id = nestedDocument.getId();
                                                    String image = nestedDocument.getString("image");
                                                    Double price = nestedDocument.getDouble("price");
                                                    String name = nestedDocument.getString("name");
                                                    long quantity = nestedDocument.getLong("quantity");
                                                    String size = nestedDocument.getString("size");

                                                    orderDetails.add(new OrderDetail(id, image, name, price, (int) quantity, size));
                                                }
                                            }
                                        }
                                        else {
                                            _mldGetOrdersByStatus.setValue(Resource.error(nestedTask.getException().getMessage(), null));
                                        }
                                    });
                            order.setItems(orderDetails);
                        }
                        orders.add(order);
                        _mldGetOrdersByStatus.setValue(Resource.success(orders));
                    }
                });
        return _mldGetOrdersByStatus;
    }

    public void updateOrder(String Id, String status) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("updateDate", new Timestamp(new Date(System.currentTimeMillis())));

        fbHelper.getCollection("Order").document(Id).update(updates);
    }

}
