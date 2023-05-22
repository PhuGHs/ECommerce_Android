package com.example.ecommerce_hvpp.repositories.adminRepositories;

import android.net.Uri;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AdminProductManagementRepository {
    private final FirebaseHelper fbHelper;
    private MutableLiveData<Resource<List<Product>>> _mldProductList;
    private MutableLiveData<Resource<String>> _mldUAddProduct = new MutableLiveData<>();
    private MutableLiveData<Resource<Product>> _mldProduct;
    private final String TAG = "AdminProductManagementRepository";
    private StorageReference imageRef;
    public AdminProductManagementRepository() {
        fbHelper = FirebaseHelper.getInstance();
        _mldProductList = new MutableLiveData<>();
        _mldProduct = new MutableLiveData<>();
        imageRef = fbHelper.getImageStorage();
    }

    public interface OnImageUploadListener {
        void onImageUploadSuccess(String imageUrl);
        void onImageUploadFailure(String exception);
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

    public LiveData<Resource<String>> addProduct(Product pd) {
        _mldUAddProduct.setValue(Resource.loading(null));
        fbHelper.getCollection("Product").add(pd)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        _mldUAddProduct.setValue(Resource.success("added product"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        _mldUAddProduct.setValue(Resource.error("Error adding product", null));
                    }
                });
        return _mldUAddProduct;
    }

    public void uploadImage(Uri uri, String fileType, final OnImageUploadListener listener) {
        String path = System.currentTimeMillis() + "." + fileType;
        UploadTask uploadTask = imageRef.child(path).putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.child(path).getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                listener.onImageUploadSuccess(downloadUrl.toString());
            }).addOnFailureListener(e -> {
                listener.onImageUploadFailure(e.getMessage());
            });
        }).addOnFailureListener(e -> {
            listener.onImageUploadFailure(e.getMessage());
        });
    }


}

