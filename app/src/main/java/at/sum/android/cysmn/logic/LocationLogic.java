package at.sum.android.cysmn.logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import at.sum.android.cysmn.gui.DisplayLocationActivity;
import at.sum.android.cysmn.sensing.googleplay.GoogleApiClientListener;
import at.sum.android.cysmn.sensing.googleplay.location.LocationUpdateService;
import at.sum.android.cysmn.utils.AppLogger;

/**
 * Created by widnig89 on 28.04.15.
 */
public class LocationLogic {


    public static final String FIELD_LONGITUDE = "Longitude";
    public static final String FIELD_LATITUDE = "Latitude";

    private boolean isInBackground;
    private Context ctx;
    private DisplayLocationActivity locationActivity;
    private Location currentLocation;

    private static LocationLogic instance;

    private LocationLogic(Context ctx, boolean isInBackground)
    {
        this.ctx = ctx;
        this.isInBackground = isInBackground;

    }

    public void registerLocationActivity(DisplayLocationActivity activity)
    {
        locationActivity = activity;
    }

    public void unregisterLocationActivity()
    {
        locationActivity = null;
    }

    public static LocationLogic getInstance(Context ctx, boolean isInBackground)
    {
        if (instance == null)
            instance = new LocationLogic(ctx, isInBackground);
        return instance;
    }

    public void updateGui() {
        if(locationActivity == null)
            return;

        locationActivity.updateLocation(currentLocation.getLongitude(), currentLocation.getLatitude());
        AppLogger.logDebug(this.getClass().getSimpleName(), "updateGui");


    }

    public void notify(LocationUpdateService listener) {

        currentLocation = listener.getCurrentLocation();
        updateGui();
    }
}
