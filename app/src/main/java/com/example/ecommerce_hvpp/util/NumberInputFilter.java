package com.example.ecommerce_hvpp.util;

import android.text.InputFilter;
import android.text.Spanned;

public class NumberInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder input = new StringBuilder(dest);
        input.replace(dstart, dend, source.subSequence(start, end).toString());

        if (!isNumber(input.toString())) {
            return "";
        }
        return null;
    }

    private boolean isNumber(String text) {
        String pattern = "^[0-9]*(\\.[0-9]*)?$";
        return text.matches(pattern);
    }
}
