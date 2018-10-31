package com.mmuu.travel.client.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.mfinterface.WXPayCallBackListener;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.tools.MFUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private static HashMap<String, WXPayCallBackListener> payMap;
    //private static WXPayCallBackListener wxPayCallBackListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, MFConstansValue.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

        //Log.d(TAG, "onReq, onReq = " + req.toString());
    }

    @Override
    public void onResp(BaseResp resp) {
        // Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "支付成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "支付失败:操作已经取消";
                MFUtil.showToast(this, result);
                break;
            default:
                result = "支付失败,请重试";
                MFUtil.showToast(this, result);
                break;
        }
//        MyUtils.showToast(this, result);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (payMap != null) {
                for (WXPayCallBackListener listener : payMap.values()) {
                    listener.OnWXpayBackListener(resp.errCode, resp.errStr, resp.transaction);
                }
            }
        }
        finish();
    }

    public static void addWXPayCallbackListener(String name, WXPayCallBackListener payCallBackListener) {
        if (payMap == null) {
            payMap = new HashMap<String, WXPayCallBackListener>();
        }
        payMap.put(name, payCallBackListener);
    }

    public static void removedWXPayCallbackListener(String name) {
        if (payMap != null) {
            payMap.remove(name);
        }
    }

}