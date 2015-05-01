package at.sum.android.cysmn.controllers;

import java.util.ArrayList;

import at.sum.android.cysmn.utils.AppLogger;

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
    public void registerServiceFacadeObserver(IServiceFacadeObserver IServiceFacadeObserver)
    {
        AppLogger.logDebug(this.getClass().getSimpleName(), "observer registered!");
        observers.add(IServiceFacadeObserver);
    }

    @Override
    public void unregisterServiceFacadeObserver(IServiceFacadeObserver IServiceFacadeObserver)
    {
        observers.remove(IServiceFacadeObserver);
    }

    @Override
    public void notifyObservers()
    {
        for(IServiceFacadeObserver o : observers)
        {
            AppLogger.logDebug(this.getClass().getSimpleName(), "notify observers!");
            o.notifyController();
        }
    }
}
