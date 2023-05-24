package com.example.ecommerce_hvpp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.model.Voucher;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VoucherRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<List<Voucher>> _mldListVoucher = new MutableLiveData<>();
    private DatabaseReference ref;
    private final String TAG = "VoucherRepository";
    public VoucherRepository(){
        _mldListVoucher = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
        ref = firebaseHelper.getDatabaseReference("Voucher");
    }
    public LiveData<List<Voucher>> getAllVouchers() {
        firebaseHelper.getCollection("Voucher").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Voucher> vouchers = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String name = snapshot.
                        Voucher voucher = snapshot.toObject(Voucher.class);
                        vouchers.add(voucher);
                    }
                    _mldListVoucher.setValue(vouchers);
                })
                .addOnFailureListener(e -> {
                    _mldListVoucher.setValue(null);
                });
        return _mldListVoucher;
    }

}
