package com.example.ecommerce_hvpp.repositories.adminRepositories;

import static com.example.ecommerce_hvpp.util.constant.STATISTIC_ORDERS;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_PRODUCT_SOLD;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_REVENUE;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_VISITORS;
import static com.example.ecommerce_hvpp.util.constant.templateDate;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.DataStatistic;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;

public class AdminStatisticsRepository {
    NavController navController;
    private final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    public AdminStatisticsRepository() {}

    public void onClickOption(View view, int option) {
        navController = Navigation.findNavController(view);
        switch (option) {
            case STATISTIC_VISITORS:
                navController.navigate(R.id.adminStatisticVisitorsFragment);
                break;
            case STATISTIC_ORDERS:
                navController.navigate(R.id.adminStatisticOrdersFragment);
                break;
            case STATISTIC_REVENUE:
                navController.navigate(R.id.adminStatisticRevenueFragment);
                break;
            case STATISTIC_PRODUCT_SOLD:
                navController.navigate(R.id.adminStatisticProductSoldFragment);
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

    public Observable<Resource<DataStatistic[]>> getObservableStatistics() {
        return Observable.create(emitter -> {
            emitter.onNext(Resource.loading(null));
            firebaseHelper
                    .getCollection("Voucher")
                    .orderBy("date_begin", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DataStatistic> mListData = new ArrayList<>();
                        Map<String, Integer> dateCounts = new HashMap<>();

                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            String date = templateDate
                                    .format(Objects.requireNonNull(snapshot.getTimestamp("date_begin")).toDate());
                            if (dateCounts.containsKey(date)) {
                                dateCounts.put(date, dateCounts.get(date) + 1);
                            } else {
                                dateCounts.put(date, 1);
                            }
                        }

                        dateCounts.forEach((key, value) -> mListData.add(new DataStatistic(key, value)));
                        DataStatistic[] DataStatistic = mListData.toArray(new DataStatistic[0]);

                        emitter.onNext(Resource.success(DataStatistic));
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }
}
