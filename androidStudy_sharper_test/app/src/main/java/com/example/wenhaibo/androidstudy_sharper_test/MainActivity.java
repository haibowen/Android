package com.example.wenhaibo.androidstudy_sharper_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText,editText1,editText2,editText3;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        init();




    }

    @Override
    public void onClick(View v) {
        init();

        SharedPreferences.Editor editor=getSharedPreferences( "data" ,MODE_PRIVATE).edit();
        editor.putString( "name",editText.getText().toString() );
        editor.putString( "compare",editText1.getText().toString() );
        editor.putString( "phone",editText2.getText().toString() );
        editor.putString( "email",editText3.getText().toString() );
        editor.apply();
        Intent intent=new Intent( MainActivity.this,Main2Activity.class );
        startActivity( intent );


    }
    void init(){
        editText=findViewById( R.id.et_name );
        editText1=findViewById( R.id.et_compare );
        editText2=findViewById( R.id.et_phone );
        editText3=findViewById( R.id.et_email );

        button=findViewById( R.id.bt_save );

        button.setOnClickListener( this );

    }
}
