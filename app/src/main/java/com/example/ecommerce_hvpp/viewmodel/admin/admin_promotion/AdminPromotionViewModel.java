package com.example.ecommerce_hvpp.viewmodel.admin.admin_promotion;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminPromotionRepository;

public class AdminPromotionViewModel extends ViewModel {
    AdminPromotionRepository repo;

    public AdminPromotionViewModel() {
        repo = new AdminPromotionRepository();
    }

    public void onClickAddPromotion(View view) {
        repo.onClickAddPromotion(view);
    }
}
