package com.mmuu.travel.client.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.http.RequestParams;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class SinaEntryActivity extends Activity implements IWeiboHandler.Response {
    private IWeiboShareAPI iWeiboShareAPI = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, MFConstansValue.APP_WEIBOKEY);
        iWeiboShareAPI.registerApp();
        boolean bol = iWeiboShareAPI.handleWeiboResponse(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean bol = iWeiboShareAPI.handleWeiboResponse(getIntent(), this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    MFUtil.showToast(this, "分享成功");
                    if ("RunDetailFrg".equals(baseResponse.transaction)) {
                        initShareTime();
                    } else if ("WebFrg".equals(baseResponse.transaction)) {
                        shareaskactivity();
                    }
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    MFUtil.showToast(this, "分享取消");
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    MFUtil.showToast(this, "分享失败");
                    break;
            }
        }
        finish();
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

    /**
     * webview分享接口回调
     */
    private void shareaskactivity() {
        if (!MFUtil.checkNetwork(this)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", MFStaticConstans.getUserBean().getUser().getMobile() + "");
        MFRunner.requestPost(1000000, MFUrl.SHAREASKACTIVITY, params, null);
    }

}
