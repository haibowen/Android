package com.example.administrator.mylibtest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventListener<String> {
    TextView    StateText;
    TextView    RecvText;
    EditText    SendText;
    Button      ScanButton;
    Button      SendButton;

    public  String Meshname;
    public  String Meshpassword;
    public final String factoryName = "AT-mesh";//"lumenus";
    public final String factoryPassword = "123456";//"0000";

    private static final String TAG = "MainActivity-Info";
    private TelinkLightApplication mApplication;
    private static final int UPDATE_ST = 0;
    private static final int UPDATE_LIST = 1;

    public   ArrayList<String>recvData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StateText = (TextView) findViewById(R.id.StateView);
        RecvText = (TextView) findViewById(R.id.RevTextView);
        SendText = (EditText) findViewById(R.id.EditSend);

       // RecvText.setMovementMethod(ScrollingMovementMethod.getInstance());

        ScanButton = (Button) findViewById(R.id.button_scan);
        SendButton = (Button) findViewById(R.id.button_send);
        ScanButton.setOnClickListener(BtListen);
        SendButton.setOnClickListener(BtListen);

       this.mApplication = (TelinkLightApplication) this.getApplication();
       this.mApplication.doInit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        int result = BuildUtils.assetSdkVersion("4.4");
        Log.d(TAG, " Version : " + result);
        this.mApplication.addEventListener(DeviceEvent.STATUS_CHANGED, this);
        this.mApplication.addEventListener(NotificationEvent.ONLINE_STATUS, this);
        this.mApplication.addEventListener(ServiceEvent.SERVICE_CONNECTED, this);
        this.mApplication.addEventListener(MeshEvent.OFFLINE, this);
        this.mApplication.addEventListener(MeshEvent.ERROR, this);
        this.mApplication.addEventListener(NotificationEvent.GET_USER_ALL_DATA, this);
    }
    private  int count;
    private void testOnOff(){
        byte[] params = new byte[]{0x00, 0x00, 0x00};

        if (count>10000)
            count = 0 ;
        count++;
        if (count%2==0){ //全开
            byte opcode = (byte) 0xD0;
            int address = 0xFFFF;
            params[0] =0x01 ;
            TelinkLightService.Instance().sendCommand(opcode, address, params);
        }else if (count%2==1){ //全关
            byte opcode = (byte) 0xD0;
            int address = 0xFFFF;
            TelinkLightService.Instance().sendCommand(opcode, address, params);
        }
        SendText.setText("Sending---->"+ Arrays.bytesToHexString(params, ","));
    }

    private void sendData2Dev(byte[] data){
        byte[] params = data;


        byte opcode = (byte) 0xD0;
        int address = 0xFFFF;

        TelinkLightService.Instance().sendCommand(opcode, address, params);
    }

    public View.OnClickListener BtListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == SendButton){
                //testOnOff();
                if(SendText.getText().length() > 10){
                    return;
                }else {
                    sendData2Dev (SendText.getText().toString().getBytes()) ;
                }
            }
            else if(v == ScanButton){
                StateText.setText("Connected.....");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (!LeBluetooth.getInstance().isSupport(this)) {
            this.finish();
        }

        if (!LeBluetooth.getInstance().isEnabled()) {
            new AlertDialog.Builder(this).setMessage("开启蓝牙，体验智能灯!").show();
        }

        DeviceInfo deviceInfo = this.mApplication.getConnectDevice();

        if (deviceInfo != null) {
            this.connectMeshAddress = this.mApplication.getConnectDevice().meshAddress & 0xFF;
        }

        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        this.mApplication.removeEventListener(this);
        TelinkLightService.Instance().autoRefreshNotify(false, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        this.mApplication.doDestroy();
    }


    private int connectMeshAddress;
    private Handler mHandler = new Handler() {
        String tempbuf=null;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_LIST:
                    NotificationInfo info = (NotificationInfo) msg.obj;
                   // RecvText.setText(RecvText.getText(), TextView.BufferType.EDITABLE);
                    //RecvText.setText(Arrays.bytesToHexString(info.params, ", "));
                    RecvText.append(Arrays.bytesToHexString(info.params, ", ")+"\n");
                    // RecvText.setText(Arrays.bytesToHexString(info.params),"," );
                    if(RecvText.getLineCount() >15)
                    {
                        RecvText.setText("");
                    }
                    break;

                case UPDATE_ST:
                   StateText.setText(" Login success" );
                    break;
            }
        }
    };

    private void AutoConnect()
    {
        System.out.println("---------------TelinkLightService.Instance()"+TelinkLightService.Instance());
        if (TelinkLightService.Instance() != null) {

            if (TelinkLightService.Instance().getMode() != LightAdapter.MODE_AUTO_CONNECT_MESH) {

                LeAutoConnectParameters connectParams = Parameters.createAutoConnectParameters();
                connectParams.setMeshName(factoryName);
                connectParams.setPassword(factoryPassword);
                connectParams.autoEnableNotification(true);
                TelinkLightService.Instance().autoConnect(connectParams);
            }

            LeRefreshNotifyParameters refreshNotifyParams = Parameters.createRefreshNotifyParameters();
            refreshNotifyParams.setRefreshRepeatCount(2);
            refreshNotifyParams.setRefreshInterval(2000);

            TelinkLightService.Instance().autoRefreshNotify(true, refreshNotifyParams);
        }
    }

    private void onOnlineStatusNotify(NotificationEvent event) {

        TelinkLog.d("Thread ID+++++ : " + Thread.currentThread().getId());
        List<OnlineStatusNotificationParser.DeviceNotificationInfo> notificationInfoList;
        //noinspection unchecked
        notificationInfoList = (List<OnlineStatusNotificationParser.DeviceNotificationInfo>) event.parse();

        if (notificationInfoList == null || notificationInfoList.size() <= 0)
            return;

        for (OnlineStatusNotificationParser.DeviceNotificationInfo notificationInfo : notificationInfoList) {

            int meshAddress = notificationInfo.meshAddress;
            int brightness = notificationInfo.brightness;
        }
    }

    private void onDeviceStatusChanged(DeviceEvent event) {

        DeviceInfo deviceInfo = event.getArgs();

        switch (deviceInfo.status) {
            case LightAdapter.STATUS_LOGIN:
               // this.connectMeshAddress = this.mApplication.getConnectDevice().meshAddress;
                //this.show("login success");
                mHandler.obtainMessage(UPDATE_ST).sendToTarget();
                break;
            case LightAdapter.STATUS_CONNECTING:
               // this.show("login");
                break;
            case LightAdapter.STATUS_LOGOUT:
               // this.show("disconnect");
                break;
            default:
                break;
        }
    }

    private void onMeshOffline(MeshEvent event) {

//        List<Light> lights = Lights.getInstance().get();
//        for (Light light : lights) {
//            light.status = ConnectionStatus.OFFLINE;
//            light.updateIcon();
//        }
//        this.deviceFragment.notifyDataSetChanged();
    }

    private void onMeshError(MeshEvent event) {
        new AlertDialog.Builder(this).setMessage("蓝牙出问题了，重启蓝牙试试!!").show();
    }

    private void onServiceConnected(ServiceEvent event) {
        this.AutoConnect();
    }

    private void onServiceDisconnected(ServiceEvent event) {

    }

    private void userAllNotify(NotificationEvent event) {
        Message message;

        message = mHandler.obtainMessage();//性能优化后
        message.what=UPDATE_LIST;
        message.obj = event.getArgs();
        mHandler.sendMessage(message); //发送消息
    }

    @Override
    public void performed(Event<String> event) {
        switch (event.getType()) {
            case NotificationEvent.ONLINE_STATUS:
                this.onOnlineStatusNotify((NotificationEvent) event);
                break;
            case DeviceEvent.STATUS_CHANGED:
                this.onDeviceStatusChanged((DeviceEvent) event);
                break;
            case MeshEvent.OFFLINE:
                this.onMeshOffline((MeshEvent) event);
                break;
            case MeshEvent.ERROR:
                this.onMeshError((MeshEvent) event);
                break;
            case ServiceEvent.SERVICE_CONNECTED:
                this.onServiceConnected((ServiceEvent) event);
                break;
            case ServiceEvent.SERVICE_DISCONNECTED:
                this.onServiceDisconnected((ServiceEvent) event);
                break;
            case NotificationEvent.GET_USER_ALL_DATA:
                this.userAllNotify((NotificationEvent) event);
                break;
        }
    }

}
