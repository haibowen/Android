package com.example.administrator.receivemybroadcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MyReceiver myReceiver;
    private Boolean is=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.MYBroadcat");
        myReceiver=new MyReceiver();
        registerReceiver(myReceiver,intentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }




    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.


            Intent intent1=new Intent(MainActivity.this,Main2Activity.class);
            startActivity(intent1);
            MainActivity.this.finish();
            is=false;









        }
    }
}
