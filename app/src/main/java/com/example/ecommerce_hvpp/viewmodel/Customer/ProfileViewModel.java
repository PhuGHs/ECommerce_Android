package com.example.ecommerce_hvpp.viewmodel.Customer;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.ItemModel;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProductManagementRepository;
import com.example.ecommerce_hvpp.repositories.customerRepositories.ProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository repo = new ProfileRepository();
    private FirebaseAuth firebaseAuth;
    public LiveData<Resource<User>> showUserName(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        Log.e("Phuc", fbUser.getEmail());
        return repo.getUserInfo(fbUser.getUid());
    }
    public void updateUser(User user){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        repo.updateUser(fbUser.getUid(), user.getUsername(), user.getDatebirth(), user.getAddress(), user.getEmail());
        Log.e("Phuc", "Da cap nhat " + user.getUsername() + fbUser.getUid());
    }
    public LiveData<String> getUserName(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getUserName(fbUser.getUid());
    }
    public LiveData<String> getUserImage(){
        FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
        return repo.getUserImage(fbUser.getUid());
    }
//    public void updateAva(ContentResolver contentResolver, User pd, Uri uriThumb, List<ItemModel> list) {
//
//        repo.uploadImage(uriThumb, getFileExtension(contentResolver, uriThumb), new AdminProductManagementRepository.OnImageUploadListener() {
//            @Override
//            public void onImageUploadSuccess(String imageUrl) {
//                pd.setUrlthumb(imageUrl);
//            }
//
//            @Override
//            public void onImageUploadFailure(String exception) {
//                combinedLiveData.setValue(Resource.error(exception, null));
//            }
//        });
//
//        for(int i = 0; i < list.size(); i++) {
//            int j = i;
//            if(list.get(i).getImageUri() != null) {
//                repo.uploadImage(list.get(i).getImageUri(), getFileExtension(contentResolver, list.get(i).getImageUri()), new AdminProductManagementRepository.OnImageUploadListener() {
//                    @Override
//                    public void onImageUploadSuccess(String imageUrl) {
//                        if(j == 0) {
//                            pd.setUrlmain(imageUrl);
//                        } else if (j == 1) {
//                            pd.setUrlsub1(imageUrl);
//                        } else {
//                            pd.setUrlsub2(imageUrl);
//                        }
//                        checkAllUploadsCompleted(pd);
//                    }
//
//                    @Override
//                    public void onImageUploadFailure(String exception) {
//                        combinedLiveData.setValue(Resource.error("error upload", null));
//                    }
//                });
//            }
//        }
//    }
//    public String getFileExtension(ContentResolver contentResolver, Uri uri) {
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
//    private void checkAllUploadsCompleted(Product pd) {
//        uploadCount++;
//        if (uploadCount == totalUploads) {
//            repo.addProduct(pd);
//            combinedLiveData.setValue(Resource.success("successful adding"));
//        }
//    }

}
