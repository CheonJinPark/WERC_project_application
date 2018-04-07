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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uconn.werc_project_application.data.SensorContentContract;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double Long, Lat;
    GPS g1,g2,g3,g4,g5;
    Information info;
    Button op_btn;
    String DataType,ALLorSelf;
    List<GPS> gpsList;
    int Radius;

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

        Intent intent = getIntent();

        contentResolver = this.getContentResolver();

        info = new Information();
        g1 = (GPS) intent.getSerializableExtra("g1");

        g1.setO3(4.6);
        g1.setCo(0.084);
        g1.setNo2(0.63);
        g1.setSo2(0.7);
        g2 = (GPS) intent.getSerializableExtra("g2");
        g3 = (GPS) intent.getSerializableExtra("g3");
        g4 = (GPS) intent.getSerializableExtra("g4");
        g5 = (GPS) intent.getSerializableExtra("g5");
        //default datatype is co
        DataType = info.CO;
        ALLorSelf = "ALL";
        Radius = 100;
        //Let's think self is uconn(g1) and others are other people
        gpsList = new ArrayList<GPS>();
        gpsList.add(g1);
        gpsList.add(g2);
        gpsList.add(g3);
        gpsList.add(g4);
        gpsList.add(g5);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       // op_btn = (Button)findViewById(R.id.map_option);
//
       // op_btn.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
//DialogRadio();
     //       }
     //   });


//        getLoaderManager().initLoader(DP_LOADER, null, this);

        Uri dataUri = SensorContentContract.Sensordata.uriBuilder();


        // Replace local cursor methods with async query handling
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);
                try {
                    cursor.moveToFirst();
                    while(cursor.moveToNext())
                    {
                        //Datapoint dp = Datapoint.fromCursorQuery(cursor);
                        // dp has all fields in it. Add to map from here.
                        //Log.d("ASyncQuery", "PacketId: " + dp.getPacketId());
                        // use dp.getSensor_co and other accessors to read data.
                        // See Datapoint class in data package for more info.


                    }
                } finally {
                    cursor.close();
                }
            }
        };
        queryHandler.startQuery(QUERY_TOKEN, null, dataUri, SensorContentContract.Sensordata.PROJECTION_ALL, null, null, null);



        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.float_option_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setupPopup(v);
            }
        });

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
        drawCircle_loop(mMap,g1,info.SAFE_COLOUR,100);
        drawCircle_loop(mMap,g2,info.SAFE_COLOUR,100);
        drawCircle_loop(mMap,g3,info.SAFE_COLOUR,100);
        drawCircle_loop(mMap,g4,info.SAFE_COLOUR,100);
        drawCircle_loop(mMap,g5,info.SAFE_COLOUR,100);
        //mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.O3_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
       // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.CO_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.DANGER_1)));
       // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.NO2_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.DANGER_2)));
       // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.SO2_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.DANGER_1)));
       // mMap.addCircle(new CircleOptions().center(new LatLng(g2.getLatitude(),g2.getLongitude())).radius(info.Dust_RANGE).strokeWidth(5.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));


      //  mMap.addCircle(new CircleOptions().center(Coop).radius(30).strokeWidth(2.0f).fillColor(Color.parseColor("#2FE0FFFF")));
      //  mMap.addCircle(new CircleOptions().center(Hilltop).radius(60).strokeWidth(3.0f).fillColor(Color.parseColor("#3390EE90")));
      //  mMap.addCircle(new CircleOptions().center(Lib).radius(50).strokeWidth(4.0f).fillColor(Color.parseColor("#44FF0000")));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((Uconn),15));

    }

    public void drawCircle_loop(GoogleMap gMap, GPS g, String state, int radius) {
        int d = radius/10;

        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(),g.getLongitude())).radius(radius).strokeWidth(0.0f).fillColor(Color.parseColor(state)));
        for(int i =1; i<6;i++){
            gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(),g.getLongitude())).radius(radius-d*i).strokeWidth(0.0f).fillColor(Color.parseColor(state)));

        }

    }
    public void updateMap(){
mMap.clear();
if(ALLorSelf.equals("ALL")){
    for(int j = 0;j<gpsList.size();j++){
        drawCircle_loop(mMap,gpsList.get(j),info.SAFE_COLOUR,Radius);
    }
}else{

    drawCircle_loop(mMap,g1,info.define_color(DataType,g1.get(DataType)),Radius);
    mMap.addMarker(new MarkerOptions().position(new LatLng(g1.getLatitude(),g1.getLongitude())).title(DataType + " : " + g1.get(DataType)));
}
    }

    public void drawCircle(GoogleMap gMap, GPS g){
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(),g.getLongitude())).radius(info.O3_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(),g.getLongitude())).radius(info.CO_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(),g.getLongitude())).radius(info.NO2_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(),g.getLongitude())).radius(info.SO2_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));
        gMap.addCircle(new CircleOptions().center(new LatLng(g.getLatitude(),g.getLongitude())).radius(info.Dust_RANGE).strokeWidth(0.0f).fillColor(Color.parseColor(info.SAFE_COLOUR)));



    }
    private void DialogRadio(){
        final String[] optionlist = {"100","200","300","400","500"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Options");
        alt_bld.setSingleChoiceItems(optionlist, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mMap.clear();
                drawCircle_loop(mMap,g1,info.SAFE_COLOUR, Integer.parseInt(optionlist[item]));
                drawCircle_loop(mMap,g2,info.SAFE_COLOUR, Integer.parseInt(optionlist[item]));
                drawCircle_loop(mMap,g3,info.SAFE_COLOUR, Integer.parseInt(optionlist[item]));
                drawCircle_loop(mMap,g4,info.SAFE_COLOUR, Integer.parseInt(optionlist[item]));
                drawCircle_loop(mMap,g5,info.SAFE_COLOUR, Integer.parseInt(optionlist[item]));
op_btn.setText(optionlist[item]);
                //Toast.makeText(getApplicationContext(), "Phone Model = "+PhoneModels[item], Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }
    public void setupPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.firstmenu, menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.popup_co:

                        DataType = info.CO;
                        Radius = 100;
                        updateMap();
                        break;

                    case R.id.popup_o3:

                        DataType = info.O3;
                        Radius = 200;
                        updateMap();
                        break;

                    case R.id.popup_no2:

                        DataType = info.NO2;
                        Radius = 300;
                        updateMap();
                        break;

                    case R.id.popup_so2:

                        DataType = info.SO2;
                        Radius = 400;
                        updateMap();
                        break;

                    case R.id.popup_dust:

                        DataType = info.DUST;
                        Radius = 500;
                        updateMap();
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

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(this,
//                SensorContentContract.Sensordata.CONTENT_URI,
//                SensorContentContract.Sensordata.PROJECTION_ALL,
//                null,
//                null,
//                SensorContentContract.Sensordata.SORT_ORDER_DEFAULT);
//    }


}

