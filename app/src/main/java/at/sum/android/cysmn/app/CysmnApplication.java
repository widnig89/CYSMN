package at.sum.android.cysmn.app;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import at.sum.android.cysmn.sensing.googleplay.GoogleApiClientProvider;

/**
 * Created by widnig89 on 28.04.15.
 */
public class CysmnApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        JodaTimeAndroid.init(this);
        initApiClient();
    }

    public void initApiClient()
    {
        GoogleApiClientProvider provider = GoogleApiClientProvider.getInstance(this);
        provider.connectGoogleApiClient();

    }
}
