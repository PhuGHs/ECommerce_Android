package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.strNation;

import android.os.Bundle;
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

import java.util.ArrayList;
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
        createChart();

        // on click back page
        mAdminFragmentStatisticNationBinding.adminStatisticsNationHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticNationBinding.getRoot();
    }

    private void initView() {
        mAdminFragmentStatisticNationBinding.adminStatisticsComponentNationQuantity.setText(strNation);
        mAdminFragmentStatisticNationBinding.adminStatisticsNationMonth.setText(repo.getCurrentMonth());
    }

    private Map<String, Integer> getData() {
        Map<String, Integer> data = new HashMap<>();

        data.put("Real Madrid" ,30);
        data.put("Manchester City", 40);
        data.put("Liverpool", 10);
        data.put("Inter Milan", 27);
        data.put("Manchester United", 10);

        return data;
    }

    // create chart
    private void createChart() {

        createPieChart();
        createBarChart();
    }

    private void createPieChart() {
        Map<String, Integer> data = getData();
        List<PieEntry> entries = new ArrayList<>();
        data.forEach((key, value) -> {
            entries.add(new PieEntry(value, key));
        });

        String title = "Top 5 Nation Top-selling";
        mAdminFragmentStatisticNationBinding.adminStatisticsNationTitlePieChart.setText(title);

        PieChart pieChart = mAdminFragmentStatisticNationBinding.adminStatisticsNationPieChart;
        repo.formatPieChart(pieChart, entries, requireContext());
    }

    private void createBarChart() {
        Map<String, Integer> data = getData();
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
}
