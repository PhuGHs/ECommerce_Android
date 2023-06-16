package com.example.ecommerce_hvpp.activities;

import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.fragments.customer_fragments.DetailProductCustomerFragment;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;
import com.example.ecommerce_hvpp.util.NetworkChangeBroadcastReceiver;
import com.example.ecommerce_hvpp.util.SessionManager;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements NetworkChangeBroadcastReceiver.NetworkConnectivityListener {
    public static ProductViewModel PDviewModel;
    private BottomNavigationView bottomNav;
    private NavController navController;
    private FirebaseHelper fbHelper;
    private UserRepository repo;

    private NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;
    private RelativeLayout noInternetLayout, hasInternetLayout;
    private SessionManager sessionManager;
    private boolean isActivityFirstCreated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbHelper = FirebaseHelper.getInstance();
        setContentView(R.layout.activity_main);
        noInternetLayout = findViewById(R.id.noInternetLayout);
        hasInternetLayout = findViewById(R.id.hasInternetLayout);

        networkChangeBroadcastReceiver = new NetworkChangeBroadcastReceiver();
        networkChangeBroadcastReceiver.setListener(this);

        bottomNav = findViewById(R.id.bottom_nav);
        navController = Navigation.findNavController(this, R.id.host_fragment);
      
        SQLiteDatabase db = openOrCreateDatabase("PD.db", MODE_PRIVATE, null);
        PDviewModel = new ProductViewModel(this);
        PDviewModel.initData();
        Log.i("Main", "onCreate");

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

                    if(getIntent().hasExtra("productID")) {
                        String id = getIntent().getStringExtra("productID");
                        Log.i("start id", id);
                        DetailProductCustomerFragment fragment = new DetailProductCustomerFragment();
                        Bundle args = new Bundle();
                        args.putString("productID", id);
                        args.putBoolean("sharing", true);
                        fragment.setArguments(args);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.host_fragment, fragment)
                                .commit();
                    }
                });
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    public void showMainLayout() {
        hasInternetLayout.setVisibility(View.VISIBLE);
        noInternetLayout.setVisibility(View.GONE);
        bottomNav.setVisibility(View.VISIBLE);
    }

    public void showNoNetworkLayout() {
        noInternetLayout.setVisibility(View.VISIBLE);
        hasInternetLayout.setVisibility(View.GONE);
        bottomNav.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeBroadcastReceiver);
    }

    @Override
    public void onNetworkConnectivityChanged(boolean isConnected) {
        if(isConnected) {
            showMainLayout();
        } else {
            showNoNetworkLayout();
        }
    }
}