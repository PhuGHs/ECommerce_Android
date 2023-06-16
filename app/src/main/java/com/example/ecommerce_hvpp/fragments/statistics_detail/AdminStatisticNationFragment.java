package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.monthClubDataStatistics;
import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.monthNationDataStatistics;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.strNation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticNationBinding;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStatisticNationFragment extends Fragment {
    AdminFragmentStatisticNationBinding mAdminFragmentStatisticNationBinding;
    AdminStatisticsRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticNationBinding = AdminFragmentStatisticNationBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // init view
        initView();

        // create chart
        createChart(repo.getCurrentMonth());

        // show dropdown
        mAdminFragmentStatisticNationBinding.adminStatisticsNationMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropdown(repo.getMinMonth(monthClubDataStatistics), repo.getCurrentMonth());
            }
        });

        // on click back page
        mAdminFragmentStatisticNationBinding.adminStatisticsNationHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticNationBinding.getRoot();
    }

    private void initView() {
        mAdminFragmentStatisticNationBinding.adminStatisticsComponentNationQuantity.setText(strNation);
        mAdminFragmentStatisticNationBinding.adminStatisticsNationMonth.setText(repo.getCurrentMonth());
    }

    // create chart
    private void createChart(String currMonth) {
        createPieChart(currMonth);
        createBarChart(currMonth);
    }

    private void createPieChart(String currMonth) {
        Map<String, Integer> data = repo.getTop5Quantities(monthNationDataStatistics, currMonth);;
        List<PieEntry> entries = new ArrayList<>();
        data.forEach((key, value) -> {
            entries.add(new PieEntry(value, key));
        });

        String title = "Top 5 Nation Top-selling";
        mAdminFragmentStatisticNationBinding.adminStatisticsNationTitlePieChart.setText(title);

        PieChart pieChart = mAdminFragmentStatisticNationBinding.adminStatisticsNationPieChart;
        repo.formatPieChart(pieChart, entries, requireContext());
    }

    private void createBarChart(String currMonth) {
        Map<String, Integer> data = monthNationDataStatistics.get(currMonth);
        List<BarEntry> entries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();
        final int[] index = {1};
        data.forEach((key, value) -> {
            entries.add(new BarEntry(index[0], value));
            index[0]++;
            xAxisLabels.add(key);
        });
        xAxisLabels.add(0, "Select");

        String title = "Quantity of Retro Football Clothes Purchased by Nation";
        mAdminFragmentStatisticNationBinding.adminStatisticsNationTitleBarChart.setText(title);

        BarChart barChart = mAdminFragmentStatisticNationBinding.adminStatisticsNationBarChart;
        repo.formatBarChart(barChart, entries, xAxisLabels, requireContext());
    }

    private void showDropdown(String minMonthYear, String maxMonthYear) {
        final Calendar today = Calendar.getInstance();
        int minMonth = Integer.parseInt(minMonthYear.substring(0, 2)) - 1;
        int maxMonth = Integer.parseInt(maxMonthYear.substring(0, 2));
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(requireContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                int currMonth = selectedMonth + 1;
                String month = String.format("%02d", currMonth);
                String monthYear = month + "/" + selectedYear;
                mAdminFragmentStatisticNationBinding.adminStatisticsNationMonth.setText(month + "/" + selectedYear);
                createChart(monthYear);
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
        builder.setActivatedMonth(5)
                .setMinYear(2023)
                .setActivatedYear(2023)
                .setMaxYear(2023)
                .setMinMonth(minMonth)
                .setMaxMonth(maxMonth)
                .setTitle("Select month")
                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int selectedMonth) {
                        Log.e("Change", "change");
                    }
                })
                .build().show();
    }
}
