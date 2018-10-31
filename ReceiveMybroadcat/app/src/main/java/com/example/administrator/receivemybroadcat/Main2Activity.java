package com.example.administrator.receivemybroadcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private MyReceiver1 myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.MYBroadcat");
        myReceiver=new MyReceiver1();
        registerReceiver(myReceiver,intentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    class MyReceiver1 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.


            Intent intent1=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(intent1);
            Main2Activity.this.finish();











        }
    }
}
