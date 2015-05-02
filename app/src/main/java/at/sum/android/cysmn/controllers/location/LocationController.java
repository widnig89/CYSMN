package at.sum.android.cysmn.controllers.location;

import android.content.Context;
import android.location.Location;

import java.util.List;

import at.sum.android.cysmn.activities.IActivityUpdater;
import at.sum.android.cysmn.controllers.IControllerObserver;
import at.sum.android.cysmn.gamelogic.Player;
import at.sum.android.cysmn.gamelogic.Session;
import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 28.04.15.
 */
public class LocationController implements IControllerObserver {


    public static final String FIELD_LONGITUDE = "Longitude";
    public static final String FIELD_LATITUDE = "Latitude";

    private Context ctx;
    private IActivityUpdater activityUpdater;
    private Location savedLocation;



    private LocationFacade locationFacade;

    public LocationController(Context ctx, IActivityUpdater activityUpdater)
    {
        this.ctx = ctx;
        this.locationFacade = new LocationFacade(ctx, this);
        this.activityUpdater = activityUpdater;
        locationFacade.registerControllerObserver(this);
    }

    public void updateGui() {
        AppLogger.logDebug(this.getClass().getSimpleName(), "updateGui");

        if(activityUpdater == null)
            return;

        activityUpdater.updateGui();
    }

    public void pauseController()
    {
        locationFacade.stopSensing();
    }

    public void activateController()
    {
        locationFacade.startSensing();
    }

    public Location getCurrentLocation()
    {
        //we do not fetch from service, because it would be overwritten when interrupt occurs
        //not a perfect solution, but in the other case threads must be synchronised (service-thread
        //& main thread.
        return savedLocation;
    }

    public boolean isNewLocationBetter(Location latestLocation, Location savedLocation)
    {
        boolean new_is_better = LocationHelper.isBetterLocation(latestLocation, savedLocation);

        if(new_is_better)
            savedLocation = latestLocation;

        return new_is_better;
    }

    public List<Player> getOnlinePlayers()
    {
        return Session.getInstance().getOnlinePlayers();
    }

    public List<Player> getRunners()
    {
        return Session.getInstance().getRunners();
    }

    public List<Player> getAllPlayers()
    {
        return Session.getInstance().getPlayers();
    }

    @Override
    public void notify(ServiceEnum e) {

        AppLogger.logDebug(getClass().getSimpleName(), "notify(ServiceEnum e)!");
        switch(e)
        {
            case LOCATION:
                if(isNewLocationBetter(locationFacade.getCurrentLocation(), savedLocation)) {
                    savedLocation = locationFacade.getCurrentLocation();
                    updateGui();
                }
                break;
            case COMPASS:
                AppLogger.logDebug(getClass().getSimpleName(), "compass event occurred in controller!");
                updateGui();
                break;

        }


    }

    public float getAzimuthInDegrees()
    {
        return locationFacade.getAzimuthInDegrees();
    }

    public float getCurrentDegrees()
    {
        return locationFacade.getCurrentDegrees();
    }
}
