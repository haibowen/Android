package com.example.wenhaibo.vister;

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

import com.example.wenhaibo.olympic.R;

import map.IndexActivity;

public class HotelActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetworkChangeReceiver  networkChangeReceiver;
    private Button BT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        BT=(Button)findViewById(R.id.BT);
        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HotelActivity.this, IndexActivity.class);
                startActivity(intent);
                finish();
            }
        });
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver =new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);

    }
    class NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectionManager.getActiveNetworkInfo();
            if (networkInfo!=null&&networkInfo.isAvailable()){
                Toast.makeText(HotelActivity.this, "网络正常", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(HotelActivity.this, "网络开小差了", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
