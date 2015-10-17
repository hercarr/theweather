package mx.hercarr.theweather.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.hercarr.theweather.R;
import mx.hercarr.theweather.adapters.HourAdapter;
import mx.hercarr.theweather.model.Hour;
import mx.hercarr.theweather.utils.Constants;

public class HourlyForecastActivity extends ActionBarActivity {

    @InjectView(R.id.reyclerView) RecyclerView recyclerView;
    @InjectView(R.id.addressLabel) TextView addressLabel;

    private Hour[] hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(Constants.Keys.HOURLY_FORECAST);
        addressLabel.setText(intent.getStringExtra(Constants.Keys.FULL_ADDRESS_FORECAST));
        hours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        recyclerView.setAdapter(new HourAdapter(this, hours));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

}