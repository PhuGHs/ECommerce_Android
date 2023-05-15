package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.databinding.AdminFragmentCustomerManagementBinding;
import com.example.ecommerce_hvpp.viewmodel.admin_customer_management.AdminCustomerManagementViewModel;

public class AdminCustomerManagementFragment extends Fragment {
    AdminFragmentCustomerManagementBinding mFragmentAdminManageCustomerBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminManageCustomerBinding = AdminFragmentCustomerManagementBinding.inflate(inflater, container, false);

        AdminCustomerManagementViewModel vmAdminCustomerManagement = new AdminCustomerManagementViewModel();
        mFragmentAdminManageCustomerBinding.setAdminCustomerManagementViewModel(vmAdminCustomerManagement);

        return mFragmentAdminManageCustomerBinding.getRoot();
    }
}
