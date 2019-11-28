package com.teecoin.javastellarsdk.stellar;

import com.google.common.io.BaseEncoding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class StellarUtils {


    public static boolean isEmpty(String str) {
        return (str == null) || str.equals("");
    }

//    public static String toHex(byte[] bytes) {
//        BigInteger bi = new BigInteger(1, bytes);
//        return String.format("%0" + (bytes.length << 1) + "X", bi);
//    }

    public static String convertToHexString(byte[] bytes) {
        return BaseEncoding.base16().encode(bytes).toLowerCase();
    }

    public static double convertToDouble(String number) {
        if (number == null) {
            //Log.e("CORE", "Null value!");
            return 0.0;
        }

        Locale theLocale = Locale.getDefault();
        NumberFormat numberFormat = DecimalFormat.getInstance(theLocale);
        Number theNumber;
        try {
            theNumber = numberFormat.parse(number);
            return theNumber.doubleValue();
        } catch (ParseException e) {
            String valueWithDot = number.replaceAll(",", ".");
            try {
                return Double.valueOf(valueWithDot);
            } catch (NumberFormatException e2) {
                return 0.0;
            }
        }
    }
}
