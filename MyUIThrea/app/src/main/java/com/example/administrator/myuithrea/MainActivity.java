package com.example.administrator.myuithrea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button ,button1;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定id
        button=findViewById(R.id.bt_change);
        button1=findViewById(R.id.bt_next);
        textView=findViewById(R.id.tx_show);


        //注册事件
        button.setOnClickListener(this);
        button1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_change:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("skr skr");

                    }
                }).start();

                break;

            case R.id.bt_next:
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }

    }
}
