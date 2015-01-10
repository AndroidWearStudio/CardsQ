package com.cardsq.cardsq.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.cardsq.cardsq.R;
import com.cardsq.cardsq.database.Factory;
import com.cardsq.cardsq.entity.Card;
import com.cardsq.cardsq.navigation.NavigationDrawerActivity;
import com.cardsq.cardsq.setting.MainSettingActivity;
import com.cardsq.cardsq.notification.SendDataToWearActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service is started when an Alarm has been raised
 *
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class NotifyService extends Service {

    Boolean showOnWear = true;

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }


    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false)) {

            if(showOnWear) {
                showPageCards();

               // sendDataToWear(Factory.getCardList());

            }else{
                showNotification();
            }
        }

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        // This is the 'title' of the notification
        CharSequence title = "Review time!!";
        // This is the icon to use on the notification
        int icon = R.drawable.ic_action_person;
        // This is the scrolling text of the notification
        CharSequence text = "Your cards in ready to review.";
        // What time to show on the notification
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, NavigationDrawerActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, title, text, contentIntent);

        notification.defaults |= Notification.DEFAULT_SOUND;

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        // Send the notification to the system.
        notificationManager.notify(NOTIFICATION, notification);

        // Stop the service when we are finished
        stopSelf();
    }

    public void showPageCards(){
        // Nuke all previous notifications and generate unique ids
        NotificationManagerCompat.from(this).cancelAll();
        int notificationId = 0;


        //A card content can be a pair of idiom and its example sentence, or a sentence with its translation
        HashMap<String, String> contentMap = new HashMap<String, String>();
        contentMap.put("Kick somebody out of somewhere", "I will kick you out of here, if you keep yelling at me");
        contentMap.put("Life has its ups and downs. It can be both brutal and beautiful.", "人生潮起潮落，残酷而美丽");
        contentMap.put( "人生潮起潮落，残酷而美丽", "Life has its ups and downs. It can be both brutal and beautiful.");
        contentMap.put( "人生潮起潮落，残酷而美丽", "Life has its ups and downs. It can be both brutal and beautiful. Life has its ups and downs. It can be both brutal and beautiful.");
        contentMap.put("Hit it off", "I don't know why they can't just hit it off.");

        List cards = new ArrayList();


        for(Map.Entry<String, String> entry: contentMap.entrySet()){
            String idiom = entry.getKey();
            String example = entry.getValue();
            NotificationCompat.BigTextStyle cardStyle = new NotificationCompat.BigTextStyle();
            cardStyle.setBigContentTitle(idiom).bigText(example);
            Notification singlePageNotification = new NotificationCompat.Builder(this)
                    .setStyle(cardStyle)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
            cards.add(singlePageNotification);
        }

        // Main notification that will appear on the phone handset and the wearable
        Intent viewIntent = new Intent(this, NavigationDrawerActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, notificationId+1, viewIntent, 0);
        Notification notification1 = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launcher, "Returned", viewPendingIntent)
                .setContentTitle("Card Heap")
                .setContentText("swipe left to review")
                .setSmallIcon(R.drawable.ic_launcher)
                .extend(new NotificationCompat.WearableExtender().addPages(cards))
                .build();

//        NotificationCompat.Builder basicBuilder= new NotificationCompat.Builder(this)
//                .addAction(R.drawable.ic_launcher, "Returned", viewPendingIntent)
//                .setContentTitle("CardHeap");
//        Notification cardsNotification = new NotificationCompat.WearableExtender().addPages(cards).extend(basicBuilder).build();
        // Issue the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId+1,  notification1);
    }


    GoogleApiClient mGoogleApiClient;
    int count = 0;
    TextView tv_response;

    public void sendDataToWear(List<Card> cardList) {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/count");
        for(Card card: cardList){
            dataMapRequest.getDataMap().putString(card.getTitle(), card.getExplanation());
        }

        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingresult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
    }
}
