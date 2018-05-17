package com.example.wenhaibo.androidstudy_broadcast02;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button=findViewById( R.id.bt_send );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent( "com.example.wenhaibo.androidstudy_broadcast02.MY_BROADCAST" );
                intent.setComponent( new ComponentName( "com.example.wenhaibo.androidstudy_broadcast02" ,
                        "com.example.wenhaibo.androidstudy_broadcast02.MyBroadCastReceiver") );
                sendBroadcast( intent );
            }
        } );
    }
}
