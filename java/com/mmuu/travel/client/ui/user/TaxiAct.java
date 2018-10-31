package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 登录
 * Created by HuangYuan on 2016/12/15.
 */

public class TaxiAct extends MFBaseActivity {

    private TaxiFrg taxiFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        taxiFrg = new TaxiFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, taxiFrg);
        transaction.commitAllowingStateLoss();
    }
}
