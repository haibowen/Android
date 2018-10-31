package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 登录手机号输入界面
 * Created by HuangYuan on 2016/12/15.
 */

public class LoginAct extends MFBaseActivity {

    private LoginFrg loginFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_up, R.anim.activity_push_no_anim);
        setContentView(R.layout.mf_base_act);
        loginFrg = new LoginFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, loginFrg);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_push_no_anim, R.anim.push_up_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN && loginFrg != null) {
            loginFrg.onKeyDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
