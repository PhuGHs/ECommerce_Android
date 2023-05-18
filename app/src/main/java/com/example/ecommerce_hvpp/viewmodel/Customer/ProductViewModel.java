package com.example.ecommerce_hvpp.viewmodel.Customer;

import static com.example.ecommerce_hvpp.fragments.ChatFragment.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.repositories.ProductRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<Resource<Product>> _product = new MutableLiveData<>();
    private ProductRepository repo = new ProductRepository();


    public ProductViewModel(){
    }
    public LiveData<Resource<List<Product>>> getListNewArrivals(){
        return repo.getListNewArrivals();
    }
    public LiveData<Resource<List<Product>>> getListBestSeller(){
        return repo.getListBestSeller();
    }
    public LiveData<Resource<List<Product>>> getListFavorite(){
        return repo.getListFavorite();
    }
}
