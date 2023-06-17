package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
    private MutableLiveData<Resource<User>> _mldUser = new MutableLiveData<>();
    private MutableLiveData<Resource<List<User>>> _mldListUser = new MutableLiveData<>();
    public interface UserCallback {
        void onSuccess(User user);
        void onError(String errorMessage);
    }
    private final String TAG = "UserRepository";

    public void addUser(User user) {
        firebaseHelper.getCollection("users").add(user);
    }

    public void updateUser(User user) {
        firebaseHelper.getDatabaseReference().child("users").setValue(user);
    }

    public void deleteUser(String userUID) {
        firebaseHelper.getDatabaseReference().child("users").child(userUID).removeValue();
    }

    public void getUserWith(String UID, UserCallback callback) {
        firebaseHelper.getCollection("users").document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        boolean isAdmin = documentSnapshot.getBoolean("admin");
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        String password = documentSnapshot.getString("password");
                        String datebirth = documentSnapshot.getString("datebirth");
                        String address = documentSnapshot.getString("address");
                        String imagePath = documentSnapshot.getString("imagePath");
                        User user = new User();
                        user.setUsername(username);
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setDatebirth(datebirth);
                        user.setAddress(address);
                        user.setImagePath(imagePath);
                        callback.onSuccess(user);
                    } else {
                        Log.d(TAG, "user not found");
                        callback.onError("User not found");
                    }
                });
    }

    public LiveData<Resource<User>> getUser(String UID) {
        _mldUser.setValue(Resource.loading(null));
        firebaseHelper.getCollection("users").document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        boolean isAdmin = documentSnapshot.getBoolean("admin");
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        String password = documentSnapshot.getString("password");
                        String datebirth = documentSnapshot.getString("datebirth");
                        String address = documentSnapshot.getString("address");
                        String imagePath = documentSnapshot.getString("imagePath");
                        boolean isNew = documentSnapshot.getBoolean("new");
                        _mldUser.setValue(Resource.success(new User(isAdmin, username, email, password, datebirth, address, imagePath, isNew)));

                    } else {
                        Log.d(TAG, "user not found");
                    }
                })
                .addOnFailureListener(e -> {
                    _mldUser.setValue(Resource.error(e.getMessage(), null));
                })
                .addOnCompleteListener(task -> {
                    // Notify observers that the data is ready
                    _mldUser.postValue(_mldUser.getValue());
                });
        return _mldUser;
    }

    public void getAllUsers() {
        firebaseHelper.getCollection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Log.d(TAG, snapshot.getData().toString());
                    }
                })
                .addOnFailureListener(e -> {
                    _mldListUser.setValue(Resource.error(e.getMessage(), null));
                });
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
    public LiveData<String> getUserName(String ID){
        MutableLiveData<String> userName = new MutableLiveData<>();

        firebaseHelper.getCollection("users").document(ID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String Name = documentSnapshot.getString("username");

                            userName.setValue(Name);
                        }
                        else userName.setValue(null);
                    }
                });
        return userName;
    }
}