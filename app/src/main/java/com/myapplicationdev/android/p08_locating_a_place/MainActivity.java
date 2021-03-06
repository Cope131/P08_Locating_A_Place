package com.myapplicationdev.android.p08_locating_a_place;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private final String DEBUG_TAG = MainActivity.class.getSimpleName();
    private final int MY_LOCATION_REQUEST_CODE = 0;

    // Locations
    private ArrayList<Location> branches;

    // Views
    private Button northBtn, centralBtn, eastBtn;
    private GoogleMap googleMap;

    private Spinner spinner;
    private ArrayList<String> spinnerItems;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBranches();
        initViews();
        initMap();
        askPermission();

        // SPinner
        spinner = findViewById(R.id.spinner);
        spinnerItems = new ArrayList<>();
        spinnerItems.add("North");
        spinnerItems.add("Central");
        spinnerItems.add("East");
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spinnerItems);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(branches.get(position).getLatlng(), 13));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // --- My Methods ---
    private void loadBranches() {
        branches = new ArrayList<>();
        branches.add(new Location(
                "North - HQ",
                "Block 333, Admiralty Ave 3, \n765654 \nOperating hours: 10am-5pm",
                new LatLng(1.3507729123507404, 103.87041096152066),
                BitmapDescriptorFactory.HUE_GREEN));
        branches.add(new Location(
                "Central",
                "Block 3A, Orchard Ave 3, \n134542 \nOperating hours: 11am-8pm Tel:67788652",
                new LatLng(1.2945847324139004, 103.83392357399069),
                BitmapDescriptorFactory.HUE_BLUE));
        branches.add(new Location(
                "East",
                "Block 555, Tampines Ave 3, \n287788 \nOperating hours: 9am-5pm",
                new LatLng(1.372336650018997, 103.85687420379503),
                BitmapDescriptorFactory.HUE_RED));
    }

    private void initViews() {
        northBtn = findViewById(R.id.north_button);
        centralBtn = findViewById(R.id.central_button);
        eastBtn = findViewById(R.id.east_button);
        northBtn.setOnClickListener(this);
        centralBtn.setOnClickListener(this);
        eastBtn.setOnClickListener(this);
    }

    private void initMap() {
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void addMarkers() {
        int i = 0;
        for (Location branch : branches) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(branch.getLatlng())
                    .title(branch.getName())
                    .snippet(branch.getAddress())
                    .icon(i == 1 ? BitmapDescriptorFactory.fromResource(R.drawable.star) : BitmapDescriptorFactory.defaultMarker(branch.getMarker()));
            googleMap.addMarker(markerOptions);
            i++;
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        Location branch = null;
        switch (v.getId()) {
            case R.id.north_button:
                branch = branches.get(0);
                break;
            case R.id.central_button:
                branch = branches.get(1);
                break;
            case R.id.east_button:
                branch = branches.get(2);
        }
        if (branch != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(branch.getLatlng(), 15));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (googleMap != null) {
            // Zoom in to Sg
            LatLng sg = new LatLng(1.3558701658116, 103.86277464840944);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sg, 11));
            // Add Branches Marker
            addMarkers();
            // Enable Features
            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setCompassEnabled(true);
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setZoomGesturesEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);

            int permissionResult = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionResult == PermissionChecker.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            } else {
                askPermission();
            }
        }
    }
}