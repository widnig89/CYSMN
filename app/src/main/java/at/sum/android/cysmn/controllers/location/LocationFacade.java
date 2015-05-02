package at.sum.android.cysmn.controllers.location;

import android.content.Context;
import android.location.Location;

import at.sum.android.cysmn.controllers.ControllerSubject;
import at.sum.android.cysmn.controllers.IServiceFacadeObserver;
import at.sum.android.cysmn.sensing.compass.CompassSensorListener;
import at.sum.android.cysmn.sensing.googleplay.location.LocationService;
import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public class LocationFacade extends ControllerSubject implements IServiceFacadeObserver {

    private Context ctx;
    private LocationController locationController;
    private CompassSensorListener compassSensorListener;

    public LocationFacade(Context ctx, LocationController locationController)
    {
        this.ctx = ctx;
        this.locationController = locationController;

        compassSensorListener = new CompassSensorListener(ctx);

        compassSensorListener.registerServiceFacadeObserver(this);

    }

    @Override
    public void notifyController(ServiceEnum serviceEnum) {
        notifyControllerObservers(serviceEnum);
        //locationController.notify(ServiceEnum.LOCATION);

    }

    public void stopSensing()
    {
        LocationService.getInstance(ctx).unregisterServiceFacadeObserver(this);
        compassSensorListener.stopSensing();
    }

    public void startSensing()
    {
        //Add more sensors to start if needed
        LocationService.getInstance(ctx).registerServiceFacadeObserver(this);
        compassSensorListener.startSensing();
    }


    public Location getCurrentLocation()
    {
        return LocationService.getInstance(ctx).getCurrentLocation();
    }

    public float getCurrentDegrees()
    {
        return compassSensorListener.getCurrentDegree();
    }

    public float getAzimuthInDegrees()
    {
        return compassSensorListener.getAzimuthInDegrees();
    }
}
