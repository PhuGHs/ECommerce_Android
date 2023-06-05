package com.example.ecommerce_hvpp.fragments.admin_fragments;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayOrdersDataStatistics;

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
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminProfileFragment extends Fragment {
    AdminFragmentProfileBinding mAdminFragmentProfileBinding;
    AdminProfileViewModel vmAdminProfile;
    Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentProfileBinding = AdminFragmentProfileBinding.inflate(inflater, container, false);

        vmAdminProfile = new ViewModelProvider(AdminProfileFragment.this).get(AdminProfileViewModel.class);
        mAdminFragmentProfileBinding.setAdminProfileViewModel(vmAdminProfile);
        mAdminFragmentProfileBinding.setLifecycleOwner(getViewLifecycleOwner());

//        testData();
        testDataPass();

        return mAdminFragmentProfileBinding.getRoot();
    }

    private void testDataPass() {
        AdminStatisticsRepository repo = new AdminStatisticsRepository();
        Observer<Resource<Map<String, Integer>>> observer = getObserverDataStatistic();
        Observable<Resource<Map<String, Integer>>> observable = repo.getObservableStatisticsOrders();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void testData() {
        AdminProfileRepository repo = new AdminProfileRepository();
        Observable<Resource<List<OrderHistory>>> observable = repo.getObservableOrderHistory();
        Observer<Resource<List<OrderHistory>>> observer = getObserverCustomers();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    private Observer<Resource<List<OrderHistory>>> getObserverCustomers() {
        return new Observer<Resource<List<OrderHistory>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                Log.e("Vucoder", "onSubscribe");
            }

            @Override
            public void onNext(@NonNull Resource<List<OrderHistory>> resource) {
                Log.e("Vucoder", "onNext");
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:

                        List<OrderHistory> orderHistory = resource.data;
                        for (OrderHistory order : orderHistory) {
                            Log.e("Vu", String.valueOf(order.getId()));
                        }
                        break;
                    case ERROR:
                        Log.i("VuError", resource.message);
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

    private Observer<Resource<Map<String, Integer>>> getObserverDataStatistic() {
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
