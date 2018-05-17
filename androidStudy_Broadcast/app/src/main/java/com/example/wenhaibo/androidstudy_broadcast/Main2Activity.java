package com.example.wenhaibo.androidstudy_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    private IntentFilter intentFilter ;
    private NetChangeReceiver netChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        IntentFilter intentFilter=new IntentFilter(  );
        intentFilter.addAction( "android.net.conn.CONNECTIVITY_CHANGE ");
        netChangeReceiver=new NetChangeReceiver();
        registerReceiver( netChangeReceiver,intentFilter );

    }

    class NetChangeReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
            NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null && networkInfo.isAvailable()){
                Toast.makeText( context,"network is available",Toast.LENGTH_SHORT ).show();

            }else {
                Toast.makeText( context,"network is unavailable",Toast.LENGTH_SHORT ).show();

            }



        }
    }
}
