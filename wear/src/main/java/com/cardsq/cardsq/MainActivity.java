package com.cardsq.cardsq;

import android.app.Activity;
import android.app.Notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        DataApi.DataListener, SensorEventListener,
        GoogleApiClient.OnConnectionFailedListener {

    private SensorManager sensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 100;

    TextView tv_last_xyz;
    TextView tv_xyz;
    TextView tv_speed;
    TextView tv_front;
    TextView tv_back;

    GoogleApiClient mGoogleApiClient;
    int shakeCount = 0;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for(DataEvent event : dataEvents) {
            if(event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals("/card")) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                String title = dataMapItem.getDataMap().getString("profileTitle");
                String explanation = dataMapItem.getDataMap().getString("profileExplanation");

                try {
                    tv_front.setText(title);
                } catch(Exception ex) {

                }
                try {
                    tv_back.setVisibility(View.INVISIBLE);
                    tv_back.setText(explanation);
                } catch (Exception e) {}

                shakeCount = 0;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tv_front = (TextView) stub.findViewById(R.id.title);
                tv_back = (TextView) stub.findViewById(R.id.explanation);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("Mobile Activity", "onConnectionFailed: " + connectionResult);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Mobile Activity", "onConnectionSuspended: " + i);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if((curTime - lastUpdate) > 1000) {
                long diffTime = curTime - lastUpdate;
                lastUpdate = curTime;

                float speed = Math.abs(y - last_y);

                if(last_y > y && last_y - y > 1) {
                    sendDataToMobile("Yes");
                } else if(y > last_y && y - last_y > 1) {
                    tv_back.setVisibility(View.VISIBLE);
                    //sendDataToMobile("No");
                }

//                tv_last_xyz.setText(String.format("%f", last_y));
//                tv_xyz.setText(String.format("%f", y));
//                tv_speed.setText(String.format("%f", speed));

                last_y = y;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    int count = 0;

    public void sendDataToMobile(String text) {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/response");
        dataMapRequest.getDataMap().putString("profileResponse", text + count++);
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingresult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
    }
}