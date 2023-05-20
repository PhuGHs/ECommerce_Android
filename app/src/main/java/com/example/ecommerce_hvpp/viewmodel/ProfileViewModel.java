package com.example.ecommerce_hvpp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.model.UserInfo;
import com.example.ecommerce_hvpp.repositories.ProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository repo = new ProfileRepository();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name;
    private MutableLiveData<Resource<UserInfo>> _userInfo = new MutableLiveData<>();
    public LiveData<Resource<User>> showUserName(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getUserInfo(fbUser.getUid());
    }

}
