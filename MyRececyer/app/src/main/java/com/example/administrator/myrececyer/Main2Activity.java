package com.example.administrator.myrececyer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    private TextView textView, textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView = findViewById(R.id.intercomState);
        textView1 = findViewById(R.id.groups);
        textView2 = findViewById(R.id.userInfo);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.INTERCOM_SYSTEM");

        registerReceiver(mIntentReceiver, filter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        /*IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.INTERCOM_SYSTEM");

        registerReceiver(mIntentReceiver, filter);
*/

    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(context, "收到广播了", Toast.LENGTH_SHORT).show();


            //wenhaibo add 20181229
            if (action.equals("com.android.INTERCOM_SYSTEM")) {


                textView.setText(intent.getStringExtra("intercomState"));
                //mintercomState.setText("message");
                textView1.setText(intent.getStringExtra("groups"));

                textView2.setText(intent.getStringExtra("userInfo"));


            }

        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mIntentReceiver);
    }
}
