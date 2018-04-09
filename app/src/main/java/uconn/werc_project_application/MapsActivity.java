package uconn.werc_project_application;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.Information.Information;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uconn.werc_project_application.data.AqiContentContract;
import uconn.werc_project_application.data.DataInterpreter;
import uconn.werc_project_application.data.SensorContentContract;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double Long, Lat;
    GPS g0, g1, g2, g3, g4, g5;
    Information info;
    Button op_btn;
    String DataType, ALLorSelf;
    ArrayList<GPS> mylist, all_list;
    int Radius;
    String myID;
    List<WeightedLatLng> WLLlist;
    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;
    /**
     * The unique identifier for the loader
     */
    private static final int DP_LOADER = 10;

    // ASync query constant
    private static final int QUERY_TOKEN = 1001;

    /**
     * Content Resolver
     */
    private ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //define the ArrayList<GPS>
        mylist = new ArrayList<>();
        all_list = new ArrayList<>();
        info = new Information();
        WLLlist = new ArrayList<WeightedLatLng>();

        //Test GPSs
        //Dummy data to set 300 is one of the max for gradient;

        g1 = new GPS(-72.253981, 41.807741); //Uconn
        g1.setCo(300);
        g1.setPM(40);
        g1.setNo2(60);
        g1.setSo2(10);
        g1.setO3(30);
        g1.setAqival(60);
        g1.setAqi_type(Information.DUST);
        g2 = new GPS(-72.250645, 41.803309); //Alumni Dorm
        g2.setCo(50);
        g2.setPM(20);
        g2.setNo2(120);
        g2.setSo2(10);
        g2.setO3(301);
        g2.setAqival(10);
        g2.setAqi_type(Information.DUST);
        g3 = new GPS(-72.253430, 41.804776); //Coop
        g3.setCo(30);
        g3.setPM(70);
        g3.setNo2(120);
        g3.setSo2(10);
        g3.setO3(10);
        g3.setAqival(5);
        g3.setAqi_type(Information.DUST);
        g4 = new GPS(-72.259929, 41.802675); //Hilltop Community Center
        g4.setCo(300);
        g4.setPM(150);
        g4.setNo2(120);
        g4.setSo2(10);
        g4.setO3(20);
        g4.setAqival(301);
        g4.setAqi_type(Information.DUST);
        g5 = new GPS(-72.251748, 41.806629); //Library
        g5.setCo(10);
        g5.setPM(40);
        g5.setNo2(60);
        g5.setSo2(310);
        g5.setO3(300);
        g5.setAqival(80);
        g5.setAqi_type(Information.DUST);

        all_list.add(g1);
        all_list.add(g2);
        all_list.add(g3);
        all_list.add(g4);
        all_list.add(g5);



        //saveLList();


        //define myiD for compare
        myID = AWSProvider.getInstance().getIdentityManager().getCachedUserID();
        Log.d("MapsActivity", "My userID : " + myID);


        ALLorSelf = "ALL";
        DataType = info.CO;
        Radius = info.CO_RANGE;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.float_option_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupPopup(v);
            }
        });

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

        Double lat_begin = DataInterpreter.getInstance().getGpsLat();
        Double long_begin = DataInterpreter.getInstance().getGpsLong();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(lat_begin, long_begin)), 15));

        downloadFromServer();
        // Add a marker in Sydney and move the camera

        //LatLng Uconn = new LatLng(g1.getLatitude(),g1.getLongitude());
        //LatLng Alumni = new LatLng(g2.getLatitude(),g2.getLongitude());
        //LatLng Coop = new LatLng(g3.getLatitude(),g3.getLongitude());
        //LatLng Hilltop = new LatLng(g4.getLatitude(),g4.getLongitude());
        //LatLng Lib = new LatLng(g5.getLatitude(),g5.getLongitude());


