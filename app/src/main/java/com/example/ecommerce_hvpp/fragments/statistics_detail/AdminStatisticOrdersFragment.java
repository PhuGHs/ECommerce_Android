package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.mListDataStatistics;
import static com.example.ecommerce_hvpp.util.constant.dateFormatter;
import static com.example.ecommerce_hvpp.util.constant.templateDate;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticOrdersBinding;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminProfileRepository;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminPromotionRepository;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomMarkerView;
import com.example.ecommerce_hvpp.util.Resource;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class AdminStatisticOrdersFragment extends Fragment {
    AdminFragmentStatisticOrdersBinding mAdminFragmentStatisticOrdersBinding;
    AdminStatisticsRepository repo;
    Observer<Resource<Map<String, Integer>>> observer;
    Observable<Resource<Map<String, Integer>>> observable;
//    private Disposable disposable;
    Map<String, Integer> mListData;
    Map<String, Integer> mListDataUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticOrdersBinding = AdminFragmentStatisticOrdersBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // init default date
        initDefaultDate();

        // format line
        formatLineChart();

        // init observable
//        observable = repo.getObservableOrders(mListDataStatistics);
//        observer = getObserverData();
//
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);


        // init chart graph (test)
        handleFilterDate();
        handleMapToEntry(mListDataUpdate);

        // create date picker
        onClickToShowDatePicker();

        // on click filter
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formatLineChart();
                handleFilterDate();
                handleMapToEntry(mListDataUpdate);
            }
        });

        // on click back page
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticOrdersBinding.getRoot();
    }

    private void initDefaultDate() {
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersTvStartDate.setText(repo.beginningOfMonth());
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersTvEndDate.setText(templateDate.format(new Date()));
    }

    private void onClickToShowDatePicker() {
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersTvStartDate.setOnClickListener(repo.createDatePickerDialog(requireContext()));
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersTvEndDate.setOnClickListener(repo.createDatePickerDialog(requireContext()));
    }

    private void formatLineChart() {
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setPinchZoom(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setScaleEnabled(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getAxisLeft().setDrawAxisLine(true);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setDrawGridBackground(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getDescription().setEnabled(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getLegend().setEnabled(true);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.animateX(2000);

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.invalidate();
    }


    private void handleFilterDate() {
        mListDataUpdate = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.parse(mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersTvStartDate.getText(), dateFormatter);
        LocalDate endDate = LocalDate.parse(mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersTvEndDate.getText(), dateFormatter);

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            String dateString = date.format(dateFormatter);
            int value = mListDataStatistics.getOrDefault(dateString, 0);
            mListDataUpdate.put(dateString, value);
        }
    }

    private void handleMapToEntry(Map<String, Integer> listData) {
        List<Entry> entries = new ArrayList<>();
        List<String> xAxisDates = new ArrayList<>();
        final int[] index = {1};
        listData.forEach((key, value) -> {
            entries.add(new Entry(index[0], value));
            index[0]++;
            xAxisDates.add(key.substring(0, 5));
        });
        xAxisDates.add(0, "Select");

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

        Legend legend = mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getLegend();
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        legend.setXEntrySpace(15);
        legend.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.crimson_pro));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setWordWrapEnabled(true);
//        legend.setMaxSizePercent(0.20f);

        XAxis xAxis = mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        CustomMarkerView marker = new CustomMarkerView(requireContext(), R.layout.admin_custom_marker);

        LineData lineData = new LineData(dataSetFinal);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setData(lineData);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setMarker(marker);

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart
                .getXAxis()
                .setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDates));

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getAxisRight().setDrawGridLines(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getAxisRight().setDrawAxisLine(true);

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getAxisLeft().setDrawGridLines(true);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getAxisLeft().setDrawAxisLine(true);

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getXAxis().setDrawGridLines(false);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.getXAxis().setDrawAxisLine(false);

        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.invalidate();
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setVisibleXRangeMaximum(5);
    }

    private void testGraph() {
        LineDataSet dataSet = new LineDataSet(getListEntry(), "Order Quantity");

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
        CustomMarkerView marker = new CustomMarkerView(requireContext(), R.layout.admin_custom_marker);

        LineData lineData = new LineData(dataSetFinal);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setData(lineData);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setMarker(marker);
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart
                .getXAxis()
                .setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(getListAxis()));
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.invalidate();
        mAdminFragmentStatisticOrdersBinding.adminStatisticsOrdersLineChart.setVisibleXRangeMaximum(5);

    }

    private List<String> getListAxis() {
        List<String> xAxisValues = new ArrayList<>();

        xAxisValues.add("Select");
        xAxisValues.add("01/09");
        xAxisValues.add("02/09");
//        xAxisValues.add("03/09");
//        xAxisValues.add("04/09");
//        xAxisValues.add("05/09");
//        xAxisValues.add("06/09");
//        xAxisValues.add("07/09");
//        xAxisValues.add("08/09");
//        xAxisValues.add("09/09");
//        xAxisValues.add("10/09");
//        xAxisValues.add("11/09");
//        xAxisValues.add("12/09");
//        xAxisValues.add("13/09");
//        xAxisValues.add("14/09");
//        xAxisValues.add("15/09");
//        xAxisValues.add("16/09");
//        xAxisValues.add("17/09");
//        xAxisValues.add("18/09");
//        xAxisValues.add("19/09");
//        xAxisValues.add("20/09");

        return xAxisValues;
    }

    private List<Entry> getListEntry() {
        List<Entry> entries = new ArrayList<>();

        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 18));
//        entries.add(new Entry(3, 6));
//        entries.add(new Entry(4, 19));
//        entries.add(new Entry(5, 5));
//        entries.add(new Entry(6, 12));
//        entries.add(new Entry(7, 1));
//        entries.add(new Entry(8, 18));
//        entries.add(new Entry(9, 21));
//        entries.add(new Entry(10, 3));
//        entries.add(new Entry(11, 10));
//        entries.add(new Entry(12, 18));
//        entries.add(new Entry(13, 6));
//        entries.add(new Entry(14, 19));
//        entries.add(new Entry(15, 5));
//        entries.add(new Entry(16, 12));
//        entries.add(new Entry(17, 1));
//        entries.add(new Entry(18, 18));
//        entries.add(new Entry(19, 21));
//        entries.add(new Entry(20, 3));

        return entries;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
