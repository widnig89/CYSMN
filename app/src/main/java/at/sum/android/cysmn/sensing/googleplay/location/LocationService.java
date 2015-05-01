package at.sum.android.cysmn.sensing.googleplay.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import at.sum.android.cysmn.sensing.googleplay.GoogleApiClientService;
import at.sum.android.cysmn.sensing.googleplay.GoogleApiClientProvider;

import at.sum.android.cysmn.sensing.googleplay.IGoogleApiClientService;
import at.sum.android.cysmn.utils.AppLogger;

/**
 * Created by widnig89 on 28.04.15.
 */
public class LocationService extends GoogleApiClientService implements LocationListener {


    private Context ctx;

    Location currentLocation;


    public static LocationService instance;

    public static void createInstance(Context ctx)
    {
        getInstance(ctx);
    }

    public static LocationService getInstance(Context ctx)
    {
        if(instance == null) {
            instance = new LocationService(ctx);
        }
        return instance;
    }

    private LocationService(Context ctx)
    {
        super(ctx);
    }

    @Override
    public void onConnected(Bundle bundle) {

        AppLogger.logDebug(this.getClass().getSimpleName(), "attempt to request location updates!");

        startLocationUpdates();
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
        AppLogger.logDebug(this.getClass().getSimpleName(), "change location happened");

        currentLocation = location;
        notifyObservers();


    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }
}
