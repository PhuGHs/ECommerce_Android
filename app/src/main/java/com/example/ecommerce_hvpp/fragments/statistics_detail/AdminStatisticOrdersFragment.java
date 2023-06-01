package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.util.constant.templateDate;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticOrdersBinding;
import com.example.ecommerce_hvpp.model.DataStatistic;
import com.example.ecommerce_hvpp.model.OrderHistory;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomMarkerView;
import com.example.ecommerce_hvpp.util.Resource;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminStatisticOrdersFragment extends Fragment {
    AdminFragmentStatisticOrdersBinding mAdminFragmentStatisticOrdersBinding;
    AdminStatisticsRepository repo;
    AdminProfileRepository repoData;
    Observer<Resource<DataStatistic[]>> observer;
    Observable<Resource<DataStatistic[]>> observable;
    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticOrdersBinding = AdminFragmentStatisticOrdersBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();
        repoData = new AdminProfileRepository();

//        repo.getQuantityOrders();
        // init observable
        observable = repo.getObservableStatistics();
        observer = getObserverData();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


        // init chart graph (test)
//        testGraph();
        testData();

        // on click back page
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticOrdersBinding.getRoot();
    }

    private Observer<Resource<DataStatistic[]>> getObserverData() {
        return new Observer<Resource<DataStatistic[]>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Perform any setup here if needed
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Resource<DataStatistic[]> resource) {
                switch (resource.status) {
                    case LOADING:
                        // Handle loading state if needed
                        break;
                    case SUCCESS:
                        // handle data here from resource
                        DataStatistic[] listData = resource.data;
                        assert listData != null;
//                        testData();
//                        testGraph();

                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setTouchEnabled(true);
                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setPinchZoom(true);
                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setDragEnabled(true);
                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setScaleEnabled(true);
                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setVisibleXRangeMaximum(5);
//                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.invalidate();

                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getDescription().setEnabled(false);
                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getLegend().setEnabled(false);
//                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.animateX(2000);

//                        ArrayList<Entry> entries = new ArrayList<>();
////                        for (DataStatistic data : listData) {
////                            Log.e("VuTest", data.getDate() + " : " + data.getQuantity());
////                            try {
////                                entries.add(new Entry(templateDate.parse(data.getDate()).getTime(), data.getQuantity()));
////                            } catch (ParseException e) {
////                                throw new RuntimeException(e);
////                            }
////                        }
//                        entries.add(new Entry(1, 10));
//                        entries.add(new Entry(2, 18));
//                        entries.add(new Entry(3, 6));
//                        entries.add(new Entry(4, 19));
//
//                        LineDataSet dataSet = new LineDataSet(entries, "Order Quantity");
//                        dataSet.setColor(Color.BLUE);
//                        dataSet.setLineWidth(2f);
//                        dataSet.setCircleColor(Color.BLUE);
//                        dataSet.setCircleRadius(4f);
//                        dataSet.setDrawValues(false);
//
//                        LineData lineData = new LineData(dataSet);
//                        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setData(lineData);

//                        XAxis xAxis = mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getXAxis();
//                        xAxis.setValueFormatter(new ValueFormatter() {
//                            @Override
//                            public String getAxisLabel(float value, AxisBase axis) {
//                                return templateDate.format(new Date((long) value));
//                            }
//                        });

                        break;
                    case ERROR:
                        Log.i("VuError", resource.message);
                        break;
                }
            }


            @Override
            public void onError(@NonNull Throwable e) {
                // Handle error state if needed
            }

            @Override
            public void onComplete() {
                // Handle completion if needed
                Log.e("Vucoder", "onComplete");
            }
        };
    }

    private void testGraph() {

        List<String> xAxisValues = new ArrayList<>();

        xAxisValues.add("Select");
        xAxisValues.add("01/09");
        xAxisValues.add("02/09");
        xAxisValues.add("03/09");
        xAxisValues.add("04/09");
        xAxisValues.add("05/09");
        xAxisValues.add("06/09");
        xAxisValues.add("07/09");
        xAxisValues.add("08/09");
        xAxisValues.add("09/09");
        xAxisValues.add("10/09");
        xAxisValues.add("11/09");
        xAxisValues.add("12/09");
        xAxisValues.add("13/09");
        xAxisValues.add("14/09");
        xAxisValues.add("15/09");
        xAxisValues.add("16/09");
        xAxisValues.add("17/09");
        xAxisValues.add("18/09");
        xAxisValues.add("19/09");
        xAxisValues.add("20/09");

        List<Entry> entries = new ArrayList<>();

        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 18));
        entries.add(new Entry(3, 6));
        entries.add(new Entry(4, 19));
        entries.add(new Entry(5, 5));
        entries.add(new Entry(6, 12));
        entries.add(new Entry(7, 1));
        entries.add(new Entry(8, 18));
        entries.add(new Entry(9, 21));
        entries.add(new Entry(10, 3));
        entries.add(new Entry(11, 10));
        entries.add(new Entry(12, 18));
        entries.add(new Entry(13, 6));
        entries.add(new Entry(14, 19));
        entries.add(new Entry(15, 5));
        entries.add(new Entry(16, 12));
        entries.add(new Entry(17, 1));
        entries.add(new Entry(18, 18));
        entries.add(new Entry(19, 21));
        entries.add(new Entry(20, 3));

        LineDataSet dataSet = new LineDataSet(entries, "Order Quantity");

        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.rgb(65, 168, 121));
        dataSet.setValueTextColor(Color.rgb(55, 70, 73));
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        ArrayList<ILineDataSet> dataSetFinal = new ArrayList<>();
        dataSetFinal.add(dataSet);

        XAxis xAxis = mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineData lineData = new LineData(dataSetFinal);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setData(lineData);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart
                .getXAxis()
                .setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (h != null) {
                    Log.e("VuTest", e.getY() + "");
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private List<Entry> getIncomeEntries() {
        ArrayList<Entry> incomeEntries = new ArrayList<>();

        incomeEntries.add(new Entry(1, 11300));
        incomeEntries.add(new Entry(2, 1390));
        incomeEntries.add(new Entry(3, 0));
        incomeEntries.add(new Entry(4, 7200));
        incomeEntries.add(new Entry(5, 4790));
        incomeEntries.add(new Entry(6, 4500));
        incomeEntries.add(new Entry(7, 8000));
        incomeEntries.add(new Entry(8, 7034));
        incomeEntries.add(new Entry(9, 4307));
        incomeEntries.add(new Entry(10, 8762));
        incomeEntries.add(new Entry(11, 4355));
        incomeEntries.add(new Entry(12, 6000));

        return incomeEntries.subList(0, 12);
    }

    public void testData() {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Jan", "Feb", "March", "April", "May", "June","July", "August", "September", "October", "November", "Decemeber"));
        List<Entry> incomeEntries = getIncomeEntries();
        dataSets = new ArrayList<>();
        LineDataSet set1;
        set1 = new LineDataSet(incomeEntries, "Income");
        set1.setColor(Color.BLUE);
        set1.setLineWidth(2f);
        set1.setCircleColor(Color.BLUE);
        set1.setCircleRadius(4f);
        set1.setDrawValues(false);
        set1.setColor(Color.rgb(65, 168, 121));
        set1.setValueTextColor(Color.rgb(55, 70, 73));
        set1.setValueTextSize(10f);
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawFilled(true);

        dataSets.add(set1);

        //customization
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setTouchEnabled(true);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setDragEnabled(true);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setScaleEnabled(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setPinchZoom(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setDrawGridBackground(true);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setExtraLeftOffset(15);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setExtraRightOffset(15);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setVisibleXRangeMaximum(12);


        XAxis xAxis = mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getXAxis();
        xAxis.setGranularity(1.0f);
        xAxis.setEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis xAxis_2 = mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getAxisLeft();
        xAxis_2.setGranularity(0.1f);
        xAxis_2.setGranularityEnabled(true);

        set1.setLineWidth(4f);
        set1.setCircleRadius(3f);
        set1.setDrawValues(false);

        //String setter in x-Axis
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        CustomMarkerView marker = new CustomMarkerView(getContext(), R.layout.admin_custom_marker);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setMarker(marker);
        LineData data = new LineData(dataSets);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setData(data);
//        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setMarker(marker);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getDescription().setEnabled(false);

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.invalidate();
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart
                .setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.refreshDrawableState();
                if (h != null) {
                    Log.e("VuTest", e.toString());
                }
            }

            @Override
            public void onNothingSelected() {
//                mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.refreshDrawableState();
            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }
}
