package com.example.administrator.myservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    
    public  static  final String TAG="WENHAIBO";

    private DownloadBinder downloadBinder=new DownloadBinder();
    class DownloadBinder extends Binder {

        public void  startDownload(){
            Log.d(TAG, "startDownload: excuted");
        }
        public  int getProgress(){
            Log.d(TAG, "getProgress: excuted");
            return 0;

        }


    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return downloadBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: excuted");
        //前台服务的创建
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        Notification notification =new  NotificationCompat.Builder(this)
                .setContentTitle("this is content title")
                .setContentText("this is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.d(TAG, "onStartCommand: excuted");
        return super.onStartCommand(intent, flags, startId);
       
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: excuted");
        
    }


}
