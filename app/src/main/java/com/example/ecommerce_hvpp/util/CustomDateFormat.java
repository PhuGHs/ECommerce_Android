package com.example.ecommerce_hvpp.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class CustomDateFormat {
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat templateDate = new SimpleDateFormat("dd/MM/yyyy");

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
}
