package com.example.administrator.mywifip2p.activity;

import android.app.ProgressDialog;
import android.content.*;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import com.example.administrator.mywifip2p.R;
import com.example.administrator.mywifip2p.broadcast.DirectBroadReceive;
import com.example.administrator.mywifip2p.callback.DirectActionListener;
import com.example.administrator.mywifip2p.common.MessageDialog;
import com.example.administrator.mywifip2p.filemode.FileTransfer;
import com.example.administrator.mywifip2p.service.WifiService;

import java.io.File;
import java.util.Collection;
import java.util.Locale;

public class ReceiveActivity extends BaseActivity implements DirectActionListener {

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private WifiService wifiService;
    private ProgressDialog progressDialog;
    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WifiService.MyBinder binder= (WifiService.MyBinder) service;

            wifiService=binder.getservice();
            wifiService.setOnProgressChangelistener(progressChangelistener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            wifiService=null;
            bindService();


        }
    };

    private WifiService.OnProgressChangelistener progressChangelistener=new WifiService.OnProgressChangelistener() {
        @Override
        public void onProgressChanged(final FileTransfer fileTransfer, final int progress) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setMessage("文件名"+new File(fileTransfer.getFilepath()).getName());


                    progressDialog.setProgress(progress);

                    progressDialog.show();
                }
            });
        }

        @Override
        public void OnTransferFinished(final File file) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();

                    if (file!=null&&file.exists()){

                        openFile(file.getPath());
                    }
                }
            });

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        wifiP2pManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel=wifiP2pManager.initialize(this,getMainLooper(),this);

        broadcastReceiver=new DirectBroadReceive(wifiP2pManager,channel,this);
        registerReceiver(broadcastReceiver,DirectBroadReceive.getIntentFilter());
        bindService();
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("正在发送");
        progressDialog.setMax(100);








    }


    @Override
    public void onBackPressed() {

        final MessageDialog messageDialog=new MessageDialog();
        messageDialog.show(null, "退出当前界面，文件传输将终止", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                messageDialog.dismiss();
                if (which==DialogInterface.BUTTON_POSITIVE){

                    ReceiveActivity.super.onBackPressed();
                }
            }
        },getSupportFragmentManager());




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiService!=null){

            wifiService.setOnProgressChangelistener(null);
            unbindService(serviceConnection);


        }

        unregisterReceiver(broadcastReceiver);

        removeGroup();
        stopService(new Intent(this,WifiService.class));


    }

    @Override
    public void wifiP2pEnabled(boolean enabled) {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
            if (wifiService != null) {
                startService(new Intent(this, WifiService.class));
            }
        }
    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void onSelfDeviceAviable(WifiP2pDevice wifiP2pDevice) {
        Log.e("123456", "onSelfDeviceAviable: "+wifiP2pDevice.deviceName );

    }

    @Override
    public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceslist) {

    }

    @Override
    public void onChannelDisconnected() {

    }

    public void  createGroup(View view){

        showLoadingDialog("正在创建群组");
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                dismissLoadingDialog();
               showToast("sucess");

            }

            @Override
            public void onFailure(int reason) {
                dismissLoadingDialog();
                showToast("failure");

            }
        });


    }
    public  void  removeGroup(View view){

        removeGroup();


    }



    public void bindService() {
        Intent intent = new Intent(ReceiveActivity.this, WifiService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void  removeGroup(){

        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                showToast("sucess");
            }

            @Override
            public void onFailure(int reason) {

                showToast("failure");

            }
        });
    }

    private void openFile(String filepath){

        String ext=filepath.substring(filepath.lastIndexOf('.')).toLowerCase(Locale.US);

        try {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mime = mimeTypeMap.getMimeTypeFromExtension(ext.substring(1));
            mime = TextUtils.isEmpty(mime) ? "" : mime;
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filepath)), mime);
            startActivity(intent);
        } catch (Exception e) {

            showToast("文件打开异常：" + e.getMessage());
        }
    }
}
