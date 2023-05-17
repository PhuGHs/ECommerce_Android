package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce_hvpp.databinding.AdminFragmentProfileBinding;
import com.example.ecommerce_hvpp.viewmodel.AdminProfileViewModel;

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

        return mAdminFragmentProfileBinding.getRoot();
    }
}
