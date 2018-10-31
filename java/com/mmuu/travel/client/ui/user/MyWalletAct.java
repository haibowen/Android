package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.lidroid.xutils.util.LogUtils;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;
import com.mmuu.travel.client.bean.user.UserVO;
import com.mmuu.travel.client.tools.LGUtil;
import com.mmuu.travel.client.tools.MFUtil;

/**
 * 我的钱包
 * Created by HuangYuan on 2016/12/13.
 */

public class MyWalletAct extends MFBaseActivity {
    private MyWalletFrg myWalletFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.activity_push_no_anim);
        setContentView(R.layout.mf_base_act);
        myWalletFrg = new MyWalletFrg();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, myWalletFrg);
        transaction.commitAllowingStateLoss();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_push_no_anim, R.anim.slide_out_right);
    }
}
