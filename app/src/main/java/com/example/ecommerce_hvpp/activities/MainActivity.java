package com.example.ecommerce_hvpp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static ProductViewModel PDviewModel;
    private BottomNavigationView bottomNav;
    private NavController navController;
    private FirebaseHelper fbHelper;
    private UserRepository repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbHelper = FirebaseHelper.getInstance();
        setContentView(R.layout.activity_main);


        bottomNav = findViewById(R.id.bottom_nav);
        navController = Navigation.findNavController(this, R.id.host_fragment);

        String Id = fbHelper.getAuth().getCurrentUser().getUid();
        fbHelper.getCollection("users").document(Id).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            boolean isAdmin = documentSnapshot.getBoolean("admin");
                            if(isAdmin) {
                                bottomNav.inflateMenu(R.menu.admin_bottom_menu);
                                navController.setGraph(R.navigation.admin_nav_graph);
                            } else {
                                bottomNav.inflateMenu(R.menu.bottom_menu);
                                navController.setGraph(R.navigation.nav_graph);
                            }
                        });

        NavigationUI.setupWithNavController(bottomNav, navController);

        PDviewModel.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}