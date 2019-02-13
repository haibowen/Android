package com.example.administrator.myuitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewgroup ,textViewnum,textViewstatus ,textViewaddress,textViewlinew;
    private int a=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewnum=findViewById(R.id.phone_num);
        textViewgroup=findViewById(R.id.group_name);
        textViewstatus=findViewById(R.id.intercomState);
        textViewaddress=findViewById(R.id.groups);
        textViewlinew=findViewById(R.id.userInfo);
        if (a==3){
            textViewnum.setVisibility(View.GONE);
            textViewgroup.setVisibility(View.GONE);

        }else {

        }
    }
}
