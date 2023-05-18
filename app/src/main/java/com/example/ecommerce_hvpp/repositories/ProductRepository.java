package com.example.ecommerce_hvpp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private FirebaseHelper helper = FirebaseHelper.getInstance();
    private MutableLiveData<Resource<Product>> _mldProduct = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Product>>> _mldListProduct = new MutableLiveData<>();
    private final String TAG = "ProductRepository";

    public LiveData<Resource<List<Product>>> getListNewArrivals(){
        _mldListProduct.setValue(Resource.loading(null));

        helper.getCollection("Product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String ID = documentSnapshot.getString("ID");
                            String Name = documentSnapshot.getString("Name");
                            String Club = documentSnapshot.getString("Club");
                            String Nation = documentSnapshot.getString("Nation");
                            String Season = documentSnapshot.getString("Season");
                            double Price = documentSnapshot.getDouble("Price");
                            long Quantity = documentSnapshot.getLong("Quantity");
                            double Point = documentSnapshot.getDouble("Point");
                            String URLmain = documentSnapshot.getString("URLmain");
                            String URLsub1 = documentSnapshot.getString("URLsub1");
                            String URLsub2 = documentSnapshot.getString("URLsub2");
                            String URLthumb = documentSnapshot.getString("URLthumb");

                            Log.d(TAG, ID + "--" + Name);
                            Product product = new Product(ID, Name, Club, Nation, Season, Price, Quantity, Point, URLmain, URLsub1, URLsub2, URLthumb);
                            products.add(product);
                        }
                        _mldListProduct.setValue(Resource.success(products));
                    }
                });

        return _mldListProduct;
    }
    public LiveData<Resource<List<Product>>> getListBestSeller(){
        _mldListProduct.setValue(Resource.loading(null));

        helper.getCollection("Product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String ID = documentSnapshot.getString("ID");
                            String Name = documentSnapshot.getString("Name");
                            String Club = documentSnapshot.getString("Club");
                            String Nation = documentSnapshot.getString("Nation");
                            String Season = documentSnapshot.getString("Season");
                            double Price = documentSnapshot.getDouble("Price");
                            long Quantity = documentSnapshot.getLong("Quantity");
                            double Point = documentSnapshot.getDouble("Point");
                            String URLmain = documentSnapshot.getString("URLmain");
                            String URLsub1 = documentSnapshot.getString("URLsub1");
                            String URLsub2 = documentSnapshot.getString("URLsub2");
                            String URLthumb = documentSnapshot.getString("URLthumb");

                            Log.d(TAG, ID + "--" + Name);
                            Product product = new Product(ID, Name, Club, Nation, Season, Price, Quantity, Point, URLmain, URLsub1, URLsub2, URLthumb);
                            products.add(product);
                        }
                        _mldListProduct.setValue(Resource.success(products));
                    }
                });

        return _mldListProduct;
    }
    public LiveData<Resource<List<Product>>> getListFavorite(){
        _mldListProduct.setValue(Resource.loading(null));

        helper.getCollection("Product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String ID = documentSnapshot.getString("ID");
                            String Name = documentSnapshot.getString("Name");
                            String Club = documentSnapshot.getString("Club");
                            String Nation = documentSnapshot.getString("Nation");
                            String Season = documentSnapshot.getString("Season");
                            double Price = documentSnapshot.getDouble("Price");
                            long Quantity = documentSnapshot.getLong("Quantity");
                            double Point = documentSnapshot.getDouble("Point");
                            String URLmain = documentSnapshot.getString("URLmain");
                            String URLsub1 = documentSnapshot.getString("URLsub1");
                            String URLsub2 = documentSnapshot.getString("URLsub2");
                            String URLthumb = documentSnapshot.getString("URLthumb");

                            Log.d(TAG, ID + "--" + Name);
                            Product product = new Product(ID, Name, Club, Nation, Season, Price, Quantity, Point, URLmain, URLsub1, URLsub2, URLthumb);
                            products.add(product);
                        }
                        _mldListProduct.setValue(Resource.success(products));
                    }
                });

        return _mldListProduct;
    }
}
