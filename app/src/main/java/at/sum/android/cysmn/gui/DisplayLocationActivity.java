package at.sum.android.cysmn.gui;

import android.content.IntentFilter;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import at.sum.android.cysmn.R;
import at.sum.android.cysmn.logic.location.LocationLogic;


public class DisplayLocationActivity extends ActionBarActivity implements GuiUpdater {


    private TextView txtLongitude;
    private TextView txtLatitude;
    private TextView txtBearing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_display);

        txtLongitude = (TextView)findViewById(R.id.location_display_txtLongitude);
        txtLatitude = (TextView)findViewById(R.id.location_display_txtLatitude);
        txtBearing = (TextView)findViewById(R.id.location_display_txtBearing);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        LocationLogic.getInstance(this, false).registerLocationActivity(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        LocationLogic.getInstance(this, false).unregisterLocationActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void updateGui() {

        LocationLogic logic = LocationLogic.getInstance(this, false);

        Location curLocation = logic.getCurrentLocation();
        txtLongitude.setText(Double.toString(curLocation.getLongitude()));
        txtLatitude.setText(Double.toString(curLocation.getLatitude()));
        txtBearing.setText(Float.toString(curLocation.getBearing()));
    }
}
