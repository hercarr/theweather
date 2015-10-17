package mx.hercarr.theweather.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hour implements Parcelable {

    private long time;
    private String summary;
    private double temperature;
    private String icon;
    private String timezone;

    public Hour() {

    }

    private Hour(Parcel in) {
        time = in.readLong();
        temperature = in.readDouble();
        summary = in.readString();
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

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
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
        parcel.writeDouble(temperature);
        parcel.writeString(summary);
        parcel.writeString(icon);
        parcel.writeString(timezone);
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {

        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }

    };

}