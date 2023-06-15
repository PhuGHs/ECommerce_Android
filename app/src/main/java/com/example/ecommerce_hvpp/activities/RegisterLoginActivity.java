package com.example.ecommerce_hvpp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Product;
import com.example.ecommerce_hvpp.model.Revenue;
import com.example.ecommerce_hvpp.util.NetworkChangeBroadcastReceiver;
import com.example.ecommerce_hvpp.util.SessionManager;
import com.example.ecommerce_hvpp.viewmodel.Customer.ProductViewModel;
import com.example.ecommerce_hvpp.viewmodel.Customer.RegisterLoginViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.api.LogDescriptor;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RegisterLoginActivity extends AppCompatActivity implements NetworkChangeBroadcastReceiver.NetworkConnectivityListener {
    private NavController navController;
    private RegisterLoginViewModel viewModel;
    private RelativeLayout noInternetLayout, hasInternetLayout;
    private MaterialButton tryAgainButton;

    private NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;

    private View image;
    private ViewModelProvider vmProvider;
    public static SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        noInternetLayout = findViewById(R.id.noInternetLayout);
        hasInternetLayout = findViewById(R.id.hasInternetLayout);
        sessionManager = new SessionManager(this);

        getDataToLocalDB();

        //if(sessionManager.isLoggedIn()) {
        //    Intent intent = new Intent(this, MainActivity.class);
        //    startActivity(intent);
        //}

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
    public void getDataToLocalDB(){
        SQLiteDatabase db = openOrCreateDatabase("PD.db", MODE_PRIVATE, null);
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name = 'PRODUCT';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            Log.d("SQLite","Database created");
        }
        else {
            Log.d("SQLite", "Creating database");

            // create table
            String createProductTable = "CREATE TABLE PRODUCT(ID TEXT Primary Key, Name TEXT, " +
                    "Club TEXT, Nation TEXT, Season TEXT, Description TEXT, PointAvg REAL, " +
                    "Price REAL, Status TEXT, SizeM INTEGER, SizeL INTEGER, SizeXL INTEGER, " +
                    "TimeAdded INTEGER, URLmain TEXT, URLsub1 TEXT, URLsub2 TEXT, URLthumb TEXT)";
            db.execSQL(createProductTable);
            String createRevenueTable = "CREATE TABLE REVENUE(ID INTEGER Primary Key AUTOINCREMENT, ProductID TEXT, Quantity INTEGER)";
            db.execSQL(createRevenueTable);

            //insert data to product table
            FirebaseHelper helper = FirebaseHelper.getInstance();
            helper.getCollection("Product")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Log.d("Get all product", "Success");
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            ContentValues product = new ContentValues();
                            product.put("ID", documentSnapshot.getString("id"));

                            String name = documentSnapshot.getString("name");
                            product.put("Name", documentSnapshot.getString("name"));
                            product.put("Club", documentSnapshot.getString("club"));
                            product.put("Nation", documentSnapshot.getString("nation"));
                            product.put("Season", documentSnapshot.getString("season"));
                            product.put("Description", documentSnapshot.getString("description"));
                            product.put("PointAvg", documentSnapshot.getDouble("pointAvg"));
                            product.put("Price", documentSnapshot.getDouble("price"));
                            product.put("Status", documentSnapshot.getString("status"));
                            product.put("SizeM", documentSnapshot.getLong("size_m"));
                            product.put("SizeL", documentSnapshot.getLong("size_l"));
                            product.put("SizeXL", documentSnapshot.getLong("size_xl"));
                            product.put("TimeAdded", documentSnapshot.getTimestamp("time_added").getSeconds());
                            product.put("URLmain", documentSnapshot.getString("url_main"));
                            product.put("URLsub1", documentSnapshot.getString("url_sub1"));
                            product.put("URLsub2", documentSnapshot.getString("url_sub2"));
                            product.put("URLthumb", documentSnapshot.getString("url_thumb"));

                            Log.d("Get product to SQLite", name);
                            long result = db.insert("PRODUCT", null, product);
                            if (result != - 1) Log.d("GET PRODUCT TO SQLite", "Success");
                        }
                    })
                    .addOnFailureListener(e -> Log.d("Get product to SQLite", "Failure"));
            helper.getCollection("Order").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String document_id = document.getId();

                                helper.getCollection("Order").document(document_id).collection("items")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()){
                                                //get all revenue
                                                for (QueryDocumentSnapshot items1 : task1.getResult()){
                                                    String product_id = items1.getString("product_id");
                                                    long quantity = items1.getLong("quantity");

                                                    ContentValues revenue = new ContentValues();
                                                    revenue.put("ProductID", product_id);
                                                    revenue.put("Quantity", quantity);

                                                    long result = db.insert("REVENUE", null, revenue);
                                                    if (result != - 1) Log.d("GET REVENUE TO SQLite", "Success");
                                                    Log.d("Revenue", product_id + quantity);
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }
}
