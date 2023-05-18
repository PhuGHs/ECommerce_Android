package com.example.ecommerce_hvpp.repositories;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCustomerManagementRepository {
    private final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
    private final MutableLiveData<Resource<Customer>> _mldCustomer = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Customer>>> _mldListCustomer = new MutableLiveData<>();
    private final String TAG = this.getClass().toString();

    public AdminCustomerManagementRepository() {

    }

    public LiveData<Resource<List<Customer>>>  getAllCustomer() {
        _mldListCustomer.setValue(Resource.loading(null));
        firebaseHelper.getCollection("Customer").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Customer> mListCustomer = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Customer customer = snapshot.toObject(Customer.class);
                        mListCustomer.add(customer);
                        Log.e("Vucoder", customer.getName());
                    }
                    _mldListCustomer.setValue(Resource.success(mListCustomer));
                })
                .addOnFailureListener(e -> {
                    _mldListCustomer.setValue(Resource.error(e.getMessage(), null));
                });
        return _mldListCustomer;
    }
}