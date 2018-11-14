package com.example.wenhaibo.vister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.wenhaibo.olympic.R;

import player.PlayerActivity;
import worker.WorkerActivity;

public class MainActivity extends Activity {
    private LinearLayout LL;
    private ImageButton IB_1;
    private ImageButton IB_2;
    private ImageButton IB_3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IB_1=(ImageButton)findViewById(R.id.IB_1);
        IB_2=(ImageButton)findViewById(R.id.IB_2);
        IB_3=(ImageButton)findViewById(R.id.IB_3);

        IB_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,FunctionActivity.class);
                startActivity(intent);

            }
        });
        IB_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, WorkerActivity.class);
                startActivity(intent);
            }
        });
        IB_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });

    }


}
