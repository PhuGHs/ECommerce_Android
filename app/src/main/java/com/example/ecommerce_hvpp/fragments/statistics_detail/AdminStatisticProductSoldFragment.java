package com.example.ecommerce_hvpp.fragments.statistics_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticProductSoldBinding;
import com.example.ecommerce_hvpp.repositories.AdminStatisticsRepository;

public class AdminStatisticProductSoldFragment extends Fragment {
    AdminFragmentStatisticProductSoldBinding mAdminFragmentStatisticProductSoldBinding;
    AdminStatisticsRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticProductSoldBinding = AdminFragmentStatisticProductSoldBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // on click back page
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticProductSoldBinding.getRoot();
    }
}
