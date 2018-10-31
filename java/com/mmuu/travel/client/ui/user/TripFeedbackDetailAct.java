package com.mmuu.travel.client.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 行程反馈、故障上报、车损上报
 * Created by HuangYuan on 2016/12/19.
 */

public class TripFeedbackDetailAct extends MFBaseActivity {
    private TripFeedbackDetailFrg tripFeedbackDetailFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        tripFeedbackDetailFrg = new TripFeedbackDetailFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, tripFeedbackDetailFrg);
        transaction.commitAllowingStateLoss();
    }
}
