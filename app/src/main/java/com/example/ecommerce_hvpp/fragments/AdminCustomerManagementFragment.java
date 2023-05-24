package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce_hvpp.adapter.AdminCustomItemCustomerAdapter;
import com.example.ecommerce_hvpp.databinding.AdminFragmentCustomerManagementBinding;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.repositories.AdminCustomerManagementRepository;
import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin_customer_management.AdminCustomerManagementViewModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminCustomerManagementFragment extends Fragment {
    AdminFragmentCustomerManagementBinding mFragmentAdminManageCustomerBinding;
    AdminCustomerManagementViewModel vmAdminCustomerManagement;
    AdminCustomItemCustomerAdapter adapterAdminCustomItemCustomer;
    AdminProfileRepository repo;
    Observer<Resource<List<User>>> observer;
    Observable<Resource<List<User>>> observable;
    private Disposable disposable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminManageCustomerBinding = AdminFragmentCustomerManagementBinding.inflate(inflater, container, false);

        // init view model
        vmAdminCustomerManagement = new ViewModelProvider(requireActivity()).get(AdminCustomerManagementViewModel.class);

        // get data and display in app
        repo = new AdminProfileRepository();
        observable = repo.getObservableCustomers();
        observer = getObserverUsers();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        // on click back page
        mFragmentAdminManageCustomerBinding.adminCustomerManagementHeaderBack.setOnClickListener(repo.onClickBackPage());

        // search bar
        mFragmentAdminManageCustomerBinding.adminCustomerManagementSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String strSearch = charSequence.toString();
                observer = getObserverAfterSearch(strSearch);

                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        return mFragmentAdminManageCustomerBinding.getRoot();
    }


    private Observer<Resource<List<User>>> getObserverUsers() {
        return new Observer<Resource<List<User>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<User>> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        adapterAdminCustomItemCustomer = new AdminCustomItemCustomerAdapter(getContext(), Objects.requireNonNull(resource.data));
                        // Set up recyclerview
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        mFragmentAdminManageCustomerBinding.adminCustomerManagementRcvItemCustomer.setLayoutManager(layoutManager);
                        mFragmentAdminManageCustomerBinding.adminCustomerManagementRcvItemCustomer.setAdapter(adapterAdminCustomItemCustomer);
                        layoutManager.scrollToPositionWithOffset(adapterAdminCustomItemCustomer.getItemCount() - 1, 0);
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

    private Observer<Resource<List<User>>> getObserverAfterSearch(String strSearch) {
        return new Observer<Resource<List<User>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<User>> resource) {
                Log.e("VuSearch", "onNext");
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        adapterAdminCustomItemCustomer.filterUser(strSearch);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }
}
