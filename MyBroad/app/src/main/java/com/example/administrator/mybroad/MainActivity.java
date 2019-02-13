package com.example.administrator.mybroad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //wenhaibo test 20190114
        Intent intent1=new Intent();
        intent1.setAction("com.android.INTERCOM_SYSTEM");
        intent1.putExtra("intercomState","在线");
        intent1.putExtra("groups","沙城");
        intent1.putExtra("userInfo","17777777777");

        sendBroadcast(intent1);


        //this.sendBroadcast(intent1);
    }
}
