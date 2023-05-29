package com.example.ecommerce_hvpp.fragments.statistics_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticRevenueBinding;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;

public class AdminStatisticRevenueFragment extends Fragment {
    AdminFragmentStatisticRevenueBinding mAdminFragmentStatisticRevenueBinding;
    AdminStatisticsRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticRevenueBinding = AdminFragmentStatisticRevenueBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // on click back page
        mAdminFragmentStatisticRevenueBinding.adminStatisticsOrdersHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticRevenueBinding.getRoot();
    }
}
