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
 * 信用积分act
 * JiaGuangWei on 2016/12/22 20:42
 */
public class CreditPointAct extends MFBaseActivity implements IUiListener {
    private CreditPointFrg creditPointFrg;
    IWeiboShareAPI iWeiboShareAPI = null;
    Tencent mTencent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.activity_push_no_anim);
        creditPointFrg = new CreditPointFrg();
        setContentView(R.layout.mf_base_act);
        iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, MFConstansValue.APP_WEIBOKEY);
        iWeiboShareAPI.registerApp();
        mTencent = Tencent.createInstance(MFConstansValue.APP_TENCENTID, this);
        creditPointFrg.setIUiListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.base_contextlayout, creditPointFrg);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10103) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(Object o) {
        MFUtil.showToast(this, "分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        MFUtil.showToast(this, "分享失败\n" + uiError.errorMessage);
    }

    @Override
    public void onCancel() {
        MFUtil.showToast(this, "分享取消");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_push_no_anim, R.anim.slide_out_right);
    }
}
