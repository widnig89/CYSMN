package at.sum.android.cysmn.activities;

import android.app.Activity;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import at.sum.android.cysmn.R;
import at.sum.android.cysmn.controllers.location.LocationController;


public class DisplayLocationActivity extends Activity implements IActivityUpdater {


    private TextView txtLongitude;
    private TextView txtLatitude;
    private TextView txtBearing;
    private LocationController locationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_display);

        locationController = new LocationController(getApplicationContext(), this);

        txtLongitude = (TextView)findViewById(R.id.location_display_txtLongitude);
        txtLatitude = (TextView)findViewById(R.id.location_display_txtLatitude);
        txtBearing = (TextView)findViewById(R.id.location_display_txtBearing);

    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override
    public void updateGui() {

        Location curLocation = locationController.getCurrentLocation();
        txtLongitude.setText(Double.toString(curLocation.getLongitude()));
        txtLatitude.setText(Double.toString(curLocation.getLatitude()));
        txtBearing.setText(Float.toString(curLocation.getBearing()));
    }
}
