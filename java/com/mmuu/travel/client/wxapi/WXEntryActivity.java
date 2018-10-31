package com.mmuu.travel.client.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.http.RequestParams;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, MFConstansValue.APP_ID);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq baseReq) {
//        System.out.print(baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {

        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                if ("RunDetailFrg".equals(baseResp.transaction)) {
                    initShareTime();
                } else if ("WebFrg".equals(baseResp.transaction)) {
                    shareaskactivity();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "分享已取消";
                break;
            default:
                result = "分享失败";
                break;
        }
        MFUtil.showToast(this, result);
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
