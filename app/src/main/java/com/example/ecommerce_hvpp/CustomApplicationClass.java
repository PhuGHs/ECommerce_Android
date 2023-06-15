package com.example.ecommerce_hvpp;

import android.app.Application;

import com.example.ecommerce_hvpp.activities.MainActivity;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;

import io.branch.referral.Branch;

public class CustomApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MainActivity.PDviewModel = new ProductViewModel();

        Branch.enableLogging();
        Branch.enableTestMode();
        Branch.getAutoInstance(this);
    }
}
