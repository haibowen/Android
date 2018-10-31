package com.example.administrator.myservicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    private Button button,button2 ,button3,button4,button5;
    private MyService.DownloadBinder mybinder;
    private Boolean isbound=false;
    private ServiceConnection connection=new ServiceConnection(){


        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            mybinder=( MyService.DownloadBinder)iBinder;
            mybinder.startDowload();
            mybinder.getprogress();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定id
        button=findViewById(R.id.b_start);
        button2=findViewById(R.id.b_stop);
        button3=findViewById(R.id.b_startbinder);
        button4=findViewById(R.id.b_stopbinder);
        button5=findViewById(R.id.b_intentservice);
        //注册点击事件
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.b_start:
                Intent intent=new Intent(MainActivity.this,MyService.class);
                startService(intent);
                break;
            case  R.id.b_stop:
                Intent intent1=new Intent(MainActivity.this,MyService.class);
                stopService(intent1);
                break;
            case R.id.b_startbinder:
                Intent intent2=new Intent(MainActivity.this,MyService.class);
               isbound= bindService(intent2,connection,BIND_AUTO_CREATE);
                break;
            case  R.id.b_stopbinder:
                if(isbound){
                    unbindService(connection);
                    isbound=false;
                }


                break;
            case R.id.b_intentservice:
                Log.d("666", "onClick: "+Thread.currentThread().getId());
                Intent intent3=new Intent(MainActivity.this,MyIntentService.class);
                startService(intent3);

            default:
                break;
        }

    }
}
