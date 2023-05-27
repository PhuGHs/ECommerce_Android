package com.example.ecommerce_hvpp.fragments.statistics_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticVisitorsBinding;
import com.example.ecommerce_hvpp.repositories.AdminStatisticsRepository;

public class AdminStatisticVisitorsFragment extends Fragment {
    AdminFragmentStatisticVisitorsBinding mAdminFragmentStatisticVisitorsBinding;
    AdminStatisticsRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticVisitorsBinding = AdminFragmentStatisticVisitorsBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // on click back page
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticVisitorsBinding.getRoot();
    }
}
