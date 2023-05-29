package com.example.ecommerce_hvpp.viewmodel.admin_promotion;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.fragments.AdminPromotionFragment;
import com.example.ecommerce_hvpp.repositories.AdminPromotionRepository;

public class AdminPromotionViewModel extends ViewModel {
    AdminPromotionRepository repo;

    public AdminPromotionViewModel() {
        repo = new AdminPromotionRepository();
    }

    public void onClickAddPromotion(View view) {
        repo.onClickAddPromotion(view);
    }
}
