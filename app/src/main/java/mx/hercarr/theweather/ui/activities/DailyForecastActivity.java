package mx.hercarr.theweather.ui.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.hercarr.theweather.R;
import mx.hercarr.theweather.adapters.DayAdapter;
import mx.hercarr.theweather.model.Day;
import mx.hercarr.theweather.utils.Constants;
import mx.hercarr.theweather.utils.StringUtil;

public class DailyForecastActivity extends ListActivity {

    @InjectView(R.id.addressLabel) TextView addressLabel;

    private Day[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(Constants.Keys.DAILY_FORECAST);
        days = Arrays.copyOf(parcelables, parcelables.length, Day[].class);
        addressLabel.setText(intent.getStringExtra(Constants.Keys.FULL_ADDRESS_FORECAST));
        setListAdapter(new DayAdapter(this, days));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String dayOfTheWeek = StringUtil.getDayOfTheWeek(days[position].getTime(), days[position].getTimezone());
        String conditions = days[position].getSummary();
        String highTemp = StringUtil.parseDoubleToIntValue(days[position].getTemperatureMax());
        String message = String.format(getString(R.string.daily_summary), dayOfTheWeek, highTemp, conditions.toLowerCase().replace(".", ""));
        Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();
    }
}