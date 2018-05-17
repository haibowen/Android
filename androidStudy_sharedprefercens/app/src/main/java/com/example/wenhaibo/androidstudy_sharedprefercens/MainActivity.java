package com.example.wenhaibo.androidstudy_sharedprefercens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private  Button button1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button=findViewById( R.id.bt_share );
        button1=findViewById( R.id.bt_select );

        button.setOnClickListener( this );
        button1.setOnClickListener( this );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_share:
                SharedPreferences.Editor editor= getSharedPreferences( "data",MODE_PRIVATE ).edit();
                editor.putString( "name","haibo" );
                editor.putBoolean( "cool",true );
                editor.putInt( "age",20);
                editor.apply();

                break;
            case R.id.bt_select:
                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE );
                String name=sharedPreferences.getString( "name","" );
                boolean sex=sharedPreferences.getBoolean( "cool",true );
                int age=sharedPreferences.getInt( "age", 0);
                Log.e( "haibo666666666",name+" "+sex+" "+age  );



                break;
                default:
                    break;

        }



    }
}
