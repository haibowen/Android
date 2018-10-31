package com.example.administrator.myservicetest;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyIntentService extends IntentService {


   public  MyIntentService(){
       super("MyIntentService");



   }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("666", "onHandleIntent:  "+Thread.currentThread().getId());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("666", "onDestroy: ");
    }
}
