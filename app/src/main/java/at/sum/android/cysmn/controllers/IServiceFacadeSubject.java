package at.sum.android.cysmn.controllers;

import android.app.Service;

import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public interface IServiceFacadeSubject {

    public void registerServiceFacadeObserver(IServiceFacadeObserver IServiceFacadeObserver);

    public void unregisterServiceFacadeObserver(IServiceFacadeObserver IServiceFacadeObserver);

    public void notifyObservers(ServiceEnum serviceEnum);
}
