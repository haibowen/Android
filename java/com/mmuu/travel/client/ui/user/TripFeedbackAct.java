package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 行程反馈列表
 */

public class TripFeedbackAct extends MFBaseActivity {

    private TripFeedbackFrg tripFeedbackFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        tripFeedbackFrg = new TripFeedbackFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, tripFeedbackFrg);
        transaction.commitAllowingStateLoss();
    }
}
