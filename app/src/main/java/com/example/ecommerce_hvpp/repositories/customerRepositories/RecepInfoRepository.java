package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.database.Observable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.RecepInfo;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecepInfoRepository {
    private FirebaseHelper firebaseHelper;
    private DatabaseReference ref;
    private MutableLiveData<List<RecepInfo>> _mldListRecepInfo = new MutableLiveData<>();
    private MutableLiveData<RecepInfo> recepDetail = new MutableLiveData<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("RecepInfo");
    private final String TAG = "RecepInfoRepository";
    public RecepInfoRepository(){
        _mldListRecepInfo = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
        ref = firebaseHelper.getDatabaseReference("RecepInfo");

    }
    public LiveData<List<RecepInfo>> getAllRecepInfos(String UID) {
        firebaseHelper.getCollection("users").document(UID).collection("recep_info")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RecepInfo> recepInfos = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String address = snapshot.getString("address");
                        String name = snapshot.getString("name");
                        String phonenumber = snapshot.getString("phonenumber");
                        boolean isApplied = snapshot.getBoolean("isApplied");
                        RecepInfo recep = new RecepInfo(snapshot.getId(), name, phonenumber, address, isApplied);
                        recepInfos.add(recep);
                    }
                    _mldListRecepInfo.setValue(recepInfos);
                })
                .addOnFailureListener(e -> {
                    _mldListRecepInfo.setValue(null);
                });
        return _mldListRecepInfo;
    }
    public LiveData<RecepInfo> getRecepInfo(String UID, String recep_id){
        firebaseHelper.getCollection("users").document(UID).collection("recep_info").document(recep_id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String address = documentSnapshot.getString("address");
                        String name = documentSnapshot.getString("name");
                        String phonenumber = documentSnapshot.getString("phonenumber");
                        boolean isApplied = documentSnapshot.getBoolean("isApplied");
                        RecepInfo recep = new RecepInfo(recep_id, name, phonenumber, address, isApplied);
                        recepDetail.setValue(recep);
                    }
                })
                .addOnFailureListener(e -> {
                    recepDetail.setValue(null);
                });
        return recepDetail;
    }
    public void updateRecepInfo(String UID, String recep_id, String name, String phonenumber, String address){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference ref = fs.collection("users").document(UID).collection("recep_info").document(recep_id);
        ref.update(
                "name", name,
                "phonenumber", phonenumber,
                "address", address
        );
    }
    public void updateStatusOfRecepInfo(String UID, String recep_id){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        firebaseHelper.getCollection("users").document(UID).collection("recep_info")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getId().equals(recep_id)){
                            DocumentReference ref = fs.collection("users").document(UID).collection("recep_info").document(snapshot.getId());
                            ref.update("isApplied", true);
                            Log.d(TAG, "cap nhat thanh cong");
                        }
                        else{
                            DocumentReference ref = fs.collection("users").document(UID).collection("recep_info").document(snapshot.getId());
                            ref.update("isApplied", false);
                            Log.d(TAG, "cap nhat false thanh cong");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "khong the cap nhat trang thai recepinfo");
                });
    }

}
