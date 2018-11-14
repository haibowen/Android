package com.example.wenhaibo.recevier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendBroadcastActivity extends AppCompatActivity {

    private Button btnSendBroadcast;
    private EditText etSendBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_broadcast);

        etSendBroadcast = (EditText) findViewById(R.id.etSendBroadcast);
        btnSendBroadcast = (Button) findViewById(R.id.btnSendBroadcast);

        btnSendBroadcast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //如何发送广播
                String strMsg = etSendBroadcast.getText().toString();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.action.NOTIFY");
                broadcastIntent.putExtra("msg",strMsg);
                sendBroadcast(broadcastIntent);

            }
        });
    }
}
