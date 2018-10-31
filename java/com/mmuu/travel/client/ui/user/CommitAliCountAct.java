package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**
 * JGW 提交支付宝账号act
 */
public class CommitAliCountAct extends MFBaseActivity {

    private CommitAliCountFrg commitAliCountFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_base_act);
        commitAliCountFrg = new CommitAliCountFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, commitAliCountFrg);
        transaction.commitAllowingStateLoss();
    }
}
