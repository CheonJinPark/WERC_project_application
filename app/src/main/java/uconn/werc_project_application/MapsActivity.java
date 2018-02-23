package uconn.werc_project_application;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double Long, Lat;
 GPS g1,g2,g3,g4,g5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        g1 = (GPS) intent.getSerializableExtra("g1");
        g2 = (GPS) intent.getSerializableExtra("g2");
        g3 = (GPS) intent.getSerializableExtra("g3");
        g4 = (GPS) intent.getSerializableExtra("g4");
        g5 = (GPS) intent.getSerializableExtra("g5");
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

        // Add a marker in Sydney and move the camera

        LatLng Uconn = new LatLng(g1.getLatitude(),g1.getLongitude());
        LatLng Alumni = new LatLng(g2.getLatitude(),g2.getLongitude());
        LatLng Coop = new LatLng(g3.getLatitude(),g3.getLongitude());
        LatLng Hilltop = new LatLng(g4.getLatitude(),g4.getLongitude());
        LatLng Lib = new LatLng(g5.getLatitude(),g5.getLongitude());


        mMap.addMarker(new MarkerOptions().position(Uconn).title("Uconn's Lat is :" +Lat+"   and Long is : "+Long));
        mMap.addMarker(new MarkerOptions().position(Alumni).title("Alumni's Lat is :" +Lat+"   and Long is : "+Long));
        mMap.addMarker(new MarkerOptions().position(Coop).title("Coop's Lat is :" +Lat+"   and Long is : "+Long));
        mMap.addMarker(new MarkerOptions().position(Hilltop).title("Hilltop's Lat is :" +Lat+"   and Long is : "+Long));
        mMap.addMarker(new MarkerOptions().position(Lib).title("Lib's Lat is :" +Lat+"   and Long is : "+Long));

        mMap.addCircle(new CircleOptions().center(Uconn).radius(70).strokeWidth(0f).fillColor(Color.parseColor("#2200FFFF")));
        mMap.addCircle(new CircleOptions().center(Alumni).radius(40).strokeWidth(1.0f).fillColor(Color.parseColor("#668A2BE2")));
        mMap.addCircle(new CircleOptions().center(Coop).radius(30).strokeWidth(2.0f).fillColor(Color.parseColor("#2FE0FFFF")));
        mMap.addCircle(new CircleOptions().center(Hilltop).radius(60).strokeWidth(3.0f).fillColor(Color.parseColor("#3390EE90")));
        mMap.addCircle(new CircleOptions().center(Lib).radius(50).strokeWidth(4.0f).fillColor(Color.parseColor("#44FF0000")));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((Uconn),15));

    }
}
