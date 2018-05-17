package com.example.wenhaibo.androidstudy_litepal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button, button1, button2, button3, button4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        button = findViewById( R.id.bt_create );
        button1 = findViewById( R.id.bt_insert );
        button2 = findViewById( R.id.bt_delete );
        button3 = findViewById( R.id.bt_update );
        button4 = findViewById( R.id.bt_select );

        button.setOnClickListener( this );
        button1.setOnClickListener( this );
        button2.setOnClickListener( this );
        button3.setOnClickListener( this );
        button4.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_create:

                LitePal.getDatabase();


                break;
            case R.id.bt_insert:
                break;
            case R.id.bt_delete:
                break;
            case R.id.bt_update:
                break;
            case R.id.bt_select:
                break;
            default:
                break;

        }

    }
}
