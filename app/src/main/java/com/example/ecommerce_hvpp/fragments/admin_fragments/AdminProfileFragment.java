package com.example.ecommerce_hvpp.fragments.admin_fragments;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayOrdersDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayProductSoldDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayRevenueDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayVisitorsDataStatistics;
import static com.example.ecommerce_hvpp.util.CustomFormat.decimalFormatter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import android.Manifest;
import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentProfileBinding;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminPromotionRepository;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin.AdminProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminProfileFragment extends Fragment {
    AdminFragmentProfileBinding mAdminFragmentProfileBinding;
    AdminProfileViewModel vmAdminProfile;
    AdminStatisticsRepository repo;
    private ActivityResultLauncher<Intent> thumbnailLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri thumbnailImage;
    private ContentResolver contentResolver;
    private String path;
    Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentProfileBinding = AdminFragmentProfileBinding.inflate(inflater, container, false);

        // init view model
        vmAdminProfile = new ViewModelProvider(AdminProfileFragment.this).get(AdminProfileViewModel.class);
        mAdminFragmentProfileBinding.setAdminProfileViewModel(vmAdminProfile);
        mAdminFragmentProfileBinding.setLifecycleOwner(getViewLifecycleOwner());

        // init repo
        repo = new AdminStatisticsRepository();
        testData(); // test data here

        // update avatar
        initLauncher();
        onClickImage();

        // get data statistics
        getDataStatistics();

        // logout
        mAdminFragmentProfileBinding.adminProfileLogoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminProfileRepository repoData = new AdminProfileRepository();
                repoData.logOutApp(requireContext());
            }
        });

        return mAdminFragmentProfileBinding.getRoot();
    }

    private void testData() {
        AdminPromotionRepository repoTest = new AdminPromotionRepository();
        repoTest.getVouchersOfUsers();
    }

    private void initLauncher() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(isGranted) {
                openGallery();
            } else {
                // do nothing
                //show message (disabled)
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == FragmentActivity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    if (data.getClipData() != null) {
                        // Image was selected
//                        processSelectedImage(data.getData());
                    }
                }
            }
        });

        thumbnailLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == FragmentActivity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    thumbnailImage = data.getData();
                    Glide.with(requireContext()).load(thumbnailImage).fitCenter().into(mAdminFragmentProfileBinding.adminProfileAvatarAdmin);
                    path = thumbnailImage.toString();
                    //viewModel.uploadAvatar(contentResolver, user, thumbnailImage);
                }
            }
        });
    }

    private void onClickImage() {
        mAdminFragmentProfileBinding.adminProfileAvatarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the permission is already granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if it has not been granted
                    requestGalleryPermission();
                } else {
                    // Permission already granted, proceed with gallery access
                    OpenThumbnailGallery();
                }
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @SuppressLint("IntentReset")
    private void OpenThumbnailGallery() {
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        thumbnailLauncher.launch(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void getDataStatistics() {
        getVisitorsDataStatistics();
        getOrdersDataStatistics();
        getProductSoldDataStatistics();
        getRevenueDataStatistics();
    }

    private void getVisitorsDataStatistics() {
        Observer<Resource<Map<String, Integer>>> observer = getObserverVisitorsDataStatistic();
        Observable<Resource<Map<String, Integer>>> observable = repo.getObservableVisitorsDataStatistics();

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void getOrdersDataStatistics() {
        Observer<Resource<Map<String, Integer>>> observer = getObserverOrdersDataStatistic();
        Observable<Resource<Map<String, Integer>>> observable = repo.getObservableOrdersDataStatistics();

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void getProductSoldDataStatistics() {
        Observer<Resource<Map<String, Integer>>> observer = getObserverProductSoldDataStatistic();
        Observable<Resource<Map<String, Integer>>> observable = repo.getObservableProductSoldDataStatistics();

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void getRevenueDataStatistics() {
        Observer<Resource<Map<String, Double>>> observer = getObserverRevenueDataStatistic();
        Observable<Resource<Map<String, Double>>> observable = repo.getObservableRevenueDataStatistics();

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private Observer<Resource<Map<String, Integer>>> getObserverVisitorsDataStatistic() {
        return new Observer<Resource<Map<String, Integer>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onNext(@NonNull Resource<Map<String, Integer>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        dayVisitorsDataStatistics = resource.data;
                        assert dayVisitorsDataStatistics != null;

                        break;
                    case ERROR:
                        Log.e("VuError", resource.message);
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Handle error state if needed
            }

            @Override
            public void onComplete() {
                // Handle completion if needed
                Log.e("Vucoder", "onComplete");
            }
        };
    }

    private Observer<Resource<Map<String, Integer>>> getObserverOrdersDataStatistic() {
        return new Observer<Resource<Map<String, Integer>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onNext(@NonNull Resource<Map<String, Integer>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        dayOrdersDataStatistics = resource.data;
                        assert dayOrdersDataStatistics != null;

                        break;
                    case ERROR:
                        Log.e("VuError", resource.message);
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Handle error state if needed
            }

            @Override
            public void onComplete() {
                // Handle completion if needed
                Log.e("Vucoder", "onComplete");
            }
        };
    }

    private Observer<Resource<Map<String, Integer>>> getObserverProductSoldDataStatistic() {
        return new Observer<Resource<Map<String, Integer>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onNext(@NonNull Resource<Map<String, Integer>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        dayProductSoldDataStatistics = resource.data;
                        assert dayProductSoldDataStatistics != null;

                        break;
                    case ERROR:
                        Log.e("VuError", resource.message);
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Handle error state if needed
            }

            @Override
            public void onComplete() {
                // Handle completion if needed
                Log.e("Vucoder", "onComplete");
            }
        };
    }

    private Observer<Resource<Map<String, Double>>> getObserverRevenueDataStatistic() {
        return new Observer<Resource<Map<String, Double>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onNext(@NonNull Resource<Map<String, Double>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        dayRevenueDataStatistics = resource.data;
                        assert dayRevenueDataStatistics != null;

                        break;
                    case ERROR:
                        Log.e("VuError", resource.message);
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Handle error state if needed
            }

            @Override
            public void onComplete() {
                // Handle completion if needed
                Log.e("Vucoder", "onComplete");
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }
}
