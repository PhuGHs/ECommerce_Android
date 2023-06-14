package com.example.ecommerce_hvpp.activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.util.NetworkChangeBroadcastReceiver;
import com.example.ecommerce_hvpp.util.SessionManager;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;
import com.example.ecommerce_hvpp.viewmodel.Customer.RegisterLoginViewModel;
import com.google.android.material.button.MaterialButton;

public class RegisterLoginActivity extends AppCompatActivity implements NetworkChangeBroadcastReceiver.NetworkConnectivityListener {
    private NavController navController;
    private RegisterLoginViewModel viewModel;
    private RelativeLayout noInternetLayout, hasInternetLayout;
    private MaterialButton tryAgainButton;

    private NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;

    private View image;
    private ViewModelProvider vmProvider;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        noInternetLayout = findViewById(R.id.noInternetLayout);
        hasInternetLayout = findViewById(R.id.hasInternetLayout);
//        MainActivity.PDviewModel = new ProductViewModel();
        sessionManager = new SessionManager(this);

//        if(sessionManager.isLoggedIn()) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        networkChangeBroadcastReceiver = new NetworkChangeBroadcastReceiver();
        networkChangeBroadcastReceiver.setListener(this);

        tryAgainButton = findViewById(R.id.btnTryAgain);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.loginFragment);

        vmProvider = new ViewModelProvider(this);
        viewModel = vmProvider.get(RegisterLoginViewModel.class);

        MainActivity.PDviewModel = new ProductViewModel();
        MainActivity.PDviewModel.initListBestSellerLiveData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void showMainLayout() {
        noInternetLayout.setVisibility(View.GONE);
        hasInternetLayout.setVisibility(View.VISIBLE);
    }

    public void showNoNetworkLayout() {
        noInternetLayout.setVisibility(View.VISIBLE);
        hasInternetLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
