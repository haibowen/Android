package com.example.myloggertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Logger.addLogAdapter(new AndroidLogAdapter());

        Logger.v("vvvv");
        Logger.d("Hello");

        new Thread(new Runnable() {
            @Override
            public void run() {

                Logger.d("hahhahh");


            }
        }).start();

    }
}
