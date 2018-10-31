package com.mmuu.travel.client.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.tools.MFUtil;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 信用明细act
 * JiaGuangWei on 2016/12/22 20:42
 */
public class CreditDetailAct extends MFBaseActivity {
    private CreditDetailFrg creditDetailFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creditDetailFrg = new CreditDetailFrg();
        setContentView(R.layout.mf_base_act);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.base_contextlayout, creditDetailFrg);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
