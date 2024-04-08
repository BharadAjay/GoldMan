package com.balaji.calculator.utils;
import java.util.Locale;

public class Utills {
        public static String formatThreeDigitAfterPoint(double value) {
            return String.format(Locale.US, "%.3f", value);
        }
        public static String formatTwoDigitAfterPoint(double value) {
             return String.format(Locale.US, "%.2f", value);
        }

        public static String getStringOrZero(String str) {
            return str.isEmpty() ? "0" : str;
        }

        public static boolean isDecimal(String string) {
            return string.matches(AppConstant.DECIMAL_REGEX);
    }



}




