package com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.repositories.adminRepositories.AdminStatisticsRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AdminStatisticsViewModel extends ViewModel {
    AdminStatisticsRepository repo;

    public AdminStatisticsViewModel() {
        repo = new AdminStatisticsRepository();
    }

    public void onClickOption(View view, int option) {
        repo.onClickOption(view, option);
    }

}
