package com.mmuu.travel.client.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * Created by XIXIHAHA on 2017/2/7.
 */

public class BookIngAct extends MFBaseActivity {

    private BookIngFrg bookIngFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        bookIngFrg = new BookIngFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, bookIngFrg);
        transaction.commitAllowingStateLoss();
    }
}
