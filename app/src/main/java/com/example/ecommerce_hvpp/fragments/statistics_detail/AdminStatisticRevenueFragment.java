package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayRevenueDataStatistics;
import static com.example.ecommerce_hvpp.util.CustomFormat.dateFormatter;
import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.dataStatisticRevenue;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.strMinDateRevenue;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticRevenueBinding;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomMarkerView;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminStatisticRevenueFragment extends Fragment {
    AdminFragmentStatisticRevenueBinding mAdminFragmentStatisticRevenueBinding;
    AdminStatisticsRepository repo;
    Map<String, Double> mListDataUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticRevenueBinding = AdminFragmentStatisticRevenueBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // init default date
        initDefaultDate();
        initDayMonthData();

        // format line
        repo.formatLineChart(mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart);

        // init chart graph
        handleFilterDate();
        handleMapToEntry(mListDataUpdate);

        // create date picker
        onClickToShowDatePicker();

        // on click filter
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.formatLineChart(mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart);
                handleFilterDate();
                handleMapToEntry(mListDataUpdate);
            }
        });

        // on click back page
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticRevenueBinding.getRoot();
    }

    private void initDefaultDate() {
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueTvStartDate.setText(repo.beginningOfMonth());
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueTvEndDate.setText(templateDate.format(new Date()));
    }

    private void initDayMonthData() {
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueDayComponentQuantity.setText(String.valueOf(dataStatisticRevenue.getDayQuantity()));
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueDayComponentPercent.setText(dataStatisticRevenue.getDayPercent());
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueDayComponentPercent.setTextColor(dataStatisticRevenue.getDayColor());

        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueMonthComponentQuantity.setText(String.valueOf(dataStatisticRevenue.getMonthQuantity()));
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueMonthComponentPercent.setText(dataStatisticRevenue.getMonthPercent());
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueMonthComponentPercent.setTextColor(dataStatisticRevenue.getMonthColor());
    }

    private void onClickToShowDatePicker() {
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueTvStartDate.setOnClickListener(repo.createDatePickerDialog(requireContext(), strMinDateRevenue));
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueTvEndDate.setOnClickListener(repo.createDatePickerDialog(requireContext(), strMinDateRevenue));
    }

    private void handleFilterDate() {
        mListDataUpdate = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.parse(mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueTvStartDate.getText(), dateFormatter);
        LocalDate endDate = LocalDate.parse(mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueTvEndDate.getText(), dateFormatter);

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            String dateString = date.format(dateFormatter);
            double defaultValue = 0;
            double value = dayRevenueDataStatistics.getOrDefault(dateString, defaultValue);
            mListDataUpdate.put(dateString, value);
        }
    }

    private void handleMapToEntry(Map<String, Double> listData) {
        List<Entry> entries = new ArrayList<>();
        List<String> xAxisDates = new ArrayList<>();
        final int[] index = {1};
        listData.forEach((key, value) -> {
            entries.add(new Entry(index[0], value.floatValue()));
            index[0]++;
            xAxisDates.add(key.substring(0, 5)); // 11/09/2003
        });
        xAxisDates.add(0, "Select");

        LineDataSet dataSet = new LineDataSet(entries, "Revenue");

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

        Legend legend = mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getLegend();
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        legend.setXEntrySpace(15);
        legend.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.crimson_pro));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setWordWrapEnabled(true);
//        legend.setMaxSizePercent(0.20f);

        XAxis xAxis = mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        CustomMarkerView marker = new CustomMarkerView(requireContext(), R.layout.admin_custom_marker);

        LineData lineData = new LineData(dataSetFinal);
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.setData(lineData);
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.setMarker(marker);

        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart
                .getXAxis()
                .setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDates));

        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getAxisRight().setDrawGridLines(false);
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getAxisRight().setDrawAxisLine(true);

        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getAxisLeft().setDrawGridLines(true);
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getAxisLeft().setDrawAxisLine(true);

        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getXAxis().setDrawGridLines(false);
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.getXAxis().setDrawAxisLine(false);

        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.invalidate();
        mAdminFragmentStatisticRevenueBinding.adminStatisticsRevenueLineChart.setVisibleXRangeMaximum(5);
    }
}
