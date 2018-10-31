package com.mmuu.travel.client.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by XIXIHAHA on 2017/2/16.
 */

public class SensorHandle {

    private Context mContext;
    private SensorEventListener sensorEventListener;

    private SensorManager mSM;
    private Sensor mSensor;

    public SensorHandle(Context mContext, SensorEventListener sensorEventListener) {
        this.mContext = mContext;
        this.sensorEventListener = sensorEventListener;
        initSensor();
    }

    private void initSensor() {

        mSM = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSM != null) {
            mSensor = mSM.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
    }

    public void onResume() {
        if (mSensor != null) {
            mSM.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);//注册回调函数
        }
    }

    public void onPause() {
        mSM.unregisterListener(sensorEventListener);
    }

}
