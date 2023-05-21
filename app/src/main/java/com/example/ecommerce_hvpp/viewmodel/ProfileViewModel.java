package com.example.ecommerce_hvpp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.ProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository repo = new ProfileRepository();
    private FirebaseAuth firebaseAuth;
    public LiveData<Resource<User>> showUserName(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        Log.e("Phuc", fbUser.getEmail());
        return repo.getUserInfo(fbUser.getUid());
    }

}
