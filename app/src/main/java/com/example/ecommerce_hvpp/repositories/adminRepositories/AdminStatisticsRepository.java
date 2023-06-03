package com.example.ecommerce_hvpp.repositories.adminRepositories;

import static com.example.ecommerce_hvpp.util.constant.STATISTIC_ORDERS;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_PRODUCT_SOLD;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_REVENUE;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_VISITORS;
import static com.example.ecommerce_hvpp.util.constant.templateDate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.DataStatistic;
import com.example.ecommerce_hvpp.util.Resource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class AdminStatisticsRepository {
    NavController navController;
    private final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
    public static Map<String, Integer> mListDataStatistics;

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

    public String beginningOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return templateDate.format(calendar.getTime());
    }

    public View.OnClickListener createDatePickerDialog(Context mContext) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEventDatePickerDialog(mContext, (TextView) view);
            }
        };
    }

    // handle date picker click
    public void handleEventDatePickerDialog(Context mContext, TextView tvDate) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String selectedDate = tvDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(selectedDate);
            c.setTime(date);
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int currMonth = monthOfYear + 1;
                        @SuppressLint("DefaultLocale") String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        @SuppressLint("DefaultLocale") String formattedMonth = String.format(Locale.getDefault(), "%02d", currMonth);
                        tvDate.setText(formattedDay + "/" + formattedMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public Observable<Resource<Map<String, Integer>>> getObservableStatisticsOrders() {
        return Observable.create(emitter -> {
            emitter.onNext(Resource.loading(null));
            firebaseHelper
                    .getCollection("Voucher")
                    .orderBy("date_begin", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        List<DataStatistic> mListData = new ArrayList<>();
                        Map<String, Integer> mDataStatistics = new HashMap<>();

                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            String date = templateDate
                                    .format(Objects.requireNonNull(snapshot.getTimestamp("date_begin")).toDate());
                            if (mDataStatistics.containsKey(date)) {
                                mDataStatistics.put(date, mDataStatistics.get(date) + 1);
                            } else {
                                mDataStatistics.put(date, 1);
                            }
                        }

//                        dateCounts.forEach((key, value) -> mListData.add(new DataStatistic(key, value)));
//                        DataStatistic[] DataStatistic = mListData.toArray(new DataStatistic[0]);

                        emitter.onNext(Resource.success(mDataStatistics));
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(Resource.error(e.getMessage(), null));
                        emitter.onComplete();
                    });
        });
    }

    public Observable<Resource<Map<String, Integer>>> getObservableOrders(Map<String, Integer> mapString) {
        return Observable.create(emitter -> {
            try {
                Resource<Map<String, Integer>> resource = Resource.success(mapString);
                emitter.onNext(resource);

                emitter.onComplete();
            } catch (Exception e) {
                Resource<Map<String, Integer>> resource = Resource.error(e.getMessage(), null);
                emitter.onNext(resource);

                emitter.onComplete();
            }
        });
    }
}
