package com.example.ecommerce_hvpp.fragments.statistics_detail;

import static com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository.monthClubDataStatistics;
import static com.example.ecommerce_hvpp.util.CustomFormat.dateFormatter;
import static com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics.AdminStatisticsComponentViewModel.strClub;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

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
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStatisticClubFragment extends Fragment {
    AdminFragmentStatisticClubBinding mAdminFragmentStatisticClubBinding;
    AdminStatisticsRepository repo;
    int monthSelected = 6;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdminFragmentStatisticClubBinding = AdminFragmentStatisticClubBinding.inflate(inflater, container, false);

        // init repo
        repo = new AdminStatisticsRepository();

        // init view
        initView();

        // create chart
        createChart(repo.getCurrentMonth());

        // show dropdown
        mAdminFragmentStatisticClubBinding.adminStatisticsClubCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropdown(repo.getMinMonth(monthClubDataStatistics), repo.getCurrentMonth());
//                showDropdown();
            }
        });

        // on click back page
        mAdminFragmentStatisticClubBinding.adminStatisticsClubHeaderBack.setOnClickListener(repo.onClickBackPage());

        return mAdminFragmentStatisticClubBinding.getRoot();
    }

    private void initView() {
        mAdminFragmentStatisticClubBinding.adminStatisticsComponentClubQuantity.setText(strClub);
        mAdminFragmentStatisticClubBinding.adminStatisticsClubMonth.setText(repo.getCurrentMonth());
    }

    // create chart
    private void createChart(String currMonth) {
        createPieChart(currMonth);
        createBarChart(currMonth);
    }

    private void createPieChart(String currMonth) {
        Map<String, Integer> data = repo.getTop5Quantities(monthClubDataStatistics, currMonth);;
        List<PieEntry> entries = new ArrayList<>();
        data.forEach((key, value) -> {
            entries.add(new PieEntry(value, key));
        });

        String title = "Top 5 Club Top-selling";
        mAdminFragmentStatisticClubBinding.adminStatisticsClubTitlePieChart.setText(title);

        PieChart pieChart = mAdminFragmentStatisticClubBinding.adminStatisticsClubPieChart;
        repo.formatPieChart(pieChart, entries, requireContext());
    }

    private void createBarChart(String currMonth) {
        Map<String, Integer> data = monthClubDataStatistics.get(currMonth);
        List<BarEntry> entries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();
        final int[] index = {1};
        assert data != null;
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

    private void showDropdown(String minMonthYear, String maxMonthYear) {
        final Calendar today = Calendar.getInstance();
        int minMonth = Integer.parseInt(minMonthYear.substring(0, 2)) - 1;
        int maxMonth = Integer.parseInt(maxMonthYear.substring(0, 2)) - 1;
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(requireContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                int currMonth = selectedMonth + 1;
                monthSelected = selectedMonth;
                String month = String.format("%02d", currMonth);
                String monthYear = month + "/" + selectedYear;
                mAdminFragmentStatisticClubBinding.adminStatisticsClubMonth.setText(month + "/" + selectedYear);
                createChart(monthYear);
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
        builder.setActivatedMonth(monthSelected)
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
