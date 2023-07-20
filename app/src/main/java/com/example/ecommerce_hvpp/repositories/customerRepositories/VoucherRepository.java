package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Voucher;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VoucherRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<List<Voucher>> _mldListVoucher = new MutableLiveData<>();
    private MutableLiveData<Integer> number_of_voucher = new MutableLiveData<>();
    private DatabaseReference ref;
    private final String TAG = "VoucherRepository";
    public VoucherRepository(){
        _mldListVoucher = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
        ref = firebaseHelper.getDatabaseReference("Voucher");
    }
    public LiveData<List<Voucher>> getAllVouchers(String UID) {
        firebaseHelper.getCollection("users").document(UID).collection("vouchers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Voucher> vouchers = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (Timestamp.now().getSeconds()*1000 - snapshot.getTimestamp("date_end").getSeconds()*1000 - 86400 < 0 && snapshot.getBoolean("isUsed") == false){
                            String name = snapshot.getString("name");
                            String code = snapshot.getString("id");
                            String apply_for = snapshot.getString("apply_for");
                            long condition = snapshot.getLong("condition");
                            long value = snapshot.getLong("value");
                            boolean isUsed = snapshot.getBoolean("isUsed");

                            Timestamp date_end = snapshot.getTimestamp("date_end");
                            Timestamp date_start = snapshot.getTimestamp("date_begin");
                            vouchers.add(new Voucher(apply_for, condition, name, code, value, date_start.getSeconds()*1000, date_end.getSeconds()*1000, isUsed));
                        }
                    }
                    _mldListVoucher.setValue(vouchers);
                })
                .addOnFailureListener(e -> {
                    _mldListVoucher.setValue(null);
                });
        return _mldListVoucher;
    }
    public LiveData<Integer> getNumofVoucher(String UID){
        firebaseHelper.getCollection("users").document(UID).collection("vouchers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = 0;
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (Timestamp.now().getSeconds()*1000 - snapshot.getTimestamp("date_end").getSeconds()*1000 - 86400 < 0 && snapshot.getBoolean("isUsed") == false){
                            count++;
                        }
                    }
                    number_of_voucher.setValue(count);
                })
                .addOnFailureListener(e -> {
                    number_of_voucher.setValue(0);
                });
        return number_of_voucher;
    }

    public void updateVoucher(String UID, String voucher_id) {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        firebaseHelper.getCollection("users").document(UID).collection("vouchers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getId().equals(voucher_id)) {
                            DocumentReference ref = fs.collection("users").document(UID).collection("vouchers").document(voucher_id);
                            ref.update("isUsed", true);
                            Log.d(TAG, "cap nhat thanh cong" + voucher_id + snapshot.getBoolean("isUsed"));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "khong the cap nhat trang thai voucher");
                });
        }
    }