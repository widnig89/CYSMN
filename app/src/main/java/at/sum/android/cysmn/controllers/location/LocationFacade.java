package at.sum.android.cysmn.controllers.location;

import android.content.Context;
import android.location.Location;

import at.sum.android.cysmn.controllers.ControllerSubject;
import at.sum.android.cysmn.controllers.IServiceFacadeObserver;
import at.sum.android.cysmn.sensing.googleplay.location.LocationService;
import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public class LocationFacade extends ControllerSubject implements IServiceFacadeObserver {

    private Context ctx;
    private LocationController locationController;

    public LocationFacade(Context ctx, LocationController locationController)
    {
        this.ctx = ctx;
        this.locationController = locationController;
        LocationService.getInstance(ctx).registerServiceFacadeObserver(this);

    }

    @Override
    public void notifyController() {
        notifyControllerObservers(ServiceEnum.LOCATION);
        //locationController.notify(ServiceEnum.LOCATION);

    }

    public Location getCurrentLocation()
    {
        return LocationService.getInstance(ctx).getCurrentLocation();
    }
}
