package com.example.administrator.myrececyer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    private Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        button=findViewById(R.id.bt_show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);

                Intent intent1=new Intent();
                intent1.setAction("com.android.INTERCOM_SYSTEM");
                intent1.putExtra("intercomState","在线");
                intent1.putExtra("groups","沙城");
                intent1.putExtra("userInfo","17777777777");


                //this.sendBroadcast(intent1);
                sendBroadcast(intent1);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
