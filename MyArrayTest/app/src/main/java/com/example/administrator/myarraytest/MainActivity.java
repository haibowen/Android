package com.example.administrator.myarraytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static int a = 1;
    private static int[] ab = {1, 2, 3};
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_show);

        button.setOnClickListener(this);


        //wenhaibo modify the datasource [start]
        if (a == 1) {

            a = 2;
            ab= new int[]{2, 4, 5,6};
            a=ab.length;


        }
    }


    @Override
    public void onClick(View v) {

        Toast.makeText(this, ""+a+"    "+ Arrays.toString(ab), Toast.LENGTH_SHORT).show();

    }
}