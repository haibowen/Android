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
 * 结束行程act
 * JiaGuangWei on 2016/12/22 15:08
 */
public class RunEndAct extends MFBaseActivity implements IUiListener {
    private RunEndFrg runEndFrg;
    private IWeiboShareAPI iWeiboShareAPI = null;
    private Tencent mTencent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.activity_push_no_anim);
        setContentView(R.layout.mf_base_act);
        runEndFrg = new RunEndFrg();
        iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, MFConstansValue.APP_WEIBOKEY);
        iWeiboShareAPI.registerApp();
        mTencent = Tencent.createInstance(MFConstansValue.APP_TENCENTID, this);
        runEndFrg.setIUiListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.base_contextlayout, runEndFrg);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent("closeCurrentAct"));
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_push_no_anim, R.anim.slide_out_right);
    }

    /**
     * 行程分享次数加积分接口
     */
    private void initShareTime() {
        if (!MFUtil.checkNetwork(this)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(1000000, MFUrl.ITINERARYSHARINGADDINTEGRAL_URL, params, null);
    }
}
