package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 费用明细
 * Created by HuangYuan on 2016/12/21.
 */

public class CostDetailAct extends MFBaseActivity {
    private CostDetailFrg costDetailFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        costDetailFrg = new CostDetailFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, costDetailFrg);
        transaction.commitAllowingStateLoss();
    }
}
