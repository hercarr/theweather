package mx.hercarr.theweather.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import mx.hercarr.theweather.R;

/**
 * Created by hercarr.mx on 06/03/2015.
 */
public class StringUtil {

    public static String parseDoubleToIntValue(double value) {
        return String.valueOf((int) Math.round(value)) + "\u00b0";
    }

    public static String getHour(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        Date date = new Date(time * 1000);
        return formatter.format(date);
    }

    public static String getDayOfTheWeek(long time, String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date dateTime = new Date(time * 1000);
        return formatter.format(dateTime);
    }

    public static String getFormattedTime(long time, String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date dateTime = new Date(time * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public static int getIconID(String iconName) {
        // TODO - put the name inside a enum and chenge to swich
        int iconID = R.drawable.clear_day;
        if (iconName.equals("clear-day")) {
            iconID = R.drawable.clear_day;
        } else if (iconName.equals("clear-night")) {
            iconID = R.drawable.clear_night;
        } else if (iconName.equals("rain")) {
            iconID = R.drawable.rain;
        } else if (iconName.equals("snow")) {
            iconID = R.drawable.snow;
        } else if (iconName.equals("sleet")) {
            iconID = R.drawable.sleet;
        } else if (iconName.equals("wind")) {
            iconID = R.drawable.wind;
        } else if (iconName.equals("fog")) {
            iconID = R.drawable.fog;
        } else if (iconName.equals("cloudy")) {
            iconID = R.drawable.cloudy;
        } else if (iconName.equals("partly-cloudy-day")) {
            iconID = R.drawable.partly_cloudy;
        } else if (iconName.equals("partly-cloudy-night")) {
            iconID = R.drawable.cloudy_night;
        }
        return iconID;
    }

}
