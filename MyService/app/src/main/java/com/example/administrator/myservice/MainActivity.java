package com.example.administrator.myservice;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button,button1,button2,button3,button4;
    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(MyService.DownloadBinder)service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_start);
        button1=findViewById(R.id.bt_stop);
        button2=findViewById(R.id.bt_bindservice);
        button3=findViewById(R.id.bt_unbindservice);
        button4=findViewById(R.id.bt_intentservice);

        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_start:
                Intent intent=new Intent(this,MyService.class);
                startService(intent);
                break;
            case R.id.bt_stop:
                Intent intent1=new Intent(this,MyService.class);
                stopService(intent1);

                break;
            case R.id.bt_bindservice:
                Intent intent2 =new Intent(this,MyService.class);
                bindService(intent2,connection,BIND_AUTO_CREATE);//绑定服务
                break;
            case R.id.bt_unbindservice:
                unbindService(connection);//解绑服务
                break;
            case  R.id.bt_intentservice:
                Log.d("888", "onClick: "+Thread.currentThread().getId());
                Intent intent3=new Intent(this,MyIntentService.class);
                startService(intent3);
                break;

                default:
                    break;
        }

    }
}
