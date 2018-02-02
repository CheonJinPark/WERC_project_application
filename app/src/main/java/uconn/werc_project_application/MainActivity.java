package uconn.werc_project_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Double longitude, latitude;
    Intent intent_test;
    String url, jfowejfjweof;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = -73.087749; // default longitude
        latitude = 41.603221; // default latitude


        Button Send = (Button)findViewById(R.id.Button_Send);


        Send.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                EditText Edit_Longitude = (EditText)findViewById(R.id.EditText_Longitude);
                EditText Edit_Latitude = (EditText)findViewById(R.id.EditText_Latitude);

                String string_long = Edit_Longitude.getText().toString();
                String string_lat = Edit_Latitude.getText().toString();

                if(isStringDouble(string_long)){
                    longitude = Double.parseDouble(string_long);
                    Toast.makeText(getApplicationContext(), "Longitude is saved",Toast.LENGTH_SHORT ).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Logitude is not correct format",Toast.LENGTH_SHORT).show();
                }

                if(isStringDouble(string_lat)){
                    latitude = Double.parseDouble(string_lat);
                    Toast.makeText(getApplicationContext(), "Latitude is saved",Toast.LENGTH_SHORT ).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Latitude is not correct format",Toast.LENGTH_SHORT).show();
                }


            }
        });
        Button GotoMap = (Button)findViewById(R.id.button_gotomap);
        GotoMap.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                intent_test = new Intent(MainActivity.this, MapsActivity.class);
                intent_test.putExtra("Lat",latitude);
                intent_test.putExtra("Long",longitude);
                startActivity(intent_test);
            }
        });

    }



    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
