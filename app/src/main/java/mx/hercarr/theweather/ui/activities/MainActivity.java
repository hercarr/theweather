package mx.hercarr.theweather.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mx.hercarr.theweather.R;
import mx.hercarr.theweather.model.Current;
import mx.hercarr.theweather.model.Day;
import mx.hercarr.theweather.model.Forecast;
import mx.hercarr.theweather.model.Hour;
import mx.hercarr.theweather.model.UserLocation;
import mx.hercarr.theweather.ui.fragments.AlertDialogFragment;
import mx.hercarr.theweather.utils.Constants;
import mx.hercarr.theweather.utils.GPSTracker;
import mx.hercarr.theweather.utils.GeneralUtils;
import mx.hercarr.theweather.utils.StringUtil;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static String FORECAST_API_KEY = "70d68d2a35ed56df64af9b5800510dea";

    @InjectView(R.id.addressLabel) TextView addressLabel;
    @InjectView(R.id.cityLabel) TextView cityLabel;
    @InjectView(R.id.countryLabel) TextView countryLabel;
    @InjectView(R.id.timeLabel) TextView timeLabel;
    @InjectView(R.id.temperatureLabel) TextView temperatureLabel;
    @InjectView(R.id.humidityValue) TextView humidityValue;
    @InjectView(R.id.precipValue) TextView precipValue;
    @InjectView(R.id.summaryLabel) TextView summaryLabel;
    @InjectView(R.id.dailyButton) Button dailyButton;
    @InjectView(R.id.hourlyButton) Button hourlyButton;
    @InjectView(R.id.iconImageView) ImageView iconImageView;
    @InjectView(R.id.refreshImageView) ImageView refreshImageView;
    @InjectView(R.id.progressBar) ProgressBar progressBar;

    private GPSTracker gpsTracker;
    private UserLocation userLocation;
    private OkHttpClient client;
    private Request request;
    private Call call;
    private String jsonData;
    private Forecast forecast;
    private Current current;
    private double latitude;
    private double longitude;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        progressBar.setVisibility(View.INVISIBLE);
        gpsTracker = new GPSTracker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchTheWeatherFromForecast();
    }

    private void searchTheWeatherFromForecast() {
        if (GeneralUtils.isNetworkAvailable(this)) {
            dailyButton.setVisibility(View.GONE);
            hourlyButton.setVisibility(View.GONE);
            try {
                if (gpsTracker.canGetLocation()) {
                    userLocation = gpsTracker.findUserLocation();
                    if (userLocation != null) {
                        latitude = userLocation.getLatitude();
                        longitude = userLocation.getLongitude();
                        url = "https://api.forecast.io/forecast/{key}/{latitude},{longitude}?lang={language}";
                        url = url.replace("{key}", FORECAST_API_KEY)
                                .replace("{latitude}", String.valueOf(latitude))
                                .replace("{longitude}", String.valueOf(longitude))
                                .replace("{language}", System.getProperty("user.language"));
                        if (GeneralUtils.isNetworkAvailable(this)) {
                            showOrHideToggleRefresh();
                            client = new OkHttpClient();
                            request = new Request.Builder().url(url).build();
                            call = client.newCall(request);
                            call.enqueue(new Callback() {

                                @Override
                                public void onFailure(Request request, IOException e) {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            showOrHideToggleRefresh();
                                        }

                                    });
                                    showErrorAlert();
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            showOrHideToggleRefresh();
                                        }

                                    });

                                    try {
                                        jsonData = response.body().string();
                                        if (response.isSuccessful()) {
                                            forecast = parseForecastDetails(jsonData);
                                            runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    updateDisplay();
                                                }

                                            });
                                        } else {
                                            showErrorAlert();
                                        }
                                    } catch (IOException e) {
                                        Log.e(TAG, "Exception caught: ", e);
                                    } catch (JSONException e) {
                                        Log.e(TAG, "Exception caught: ", e);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showErrorAlert();
                    }
                } else {
                    gpsTracker.showSettingsAlert();
                }
            } catch (Exception e) {
                showErrorAlert();
            }
        } else {
            openInternetSettings();
        }
    }

    private void showOrHideToggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        current = forecast.getCurrent();
        addressLabel.setText(userLocation.getAddress());
        cityLabel.setText(userLocation.getCity());
        countryLabel.setText(userLocation.getCountry());
        temperatureLabel.setText(StringUtil.parseDoubleToIntValue(current.getTemperature()));
        timeLabel.setText(getResources().getString(R.string.time_msg).replace("#TIME", StringUtil.getFormattedTime(current.getTime(), current.getTimeZone())));
        humidityValue.setText(GeneralUtils.percentage(current.getHumidity()));
        precipValue.setText(GeneralUtils.percentage(current.getPrecipChance()));
        summaryLabel.setText(current.getSummary());
        iconImageView.setImageDrawable(getResources().getDrawable(StringUtil.getIconID(current.getIcon())));
        dailyButton.setVisibility(View.VISIBLE);
        hourlyButton.setVisibility(View.VISIBLE);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHours(getHourlyForecast(jsonData));
        forecast.setDays(getDailyForecast(jsonData));
        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        JSONObject jsonDay;
        Day[] days = new Day[data.length()];
        Day day;
        for (int i = 0; i < data.length(); i++) {
            jsonDay = data.getJSONObject(i);
            day = new Day();
            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(GeneralUtils.convertFahrenheitToCelsius(jsonDay.getDouble("temperatureMax")));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);
            days[i] = day;
        }
        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");
        JSONObject jsonHour;
        Hour[] hours = new Hour[data.length()];
        Hour hour;
        for (int i = 0; i < data.length(); i++) {
            jsonHour = data.getJSONObject(i);
            hour = new Hour();
            hour.setSummary(jsonHour.getString("summary"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTemperature(GeneralUtils.convertFahrenheitToCelsius(jsonHour.getDouble("temperature")));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);
            hours[i] = hour;
        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");
        current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(GeneralUtils.convertFahrenheitToCelsius(currently.getDouble("temperature")));
        current.setTimeZone(timezone);
        return current;
    }

    private void showErrorAlert() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void openInternetSettings() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.open_intertet_settings);
        builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        dialog = builder.create();
        dialog.setTitle(R.string.network_unavailable_message);
        dialog.show();
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(Constants.Keys.FULL_ADDRESS_FORECAST, userLocation.getAddress() + ", " + userLocation.getCity());
        intent.putExtra(Constants.Keys.DAILY_FORECAST, forecast.getDays());
        startActivity(intent);
    }

    @OnClick (R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(Constants.Keys.FULL_ADDRESS_FORECAST, userLocation.getAddress() + ", " + userLocation.getCity());
        intent.putExtra(Constants.Keys.HOURLY_FORECAST, forecast.getHours());
        startActivity(intent);
    }

    @OnClick(R.id.refreshImageView)
    public void refresh() {
        searchTheWeatherFromForecast();
    }

}