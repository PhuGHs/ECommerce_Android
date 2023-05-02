package com.example.ecommerce_hvpp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
        firebaseHelper.getDatabaseReference().child("users").child(UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if(user == null) {
                            //do nothing
                            return;
                        }
                        Log.i(TAG, "success");
                        _mldUser.setValue(Resource.success(user));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.getMessage());
                        _mldUser.setValue(Resource.error(error.getMessage(), null));
                    }
                });
        return _mldUser;
    }

    public LiveData<Resource<List<User>>> getUsers() {
        _mldListUser.setValue(Resource.loading(null));
        firebaseHelper.getDatabaseReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        LiveData<Resource<List<User>>> data = new MutableLiveData<>();
                        List<User> userList = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            userList.add(user);
                        }
                        _mldListUser.setValue(Resource.success(userList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                            // handle errors
                    }
                });
        return _mldListUser;
    }
}
