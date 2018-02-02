package uconn.werc_project_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
// AWS Database
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.*;
import com.amazonaws.models.nosql.GpsdataDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


public class MainActivity extends AppCompatActivity {

    Double longitude, latitude;
    Intent intent_test;
    String url;

    /** AWS Global Variables **/
    // AWS Pinpoint Data Analytics
    public static PinpointManager pinpointManager;
    // AWS DynamoDB
    DynamoDBMapper dynamoDBMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /** AWS Initializations **/
        AWSMobileClient.getInstance().initialize(this).execute();
        // AWS Pinpoint Data Analytics
        PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                getApplicationContext(),
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration());

        pinpointManager = new PinpointManager(pinpointConfig);

        // AWS DynamoDB
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        // Start a session with Pinpoint
        pinpointManager.getSessionClient().startSession();

        // Stop the session and submit the default app started event
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();


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
