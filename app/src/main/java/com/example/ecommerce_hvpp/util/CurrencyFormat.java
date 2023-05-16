package com.example.ecommerce_hvpp.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormat {
    public static String getVNDCurrency(double value) {
        Locale locale = new Locale("vi", "VN");
        Currency currency = Currency.getInstance("VND");
        NumberFormat currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(currency);
        String formattedCurrency = currencyFormatter.format(value);
        return formattedCurrency;
    }
}
