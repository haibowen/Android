package com.mmuu.travel.client.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.lidroid.xutils.http.RequestParams;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 行程详情Act
 */

public class RunDetailAct extends MFBaseActivity implements IUiListener {
    private RunDetailFrg runDetailFrg;
    private IWeiboShareAPI iWeiboShareAPI = null;
    private Tencent mTencent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.activity_push_no_anim);
        setContentView(R.layout.mf_base_act);
        runDetailFrg = new RunDetailFrg();
        iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, MFConstansValue.APP_WEIBOKEY);
        iWeiboShareAPI.registerApp();
        mTencent = Tencent.createInstance(MFConstansValue.APP_TENCENTID, this);
        runDetailFrg.setIUiListener(this);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, runDetailFrg);
        transaction.commitAllowingStateLoss();

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
        initShareTime();
    }

    @Override
    public void onError(UiError uiError) {
        MFUtil.showToast(this, "分享失败\n" + uiError.errorMessage);
    }

    @Override
    public void onCancel() {
        MFUtil.showToast(this, "分享取消");
    }

    private void initShareTime() {
        if (!MFUtil.checkNetwork(this)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(1000000, MFUrl.ITINERARYSHARINGADDINTEGRAL_URL, params, null);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_push_no_anim, R.anim.slide_out_right);
    }
}
