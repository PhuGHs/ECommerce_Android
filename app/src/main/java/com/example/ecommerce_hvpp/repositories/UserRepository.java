package com.example.ecommerce_hvpp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
    private MutableLiveData<Resource<User>> _mldUser = new MutableLiveData<>();
    private MutableLiveData<Resource<List<User>>> _mldListUser = new MutableLiveData<>();
    private final String TAG = "UserRepository";

    public void addUser(User user) {
        firebaseHelper.getDatabaseReference().child("users").push().setValue(user);
    }

    public void updateUser(User user) {
        firebaseHelper.getDatabaseReference().child("users").setValue(user);
    }

    public void deleteUser(String userUID) {
        firebaseHelper.getDatabaseReference().child("users").child(userUID).removeValue();
    }

    public LiveData<Resource<User>> getUser(String UID) {
        _mldUser.setValue(Resource.loading(null));
        firebaseHelper.getCollection("users").document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                   _mldUser.setValue(Resource.success(user));
                })
                .addOnFailureListener(e -> {
                    _mldUser.setValue(Resource.error(e.getMessage(), null));
                });
        return _mldUser;
    }

    public LiveData<Resource<List<User>>> getUsers() {
        _mldListUser.setValue(Resource.loading(null));
        firebaseHelper.getCollection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);
                        users.add(user);
                    }
                    _mldListUser.setValue(Resource.success(users));
                })
                .addOnFailureListener(e -> {
                    _mldListUser.setValue(Resource.error(e.getMessage(), null));
                });
        return _mldListUser;
    }
}
