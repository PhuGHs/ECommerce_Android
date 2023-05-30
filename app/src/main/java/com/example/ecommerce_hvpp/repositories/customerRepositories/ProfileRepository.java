package com.example.ecommerce_hvpp.repositories.customerRepositories;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProductManagementRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileRepository {
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<Resource<User>> _mldUserInfo;
    String username;
    private DatabaseReference ref;
    private StorageReference imageRef;
    private final String TAG = "ProfileRepository";
    public ProfileRepository(){
        _mldUserInfo = new MutableLiveData<>();
        firebaseHelper = FirebaseHelper.getInstance();
        ref = firebaseHelper.getDatabaseReference("users");

    }
    public interface OnImageUploadListener {
        void onImageUploadSuccess(String imageUrl);
        void onImageUploadFailure(String exception);
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
    public void uploadImage(Uri uri, String fileType, final AdminProductManagementRepository.OnImageUploadListener listener) {
        String path = System.currentTimeMillis() + "." + fileType;
        imageRef = firebaseHelper.getImageStorage();
        UploadTask uploadTask = imageRef.child(path).putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.child(path).getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                listener.onImageUploadSuccess(downloadUrl.toString());
            }).addOnFailureListener(e -> {
                listener.onImageUploadFailure(e.getMessage());
            });
        }).addOnFailureListener(e -> {
            listener.onImageUploadFailure(e.getMessage());
        });
    }

}
