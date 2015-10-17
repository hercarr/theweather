package mx.hercarr.theweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.MessageFormat;
import java.util.Scanner;

/**
 * Created by hercarr.mx on 06/03/2015.
 */
public class GeneralUtils {

    public static boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        return connected;
    }

    public static double convertFahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit -32) *5 / 9;
    }

    public static String percentage(double value) {
        return MessageFormat.format("{0,number,#.##%}", value);
    }

}
