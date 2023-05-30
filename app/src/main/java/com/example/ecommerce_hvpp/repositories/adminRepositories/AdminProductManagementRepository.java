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
import com.google.firebase.Timestamp;
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
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String id = documentSnapshot.getString("id");
                            String name = documentSnapshot.getString("name");
                            String club = documentSnapshot.getString("club");
                            String nation = documentSnapshot.getString("nation");
                            String season = documentSnapshot.getString("season");
                            double Price = documentSnapshot.getDouble("price");
                            double Point = documentSnapshot.getDouble("pointAvg");
                            String urlmain = documentSnapshot.getString("url_main");
                            String urlsub1 = documentSnapshot.getString("url_sub1");
                            String urlsub2 = documentSnapshot.getString("url_sub2");
                            String urlthumb = documentSnapshot.getString("url_thumb");
                            long sizeM = documentSnapshot.getLong("size_m");
                            long sizeL = documentSnapshot.getLong("size_l");
                            long sizeXL = documentSnapshot.getLong("size_xl");
                            String status = documentSnapshot.getString("status");
                            Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                            String desc = documentSnapshot.getString("description");
                            products.add(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds()*1000));
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
                        String id = documentSnapshot.getString("id");
                        String name = documentSnapshot.getString("name");
                        String club = documentSnapshot.getString("club");
                        String nation = documentSnapshot.getString("nation");
                        String season = documentSnapshot.getString("season");
                        double Price = documentSnapshot.getDouble("price");
                        double Point = documentSnapshot.getDouble("pointAvg");
                        String urlmain = documentSnapshot.getString("url_main");
                        String urlsub1 = documentSnapshot.getString("url_sub1");
                        String urlsub2 = documentSnapshot.getString("url_sub2");
                        String urlthumb = documentSnapshot.getString("url_thumb");
                        long sizeM = documentSnapshot.getLong("size_m");
                        long sizeL = documentSnapshot.getLong("size_l");
                        long sizeXL = documentSnapshot.getLong("size_xl");
                        String status = documentSnapshot.getString("status");
                        Timestamp timeAdded = documentSnapshot.getTimestamp("time_added");
                        String desc = documentSnapshot.getString("description");
                        _mldProduct.setValue(Resource.success(new Product(id, name, club, nation, season, desc, Price, Point, sizeM, sizeL, sizeXL, urlmain, urlsub1, urlsub2, urlthumb, status, timeAdded.getSeconds()*1000)));
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
        Map<String, Object> add = new HashMap<>();
        add.put("club", pd.getClub());
        add.put("description", pd.getDescription());
        add.put("name", pd.getName());
        add.put("nation", pd.getNation());
        add.put("pointAvg", pd.getPointAvg());
        add.put("price", pd.getPrice());
        add.put("season", pd.getSeason());
        add.put("size_l", pd.getSize_l());
        add.put("size_m", pd.getSize_m());
        add.put("size_xl", pd.getSize_xl());
        add.put("url_thumb", pd.getUrlthumb());
        add.put("url_main", pd.getUrlmain());
        add.put("url_sub1", pd.getUrlsub1());
        add.put("url_sub2", pd.getUrlsub2());
        add.put("status", pd.getStatus());
        add.put("time_added", new Timestamp(pd.getTimeAdded() / 1000, 0));

        fbHelper.getCollection("Product").add(add)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String documentId = documentReference.getId();
                        pd.setId(documentId);
                        updateProductWithId(documentReference, pd);
                        _mldUAddProduct.setValue(Resource.success("added product"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
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
        updates.put("point", pd.getPointAvg());
        updates.put("price", pd.getPrice());
        updates.put("season", pd.getSeason());
        updates.put("size_l", pd.getSize_l());
        updates.put("size_m", pd.getSize_m());
        updates.put("size_xl", pd.getSize_xl());
        updates.put("url_thumb", pd.getUrlthumb());
        updates.put("url_main", pd.getUrlmain());
        updates.put("url_sub1", pd.getUrlsub1());
        updates.put("url_sub2", pd.getUrlsub2());
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

