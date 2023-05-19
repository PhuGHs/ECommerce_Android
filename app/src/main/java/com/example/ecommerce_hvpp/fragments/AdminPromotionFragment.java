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
import com.example.ecommerce_hvpp.databinding.AdminFragmentPromotionBinding;
import com.example.ecommerce_hvpp.viewmodel.admin_promotion.AdminPromotionViewModel;

public class AdminPromotionFragment extends Fragment {
    AdminFragmentPromotionBinding mAdminFragmentPromotionBinding;
    AdminPromotionViewModel vmAdminPromotion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentPromotionBinding = AdminFragmentPromotionBinding.inflate(inflater, container, false);

        // init view model
        vmAdminPromotion = new ViewModelProvider(requireActivity()).get(AdminPromotionViewModel.class);

        // get data
        getData();

        // display data
        displayData();

        // on click back page
        mAdminFragmentPromotionBinding.adminPromotionHeaderBack.setOnClickListener(onClickBackPage());

        return mAdminFragmentPromotionBinding.getRoot();
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
