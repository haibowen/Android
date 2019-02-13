package com.example.administrator.myalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bt_service)
    Button button;
    @OnClick(R.id.bt_service)
    public  void change(){
        Toast.makeText(this,"wozhiixngle",Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this,LongRuningService.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public  void testLambda(){

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        
        new Thread(()->{

        }).start();

    }
}
