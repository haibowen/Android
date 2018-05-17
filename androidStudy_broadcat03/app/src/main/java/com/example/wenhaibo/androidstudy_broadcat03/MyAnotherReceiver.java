package com.example.wenhaibo.androidstudy_broadcat03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyAnotherReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException( "Not yet implemented" );

        Toast.makeText(context,"海波",Toast.LENGTH_LONG ).show();

    }
}
