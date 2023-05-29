package com.example.ecommerce_hvpp.viewmodel.admin.admin_product_management;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.ItemModel;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProductManagementRepository;
import com.example.ecommerce_hvpp.util.Resource;

import java.util.List;

public class AdminProductDetailsViewModel extends ViewModel {
    private AdminProductManagementRepository repo;
    private MutableLiveData<Boolean> isEditMode = new MutableLiveData<>();
    private MutableLiveData<Resource<String>> combinedLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<String>> _mldEdit = new MutableLiveData<>();

    int uploadCount;
    int totalUploads;

    public LiveData<Resource<String>> getCombinedLiveData()
    {
        return combinedLiveData;
    }
    public AdminProductDetailsViewModel() {
        repo = new AdminProductManagementRepository();
    }

    public LiveData<Resource<Product>> getProduct(String Id) {
        return repo.getProduct(Id);
    }
    public void addProduct(ContentResolver contentResolver, Product pd, Uri uriThumb, List<ItemModel> list) {
        combinedLiveData.setValue(Resource.loading(null));
        uploadCount = 0;
        totalUploads = list.size();
        repo.uploadImage(uriThumb, getFileExtension(contentResolver, uriThumb), new AdminProductManagementRepository.OnImageUploadListener() {
            @Override
            public void onImageUploadSuccess(String imageUrl) {
                pd.setUrlthumb(imageUrl);
            }

            @Override
            public void onImageUploadFailure(String exception) {
                combinedLiveData.setValue(Resource.error(exception, null));
            }
        });

        for(int i = 0; i < list.size(); i++) {
            int j = i;
            if(list.get(i).getImageUri() != null) {
                repo.uploadImage(list.get(i).getImageUri(), getFileExtension(contentResolver, list.get(i).getImageUri()), new AdminProductManagementRepository.OnImageUploadListener() {
                    @Override
                    public void onImageUploadSuccess(String imageUrl) {
                        if(j == 0) {
                            pd.setUrlmain(imageUrl);
                        } else if (j == 1) {
                            pd.setUrlsub1(imageUrl);
                        } else {
                            pd.setUrlsub2(imageUrl);
                        }
                        checkAllUploadsCompleted(pd);
                    }

                    @Override
                    public void onImageUploadFailure(String exception) {
                        combinedLiveData.setValue(Resource.error("error upload", null));
                    }
                });
            }
        }
    }

    public LiveData<Resource<String>> editProduct(Product pd) {
        return repo.updateProduct(pd);
    }

    public LiveData<Boolean> getIsEditMode() {
        return isEditMode;
    }
    public void setIsEditMode(Boolean isEditMode) {
        this.isEditMode.setValue(isEditMode);
    }

    public String getFileExtension(ContentResolver contentResolver, Uri uri) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void checkAllUploadsCompleted(Product pd) {
        uploadCount++;
        if (uploadCount == totalUploads) {
            repo.addProduct(pd);
            combinedLiveData.setValue(Resource.success("successful adding"));
        }
    }

}
