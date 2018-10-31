package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 常用地址
 * Created by HuangYuan on 2016/12/22.
 */

public class CommonlyUsedAddressAct extends MFBaseActivity {

    private CommonlyUsedAddressFrg commonlyUsedAddressFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        commonlyUsedAddressFrg = new CommonlyUsedAddressFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, commonlyUsedAddressFrg);
        transaction.commitAllowingStateLoss();
    }
}
