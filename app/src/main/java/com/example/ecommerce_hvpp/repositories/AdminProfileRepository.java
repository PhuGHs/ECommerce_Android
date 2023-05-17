package com.example.ecommerce_hvpp.repositories;


import static com.example.ecommerce_hvpp.util.constant.CUSTOMER_MANAGEMENT;
import static com.example.ecommerce_hvpp.util.constant.DATA_STATISTICS;
import static com.example.ecommerce_hvpp.util.constant.LOG_OUT;
import static com.example.ecommerce_hvpp.util.constant.ORDER_HISTORY;
import static com.example.ecommerce_hvpp.util.constant.PROMOTION_MANAGEMENT;

import android.text.Layout;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;


public class AdminProfileRepository {
    NavController navController;

    public AdminProfileRepository() {

    }
    public void onClickOption(View view, int option) {
        navController = Navigation.findNavController(view);
        switch (option) {
            case CUSTOMER_MANAGEMENT:
                navController.navigate(R.id.adminCustomerManagementFragment);
                break;
            case DATA_STATISTICS:
                navController.navigate(R.id.adminStatisticsFragment);
                break;
            case ORDER_HISTORY:
                navController.navigate(R.id.adminOrderHistoryFragment);
                break;
            case PROMOTION_MANAGEMENT:
                navController.navigate(R.id.adminPromotionFragment);
                break;
            case LOG_OUT:
                Toast.makeText(view.getContext(), "Logout", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
