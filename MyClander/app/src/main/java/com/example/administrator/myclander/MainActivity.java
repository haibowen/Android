package com.example.administrator.myclander;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;
   private   NetconnectWork netconnectWork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
       netconnectWork=new NetconnectWork();
        registerReceiver(netconnectWork,intentFilter);


        button=findViewById(R.id.btstart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent("android.intent.action.hiabo345");
                intent.putExtra("haibo","hello world");

                sendBroadcast(intent);

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(netconnectWork);
    }

    class   NetconnectWork extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager=
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo !=null&&networkInfo.isAvailable()){
                Toast.makeText(MainActivity.this,"有网络存在",Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(MainActivity.this,"没有网络",Toast.LENGTH_SHORT).show();

            }


        }
    }
}
