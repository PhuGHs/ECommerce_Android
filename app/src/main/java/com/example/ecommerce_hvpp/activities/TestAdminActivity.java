package com.example.ecommerce_hvpp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ecommerce_hvpp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TestAdminActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity_admin);

        bottomNav = findViewById(R.id.bottom_nav_test);
        navController = Navigation.findNavController(this, R.id.host_fragment_test);

        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_test);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}