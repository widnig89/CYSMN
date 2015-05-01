package at.sum.android.cysmn.sensing.googleplay;

import android.content.Context;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import at.sum.android.cysmn.controllers.IServiceFacadeObserver;
import at.sum.android.cysmn.controllers.ServiceFacadeSubject;

/**
 * Created by widnig89 on 28.04.15.
 */
public abstract class GoogleApiClientService extends ServiceFacadeSubject implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, IGoogleApiClientService {

    private Context ctx;

    protected GoogleApiClientService(Context ctx)
    {
        this.ctx = ctx;
    }

}
