package com.example.wenhaibo.vister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.wenhaibo.olympic.R;

public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY,0000);
    }
    private static final int GOTO_MAIN_ACTIVITY=0;
    private Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case GOTO_MAIN_ACTIVITY:
                    Intent intent=new Intent();
                    intent.setClass(WelcomeActivity.this,SingupActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }

    };
    void InitUI(){

        welcome=(LinearLayout)findViewById(R.id.welcome);
        @SuppressLint("ResourceType") Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.drawable.scale);
        welcome.startAnimation(scaleAnimation);
    }

}
