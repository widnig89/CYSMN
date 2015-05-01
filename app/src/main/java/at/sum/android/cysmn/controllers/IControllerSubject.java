package at.sum.android.cysmn.controllers;

import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public interface IControllerSubject {

    public void registerControllerObserver(ControllerObserver controllerObserver);

    public void unregisterControllerObserver(ControllerObserver controllerObserver);

    public void notifyControllerObservers(ServiceEnum serviceEnum);


}
