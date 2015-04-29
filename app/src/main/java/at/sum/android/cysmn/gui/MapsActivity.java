package at.sum.android.cysmn.gui;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import at.sum.android.cysmn.R;
import at.sum.android.cysmn.logic.location.LocationLogic;
import at.sum.android.cysmn.utils.AppLogger;

public class MapsActivity extends Activity implements OnMapReadyCallback, GuiUpdater {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker myMarker;
    private CameraPosition cameraPosition;
    private Circle mCircle;

    private float zoomLevel;
    private boolean isZooming = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        zoomLevel = 17.0f;

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    public void onResume()
    {
        super.onResume();
        LocationLogic logic = LocationLogic.getInstance(this, false);
        logic.registerLocationActivity(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                AppLogger.logDebug(this.getClass().getSimpleName(), "Zoom: " + cameraPosition.zoom);

                zoomLevel = cameraPosition.zoom;
            }
        });


        LocationLogic logic = LocationLogic.getInstance(this, false);

        //map.setMyLocationEnabled(true);

        Location currentLocation = logic.getCurrentLocation();
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow)).position(latLng).anchor(0.5f, 0.5f).flat(true).title("ME"));


        cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(17)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                1000, null);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        LocationLogic logic = LocationLogic.getInstance(this, false);

        logic.unregisterLocationActivity();
    }

    @Override
    public void updateGui() {

        LocationLogic logic = LocationLogic.getInstance(this, false);

        Location location = logic.getCurrentLocation();
        if(hasCurrentLocation(location) == false || mMap==null)
            return;

        updateMarker(location);
        updateCamera(location);
    }

    private void updateMarker(Location currentLocation)
    {
        if(myMarker == null)
            return;

        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        myMarker.setPosition(latLng);
        myMarker.setRotation(currentLocation.getBearing());

        if(mCircle == null){
            drawMarkerWithCircle(latLng);
        }else{
            updateMarkerWithCircle(latLng);
        }
    }

    private void updateMarkerWithCircle(LatLng position)
    {
        mCircle.setCenter(position);
    }

    private void drawMarkerWithCircle(LatLng position) {
        double radiusInMeters = 15.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);

        //MarkerOptions markerOptions = new MarkerOptions().position(position);
        //mMarker = mMap.addMarker(markerOptions);
    }

    private void updateCamera(Location currentLocation)
    {
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(zoomLevel)
                .bearing(currentLocation.getBearing())
                .build();


        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                1000, null);
    }

    private boolean hasCurrentLocation(Location currentLocation)
    {
        if(currentLocation == null)
        {
            AppLogger.logDebug(this.getClass().getSimpleName(), "current location is null!");
            return false;
        }
        return true;
    }



}
