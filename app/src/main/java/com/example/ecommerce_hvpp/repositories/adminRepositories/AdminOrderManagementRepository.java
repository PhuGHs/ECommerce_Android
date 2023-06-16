package com.example.ecommerce_hvpp.repositories.adminRepositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderDetail;
import com.example.ecommerce_hvpp.model.Product;
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
                                String deliveryMethod = documentSnapshot.getString("deliverMethod");
                                String paymentMethod = documentSnapshot.getString("paymentMethod");
                                String recipientName = documentSnapshot.getString("recipientName");
                                String note = documentSnapshot.getString("note");
                                String phoneNumber = documentSnapshot.getString("phoneNumber");
                                String orderStatus = documentSnapshot.getString("status");
                                long createdDate = documentSnapshot.getTimestamp("createdDate").toDate().getTime();
//                                long updateDate = documentSnapshot.getTimestamp("updateDate").toDate().getTime();
                                long receiveDate = documentSnapshot.getTimestamp("receiveDate").toDate().getTime();
                                double totalPrice = documentSnapshot.getDouble("totalPrice");
                                order = new Order(documentId, address, customerId, deliveryMethod, paymentMethod, recipientName, note, phoneNumber, orderStatus, createdDate, receiveDate, totalPrice);
                                List<OrderDetail> orderDetails = new ArrayList<>();
                                CollectionReference nestedCol = documentSnapshot.getReference().collection("items");
                                nestedCol.get()
                                        .addOnCompleteListener(nestedTask -> {
                                            if(nestedTask.isSuccessful()) {
                                                QuerySnapshot nestedShot = nestedTask.getResult();
                                                if(nestedShot != null) {
                                                    for(DocumentSnapshot nestedDocument : nestedShot.getDocuments()) {
                                                        String id = nestedDocument.getId();
                                                        String product_id = nestedDocument.getString("product_id");
                                                        Product pd = MainActivity.PDviewModel.getDetailProduct(product_id).getValue();
                                                        String image = pd.getUrlthumb();
                                                        String name = pd.getName();
                                                        double price = pd.getPrice();
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
                                orders.add(order);
                            }
                            Log.i("size", String.valueOf(orders.size()));
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
                            order = new Order(documentId, address, customerId, deliveryMethod, paymentMethod, recipientName, note, phoneNumber, orderStatus, createdDate, receiveDate, totalPrice);
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

//    public void createDummyOrder() {
//        List<Voucher> vouchers = new ArrayList<>();
//        List<Order> orders = new ArrayList<>();
//        vouchers.add(new Voucher("England, France, Germany, Spain", 6, "voucher1", "FES", 4.2, System.currentTimeMillis(), System.currentTimeMillis()));
//        vouchers.add(new Voucher("England, France, Germany, Spain", 6, "voucher1", "FES", 4.2, System.currentTimeMillis(), System.currentTimeMillis()));
//
//        List<OrderDetail> items = new ArrayList<>();
//        String image = "https://firebasestorage.googleapis.com/v0/b/ecommerce-hvpp.appspot.com/o/uploads%2F1684920025553.jpg?alt=media&token=4c680f30-2475-4322-87c2-4190cae39b58";
//        items.add(new OrderDetail("item1", image, "Bayern Munich Away", (float) 35.2, 2, "L"));
//        items.add(new OrderDetail("item2", image, "Bayern Munich Home", (float) 32.2, 2, "XL"));
//        orders.add(new Order("1", "43 Tan Lap, Di An, Binh Duong", "CUS001", "HVPPXpress", "Cash", "Lê Văn Phú", "please", "0814321006", "PENDING", System.currentTimeMillis(), System.currentTimeMillis(), (float) 485.2, vouchers, items));
//        orders.add(new Order("2", "47 Tan Lap, Dong Hoa, Di An, Binh Duong", "CUS002", "GHTK", "VISA DEBIT", "Lê Văn Phi", "please", "0814321006", "DELIVERING", System.currentTimeMillis(), System.currentTimeMillis(), (float) 325.2, vouchers, items));
//        orders.add(new Order("3", "47 Tan Lap, Dong Hoa, Di An, Binh Duong", "CUS002", "GHTK", "VISA DEBIT", "Lê Văn Phi", "please", "0814321006", "CANCELED", System.currentTimeMillis(), System.currentTimeMillis(), (float) 325.2, vouchers, items));
//
//        fbHelper.getCollection("Order").add(orders.get(0));
//    }

}
