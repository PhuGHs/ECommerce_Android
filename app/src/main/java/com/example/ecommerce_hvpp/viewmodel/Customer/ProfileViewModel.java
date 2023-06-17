package com.example.ecommerce_hvpp.viewmodel.Customer;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProductManagementRepository;
import com.example.ecommerce_hvpp.repositories.customerRepositories.ProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository repo = new ProfileRepository();
    private AdminProductManagementRepository repo_ava;
    private FirebaseAuth firebaseAuth;
    public LiveData<Resource<User>> showUserName(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        Log.e("Phuc", fbUser.getEmail());
        return repo.getUserInfo(fbUser.getUid());
    }
    public void updateUser(User user, ContentResolver contentResolver, Uri uri){
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String imagePath = "";
        repo.uploadImage(uri, getFileExtension(contentResolver, uri), new AdminProductManagementRepository.OnImageUploadListener() {
            @Override
            public void onImageUploadSuccess(String imageUrl) {
                repo.updateUser(fbUser.getUid(), user.getUsername(), user.getDatebirth(), user.getAddress(), user.getEmail(), imageUrl);
                Log.i("image", imageUrl);
            }

            @Override
            public void onImageUploadFailure(String exception) {

            }
        });
        Log.e("Phuc", "Da cap nhat " + user.getUsername() + fbUser.getUid());
    }

    public void updateUser(User user) {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        repo.updateUser(fbUser.getUid(), user.getUsername(), user.getDatebirth(), user.getAddress(), user.getEmail(), user.getImagePath());
    }
    public LiveData<String> getUserName(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getUserName(fbUser.getUid());
    }
    public LiveData<String> getUserImage(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getUserImage(fbUser.getUid());
    }
    public void uploadAvatar(ContentResolver contentResolver, User user, Uri uriThumb){
        repo.uploadImage(uriThumb, getFileExtension(contentResolver, uriThumb), new AdminProductManagementRepository.OnImageUploadListener() {
            @Override
            public void onImageUploadSuccess(String imageUrl) {
                user.setImagePath(imageUrl);
            }

            @Override
            public void onImageUploadFailure(String exception) {

            }
        });
    }
    public String getFileExtension(ContentResolver contentResolver, Uri uri) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
