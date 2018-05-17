package com.example.wenhaibo.androidstudy02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    private TextView textView;
    private EditText editText;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        textView = findViewById( R.id.tv5 );
        editText=findViewById( R.id.tv4 );
        button=findViewById( R.id.jisuan );
        final Intent intent = getIntent();
        String a = intent.getStringExtra( "a" );
        String b = intent.getStringExtra( "b" );
        String c = intent.getStringExtra( "c" );
        textView.setText( a +"+"+b+"="+"?");
        //setResult( 1,intent );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(  );
                String d= editText.getText().toString();
                intent1.putExtra( "result",d );
                setResult( RESULT_OK,intent1 );
                finish();
            }
        } );

    }


}
