package com.example.ecommerce_hvpp;

import android.app.Application;

import io.branch.referral.Branch;

public class CustomApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Branch.enableLogging();
        Branch.enableTestMode();
        Branch.getAutoInstance(this);
    }
}
