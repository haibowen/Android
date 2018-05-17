package com.example.wenhaibo.androidstudy_sqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button=findViewById( R.id.bt_sqlite );

        button.setOnClickListener( this );


    }

    @Override
    public void onClick(View v) {
        MyDBHelper myDBHelper=new MyDBHelper( this,"BookStore.db",null,3 );

        myDBHelper.getWritableDatabase();




    }
}
