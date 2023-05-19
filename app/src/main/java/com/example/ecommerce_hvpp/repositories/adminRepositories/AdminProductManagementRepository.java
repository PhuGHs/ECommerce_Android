package com.example.ecommerce_hvpp.repositories.adminRepositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminProductManagementRepository {
    private final FirebaseHelper fbHelper;
    private MutableLiveData<Resource<List<Product>>> _mldProductList;
    private MutableLiveData<Resource<Product>> _mldProduct;
    private final String TAG = "AdminProductManagementRepository";
    public AdminProductManagementRepository() {
        fbHelper = FirebaseHelper.getInstance();
        _mldProductList = new MutableLiveData<>();
        _mldProduct = new MutableLiveData<>();
    }

    public LiveData<Resource<List<Product>>> getAllProductWithNoCriteria() {
        _mldProductList.setValue(Resource.loading(null));
        fbHelper.getCollection("Product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Product> products = new ArrayList<>();
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Product pd = snapshot.toObject(Product.class);
                            products.add(pd);
                        }
                        _mldProductList.setValue(Resource.success(products));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        _mldProductList.setValue(Resource.error(e.getMessage(), null));
                        Log.d(TAG, "on Failure get all " + e.getMessage());
                    }
                });
        return _mldProductList;
    }

    public LiveData<Resource<Product>> getProduct(String Id) {
        _mldProduct.setValue(Resource.loading(null));
        fbHelper.getCollection("Product").document(Id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        _mldProduct.setValue(Resource.success(documentSnapshot.toObject(Product.class)));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        _mldProductList.setValue(Resource.error(e.getMessage(), null));
                    }
                });
        return _mldProduct;
    }

    public void addProduct(Product pd) {
        fbHelper.getCollection("Product").add(pd)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with Id: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document");
                    }
                });
    }
}

