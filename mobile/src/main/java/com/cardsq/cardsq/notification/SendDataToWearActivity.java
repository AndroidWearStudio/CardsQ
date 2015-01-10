package com.cardsq.cardsq.notification;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cardsq.cardsq.R;
import com.cardsq.cardsq.entity.Card;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

/**
 * Created by lzhu on 1/10/15.
 */
public class SendDataToWearActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    int count = 0;
    TextView tv_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_data_to_wear);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        tv_response = (TextView)findViewById(R.id.tv_response);

        //notificationExtendClassForWearOnly();
        //getVoiceInput();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendDataToWear(View view) {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/card");
        dataMapRequest.getDataMap().putString("profileTitle", "Apple" + count++);
        dataMapRequest.getDataMap().putString("profileExplanation", "Fruit" + count++);
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingresult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for(DataEvent event : dataEvents) {
            if(event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals("/response")) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                String data = dataMapItem.getDataMap().getString("profileResponse");

                tv_response.setText(data);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
