package mx.hercarr.theweather.utils;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mx.hercarr.theweather.R;
import mx.hercarr.theweather.model.UserLocation;

/**
 * The original class is from tutorial : http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial
 * For Geocoder read this : http://stackoverflow.com/questions/472313/android-reverse-geocoding-getfromlocation
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private UserLocation userLocation;
    private Location location;

    /* The minimum distance to change updates in metters */
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    /* The minimum time beetwen updates in milliseconds = one minute*/
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
    }

    public UserLocation findUserLocation() throws IOException {
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return buildLocation(location);
    }

    public boolean canGetLocation() {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled)
            return false;
        else
            return true;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(R.string.GPSAlertDialogTitle);
        alertDialog.setMessage(R.string.GPSAlertDialogMessage);
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private UserLocation buildLocation(Location location) throws IOException {
        Address address;
        List<Address> addresses;
        userLocation = new UserLocation();
        if (location != null) {
            userLocation.setLatitude(location.getLatitude());
            userLocation.setLongitude(location.getLongitude());
        }
        addresses = getGeocoderAddress(mContext);
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0);
            userLocation.setAddress(address.getAddressLine(0));
            userLocation.setCity(address.getLocality());
            userLocation.setCountry(address.getCountryName());
            userLocation.setPostalCode(address.getPostalCode());
        } else {
            userLocation.setAddress(mContext.getString(R.string.address_not_available));
            userLocation.setCity(mContext.getString(R.string.city_not_available));
            userLocation.setCountry(mContext.getString(R.string.country_not_available));
            userLocation.setPostalCode(mContext.getString(R.string.postal_code_not_available));
        }
        return userLocation;
    }

    private List<Address> getGeocoderAddress(Context context) throws IOException {
        if (location != null) {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);
            return addresses;
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}