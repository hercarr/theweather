package mx.hercarr.theweather.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable {

    private long time;
    private String summary;
    private double temperatureMax;
    private String icon;
    private String timezone;

    public Day() {

    }

    private Day(Parcel in) {
        time = in.readLong();
        summary = in.readString();
        temperatureMax = in.readDouble();
        icon = in.readString();
        timezone = in.readString();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(time);
        parcel.writeString(summary);
        parcel.writeDouble(temperatureMax);
        parcel.writeString(icon);
        parcel.writeString(timezone);
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {

        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

}