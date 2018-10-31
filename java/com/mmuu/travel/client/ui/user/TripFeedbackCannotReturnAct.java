package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 无法结束订单反馈页面
 * Created by XIXIHAHA on 2017/7/31.
 */

public class TripFeedbackCannotReturnAct extends MFBaseActivity {

    private TripFeedbackCannotReturnFrg tripFeedbackFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        tripFeedbackFrg = new TripFeedbackCannotReturnFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, tripFeedbackFrg);
        transaction.commitAllowingStateLoss();
    }
}
