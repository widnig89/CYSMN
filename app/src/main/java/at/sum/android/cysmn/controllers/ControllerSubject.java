package at.sum.android.cysmn.controllers;

import java.util.ArrayList;

import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public abstract class ControllerSubject implements IControllerSubject {

    protected ArrayList<IControllerObserver> observers;

    public ControllerSubject()
    {
        observers = new ArrayList<>();
    }

    public void registerControllerObserver(IControllerObserver controllerObserver)
    {
        AppLogger.logDebug(this.getClass().getSimpleName(), "observer registered!");
        observers.add(controllerObserver);
    }

    public void unregisterControllerObserver(IControllerObserver controllerObserver)
    {
        observers.remove(controllerObserver);
    }

    public void notifyControllerObservers(ServiceEnum serviceEnum)
    {
        for(IControllerObserver o : observers)
        {
            AppLogger.logDebug(getClass().getSimpleName(), "notifyControllerObservers for Observer!");
            o.notify(serviceEnum);
        }
    }
}
