package com.mmuu.travel.client.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.mmuu.travel.client.tools.InitSystemBarColorUtil;
import com.umeng.analytics.MobclickAgent;

import android.view.KeyEvent;

public class MFBaseActivity extends FragmentActivity {
    public Handler baseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MFApp.getInstance().addActivity(this);
        InitSystemBarColorUtil.initSystemBar(this);//初始化状态栏
    }

   
}
