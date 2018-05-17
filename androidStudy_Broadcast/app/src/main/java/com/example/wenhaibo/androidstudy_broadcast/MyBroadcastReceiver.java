package com.example.wenhaibo.androidstudy_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException( "Not yet implemented" );
        Toast.makeText( context,"我收到广播了",Toast.LENGTH_SHORT ).show();

    }
}
