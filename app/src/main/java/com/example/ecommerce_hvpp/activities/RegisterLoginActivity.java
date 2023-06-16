package com.example.ecommerce_hvpp.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.util.NetworkChangeBroadcastReceiver;
import com.example.ecommerce_hvpp.util.SessionManager;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;
import com.example.ecommerce_hvpp.viewmodel.Customer.RegisterLoginViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class RegisterLoginActivity extends AppCompatActivity implements NetworkChangeBroadcastReceiver.NetworkConnectivityListener {
    private NavController navController;
    private RegisterLoginViewModel viewModel;
    private RelativeLayout noInternetLayout, hasInternetLayout;
    private MaterialButton tryAgainButton;

    private NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;

    private View image;
    private ViewModelProvider vmProvider;
    public static SessionManager sessionManager;
    private FirebaseHelper fbHelper;
    private String iD = "";

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.PDviewModel = new ProductViewModel();

        Branch.sessionBuilder(this).withCallback(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(@Nullable BranchUniversalObject branchUniversalObject, @Nullable LinkProperties linkProperties, @Nullable BranchError error) {
                if(branchUniversalObject == null) {
                    Log.e("BUO", "null");
                } else {
                    Log.d("BUO", branchUniversalObject.getCanonicalIdentifier());
                    String path = branchUniversalObject.getCanonicalIdentifier();
                    String id = path.split("product/")[1];
                    Log.d("branch id", id);
                    iD = id;
                }
            }
        }).withData(this.getIntent().getData()).init();

        Log.i("init", "init");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                MainActivity.PDviewModel.initListBestSellerLiveData().thenRunAsync(new Runnable() {
                    @Override
                    public void run() {
                        if(fbHelper.getAuth().getCurrentUser() != null && !Objects.equals(iD, "")) {
                            Log.i("Register", "onStart inside success");
                            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                            scheduledExecutorService.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                                    intent.putExtra("productID", iD);
                                    Log.i("iD", iD);
                                    startActivity(intent);
                                }
                            }, 2, TimeUnit.SECONDS);
                        } else if (fbHelper.getAuth().getCurrentUser() != null){
                            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                            scheduledExecutorService.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }, 2, TimeUnit.SECONDS);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        Branch.sessionBuilder(this).withCallback(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(@Nullable BranchUniversalObject branchUniversalObject, @Nullable LinkProperties linkProperties, @Nullable BranchError error) {
                if(branchUniversalObject == null) {
                    Log.e("BUO_re", "null");
                } else {
                    Log.d("BUO_re", branchUniversalObject.getCanonicalIdentifier());
                }
                if (linkProperties == null) {
                    Log.e("LINK PROPERTIES_re", "null");
                } else {
                    Log.d("LINK PROPERTIES_Re", linkProperties.getControlParams().get("custom_data"));
                }
            }
        }).reInit();

        Log.i("reinit", "reinit");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        Log.i("Register", "onCreate");
        noInternetLayout = findViewById(R.id.noInternetLayout);
        hasInternetLayout = findViewById(R.id.hasInternetLayout);
        fbHelper = FirebaseHelper.getInstance();
        networkChangeBroadcastReceiver = new NetworkChangeBroadcastReceiver();
        networkChangeBroadcastReceiver.setListener(this);
        tryAgainButton = findViewById(R.id.btnTryAgain);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.loginFragment);

        vmProvider = new ViewModelProvider(this);
        viewModel = vmProvider.get(RegisterLoginViewModel.class);
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
