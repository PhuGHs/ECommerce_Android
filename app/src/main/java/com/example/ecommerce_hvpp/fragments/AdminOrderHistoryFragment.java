package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentOrderHistoryBinding;
import com.example.ecommerce_hvpp.viewmodel.admin_order_history.AdminOrderHistoryViewModel;

public class AdminOrderHistoryFragment extends Fragment {
    AdminFragmentOrderHistoryBinding mAdminFragmentOrderHistoryBinding;
    AdminOrderHistoryViewModel vmAdminOrderHistory;


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
}
