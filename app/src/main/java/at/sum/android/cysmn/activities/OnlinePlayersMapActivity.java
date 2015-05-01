package at.sum.android.cysmn.activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;

import at.sum.android.cysmn.R;
import at.sum.android.cysmn.controllers.location.LocationController;

public class OnlinePlayersMapActivity extends Activity implements OnMapReadyCallback, IActivityUpdater {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker myMarker;
    private CameraPosition cameraPosition;
    private Circle mCircle;

    private float zoomLevel;
    private LocationController locationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_players_map);

        locationController = new LocationController(this.getApplicationContext(), this);

        zoomLevel = 17.0f;

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.runner_map);
        mapFragment.getMapAsync(this);
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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
