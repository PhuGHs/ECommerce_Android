package com.example.ecommerce_hvpp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<Resource<User>> _mldUserInfo;
    String username;
    private DatabaseReference ref;
    private final String TAG = "ProfileRepository";
    public ProfileRepository(){
        _mldUserInfo = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
        ref = firebaseHelper.getDatabaseReference("users");

    }

    public LiveData<Resource<User>> getUserInfo(String UID){
        _mldUserInfo.setValue(Resource.loading(null));
        firebaseHelper.getCollection("users").document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        User userinfo = documentSnapshot.toObject(User.class);
                        _mldUserInfo.setValue(Resource.success(userinfo));
                        Log.e("Phuc", userinfo.getUsername());
                    } else {
                        Log.d(TAG, "user not found");
                    }
                });
        return _mldUserInfo;
    }
    public void updateUser(String UID, String name, String datebirth, String address, String email) {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference ref = fs.collection("users").document(UID);
        ref.update(
                "username", name,
                "datebirth", datebirth,
                "address", address,
                "email", email
        );
    }

}