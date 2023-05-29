package com.example.ecommerce_hvpp.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormat {
    public static String getVNDCurrency(double value) {;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedPrice = currencyFormat.format(value);
        return formattedPrice;
    }
}
