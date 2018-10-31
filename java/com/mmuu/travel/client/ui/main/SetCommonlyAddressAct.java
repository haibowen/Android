package com.mmuu.travel.client.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 设置常用地址
 * Created by HuangYuan on 2016/12/22.
 */

public class SetCommonlyAddressAct extends MFBaseActivity {


    private SetCommonlyAddressFrg setCommonlyAddressFrg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mf_base_act);
        setCommonlyAddressFrg = new SetCommonlyAddressFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, setCommonlyAddressFrg);
        transaction.commitAllowingStateLoss();
    }


}
