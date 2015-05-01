package at.sum.android.cysmn.activities;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import at.sum.android.cysmn.R;
import at.sum.android.cysmn.controllers.location.LocationController;
import at.sum.android.cysmn.gamelogic.Player;
import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.FactionEnum;

public class RunnersMapActivity extends Activity implements OnMapReadyCallback, IActivityUpdater {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker myMarker;
    private CameraPosition cameraPosition;
    private Circle mCircle;

    private float zoomLevel;


    private LocationController locationController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runners_map);

        locationController = new LocationController(this.getApplicationContext(), this);

        zoomLevel = 17.0f;

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.runner_map);
        mapFragment.getMapAsync(this);



    }

    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                AppLogger.logDebug(this.getClass().getSimpleName(), "Zoom: " + cameraPosition.zoom);

                zoomLevel = cameraPosition.zoom;
            }
        });

        List<Player> players = locationController.getAllPlayers();

        if(players == null)
            return;

        for(Player p : players)
        {
            createMarker(p);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override
    public void updateGui()
    {
        if(mMap == null)
            return;

        updateMyPosition();
        updateOthersPosition();
    }

    private void updateMyPosition() {
        Location location = locationController.getCurrentLocation();
        if(hasCurrentLocation(location) == false)
            return;

        updateMarker(location);
        updateCamera(location);
    }

    private void updateOthersPosition()
    {
        updateRunnersPosition();
        updateOnlinePlayersPosition();
    }

    private void updateRunnersPosition() //these are the green one
    {
        List<Player> runners = locationController.getRunners();

        for(Player runner : runners)
        {
            updateRunnersMarker(runner);
        }
    }

    private void updateOnlinePlayersPosition() //these are the red one
    {
        List<Player> onlinePlayers = locationController.getOnlinePlayers();

        for(Player onlinePlayer : onlinePlayers)
        {
            updateOnlinePlayersMarker(onlinePlayer);
        }
    }



    private void updateRunnersMarker(Player runner)
    {
       if(runner.getMarker() == null) //first time we create one
       {
           createMarker(runner);
       }
       else
       {
           updateMarker(runner);
       }
       //TODO: maybe this method should also draw the circle for the other runners
    }

    private void updateOnlinePlayersMarker(Player onlinePlayer)
    {
        if(onlinePlayer.getMarker() == null)
        {
            createMarker(onlinePlayer);
        }
        else
        {
            updateMarker(onlinePlayer);
        }
    }

    private void createMarker(Player player)
    {
        switch(player.getFaction())
        {
            case RUNNER:
                player.setMarkerOptions(player.getMarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow_green)));

                break;
            case ONLINE_PLAYER:
                player.setMarkerOptions(player.getMarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow_red)));
                break;
            default:
                player.setMarkerOptions(player.getMarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow)));
        }
        player.setMarker(mMap.addMarker(player.getMarkerOptions()));

    }

    private void updateMarker(Player player)
    {
        player.getMarker().setPosition(new LatLng(player.getLatitude(), player.getLongitude()));
        player.getMarker().setRotation(player.getBearing());
    }

    private void updateMarker(Location currentLocation)
    {
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        if(myMarker == null) //first time initialize the marker
        {
            myMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow))
                    .position(latLng)
                    .anchor(0.5f, 0.5f)
                    .flat(true)
                    .title("ME"));
        }

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
