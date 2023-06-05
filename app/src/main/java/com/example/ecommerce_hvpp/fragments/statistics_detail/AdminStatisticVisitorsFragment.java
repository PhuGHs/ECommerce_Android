package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayVisitorsDataStatistics;
import static com.example.ecommerce_hvpp.util.CustomFormat.dateFormatter;
import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.dataStatisticVisitors;

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
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticVisitorsBinding;
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

public class AdminStatisticVisitorsFragment extends Fragment {
    AdminFragmentStatisticVisitorsBinding mAdminFragmentStatisticVisitorsBinding;
    AdminStatisticsRepository repo;
    Map<String, Integer> mListDataUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticVisitorsBinding = AdminFragmentStatisticVisitorsBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // init default date
        initDefaultDate();
        initDayMonthData();

        // format line
        repo.formatLineChart(mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart);

        // init chart graph
        handleFilterDate();
        handleMapToEntry(mListDataUpdate);

        // create date picker
        onClickToShowDatePicker();

        // on click filter
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.formatLineChart(mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart);
                handleFilterDate();
                handleMapToEntry(mListDataUpdate);
            }
        });

        // on click back page
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticVisitorsBinding.getRoot();
    }

    private void initDefaultDate() {
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsTvStartDate.setText(repo.beginningOfMonth());
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsTvEndDate.setText(templateDate.format(new Date()));
    }

    private void initDayMonthData() {
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsDayComponentQuantity.setText(String.valueOf(dataStatisticVisitors.getDayQuantity()));
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsDayComponentPercent.setText(dataStatisticVisitors.getDayPercent());
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsDayComponentPercent.setTextColor(dataStatisticVisitors.getDayColor());

        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsMonthComponentQuantity.setText(String.valueOf(dataStatisticVisitors.getMonthQuantity()));
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsMonthComponentPercent.setText(dataStatisticVisitors.getMonthPercent());
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsMonthComponentPercent.setTextColor(dataStatisticVisitors.getMonthColor());
    }

    private void onClickToShowDatePicker() {
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsTvStartDate.setOnClickListener(repo.createDatePickerDialog(requireContext()));
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsTvEndDate.setOnClickListener(repo.createDatePickerDialog(requireContext()));
    }

    private void handleFilterDate() {
        mListDataUpdate = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.parse(mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsTvStartDate.getText(), dateFormatter);
        LocalDate endDate = LocalDate.parse(mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsTvEndDate.getText(), dateFormatter);

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            String dateString = date.format(dateFormatter);
            int value = dayVisitorsDataStatistics.getOrDefault(dateString, 0);
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
            xAxisDates.add(key.substring(0, 5)); // 11/09/2003
        });
        xAxisDates.add(0, "Select");

        LineDataSet dataSet = new LineDataSet(entries, "Visitor Quantity");

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

        Legend legend = mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getLegend();
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        legend.setXEntrySpace(15);
        legend.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.crimson_pro));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setWordWrapEnabled(true);
//        legend.setMaxSizePercent(0.20f);

        XAxis xAxis = mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        CustomMarkerView marker = new CustomMarkerView(requireContext(), R.layout.admin_custom_marker);

        LineData lineData = new LineData(dataSetFinal);
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.setData(lineData);
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.setMarker(marker);

        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart
                .getXAxis()
                .setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDates));

        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getAxisRight().setDrawGridLines(false);
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getAxisRight().setDrawAxisLine(true);

        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getAxisLeft().setDrawGridLines(true);
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getAxisLeft().setDrawAxisLine(true);

        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getXAxis().setDrawGridLines(false);
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.getXAxis().setDrawAxisLine(false);

        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.invalidate();
        mAdminFragmentStatisticVisitorsBinding.adminStatisticsVisitorsLineChart.setVisibleXRangeMaximum(5);
    }

}
