package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * Created by XIXIHAHA on 2016/12/12.
 * 用户中心
 */

public class UserCenterAct extends MFBaseActivity {
    private UserCenterFrg userCenterFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        userCenterFrg = new UserCenterFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, userCenterFrg);
        transaction.commitAllowingStateLoss();
    }
}
