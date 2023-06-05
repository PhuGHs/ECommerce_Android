package com.example.ecommerce_hvpp.repositories.adminRepositories;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayOrdersDataStatistics;
import static com.example.ecommerce_hvpp.util.constant.CUSTOMER_MANAGEMENT;
import static com.example.ecommerce_hvpp.util.constant.DATA_STATISTICS;
import static com.example.ecommerce_hvpp.util.constant.LOG_OUT;
import static com.example.ecommerce_hvpp.util.constant.ORDER_HISTORY;
import static com.example.ecommerce_hvpp.util.constant.PROMOTION_MANAGEMENT;
import static com.example.ecommerce_hvpp.util.CustomDateFormat.templateDate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.Customer;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

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

//  GET DATA CUSTOMER
    public Observable<Resource<List<User>>> getObservableCustomers() {
        return Observable.create(emitter -> {
            Log.e("VucoderSearch", Thread.currentThread().getName());
            emitter.onNext(Resource.loading(null));
            firebaseHelper.getCollection("users").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<User> mListUser = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            User user = snapshot.toObject(User.class);
                            mListUser.add(user);
                            Log.e("VucoderSearch", user.getUsername());
                        }
                        emitter.onNext(Resource.success(mListUser));
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }

    public Observable<Resource<User>> getObservableCustomerById(String userID) {
        return Observable.create(emitter -> {
            emitter.onNext(Resource.loading(null));
            firebaseHelper.getCollection("users").document(userID)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.exists()) {
                            User user = queryDocumentSnapshots.toObject(User.class);
                            Log.e("VuAdapter", user.getUsername());
                            emitter.onNext(Resource.success(user));
                        } else {
                            emitter.onNext(Resource.error("User not found", null));
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
                        List<OrderHistory> mListOrderHistory = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            OrderHistory orderHistory = snapshot.toObject(OrderHistory.class);
                            mListOrderHistory.add(orderHistory);
                            Log.e("Vucoder", String.valueOf(orderHistory.getId()));
                        }
                        emitter.onNext(Resource.success(mListOrderHistory));
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }

    // GET DATA PROMOTION
    public Observable<Resource<List<Promotion>>> getObservablePromotion() {
        return Observable.create(emitter -> {
            emitter.onNext(Resource.loading(null));
            firebaseHelper.getCollection("Voucher").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Promotion> mListPromotion = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Promotion promotion = snapshot.toObject(Promotion.class);
                            mListPromotion.add(promotion);
                            Log.e("Vucoder", String.valueOf(promotion.getDate_begin()));
                        }
                        emitter.onNext(Resource.success(mListPromotion));
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }
}
