package at.sum.android.cysmn.controllers;

import java.util.ArrayList;

import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public abstract class ServiceFacadeSubject implements IServiceFacadeSubject {

    protected ArrayList<IServiceFacadeObserver> observers;

    public ServiceFacadeSubject()
    {
        observers = new ArrayList<>();
    }

    @Override
    public void registerServiceFacadeObserver(IServiceFacadeObserver serviceFacadeObserver)
    {
        AppLogger.logDebug(this.getClass().getSimpleName(), "observer registered!");
        observers.add(serviceFacadeObserver);
    }

    @Override
    public void unregisterServiceFacadeObserver(IServiceFacadeObserver serviceFacadeObserver)
    {
        observers.remove(serviceFacadeObserver);
    }

    @Override
    public void notifyObservers(ServiceEnum serviceEnum)
    {
        for(IServiceFacadeObserver o : observers)
        {
            AppLogger.logDebug(this.getClass().getSimpleName(), serviceEnum + " fired!");
            o.notifyController(serviceEnum);
        }
    }
}
