package at.sum.android.cysmn.sensing.googleplay.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.text.DateFormat;
import java.util.Date;

import at.sum.android.cysmn.logic.location.LocationLogic;
import at.sum.android.cysmn.sensing.googleplay.GoogleApiClientListener;
import at.sum.android.cysmn.sensing.googleplay.GoogleApiClientProvider;
import at.sum.android.cysmn.utils.AppLogger;

/**
 * Created by widnig89 on 28.04.15.
 */
public class LocationUpdateService extends GoogleApiClientListener implements LocationListener {

    private LocationManager locationManager;

    private Context ctx;

    private LocationLogic locationLogic;

    private boolean requestingLocationUpdates;

    Location currentLocation;
    String mLastUpdateTime;

    public LocationUpdateService(Context ctx)
    {
        super(ctx);
        requestingLocationUpdates = true;

    }



    public void setRequestingLocationUpdates(boolean requesting)
    {
        this.requestingLocationUpdates = requesting;
    }

    @Override
    public void onConnected(Bundle bundle) {

        AppLogger.logDebug(this.getClass().getSimpleName(), "attempt to request location updates!");
        GoogleApiClient client = GoogleApiClientProvider.getInstance(ctx).getGoogleApiClient();
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);

        if(lastLocation != null)
        {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
        }

        if(requestingLocationUpdates)
        {
            startLocationUpdates();
        }
    }

    public void startLocationUpdates()
    {
        GoogleApiClient client = GoogleApiClientProvider.getInstance(ctx).getGoogleApiClient();
        LocationServices.FusedLocationApi.requestLocationUpdates(client, createLocationRequest(), this);
        AppLogger.logDebug(this.getClass().getSimpleName(), "starting LocationUpdates");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public Api<Api.ApiOptions.NoOptions> getAPI() {
        return LocationServices.API;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        LocationLogic.getInstance(ctx,false).notify(this);
        AppLogger.logDebug(this.getClass().getSimpleName(), "LocationChangedHappened");

    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }
}
