package com.example.ecommerce_hvpp.repositories;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecepInfoRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<List<RecepInfo>> _mldListRecepInfo = new MutableLiveData<>();
    private MutableLiveData<Resource<RecepInfo>> _mldRecepInfo = new MutableLiveData<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("RecepInfo");
    private DatabaseReference ref;
    private final String TAG = "RecepInfoRepository";
    public RecepInfoRepository(){
        _mldListRecepInfo = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
        ref = firebaseHelper.getDatabaseReference("RecepInfo");

    }
    public LiveData<List<RecepInfo>> getAllRecepInfos() {
        firebaseHelper.getCollection("RecepInfo").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RecepInfo> recepInfos = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        RecepInfo recep = snapshot.toObject(RecepInfo.class);
                        recepInfos.add(recep);
                    }
                    _mldListRecepInfo.setValue(recepInfos);
                })
                .addOnFailureListener(e -> {
                    _mldListRecepInfo.setValue(null);
                });
        return _mldListRecepInfo;
    }


}
