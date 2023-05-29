package com.example.ecommerce_hvpp.viewmodel.admin;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;


public class AdminStatisticsViewModel extends ViewModel {
    AdminStatisticsRepository repo;

    public AdminStatisticsViewModel() {
        repo = new AdminStatisticsRepository();
    }

    public void onClickOption(View view, int option) {
        repo.onClickOption(view, option);
    }
}
