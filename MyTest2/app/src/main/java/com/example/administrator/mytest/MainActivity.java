package com.example.administrator.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="222222222" ;
    private String a="adc";

    private Button button,button1,button2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_show1);
        button1=findViewById(R.id.bt_show2);
        button2=findViewById(R.id.bt_show3);


        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_show1:
                a+="/"+"222";

                Log.e(TAG, "onClick: "+a );

                break;


            case R.id.bt_show2:

                a+="/"+"333";
                Log.e(TAG, "onClick: "+a );
                break;

            case R.id.bt_show3:

                a+="/"+"444";
                Log.e(TAG, "onClick: "+a );
                break;
        }

    }
}
