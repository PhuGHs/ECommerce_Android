package com.example.ecommerce_hvpp.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.repositories.AdminProfileRepository;

public class AdminProfileViewModel extends ViewModel {
    AdminProfileRepository repo;
    public AdminProfileViewModel() {
        repo = new AdminProfileRepository();
    }

    public void onClickOption(View view, int option) {
        repo.onClickOption(view, option);
    }
}
