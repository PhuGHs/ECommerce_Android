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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminProductManagementRepository {
    private final FirebaseHelper fbHelper;
    private MutableLiveData<Resource<List<Product>>> _mldProductList;
    private MutableLiveData<Resource<String>> _mldEdit;
    private MutableLiveData<Resource<String>> _mldUAddProduct = new MutableLiveData<>();
    private MutableLiveData<Resource<Product>> _mldProduct;
    private final String TAG = "AdminProductManagementRepository";
    private StorageReference imageRef;
    public AdminProductManagementRepository() {
        fbHelper = FirebaseHelper.getInstance();
        _mldProductList = new MutableLiveData<>();
        _mldProduct = new MutableLiveData<>();
        imageRef = fbHelper.getImageStorage();
        _mldEdit = new MutableLiveData<>();
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
                        String documentId = documentReference.getId();
                        pd.setId(documentId);
                        updateProductWithId(documentReference, pd);
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

    private void updateProductWithId(DocumentReference documentReference, Product pd) {
        documentReference
                .update("id", pd.getId())
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    public LiveData<Resource<String>> updateProduct(Product pd) {
        _mldEdit.setValue(Resource.loading(null));
        Map<String, Object> updates = new HashMap<>();
        updates.put("club", pd.getClub());
        updates.put("description", pd.getDescription());
        updates.put("name", pd.getName());
        updates.put("nation", pd.getNation());
        updates.put("point", pd.getPoint());
        updates.put("price", pd.getPrice());
        updates.put("season", pd.getSeason());
        updates.put("size_l", pd.getSize_l());
        updates.put("size_m", pd.getSize_m());
        updates.put("size_xl", pd.getSize_xl());
        updates.put("url_thumb", pd.getUrl_thumb());
        updates.put("url_main", pd.getUrl_main());
        updates.put("url_sub1", pd.getUrl_sub1());
        updates.put("url_sub2", pd.getUrl_sub2());
        updates.put("status", pd.getStatus());

        fbHelper.getCollection("Product").document(pd.getId())
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        _mldEdit.setValue(Resource.success("Product is modified successfully!"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        _mldEdit.setValue(Resource.error(e.getMessage(), null));
                    }
                });
        return _mldEdit;
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

