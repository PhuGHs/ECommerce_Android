package com.example.ecommerce_hvpp.repositories.adminRepositories;

import static com.example.ecommerce_hvpp.util.constant.CUSTOMER_MANAGEMENT;
import static com.example.ecommerce_hvpp.util.constant.DATA_STATISTICS;
import static com.example.ecommerce_hvpp.util.constant.LOG_OUT;
import static com.example.ecommerce_hvpp.util.constant.ORDER_HISTORY;
import static com.example.ecommerce_hvpp.util.constant.PROMOTION_MANAGEMENT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.model.Promotion;
import com.example.ecommerce_hvpp.model.User;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomToast;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
        }
    }

    public void logOutApp(Context context) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
//                        mAuth.signOut();
                        navController.navigate(R.id.loginFragment);
                        CustomToast.ShowToastMessage(context, 1, "Đăng xuất thành công");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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
            firebaseHelper
                    .getCollection("Voucher")
                    .orderBy("date_end", Query.Direction.DESCENDING)
                    .get()
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