//Test Uconn will be have 5 range, 5 stroke, Let's try


        // mMap.addMarker(new MarkerOptions().position(Uconn).title("Uconn's "+"\r\n"+g1.showInfo()));
        //  mMap.addMarker(new MarkerOptions().position(Alumni).title("Alumni's").snippet("Lat is :" +g2.getLatitude()+System.getProperty("line.separator")+"Long is : "+g2.getLongitude()));
        //   mMap.addMarker(new MarkerOptions().position(Coop).title("Coop's "+System.getProperty("line.separator")+"Lat is :" +g3.getLatitude()+System.getProperty("line.separator")+"Long is : "+g3.getLongitude()));
        //   mMap.addMarker(new MarkerOptions().position(Hilltop).title("Hilltop's "+System.getProperty("line.separator")+"Lat is :" +g4.getLatitude()+System.getProperty("line.separator")+"Long is : "+g4.getLongitude()));
        //    mMap.addMarker(new MarkerOptions().position(Lib).title("Lib's "+System.getProperty("line.separator")+"Lat is :" +g5.getLatitude()+System.getProperty("line.separator")+"Long is : "+g5.getLongitude()));


        //mMap.addCircle(new CircleOptions().center(Uconn).radius(70).strokeWidth(0f).fillColor(Color.parseColor("#2200FFFF")));
        // drawCircle(mMap,g1);
        //mMap.addCircle(new CircleOptions().center(Alumni).radius(40).strokeWidth(1.0f).fillColor(Color.parseColor("#668A2BE2")));
        //without drawCircle function
        //drawCircle_loop(mMap,g1,info.SAFE_COLOUR,100);
        //drawCircle_loop(mMap,g2,info.SAFE_COLOUR,100);
        //drawCircle_loop(mMap,g3,info.SAFE_COLOUR,100);
        //drawCircle_loop(mMap,g4,info.SAFE_COLOUR,100);
        //drawCircle_loop(mMap,g5,info.SAFE_COLOUR,100);
        //mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.O3_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.CO_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.DANGER_1)));
        // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.NO2_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.DANGER_2)));
        // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.SO2_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.DANGER_1)));
        // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.Dust_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));


        //  mMap.addCircle(new CircleOptions().center(Coop).radius(30).strokeWidth(2.0f).fillColor(Color.parseColor("#2FE0FFFF")));
        //  mMap.addCircle(new CircleOptions().center(Hilltop).radius(60).strokeWidth(3.0f).fillColor(Color.parseColor("#3390EE90")));
        //  mMap.addCircle(new CircleOptions().center(Lib).radius(50).strokeWidth(4.0f).fillColor(Color.parseColor("#44FF0000")));


    }

    public void drawCircle_loop(GoogleMap gMap, GPS g, String dataType, int radius) {
        int d = radius / 10;

        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(), g.getLongitude())).radius(radius).strokeWidth(0.0f).fillColor(Color.parseColor(info.define_color_aqivalue(dataType, g.get(dataType)))));
        for (int i = 1; i < 6; i++) {
            gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(), g.getLongitude())).radius(radius - d * i).strokeWidth(0.0f).fillColor(Color.parseColor(info.define_color_aqivalue(dataType, g.get(dataType)))));

        }

    }

    public void updateMap() {
        Double Long, Lang;
        mMap.clear();
        if (ALLorSelf.equals("ALL")) {
            Log.d("MapsActivity", "all_list's size is : " + all_list.size());
            for (int j = 0; j < all_list.size(); j++) {
                drawCircle_loop(mMap, all_list.get(j), DataType, Radius);

            }
        } else {

            for (int j = 0; j < mylist.size(); j++) {
                drawCircle_loop(mMap, mylist.get(j), DataType, Radius);
            }

        }
    }

    public void drawCircle(GoogleMap gMap, GPS g) {
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(), g.getLongitude())).radius(info.O3_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(), g.getLongitude())).radius(info.CO_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(), g.getLongitude())).radius(info.NO2_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(), g.getLongitude())).radius(info.SO2_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(), g.getLongitude())).radius(info.Dust_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));


    }



    public void setupPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.firstmenu, menu);

        //MenuItem tt = (MenuItem)findViewById(R.id.popup_co);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_co:

                        DataType = info.CO;
                        Radius = info.CO_RANGE;
                        updateMap();
                        //saveLList();
                        //updateHeatMap();
                        break;

                    case R.id.popup_o3:

                        DataType = info.O3;
                        Radius = info.O3_RANGE;
                        updateMap();
                        //saveLList();
                        //updateHeatMap();
                        break;

                    case R.id.popup_no2:

                        DataType = info.NO2;
                        Radius = info.NO2_RANGE;
                        updateMap();
                        //saveLList();
                        //updateHeatMap();
                        break;

                    case R.id.popup_so2:

                        DataType = info.SO2;
                        Radius = info.SO2_RANGE;
                        updateMap();
                        //saveLList();
                        //updateHeatMap();
                        break;

                    case R.id.popup_dust:

                        DataType = info.DUST;
                        Radius = info.Dust_RANGE;
                        updateMap();
                        //saveLList();
                        //updateHeatMap();
                        break;

                    case R.id.popup_aqi_value:
                        DataType = info.AQIvalue;
                        updateMap_withAQI();
                        //saveLList();
                        //updateHeatMap();
                        break;

                    case R.id.popup_all:

                        ALLorSelf = "ALL";
                        updateMap();
                        break;
                    case R.id.popup_self:

                        ALLorSelf = "SELF";
                       updateMap();
                        break;


                }
                return false;
            }

        });

        popup.show();

    }


    void addheatmap() {

        int[] colors = {
                Color.rgb(3,192,60),        // green
                Color.rgb(255, 211, 0),     // yellow
                Color.rgb(255,153,102),     //orange
                Color.rgb(206,32,41),       //red
                Color.rgb(120,81,169),      //purple
                Color.rgb(176,48,96)        //maroon
        };


        float[] startPoints = {
                0.05f, 0.15f,0.25f,0.35f,0.5f, 0.8f
        };
        Gradient gradient = new Gradient(colors, startPoints);
        //mProvider
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(WLLlist)
                .gradient(gradient)
                .radius(50)
                .build();



        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }

    void saveLList() {
        WLLlist.clear();
        for (int i = 0; i < all_list.size(); i++) {
            WeightedLatLng WLL = new WeightedLatLng(new LatLng(all_list.get(i).getLatitude(), all_list.get(i).getLongitude()), all_list.get(i).get(DataType));
            Log.d("MapsActivity", "save list the data type is : " + DataType + " the value is : " + Integer.toString(all_list.get(i).get(DataType)));
            WLLlist.add(WLL);
        }
    }

    void updateHeatMap() {
        mProvider.setWeightedData(WLLlist);
        mOverlay.clearTileCache();

    }
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(this,
//                SensorContentContract.Sensordata.CONTENT_URI,
//                SensorContentContract.Sensordata.PROJECTION_ALL,
//                null,
//                null,
//                SensorContentContract.Sensordata.SORT_ORDER_DEFAULT);
//    }

    void moveToCenter() {
        Long = all_list.get(all_list.size() - 1).getLongitude();
        Lat = all_list.get(all_list.size() - 1).getLatitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(Lat, Long)), 15));
        Log.d("MapsActivity", "moveToCenter is worked to : Lat = "+Double.toString(Lat)+" Long = "+Double.toString(Long));
    }
    void downloadFromServer(){
        // Replace local cursor methods with async query handling
        contentResolver = this.getContentResolver();
        Uri dataUri = AqiContentContract.Aqidata.uriBuilder();
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);
                Log.d("MapsActivity", "Cursor complete");
                try {
                    cursor.moveToFirst();
                    GPS gpsfinal = new GPS();
                    gpsfinal.savefromCursorQuery(cursor);
                    Log.d("MapsActivity", "Cursor's UserID : " + gpsfinal.getUserID());
                    all_list.add(gpsfinal);

                    while (cursor.moveToNext()) {
                        GPS gps = new GPS();
                        gps.savefromCursorQuery(cursor);
                        Log.d("MapsActivity", "Cursor's UserID : " + gps.getUserID());
                        if (myID.equals(gps.getUserID())) {
                            mylist.add(gps);
                            //    mylist.add(gps);
                        }
                        all_list.add(gps);
                    }


                } finally {
                    cursor.close();
                    Log.d("MapsActivity", "Cursor is closed");
                    //saveLList();
                    //addheatmap();
                    updateMap();

                }
            }
        };
        queryHandler.startQuery(QUERY_TOKEN, null, dataUri, SensorContentContract.Sensordata.PROJECTION_ALL, null, null, null);


    }

    public void updateMap_withAQI() {
        Double Long, Lang;
        mMap.clear();
        if (ALLorSelf.equals("ALL")) {
            Log.d("MapsActivity", "all_list's size is : " + all_list.size());
            for (int j = 0; j < all_list.size(); j++) {
                drawCircle_loop(mMap, all_list.get(j), DataType, info.getTheRadius(all_list.get(j).getAqi_type()));

            }
        } else {

            for (int j = 0; j < mylist.size(); j++) {
                drawCircle_loop(mMap, mylist.get(j), DataType, info.getTheRadius(mylist.get(j).getAqi_type()));
            }

        }
    }


}

