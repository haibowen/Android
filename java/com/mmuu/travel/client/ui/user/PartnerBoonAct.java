package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 采蜜人福利Act
 */

public class PartnerBoonAct extends MFBaseActivity {
    private PartnerBoonFrg partnerBoonFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        partnerBoonFrg = new PartnerBoonFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, partnerBoonFrg);
        transaction.commitAllowingStateLoss();
    }
}
