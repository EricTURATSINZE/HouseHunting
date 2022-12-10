package com.example.househunting;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Author NGIRIMANA Schadrack
 */

public class HouseMapLocation extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap googleMapLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.house_map_location);
        mapFragment.getMapAsync(this);
    }

        @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
        {
            double lng= getIntent().getDoubleExtra("longitude",30.061457);
            double lat= getIntent().getDoubleExtra("latitude",-1.953354);

            googleMapLocation = googleMap;
            LatLng houseLocation = new LatLng(lat, lng);
            googleMapLocation.addMarker(new MarkerOptions().position(houseLocation).title(String.valueOf(R.string.house_map_title)));
            googleMapLocation.moveCamera(CameraUpdateFactory.newLatLng(houseLocation));
            googleMapLocation.animateCamera(CameraUpdateFactory.newLatLngZoom(houseLocation,20));
    }
}
