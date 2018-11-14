package com.example.wenhaibo.recevier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by wj on 2017/11/21.
 */
public class ActivityLifeCycle extends Activity {

    private EditText etMessageBox;
    private Button btnSendMessage;
    private int count = 0;

    private  static final int RC_DATA_ACTIVITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("count");
            Toast.makeText(this, "count =" + count, Toast.LENGTH_SHORT).show();

        }


        btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        etMessageBox = (EditText) findViewById(R.id.etInputMessageBox);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strMsg =  etMessageBox.getText().toString();
                Intent intent = new Intent(ActivityLifeCycle.this,DataActivity.class);
                intent.putExtra("joke",strMsg);
                startActivityForResult(intent,RC_DATA_ACTIVITY);

            }
        });
        Log.i("ActivityLifeCycle", "onCreate执行完毕！");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.w("ActivityLifeCycle", "onPause被执行");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ActivityLifeCycle", "onStart被执行");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ActivityLifeCycle", "onRestart被执行");
    }

    @Override
    protected void onResume() {
        super.onResume();
        count++;
        Log.e("ActivityLifeCycle", "onResume被执行");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf("ActivityLifeCycle", "onStop被执行");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ActivityLifeCycle", "onDestory被执行");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理逻辑
        switch (requestCode) {
            case RC_DATA_ACTIVITY:
                if(requestCode ==RESULT_OK) {
                    String strGetMsg = getIntent().getStringExtra("joke");
                    String strResult = data.getStringExtra("ma");
                    Toast.makeText(ActivityLifeCycle.this,strResult,Toast.LENGTH_SHORT).show();
                    Log.e("123", "456");
                }else if(resultCode == RESULT_CANCELED) {
                    Toast.makeText(ActivityLifeCycle.this,"未能正确获取返回结果",Toast.LENGTH_SHORT).show();
                    Log.e("789", "456");
                }
                break;
            default:
                break;
        }
    }
}
