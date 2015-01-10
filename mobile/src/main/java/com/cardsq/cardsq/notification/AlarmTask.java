package com.cardsq.cardsq.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;

/**
 *  Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 *
 * This uses the android build in alarm manager
 *
 * This will run on it's own thread.
 *
*/
public class AlarmTask implements Runnable{
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager alarmManager;
    // Your context to retrieve the alarm manager from
    private final Context context;

    public AlarmTask(Context context, Calendar date) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
    }

    @Override
    public void run() {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


        //alarmManager.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);



         alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,  SystemClock.elapsedRealtime(), 10000, pendingIntent);
    }
}
