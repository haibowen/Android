package com.mmuu.travel.client.ui.other;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * Created by XIXIHAHA on 2017/2/8.
 */

public class CreateOrderAct extends MFBaseActivity {


    private CreateOrderFrg createOrderFrg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        createOrderFrg = new CreateOrderFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, createOrderFrg);
        transaction.commitAllowingStateLoss();
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (createOrderFrg != null && !createOrderFrg.isCanBack()) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
