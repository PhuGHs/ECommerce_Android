package com.example.ecommerce_hvpp.repositories.adminRepositories;

import static com.example.ecommerce_hvpp.util.CustomDateFormat.dateFormatter;
import static com.example.ecommerce_hvpp.util.CustomDateFormat.monthFormatter;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_ORDERS;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_PRODUCT_SOLD;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_REVENUE;
import static com.example.ecommerce_hvpp.util.constant.STATISTIC_VISITORS;
import static com.example.ecommerce_hvpp.util.CustomDateFormat.templateDate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.firebase.FirebaseHelper;
import com.example.ecommerce_hvpp.model.DataStatisticInt;
import com.example.ecommerce_hvpp.util.Resource;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;


public class AdminStatisticsRepository {
    NavController navController;
    private final FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
    public static Map<String, Integer> dayOrdersDataStatistics;

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
    public String getCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(dateFormatter);
    }

    public String getPreviousDay() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusDays(1);
        return previousDate.format(dateFormatter);
    }

    public String getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(monthFormatter);
    }

    public String getPreviousMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonth = currentDate.minusMonths(1);
        return previousMonth.format(monthFormatter);
    }

    public int handlePercent(int currentQuantity, int previousQuantity) {
        int result = 0;
        if (previousQuantity == 0 && currentQuantity != 0) {
            result = 100;
        } else if (previousQuantity != 0) {
            if (currentQuantity == 0) {
                result = -100;
            } else {
                float percent = (float) (currentQuantity - previousQuantity) / previousQuantity * 100;
                result = (int) Math.round(percent);
            }
        }

        return result;
    }

    public Pair<String, Integer> handleResult(Context context, int result) {
        if (result <= 0) {
            return new Pair<>("" + result + "%", ContextCompat.getColor(context, R.color.decrease_percent));
        } else {
            return new Pair<>("+" + result + "%", ContextCompat.getColor(context, R.color.increase_percent));
        }
    }

    @SuppressLint("SetTextI18n")
    public Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> handleComponentStatistics(Map<String, Integer> dayDataStatistics, TextView tvContent) {
        Map<String, Integer> monthDataStatistics = new HashMap<>();
        dayDataStatistics.forEach((key, value) -> {
            String strMonth = key.substring(3, 10);
            if (monthDataStatistics.containsKey(strMonth)) {
                monthDataStatistics.put(strMonth, monthDataStatistics.get(strMonth) + value);
            } else {
                monthDataStatistics.put(strMonth, value);
            }
        });

        int currDayQuantity = dayDataStatistics.getOrDefault(getCurrentDay(), 0);
        int prevDayQuantity = dayDataStatistics.getOrDefault(getPreviousDay(), 0);
        int resultDay = handlePercent(currDayQuantity, prevDayQuantity);

        int currMonthQuantity = monthDataStatistics.getOrDefault(getCurrentMonth(), 0);
        int prevMonthQuantity = monthDataStatistics.getOrDefault(getPreviousMonth(), 0);
        int resultMonth = handlePercent(currMonthQuantity, prevMonthQuantity);

        tvContent.setText(currDayQuantity + "");
        return new Pair<>(new Pair<>(resultDay, resultMonth), new Pair<>(currDayQuantity, currMonthQuantity));
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
    
    public void formatLineChart(LineChart lineChart) {
        lineChart.setPinchZoom(false);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisLeft().setDrawAxisLine(true);
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.animateX(2000);

        lineChart.invalidate();
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
