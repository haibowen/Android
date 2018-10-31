package com.example.administrator.myservicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class MyService extends Service {

    private  DownloadBinder mybinder=new DownloadBinder();
    class DownloadBinder extends Binder{
        public void startDowload(){


            Log.d("Myservice", "startDowload: ");
        }
        public  int getprogress(){

            Log.d("Myservice", "getprogress: ");

            return 0;

        }


    }
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mybinder;



    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent=new Intent(MyService.this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        Notification notification=new Notification.Builder(this)
                .setContentTitle("NEWS")
                .setContentText("beijingshijian xiawuzhong")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);


        Log.d("wenhaibo", "wo oncreate ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wenhaibo", "onStartCommand: ");

        new Thread(new Runnable() {
            @Override
            public void run() {

                //处理具体的逻辑


                stopSelf();

            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wenhaibo", "onDestroy: ");
    }
}
