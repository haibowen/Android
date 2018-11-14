package com.example.wenhaibo.recevier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by wj on 2017/11/20.
 */
public class IndexActivity extends Activity {
    private Button btnMainActivity,btnSecondActivity,btnActivityLifeCycle,btnCaptureActivity,btnBroadcastActivity;
    private ButtonListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定布局文件
        setContentView(R.layout.activity_index);

        listener = new ButtonListener();

        FindView();

        SetClickListner();
    }


    private void FindView() {
        btnMainActivity = (Button) findViewById(R.id.btnMainActivity);
        btnSecondActivity = (Button) findViewById(R.id.btnSecondActivity);
        btnActivityLifeCycle = (Button) findViewById(R.id.btnActivityLifeCycle);
        btnCaptureActivity = (Button)findViewById(R.id.btnCaptureActivity);
        btnBroadcastActivity=(Button)findViewById(R.id.btnBroadcastActivity);


    }

    private void SetClickListner() {
        btnMainActivity.setOnClickListener(listener);
        btnSecondActivity.setOnClickListener(listener);
        btnActivityLifeCycle.setOnClickListener(listener);
       btnCaptureActivity.setOnClickListener(listener);
        btnBroadcastActivity.setOnClickListener(listener);

    }

    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnMainActivity:
                    //功能
                    Intent i1 =new Intent(IndexActivity.this,MainActivity.class);
                    startActivity(i1);
                    break;
                case R.id.btnSecondActivity:
                    Intent i2 =new Intent(IndexActivity.this,SecondActivity.class);
                    startActivity(i2);
                    break;
                case R.id.btnActivityLifeCycle:
                    Intent i3 = new Intent(IndexActivity.this,ActivityLifeCycle.class);
                    startActivity(i3);
                    break;
                case R.id.btnCaptureActivity:
                    Intent i4 = new Intent(IndexActivity.this,CaptureActivity.class);
                    startActivity(i4);
                    break;
                case R.id.btnBroadcastActivity:
                    Intent i5 = new Intent(IndexActivity.this,SendBroadcastActivity.class);
                    startActivity(i5);
                    break;
            }
        }
    }

}
