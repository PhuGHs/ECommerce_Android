package com.example.ecommerce_hvpp.viewmodel.Customer;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Feedback;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private FirebaseHelper helper;
    private List<Product> listNewArrivals, listBestSeller, listFavorite;
    private MutableLiveData<List<Product>> mldListNewArrivals, mldListBestSeller, mldListFavorite;
    private MutableLiveData<List<Feedback>> mldListFeedback;
    private MutableLiveData<Product> detailProduct;
    private String TAG = "Product ViewModel";
    public ProductViewModel(){
        //init
        helper = FirebaseHelper.getInstance();
        mldListNewArrivals = new MutableLiveData<>();
        mldListBestSeller = new MutableLiveData<>();
        mldListFavorite = new MutableLiveData<>();
        mldListFeedback = new MutableLiveData<>();

        initListNewArrivalsLiveData();
        initListBestSellerLiveData();
        initListFavoriteLiveData();
    }

    private void initListFavoriteLiveData() {
        listFavorite = new ArrayList<>();

        helper.getCollection("Product").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                        Product product = documentSnapshot.toObject(Product.class);

                        Log.d(TAG, product.getID() + "--" + product.getName());

                        listFavorite.add(product);
                    }
                    mldListFavorite.setValue(listFavorite);
                });
    }

    private void initListBestSellerLiveData() {
        listBestSeller = new ArrayList<>();

        helper.getCollection("Product").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Product product = documentSnapshot.toObject(Product.class);

                        Log.d(TAG, product.getID() + "--" + product.getName());

                        listBestSeller.add(product);
                    }
                    mldListBestSeller.setValue(listBestSeller);
                });
    }

    private void initListNewArrivalsLiveData() {
        listNewArrivals = new ArrayList<>();

        helper.getCollection("Product").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Product product = documentSnapshot.toObject(Product.class);

                        Log.d(TAG, product.getID() + "--" + product.getName());

                        listNewArrivals.add(product);
                    }
                    mldListNewArrivals.setValue(listNewArrivals);
                });
    }

    public MutableLiveData<List<Product>> getMldListNewArrivals() {
        return mldListNewArrivals;
    }

    public MutableLiveData<List<Product>> getMldListBestSeller() {
        return mldListBestSeller;
    }

    public MutableLiveData<List<Product>> getMldListFavorite() {
        return mldListFavorite;
    }

    public MutableLiveData<Product> getDetailProduct(String productID) {
        detailProduct = new MutableLiveData<>();

        helper.getCollection("Product").document(productID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        Product product = documentSnapshot.toObject(Product.class);

                        detailProduct.setValue(product);
                    }
                });

        return detailProduct;
    }
    public MutableLiveData<List<Feedback>> getFeedbackProduct(String productID){
        List<Feedback> listFeedback = new ArrayList<>();

        helper.getCollection("Feedback").whereEqualTo("product_id", productID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String name = documentSnapshot.getString("name");
                        String customerID = documentSnapshot.getString("customer_id");
                        String product_ID = documentSnapshot.getString("product_id");
                        String comment = documentSnapshot.getString("comment");
                        Long point = documentSnapshot.getLong("point");
                        Timestamp date = documentSnapshot.getTimestamp("date");

                        Log.d(TAG, date.toString());

                        listFeedback.add(new Feedback(customerID, productID, point, date.getSeconds() * 1000, comment));
                    }
                    mldListFeedback.setValue(listFeedback);
                });
        return mldListFeedback;
    }
}
