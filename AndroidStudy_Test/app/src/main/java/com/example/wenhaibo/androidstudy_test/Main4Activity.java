package com.example.wenhaibo.androidstudy_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main4 );

        button=findViewById( R.id.bt_back );

        textView=findViewById( R.id.tv_book );
        imageView=findViewById( R.id.im_show );

        final  Intent intent=getIntent();
        String a=intent.getStringExtra( "JAVA" );
        textView.setText( a );


//        textView.setText( "JAVA" );
//        imageView.setImageResource( R.drawable.a );






        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(  );
                String d= "已经加入购物车";
                intent1.putExtra( "result",d );
                setResult( RESULT_OK,intent1 );
                finish();


            }
        } );

    }


}
