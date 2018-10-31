package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * 出行券
 * Created by HuangYuan on 2016/12/13.
 */

public class MyVoucherAct extends MFBaseActivity {
    private MyVoucherFrg myVoucherFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        myVoucherFrg = new MyVoucherFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, myVoucherFrg);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return myVoucherFrg != null ? myVoucherFrg.dispatchTouchEvent(ev) : false;
    }
}
