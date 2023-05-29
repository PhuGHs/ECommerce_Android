package com.example.ecommerce_hvpp.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;

import com.example.ecommerce_hvpp.adapter.AdminCustomItemOrderHistoryAdapter;
import com.example.ecommerce_hvpp.databinding.AdminFragmentOrderHistoryBinding;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.util.Resource;
import com.example.ecommerce_hvpp.viewmodel.admin_order_history.AdminOrderHistoryViewModel;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminOrderHistoryFragment extends Fragment {
    AdminFragmentOrderHistoryBinding mAdminFragmentOrderHistoryBinding;
    AdminOrderHistoryViewModel vmAdminOrderHistory;
    AdminCustomItemOrderHistoryAdapter adapterAdminCustomItemOrderHistory;
    AdminProfileRepository repo;
    Observable<Resource<List<OrderHistory>>> observable;
    Observer<Resource<List<OrderHistory>>> observer;
    private Disposable disposable;
    static int PERMISSION_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentOrderHistoryBinding = AdminFragmentOrderHistoryBinding.inflate(inflater, container, false);

        // init view model
        vmAdminOrderHistory = new ViewModelProvider(requireActivity()).get(AdminOrderHistoryViewModel.class);

        // display data into app
        repo = new AdminProfileRepository();

        observable = repo.getObservableOrderHistory();
        observer = getObserverCustomers();

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        // on click back page
        mAdminFragmentOrderHistoryBinding.adminOrderHistoryHeaderBack.setOnClickListener(repo.onClickBackPage());

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }

        // search bar
        mAdminFragmentOrderHistoryBinding.adminOrderHistorySearchBar.addTextChangedListener(new TextWatcher() {
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


        return mAdminFragmentOrderHistoryBinding.getRoot();
    }

    private Observer<Resource<List<OrderHistory>>> getObserverCustomers() {
        return new Observer<Resource<List<OrderHistory>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                Log.e("Vucoder", "onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<OrderHistory>> resource) {
                Log.e("Vucoder", "onNext");
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        adapterAdminCustomItemOrderHistory = new AdminCustomItemOrderHistoryAdapter(getContext(), Objects.requireNonNull(resource.data));

                        //set up recyclerview
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        mAdminFragmentOrderHistoryBinding.adminOrderHistoryRcvItemOrderHistory.setLayoutManager(layoutManager);
                        mAdminFragmentOrderHistoryBinding.adminOrderHistoryRcvItemOrderHistory.setAdapter(adapterAdminCustomItemOrderHistory);

                        layoutManager.scrollToPositionWithOffset(adapterAdminCustomItemOrderHistory.getItemCount() - 1, 0);
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

    private Observer<Resource<List<OrderHistory>>> getObserverAfterSearch(String strSearch) {
        return new Observer<Resource<List<OrderHistory>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<List<OrderHistory>> resource) {
                Log.e("VuSearch", "onNext");
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        adapterAdminCustomItemOrderHistory.filterOrderHistory(strSearch);
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
