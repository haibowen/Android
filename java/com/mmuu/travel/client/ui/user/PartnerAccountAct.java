package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 采蜜人账户
 */

public class PartnerAccountAct extends MFBaseActivity {

    private PartnerAccountFrg partnerAccountFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        partnerAccountFrg = new PartnerAccountFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, partnerAccountFrg);
        transaction.commitAllowingStateLoss();

    }
}
