package at.sum.android.cysmn.activities;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;


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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        locationController = new LocationController(this.getApplicationContext(), this);

        zoomLevel = 17.0f;

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.runner_map);
        mapFragment.getMapAsync(this);



    }

    public void onResume()
    {
        super.onResume();
        locationController.activateController();
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
            registerMarkerClickHandler(map,p);
        }
    }

    private void registerMarkerClickHandler(GoogleMap map, Player dst) {

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker p1) {

                List<Player> all = locationController.getAllPlayers();
                Player selected = null;
                for (Player p : all) {
                    if (p1.equals(p.getMarker()))
                        selected = p;
                }

                if (selected != null && selected.isSelected()) {
                    selected.deletePolyline();
                    selected.selectAndUnselect(); // unselect in this case
                    return true;
                }

                if (selected != null && !selected.isSelected())
                    selected.selectAndUnselect();

                return true;
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        locationController.pauseController();
    }

    @Override
    public void updateGui()
    {
        if(mMap == null)
            return;

        updateMyPosition();
        updateOthersPosition();
        updateMarkerLabel();
        //updateMarkerOrientation(); //compass
    }

    private void updateMarkerLabel() {
        List<Player> players = locationController.getAllPlayers();
        Marker p1 = null;
        Player selected = null;
        for(Player player : players)
        {
            if(player.isSelected())
                p1 = player.getMarker();
                selected = player;
        }

        if( p1 != null && myMarker != null){
            float[] results = new float[1];
            Location.distanceBetween(myMarker.getPosition().latitude, myMarker.getPosition().longitude,
                    p1.getPosition().latitude, myMarker.getPosition().longitude, results);
            p1.setTitle(selected.getPlayerType() + " " + new DecimalFormat("#.#").format(results[0]) + "m");
            p1.showInfoWindow();


            // draw polyline to selected marker
            selected.deletePolyline();


            ArrayList<LatLng> arrayPoints = new ArrayList<LatLng>();
            arrayPoints.add(p1.getPosition());
            arrayPoints.add(myMarker.getPosition());

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            polylineOptions.addAll(arrayPoints);
            Polyline polyline = mMap.addPolyline(polylineOptions);
            selected.setPolyline(polyline);

        }



    }


    private void updateMarkerOrientation()
    {
//        RotateAnimation rotateAnimation = new RotateAnimation(locationController.getCurrentDegrees(),
//                locationController.getAzimuthInDegrees(), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setDuration(250);
//        rotateAnimation.setFillAfter(true);
        //myMarker.setRotation();
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
        //myMarker.setRotation(currentLocation.getBearing());
        myMarker.setRotation(locationController.getAzimuthInDegrees());

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
                .bearing(locationController.getAzimuthInDegrees())
                //.bearing(currentLocation.getBearing())
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
