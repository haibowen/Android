package com.example.administrator.mybatterytest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final String STATUS = "/sys/class/power_supply/battery/status";

    boolean ret = false;
    String tmp = null;
    float voltage = 0;
    String result = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mybroadcast, ifilter);


    }


    private BroadcastReceiver mybroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;


            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            if (isCharging) {
                if (usbCharge) {
                    Toast.makeText(MainActivity.this, "手机正处于USB连接！", Toast.LENGTH_SHORT).show();
                } else if (acCharge) {
                    Toast.makeText(MainActivity.this, "手机通过电源充电中！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "手机未连接USB线！", Toast.LENGTH_SHORT).show();
            }








            /**
             *  Bundle extra = intent.getExtras();
            int status = extra.getInt(BatteryManager.EXTRA_STATUS);


            switch (status) {
               // case BatteryManager.BATTERY_STATUS_CHARGING://充电


                   // break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING://放电
                    Toast.makeText(MainActivity.this,"手机正在放电",Toast.LENGTH_SHORT).show();
                    break;

                case BatteryManager.BATTERY_STATUS_FULL://充满
                    Toast.makeText(MainActivity.this,"手机已充满",Toast.LENGTH_SHORT).show();
                    break;

                case BatteryManager.BATTERY_PLUGGED_AC://电源充电
                    Toast.makeText(MainActivity.this,"手机正在用电源充电",Toast.LENGTH_SHORT).show();
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB://usb充电
                    Toast.makeText(MainActivity.this,"手机正在用usb充电",Toast.LENGTH_SHORT).show();

                    break;



            }
            **/


        }
    };
}
