package com.example.wenhaibo.androidstudy_sharper_test;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {
    private EditText editText ,editText1,editText2,editText3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        editText=findViewById( R.id.et_name_show );
        editText1=findViewById( R.id.et_gongsi_show );
        editText2=findViewById( R.id.et_phone_show );
        editText3=findViewById( R.id.et_email_show );

        SharedPreferences sharedPreferences=getSharedPreferences( "data",MODE_PRIVATE );
        editText.setText( sharedPreferences.getString( "name","" ) );
        editText1.setText( sharedPreferences.getString( "compare","" ) );
        editText2.setText( sharedPreferences.getString( "phone","" ) );
        editText3.setText( sharedPreferences.getString( "email","" ) );


    }
}
