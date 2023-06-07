package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.dayProductSoldDataStatistics;
import static com.example.ecommerce_hvpp.util.CustomFormat.dateFormatter;
import static com.example.ecommerce_hvpp.util.CustomFormat.templateDate;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.dataStatisticProductSold;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.strMinDateProductSold;

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
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticProductSoldBinding;
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

public class AdminStatisticProductSoldFragment extends Fragment {
    AdminFragmentStatisticProductSoldBinding mAdminFragmentStatisticProductSoldBinding;
    AdminStatisticsRepository repo;
    Map<String, Integer> mListDataUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticProductSoldBinding = AdminFragmentStatisticProductSoldBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // init default date
        initDefaultDate();
        initDayMonthData();

        // format line
        repo.formatLineChart(mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart);

        // init chart graph
        handleFilterDate();
        handleMapToEntry(mListDataUpdate);

        // create date picker
        onClickToShowDatePicker();

        // on click filter
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.formatLineChart(mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart);
                handleFilterDate();
                handleMapToEntry(mListDataUpdate);
            }
        });

        // on click back page
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticProductSoldBinding.getRoot();
    }

    private void initDefaultDate() {
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldTvStartDate.setText(repo.beginningOfMonth());
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldTvEndDate.setText(templateDate.format(new Date()));
    }

    private void initDayMonthData() {
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldDayComponentQuantity.setText(String.valueOf(dataStatisticProductSold.getDayQuantity()));
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldDayComponentPercent.setText(dataStatisticProductSold.getDayPercent());
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldDayComponentPercent.setTextColor(dataStatisticProductSold.getDayColor());

        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldMonthComponentQuantity.setText(String.valueOf(dataStatisticProductSold.getMonthQuantity()));
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldMonthComponentPercent.setText(dataStatisticProductSold.getMonthPercent());
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldMonthComponentPercent.setTextColor(dataStatisticProductSold.getMonthColor());
    }

    private void onClickToShowDatePicker() {
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldTvStartDate.setOnClickListener(repo.createDatePickerDialog(requireContext(), strMinDateProductSold));
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldTvEndDate.setOnClickListener(repo.createDatePickerDialog(requireContext(), strMinDateProductSold));
    }

    private void handleFilterDate() {
        mListDataUpdate = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.parse(mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldTvStartDate.getText(), dateFormatter);
        LocalDate endDate = LocalDate.parse(mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldTvEndDate.getText(), dateFormatter);

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            String dateString = date.format(dateFormatter);
            int value = dayProductSoldDataStatistics.getOrDefault(dateString, 0);
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

        LineDataSet dataSet = new LineDataSet(entries, "Product Sold Quantity");

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

        Legend legend = mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getLegend();
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        legend.setXEntrySpace(15);
        legend.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.crimson_pro));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setWordWrapEnabled(true);
//        legend.setMaxSizePercent(0.20f);

        XAxis xAxis = mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        CustomMarkerView marker = new CustomMarkerView(requireContext(), R.layout.admin_custom_marker);

        LineData lineData = new LineData(dataSetFinal);
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.setData(lineData);
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.setMarker(marker);

        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart
                .getXAxis()
                .setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDates));

        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getAxisRight().setDrawGridLines(false);
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getAxisRight().setDrawAxisLine(true);

        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getAxisLeft().setDrawGridLines(true);
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getAxisLeft().setDrawAxisLine(true);

        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getXAxis().setDrawGridLines(false);
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.getXAxis().setDrawAxisLine(false);

        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.invalidate();
        mAdminFragmentStatisticProductSoldBinding.adminStatisticsProductSoldLineChart.setVisibleXRangeMaximum(5);
    }
}
