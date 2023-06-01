package com.example.ecommerce_hvpp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TestActivityAdmin extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_admin);

        bottomNav = findViewById(R.id.test_bottom_nav);
        navController = Navigation.findNavController(this, R.id.test_host_fragment);

        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.test_bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}