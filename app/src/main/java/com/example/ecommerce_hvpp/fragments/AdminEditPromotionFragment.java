package com.example.ecommerce_hvpp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.databinding.AdminFragmentAddPromotionBinding;

public class AdminEditPromotionFragment extends Fragment {
    AdminFragmentAddPromotionBinding mAdminFragmentEditPromotionBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentEditPromotionBinding = AdminFragmentAddPromotionBinding.inflate(inflater, container, false);

        return mAdminFragmentEditPromotionBinding.getRoot();
    }
}
