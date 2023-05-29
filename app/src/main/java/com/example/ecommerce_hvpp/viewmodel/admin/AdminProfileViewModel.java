package com.example.ecommerce_hvpp.viewmodel.admin;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;

public class AdminProfileViewModel extends ViewModel {
    AdminProfileRepository repo;
    public AdminProfileViewModel() {
        repo = new AdminProfileRepository();
    }

    public void onClickOption(View view, int option) {
        repo.onClickOption(view, option);
    }
}
