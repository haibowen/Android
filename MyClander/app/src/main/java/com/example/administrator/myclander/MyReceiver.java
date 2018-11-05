package com.example.administrator.myclander;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String messafe= intent.getStringExtra("haibo");
        Toast.makeText(context,messafe,Toast.LENGTH_SHORT).show();




    }
}
