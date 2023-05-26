package com.example.ecommerce_hvpp.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class constant {
    public static final int CUSTOMER_MANAGEMENT = 100;
    public static final int DATA_STATISTICS = 101;
    public static final int ORDER_HISTORY = 102;
    public static final int PROMOTION_MANAGEMENT = 103;
    public static final int LOG_OUT = 0;

    public static final String KEY_INTENT_PROMOTION = "KEY_INTENT_PROMOTION";

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat templateDate = new SimpleDateFormat("dd/MM/yyyy");
}
