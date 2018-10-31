package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * Created by XIXIHAHA on 2017/8/30.
 * <p>
 * 采蜜人提现--支付宝
 */

public class PartnerWithdrawAct extends MFBaseActivity {

    private PartnerWithdrawFrg partnerWithdrawFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        partnerWithdrawFrg = new PartnerWithdrawFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, partnerWithdrawFrg);
        transaction.commitAllowingStateLoss();

    }


}
