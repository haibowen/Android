package com.example.administrator.mybootcomplte;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MybroasReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//Log.d("","onReceive boot_complete start MainActivity");

        //Log.e("2222", "onReceive: ");
        Intent intent1=new Intent(context,MainActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent1);



    }
}
