package at.sum.android.cysmn.controllers;

import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public interface ControllerObserver {

    public void notify(ServiceEnum e);

}