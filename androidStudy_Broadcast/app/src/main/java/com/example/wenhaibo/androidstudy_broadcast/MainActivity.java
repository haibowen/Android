package com.example.wenhaibo.androidstudy_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        //IntentFiler 实例
        intentFilter=new IntentFilter(  );
        intentFilter.addAction( "android.net.conn.CONNECTIVITY_CHANGE" );
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver( networkChangeReceiver,intentFilter );

        //button 绑定id

        button=findViewById( R.id.bt_send );

        // 点击事件


        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(  "com.example.wenhaibo.androidstudy_broadcast.MY_BROADCAST");
                sendBroadcast( intent );


            }
        } );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver( networkChangeReceiver );
    }

    class  NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText( context,"network changes",Toast.LENGTH_SHORT ).show();
            ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo !=null &&networkInfo.isAvailable()){

                Toast.makeText( context,"network is available",Toast.LENGTH_SHORT ).show();

            }else {
                Toast.makeText( context,"network is unavailable",Toast.LENGTH_SHORT ).show();

            }

        }
    }
}
