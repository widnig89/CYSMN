package at.sum.android.cysmn.gamelogic;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import at.sum.android.cysmn.R;
import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.FactionEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public class Player {
    private Participant participant;
    private double latitude;
    private double longitude;
    private float bearing;
    private Marker marker;
    private MarkerOptions markerOptions;
    private FactionEnum faction;


    public FactionEnum getFaction() {
        return faction;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public Player(Participant participant, double latitude, double longitude, float bearing, int faction_id)
    {
        this.participant = participant;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.faction = FactionEnum.values()[faction_id];

        AppLogger.logDebug(getClass().getSimpleName(), "Created Player: " /* + participant.getDisplayName() */
        + "\n Latitude: " + latitude + "\n Longitude: " + longitude + "\n Bearing: " + bearing + "\n Faction: " + this.faction);


        markerOptions = new MarkerOptions();
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow))
        markerOptions.position(new LatLng(latitude,longitude))
        .anchor(0.5f, 0.5f)
        .flat(true);
        //.title(participant.getDisplayName());

        if(participant == null)
            markerOptions.title("DummyTest " + faction);
        else
            markerOptions.title(participant.getDisplayName());
    }


    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }
}
