package com.example.administrator.myuithrea;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private TextView textView;
    private static  final  int UPDATE_TEXT=1;
    private Handler handler=new Handler(){
        public  void handleMessage(Message message){
            switch ( message.what){
                case UPDATE_TEXT:
                    Log.d("wenhaibo", "handleMessage: "+message);
                    textView.setText("skr,skr");

            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //绑定id
        button=findViewById(R.id.bt_change2);
        textView=findViewById(R.id.tx_show2);

        //注册事件
        button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_change2:
                Log.d("wenhaibo", "onClick: woshi haibo");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //textView.setText("skr skr");
                        //发送message
                        Message message=new Message();
                        message.what=UPDATE_TEXT;
                        handler.sendMessage(message);
                        Log.d("wenhaibo", "run: "+message);

                    }
                }).start();

                break;
                default:
                    break;
        }

    }

}
