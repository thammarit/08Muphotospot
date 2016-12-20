package com.egco428.mupts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference locationRef;
    String recieveLo;
    String order;
    String Lat;
    String Long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*get data that passed from PhotoActivity (LocationPicsAdapter)*/
        Intent recieveIntent = getIntent();
        recieveLo = recieveIntent.getStringExtra("LONAME");
        order = recieveIntent.getStringExtra("POSITION");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationRef = FirebaseDatabase.getInstance().getReference().child("LocationPics").
                child(recieveLo).child("pic" + order); //Create database reference to access wanted node

        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //for loop in our wanted node and get value of latitude and longitude
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        if (Objects.equals(i.getKey(), "latitude")) {
                            Lat =  i.getValue().toString(); //keep value in variable Lat
                        } else if (Objects.equals(i.getKey(), "longtitude")) {
                            Long = i.getValue().toString(); //keep value in variable Long
                        }
                    }
                    /*pass Latitude and longitude value in map marker's function*/
                    LatLng sydney = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Long));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("It's here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Request Permission to access location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
