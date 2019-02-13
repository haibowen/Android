package com.example.administrator.myalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class LongRuningService extends Service {
    public LongRuningService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("wenhaibo", "run: 777777");



            }
        }).start();

        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour=60*60*1000;
        long triggerAtTime=SystemClock.elapsedRealtime()+anHour;
        Intent intent1=new Intent(this,LongRuningService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,intent1,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);



        return super.onStartCommand(intent, flags, startId);
    }
}
