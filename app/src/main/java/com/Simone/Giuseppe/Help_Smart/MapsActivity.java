package com.Simone.Giuseppe.Help_Smart;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.maps.model.Marker;

import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.util.Log;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private LocationManager lm;
    private double posLat ;
    private double posLng ;
    private LatLng position;
    private Marker mPosition;
    Polyline polyline2;
    private Intent serv;
    private SharedPreferences mapTrack;
    private SharedPreferences.Editor editor;
    private Calendar calendar;


    public void onProviderDisabled(String arg0) {

        Log.e("GPS", "provider disabled " + arg0);

    }

    public void onProviderEnabled(String arg0) {

        Log.e("GPS", "provider enabled " + arg0);

    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        Log.e("GPS", "status changed to " + arg0 + " [" + arg1 + "]");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        calendar= Calendar.getInstance();
        mapTrack=getSharedPreferences("track"+getIntent().getStringExtra("day"),Context.MODE_PRIVATE);
        Log.e("day",getIntent().getStringExtra("day"));
        editor=mapTrack.edit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    protected void onPause(){
        super.onPause();
      // stopService(serv);
    }
    @Override
    protected void onResume(){

        super.onResume();
       // stopService(serv);
    }

    public void onLocationChanged(Location location) {


    }
private int last=0;

    @Override
    public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                polyline2 = mMap.addPolyline(new PolylineOptions().clickable(true));

                int i=0;
                for(i=0;i<mapTrack.getInt("cnt",0);i++) {
                    posLat = mapTrack.getFloat("lat" + i, 0);
                    posLng = mapTrack.getFloat("lon" + i, 0);


                    position = new LatLng(posLat, posLng);
                    if (mPosition != null) {
                        mPosition.remove();
                    }
                    List<LatLng> points = polyline2.getPoints();
                    points.add(position);
                    polyline2.setPoints(points);
                }
                last=i;

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    int i=0;
                    @Override
                    public void onMapClick(LatLng clickCoords) {
                        for(i=last;i<mapTrack.getInt("cnt",0);i++) {
                            posLat = mapTrack.getFloat("lat" + i, 0);
                            posLng = mapTrack.getFloat("lon" + i, 0);


                            position = new LatLng(posLat, posLng);
                            if (mPosition != null) {
                                mPosition.remove();
                                mPosition=null;
                            }
                            List<LatLng> points = polyline2.getPoints();
                            points.add(position);
                            polyline2.setPoints(points);


                        }
                        last=i;
                        if(mPosition==null)
                            mPosition = mMap.addMarker(new MarkerOptions().position(position).title("Your position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    }
                });
                mPosition = mMap.addMarker(new MarkerOptions().position(position).title("Your position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

    }

}
