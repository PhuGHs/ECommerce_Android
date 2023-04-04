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
}
