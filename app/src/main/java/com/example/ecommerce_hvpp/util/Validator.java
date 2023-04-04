package com.example.ecommerce_hvpp.util;

public class Validator {
    public static boolean isValidUsername(String val) {
        if (val.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String val) {
        if (val == null) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches();
    }

    public static boolean isValidPassword(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        return password.equals(confirmPassword);
    }
}
