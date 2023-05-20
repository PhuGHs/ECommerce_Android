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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.adapter.AdminCustomItemCustomerAdapter;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentOrderHistoryBinding = AdminFragmentOrderHistoryBinding.inflate(inflater, container, false);

        // init view model
        vmAdminOrderHistory = new ViewModelProvider(requireActivity()).get(AdminOrderHistoryViewModel.class);

        // get data from firestore
        getData();

        // display data into app
        displayData();

        // on click back page
        mAdminFragmentOrderHistoryBinding.adminOrderHistoryHeaderBack.setOnClickListener(onClickBackPage());

        return mAdminFragmentOrderHistoryBinding.getRoot();
    }

    private void getData() {
        AdminProfileRepository repo = new AdminProfileRepository();
        Observable<Resource<List<OrderHistory>>> observable = repo.getDataOrderHistory();
        Observer<Resource<List<OrderHistory>>> observer = getObserverCustomers();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void displayData() {

    }

    private View.OnClickListener onClickBackPage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
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
}
