package com.example.ecommerce_hvpp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.UserRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterLoginViewModel extends ViewModel {
    private final static String TAG = "RegisterLoginViewModel";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserRepository repo = new UserRepository();
    private MutableLiveData<Resource<User>> _register = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> resetPasswordResult = new MutableLiveData<>();
    public LiveData<Resource<Void>> getResetPasswordResult() {
        return resetPasswordResult;
    }
    public String UID;

    public LiveData<Resource<User>> registerUser(String email, String password) {
        _register.setValue(Resource.loading(null));
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        UID = user.getUid();
                        createUser(UID, new User(email, password));
                    } else {
                        String errMessage = task.getException().getMessage();
                        _register.setValue(Resource.error(errMessage, null));
                    }
                });
        return _register;
    }

    public void createUser(String userUID, User user) {
        db.collection("users")
                .document(userUID)
                .set(user)
                .addOnSuccessListener(value -> {
                    _register.setValue(Resource.success(user));
                })
                .addOnFailureListener(ex -> {
                    _register.setValue(Resource.error(ex.getMessage(), null));
                });
    }

    public LiveData<Resource<User>> loginUser(String email, String password) {
        _register.setValue(Resource.loading(null));
        firebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
                        _register.setValue(Resource.success(repo.getUser(fbUser.getUid()).getValue().data));
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
