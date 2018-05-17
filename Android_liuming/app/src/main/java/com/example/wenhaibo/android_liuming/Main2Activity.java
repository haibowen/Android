package com.example.wenhaibo.android_liuming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity  implements View.OnClickListener{
    private Button button;
    private TextView textView;
    private TextView textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        textView=findViewById( R.id.te_zhanghao );
        textView1=findViewById( R.id.te_password);

        button=findViewById( R.id.bt_next );

        button.setOnClickListener( this );


        Intent intent=getIntent();

        String receive_zhanghao=intent.getStringExtra( "zhanghao" );
        String receive_password= intent.getStringExtra( "password" );

        textView.setText( receive_zhanghao );
       textView1.setText( receive_password );






    }

    @Override
    public void onClick(View v) {

        Intent intent=new Intent( Main2Activity.this, Main3Activity.class);
        startActivityForResult( intent,1 );



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1){
            if(resultCode==1){
                String receive=data.getStringExtra( "a" );
                Toast.makeText( Main2Activity.this, receive,Toast.LENGTH_SHORT).show();

            }
        }
        super.onActivityResult( requestCode, resultCode, data );
    }
}
