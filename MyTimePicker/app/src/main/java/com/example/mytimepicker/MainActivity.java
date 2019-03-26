package com.example.mytimepicker;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.sql.Time;
import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    private Chronometer chronometer,chronometer1;
    private Button buttonstart,buttonstop,buttonrestart,buttoncancel;
    private int time=120000;

    private StringBuilder stringBuilder=new StringBuilder();
    private Formatter formatter=new Formatter(stringBuilder, Locale.getDefault());

    private static final String TAG = "MainActivity";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();


    }

    private void init() {
        chronometer=findViewById(R.id.bt_chronometer);

        chronometer1=findViewById(R.id.bt_chronometer_start);

        buttonstart=findViewById(R.id.bt_start);
        buttonstop=findViewById(R.id.bt_stop);
        buttonrestart=findViewById(R.id.bt_restart);
        buttoncancel=findViewById(R.id.bt_clean);
        buttonstart.setOnClickListener(this);
        buttonstop.setOnClickListener(this);
        buttonrestart.setOnClickListener(this);
        buttoncancel.setOnClickListener(this);



        chronometer1.setOnChronometerTickListener(this);




        chronometer1.setText(stringForTime(time));
        chronometer.setOnChronometerTickListener(this);
       // chronometer1.setOnChronometerTickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_start:
                chronometer.start();


                break;

            case R.id.bt_stop:

                chronometer.stop();

                break;

            case R.id.bt_restart:
                chronometer.setBase(SystemClock.elapsedRealtime());


                break;

            case R.id.bt_clean:
                chronometer.setFormat("T:%s");

                break;


        }

    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {




                if (time!=0){
                    time=time-1000;
                    Log.e(TAG, "onChronometerTick: "+stringForTime(time) );




                    chronometer1.setText(stringForTime(time));
                }else {
                    chronometer1.stop();
                    chronometer1.setText("00:00");

                    Toast.makeText(this,"时间到！！！",Toast.LENGTH_SHORT).show();
                }

    }


    public String stringForTime(int timeMs){

        int totalSeconds = timeMs/1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds/60)%60;
        int hours = totalSeconds/3600;
        stringBuilder.setLength(0);
        if(hours>0){
            return formatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        } else {
            return formatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }
}
