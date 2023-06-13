package com.example.ecommerce_hvpp.fragments.statistics_detail;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_hvpp.R;
import com.example.ecommerce_hvpp.databinding.AdminFragmentStatisticClubBinding;
import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;
import com.example.ecommerce_hvpp.util.CustomComponent.CustomMarkerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStatisticClubFragment extends Fragment {
    AdminFragmentStatisticClubBinding mAdminFragmentStatisticClubBinding;
    AdminStatisticsRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticClubBinding = AdminFragmentStatisticClubBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // create chart
        createChart();

        // on click back page
        mAdminFragmentStatisticClubBinding.adminStatisticsClubHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticClubBinding.getRoot();
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

        String title = "Top 5 Club Top-selling";
        mAdminFragmentStatisticClubBinding.adminStatisticsClubTitlePieChart.setText(title);

        PieChart pieChart = mAdminFragmentStatisticClubBinding.adminStatisticsClubPieChart;
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

        String title = "Quantity of Retro Football Clothes Purchased by Club";
        mAdminFragmentStatisticClubBinding.adminStatisticsClubTitleBarChart.setText(title);

        BarChart barChart = mAdminFragmentStatisticClubBinding.adminStatisticsClubBarChart;
        repo.formatBarChart(barChart, entries, xAxisLabels, requireContext());
    }
}
