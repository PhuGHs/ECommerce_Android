package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Order;
import com.example.ecommerce_hvpp.model.OrderDetail;
import com.example.ecommerce_hvpp.model.OrderHistoryItem;
import com.example.ecommerce_hvpp.model.OrderHistorySubItem;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedBackRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<List<Feedback>> _mldListReviewedFeedback = new MutableLiveData<>();
    private MutableLiveData<List<OrderHistorySubItem>> _mldListUnreviewedItem = new MutableLiveData<>();
    private List<String> orderidList = new ArrayList<>();
    private MutableLiveData<OrderHistorySubItem> unreviewedItem = new MutableLiveData<>();
    private final String TAG = "FeedBackRepository";
    public FeedBackRepository(){
        firebaseHelper = FirebaseHelper.getInstance();
    }
    public LiveData<List<Feedback>> getListReviewedFeedback(String UID){
        firebaseHelper.getCollection("Feedback").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Feedback> feedbacks = new ArrayList<>();
                    Feedback fb1 = null;
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("customer_id").equals(UID)){
                            String comment = snapshot.getString("comment");
                            Timestamp time_feedback = snapshot.getTimestamp("date");
                            long point = snapshot.getLong("point");
                            String product_id = snapshot.getString("product_id");
                            fb1 = new Feedback(comment, point, time_feedback.getSeconds()*1000, product_id);
                        }
                    }
                    feedbacks.add(fb1);
                    Log.d(TAG, "name product: " + fb1.getProductID());
                    _mldListReviewedFeedback.setValue(feedbacks);

                })
                .addOnFailureListener(e -> {
                    _mldListReviewedFeedback.setValue(null);
                });
        return _mldListReviewedFeedback;
    }
    public LiveData<List<OrderHistorySubItem>> getOrderId(String UID){
        firebaseHelper.getCollection("users").document(UID).collection("bought_items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OrderHistorySubItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        if (snapshot.getBoolean("isReviewed").equals(false)){
                            boolean isreviewed = snapshot.getBoolean("isReviewed");
                            String productid = snapshot.getString("productId");
                            String image = snapshot.getString("image");
                            String name = snapshot.getString("name");
                            double price = snapshot.getDouble("price");
                            long quantity = snapshot.getLong("quantity");
                            items.add(new OrderHistorySubItem(productid, image, name, Long.toString(quantity), price, isreviewed));
                        }
                    }
                    _mldListUnreviewedItem.setValue(items);
                })
                .addOnFailureListener(e -> {
                    _mldListUnreviewedItem.setValue(null);
                });
        return _mldListUnreviewedItem;
    }
    public void addFeedback(String UID, String content, float point, String product_id, Timestamp date){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("comment", content);
        feedback.put("point", point);
        feedback.put("customer_id", UID);
        feedback.put("product_id", product_id);
        feedback.put("date", date);

        fs.collection("Feedback").document().set(feedback);
    }
    public void updateProduct(String UID, String product_id){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        firebaseHelper.getCollection("users").document(UID).collection("bought_items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getString("productId").equals(product_id)){
                            DocumentReference ref = fs.collection("users").document(UID).collection("bought_items").document(snapshot.getId());
                            ref.update("isReviewed", true);
                            Log.d(TAG, "cap nhat thanh cong");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "khong the cap nhat");
                });
    }
}
