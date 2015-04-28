package at.sum.android.cysmn.sensing.googleplay;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import at.sum.android.cysmn.sensing.googleplay.location.LocationUpdateService;
import at.sum.android.cysmn.utils.AppLogger;

/**
 * Created by widnig89 on 28.04.15.
 */
public class GoogleApiClientProvider {

    private Context ctx;
    private GoogleApiClient client;

    public static GoogleApiClientProvider instance;

    private GoogleApiClientProvider(Context ctx)
    {
        this.ctx = ctx;
        createGoogleApiClient();
    }

    public static GoogleApiClientProvider getInstance(Context ctx)
    {
        if(instance == null)
            instance = new GoogleApiClientProvider(ctx);
        return instance;
    }

    private void createGoogleApiClient()
    {
        List<GoogleApiClientListener> listeners = getListeners();

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(ctx);

        for(GoogleApiClientListener listener : listeners)
        {
            builder.addApi(listener.getAPI())
                    .addConnectionCallbacks(listener)
                    .addOnConnectionFailedListener(listener);
        }

        client = builder.build();
    }

    public GoogleApiClient getGoogleApiClient()
    {
        return client;
    }

    public void connectGoogleApiClient()
    {
        client.connect();
        AppLogger.logDebug(this.getClass().getSimpleName(), "Client connected!");
    }

    public void disconnectGoogleApiClient()
    {
        client.disconnect();
        AppLogger.logDebug(this.getClass().getSimpleName(), "Client disconnected!");
    }

    public List<GoogleApiClientListener> getListeners()
    {
        List<GoogleApiClientListener> listeners = new ArrayList<GoogleApiClientListener>();

        /* Add new GoogleApiClientListeners here */
        GoogleApiClientListener locationUpdateStarter = new LocationUpdateService(ctx);
        listeners.add(locationUpdateStarter);

        return listeners;
    }
}
