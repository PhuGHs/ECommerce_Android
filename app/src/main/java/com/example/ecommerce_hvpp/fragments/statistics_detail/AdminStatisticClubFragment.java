package com.example.ecommerce_hvpp.fragments.statistics_detail;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
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
        createPieChart();

        mAdminFragmentStatisticClubBinding.adminStatisticsClubHeaderBack.setOnClickListener(repo.onClickBackPage());
        return mAdminFragmentStatisticClubBinding.getRoot();
    }

    private Map<String, Integer> getData() {
        Map<String, Integer> data = new HashMap<>();

        data.put("Real Madrid" ,10);
        data.put("Manchester City", 50);
        data.put("Liverpool", 19);
        data.put("Inter Milan", 27);
        data.put("Manchester United", 10);
        data.put("Arsenal", 43);
        data.put("Juventus", 43);
        data.put("Chelsea", 43);
        data.put("VietNam", 43);

        return data;
    }

    private void createPieChart() {
        Map<String, Integer> data = getData();
        List<PieEntry> entries = new ArrayList<>();
        data.forEach((key, value) -> {
            entries.add(new PieEntry(value, key));
        });

        PieDataSet pieDataSet = new PieDataSet(entries, "");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        pieDataSet.setValueTextSize(12f);

        pieDataSet.setSliceSpace(3f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        Legend legend = mAdminFragmentStatisticClubBinding.adminStatisticsClubPieChart.getLegend();
        legend.setFormSize(14);
        legend.setFormToTextSpace(5);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(12);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        legend.setYOffset(10);
        legend.setXEntrySpace(20);
        legend.setYEntrySpace(5);

        PieChart pieChart = mAdminFragmentStatisticClubBinding.adminStatisticsClubPieChart;
        pieChart.setRotationEnabled(false);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Pie Chart");
        pieChart.animateX(2000);

        pieChart.invalidate();
    }
}
