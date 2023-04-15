package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterLoginViewModel extends ViewModel {
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Resource<FirebaseUser>> _register = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> resetPasswordResult = new MutableLiveData<>();
    public LiveData<Resource<Void>> getResetPasswordResult() {
        return resetPasswordResult;
    }

    public LiveData<Resource<FirebaseUser>> registerUser(String email, String password) {
        _register.setValue(Resource.loading(null));
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        _register.setValue(Resource.success(user));
                    } else {
                        String errMessage = task.getException().getMessage();
                        _register.setValue(Resource.error(errMessage, null));
                    }
                });
        return _register;
    }

    public LiveData<Resource<FirebaseUser>> loginUser(String email, String password) {
        _register.setValue(Resource.loading(null));
        firebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                        _register.setValue(Resource.success(user));
                    } else {
                        _register.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
        return _register;
    }

    public LiveData<Resource<Void>> resetUserPassword(String email) {
        resetPasswordResult.setValue(Resource.loading(null));
        firebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(authResult -> {
                    resetPasswordResult.setValue(Resource.success(null));
                })
                .addOnFailureListener(error -> {
                    resetPasswordResult.setValue(Resource.error(error.getMessage(), null));
                });
        return resetPasswordResult;
    }
}
