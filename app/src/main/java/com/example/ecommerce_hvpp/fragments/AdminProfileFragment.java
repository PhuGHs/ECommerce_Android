package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce_hvpp.databinding.AdminFragmentProfileBinding;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.AdminProfileViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminProfileFragment extends Fragment {
    AdminFragmentProfileBinding mAdminFragmentProfileBinding;
    AdminProfileViewModel vmAdminProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentProfileBinding = AdminFragmentProfileBinding.inflate(inflater, container, false);

        vmAdminProfile = new ViewModelProvider(AdminProfileFragment.this).get(AdminProfileViewModel.class);
        mAdminFragmentProfileBinding.setAdminProfileViewModel(vmAdminProfile);
        mAdminFragmentProfileBinding.setLifecycleOwner(getViewLifecycleOwner());

//        testData();

        return mAdminFragmentProfileBinding.getRoot();
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
                            Log.e("Vu", String.valueOf(order.getID()));
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
}
