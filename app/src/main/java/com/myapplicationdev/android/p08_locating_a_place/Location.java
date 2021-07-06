package com.myapplicationdev.android.p08_locating_a_place;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Location {

    private String name, address;
    private LatLng latlng;
    private float marker;
    private Marker myMarker;

    public Location(String name, String address, LatLng latlng, float marker) {
        this.name = name;
        this.address = address;
        this.latlng = latlng;
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public float getMarker() {
        return marker;
    }

    public void setMyMarker(Marker myMarker) {
        this.myMarker = myMarker;
    }

    public Marker getMyMarker() {
        return myMarker;
    }
}
