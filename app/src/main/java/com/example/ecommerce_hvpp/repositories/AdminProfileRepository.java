package com.example.ecommerce_hvpp.repositories;


import static com.example.ecommerce_hvpp.util.constant.CUSTOMER_MANAGEMENT;
import static com.example.ecommerce_hvpp.util.constant.DATA_STATISTICS;
import static com.example.ecommerce_hvpp.util.constant.LOG_OUT;
import static com.example.ecommerce_hvpp.util.constant.ORDER_HISTORY;
import static com.example.ecommerce_hvpp.util.constant.PROMOTION_MANAGEMENT;

import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class AdminProfileRepository {
    NavController navController;
    private final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    public AdminProfileRepository() {

    }
    public void onClickOption(View view, int option) {
        navController = Navigation.findNavController(view);
        switch (option) {
            case CUSTOMER_MANAGEMENT:
                navController.navigate(R.id.adminCustomerManagementFragment);
                break;
            case DATA_STATISTICS:
                navController.navigate(R.id.adminStatisticsFragment);
                break;
            case ORDER_HISTORY:
                navController.navigate(R.id.adminOrderHistoryFragment);
                break;
            case PROMOTION_MANAGEMENT:
                navController.navigate(R.id.adminPromotionFragment);
                break;
            case LOG_OUT:
                Toast.makeText(view.getContext(), "Logout", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public View.OnClickListener onClickBackPage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
    }

    // GET DATA CUSTOMER
    public Observable<Resource<List<Customer>>> getObservableCustomers() {
        return Observable.create(emitter -> {
            emitter.onNext(Resource.loading(null));
            firebaseHelper.getCollection("Customer").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Customer> mListCustomer = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Customer customer = snapshot.toObject(Customer.class);
                            mListCustomer.add(customer);
                            Log.e("Vucoder", customer.getName());
                        }
                        emitter.onNext(Resource.success(mListCustomer));
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }

    public Observable<Resource<Customer>> getObservableCustomerById(String customerID) {
        return Observable.create(emitter -> {
            emitter.onNext(Resource.loading(null));
            firebaseHelper.getCollection("Customer")
                    .whereEqualTo("ID", customerID)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Customer> customers = queryDocumentSnapshots.toObjects(Customer.class);
                        if (!customers.isEmpty()) {
                            Customer customer = customers.get(0);
                            emitter.onNext(Resource.success(customer));
                        } else {
                            emitter.onNext(Resource.error("Customer not found", null));
                        }
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }

    // GET DATA ORDER HISTORY
    public Observable<Resource<List<OrderHistory>>> getObservableOrderHistory() {
        return Observable.create(emitter -> {
            emitter.onNext(Resource.loading(null));
            firebaseHelper.getCollection("Order").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<OrderHistory> mListCustomer = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            OrderHistory customer = snapshot.toObject(OrderHistory.class);
                            mListCustomer.add(customer);
                            Log.e("Vucoder", String.valueOf(customer.getID()));
                            Log.e("Vucoder", String.valueOf(customer.getTimeCreate()));
                        }
                        emitter.onNext(Resource.success(mListCustomer));
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }

    // GET DATA PROMOTION


}
