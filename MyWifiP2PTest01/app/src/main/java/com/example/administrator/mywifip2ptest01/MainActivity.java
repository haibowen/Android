package com.example.administrator.mywifip2ptest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DirectActionListener {


    private Button button;
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pDevice mWifiP2pDevice;
    private BroadcastReceiver broadcastReceiver;
    private boolean mWifiP2pEnabled = false;
    private List<WifiP2pDevice> mWifiP2pDeviceList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(this, getMainLooper(), this);
        broadcastReceiver = new DirectBroadCastReceiver(mWifiP2pManager, mChannel, (DirectActionListener) this);
        registerReceiver(broadcastReceiver, DirectBroadCastReceiver.getIntentFilter());
        //如果需要P2P设备列表的话，可以在这里初始化一个集合
        mWifiP2pDeviceList = new ArrayList<>();

        /**
         *
         *

        button=findViewById(R.id.bt_discover);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {


                    }
                });
            }
        });
         */

    }

    @Override
    public void wifiP2pEnabled(boolean enabled) {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {

        Log.e("wenhaibo", "onSelfDeviceAvailable: "+wifiP2pDevice.deviceName );
        Log.e("wenhaibo", "onSelfDeviceAvailable: "+wifiP2pDevice.deviceAddress );

        Log.e("wenhaibo", "onSelfDeviceAvailable: "+wifiP2pDevice.status);
    }

    @Override
    public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
        Log.e("0000", "onPeersAvailable: "+wifiP2pDeviceList.size() );
        Log.e("0000", "onPeersAvailable: "+wifiP2pDeviceList.toString() );

    }

    @Override
    public void onChannelDisconnected() {

    }
}
