package com.example.ecommerce_hvpp.repositories;

import static com.example.ecommerce_hvpp.util.constant.STATISTIC_ORDERS;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_PRODUCT_SOLD;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_REVENUE;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_VISITORS;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminStatisticsRepository {
    NavController navController;
    private final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    public AdminStatisticsRepository() {}

    public void onClickOption(View view, int option) {
        navController = Navigation.findNavController(view);
        switch (option) {
            case STATISTIC_VISITORS:
                navController.navigate(R.id.adminStatisticVisitorsFragment);
                break;
            case STATISTIC_ORDERS:
                navController.navigate(R.id.adminStatisticOrdersFragment);
                break;
            case STATISTIC_REVENUE:
                navController.navigate(R.id.adminStatisticRevenueFragment);
                break;
            case STATISTIC_PRODUCT_SOLD:
                navController.navigate(R.id.adminStatisticProductSoldFragment);
                break;
        }
    }

    public View.OnClickListener onClickBackPage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
    }

    public void getQuantityOrders() {
        CollectionReference ordersRef = firebaseHelper.getCollection("Voucher");
        ordersRef.orderBy("date_begin", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Integer> dateCounts = new HashMap<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String date = document.getString("date_begin");
//                            if (dateCounts.containsKey(date)) {
//                                dateCounts.put(date, dateCounts.get(date) + 1);
//                            } else {
//                                dateCounts.put(date, 1);
//                            }
                        }
//                    dateCounts.forEach((key, value) -> Log.e("VuCount", key + " : " + value));

                    } else {
                        Log.e("VuError", "Error");
                    }
                });
    }

    public void getQuantityOrder() {
        CollectionReference ordersRef = firebaseHelper.getCollection("Voucher");
        ordersRef.orderBy("date_begin", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // handle code here
                    } else {
                        Log.e("VuError", "Error");
                    }
                });
    }
}
