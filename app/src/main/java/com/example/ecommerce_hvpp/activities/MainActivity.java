package com.example.ecommerce_hvpp.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.repositories.customerRepositories.UserRepository;
import com.example.ecommerce_hvpp.util.NetworkChangeBroadcastReceiver;
import com.example.ecommerce_hvpp.util.SessionManager;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class MainActivity extends AppCompatActivity implements NetworkChangeBroadcastReceiver.NetworkConnectivityListener {
    public static ProductViewModel PDviewModel;
    private BottomNavigationView bottomNav;
    private NavController navController;
    private FirebaseHelper fbHelper;
    private UserRepository repo;

    private NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;
    private RelativeLayout noInternetLayout, hasInternetLayout;
    private SessionManager sessionManager;
    private String iD;
    private Bundle savedInstanceState;

    @Override
    protected void onStart() {
        super.onStart();
        Branch.sessionBuilder(this).withCallback(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(@Nullable BranchUniversalObject branchUniversalObject, @Nullable LinkProperties linkProperties, @Nullable BranchError error) {
                if(branchUniversalObject == null) {
                    Log.e("BUO", "null");
                } else {
                    Log.d("BUO", branchUniversalObject.getCanonicalIdentifier());
                    String path = branchUniversalObject.getCanonicalIdentifier();
                    String id = path.split("product/")[1];
                    Log.d("id", id);
                    iD = id;

                    Bundle args = new Bundle();
                    args.putString("productID", iD);
                    args.putBoolean("sharing", true);

//                    if(savedInstanceState == null) {
//                        Log.i("running", "true");
//                        DetailProductCustomerFragment fragment = new DetailProductCustomerFragment();
//                        fragment.setArguments(args);
//
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.host_fragment, fragment)
//                                .commit();
//                    } else {
//                        DetailProductCustomerFragment fragment = (DetailProductCustomerFragment) getSupportFragmentManager().findFragmentById(R.id.host_fragment);
//                        fragment.setArguments(args);
//                    }
                }
            }
        }).withData(this.getIntent().getData()).init();
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbHelper = FirebaseHelper.getInstance();
        setContentView(R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;
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