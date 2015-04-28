package at.sum.android.cysmn.sensing.googleplay;

import android.content.Context;
import android.location.LocationListener;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by widnig89 on 28.04.15.
 */
public abstract class GoogleApiClientListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context ctx;


    public GoogleApiClientListener(Context ctx)
    {
        this.ctx = ctx;

    }

    public abstract Api<Api.ApiOptions.NoOptions> getAPI();

}
