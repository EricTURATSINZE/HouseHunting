package com.example.househunting.houseActivities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.househunting.utils.MyCustomApplication;
import com.example.househunting.R;
import com.example.househunting.model.HouseRegister.House;
import com.example.househunting.services.LocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends FragmentActivity implements LocationDialog.LocationDialogListener {
    private static final int LOCATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity2);


        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions markerOptions=new MarkerOptions();
                Location loc = LocationService.getCurrentLocation(MapActivity.this, MapActivity.this, LOCATION_PERMISSION_CODE);

                LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        Location newLocation = new Location(loc);
                        newLocation.setLatitude(latLng.latitude);
                        newLocation.setLongitude(latLng.longitude);
                        LocationService.setLocation(newLocation);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                openDialog();
                            }
                        }, 2000L);
                    }
                });
            }
        });
    }

    public void openDialog() {
        LocationDialog exampleDialog = new LocationDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void onYesClicked() {
        House house = ((MyCustomApplication)getApplication()).getHouse();
        house.getLocation().setCoordinates(new double[]{LocationService.getLocation().getLatitude(), LocationService.getLocation().getLongitude()});
        startActivity(new Intent(MapActivity.this, RegisterHouseFirstStep.class));
    }
}
