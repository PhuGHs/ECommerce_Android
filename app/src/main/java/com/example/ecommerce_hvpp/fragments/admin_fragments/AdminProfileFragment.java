package com.example.ecommerce_hvpp.fragments.admin_fragments;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayOrdersDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayProductSoldDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayRevenueDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayVisitorsDataStatistics;
import static com.example.ecommerce_hvpp.util.CustomFormat.decimalFormatter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentProfileBinding;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;
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

        // get data statistics
        getDataStatistics();

        return mAdminFragmentProfileBinding.getRoot();
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
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.test_bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }
}
