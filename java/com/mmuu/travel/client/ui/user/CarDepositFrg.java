package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.MyWalletBean;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.aliorder.ALiPayOrderVO;
import com.mmuu.travel.client.bean.aliorder.AliPayResult;
import com.mmuu.travel.client.bean.mfinterface.WXPayCallBackListener;
import com.mmuu.travel.client.bean.user.CarDepositVO;
import com.mmuu.travel.client.bean.wxorder.WXPayBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.wxapi.WXPayEntryActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JGW 押金充值frg
 */

public class CarDepositFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, WXPayCallBackListener {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.alipay)
    LinearLayout alipay;
    @BindView(R.id.wechat)
    LinearLayout wechat;
    @BindView(R.id.alipay_check)
    TextView alipayCheck;
    @BindView(R.id.wechat_check)
    TextView wechatCheck;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_despositmoney)
    TextView tvDespositmoney;
    private int type;
    private String wxoutTradeNo = "";
    private final int TAG_GETDEPOSITCONFIG = 1695701;
    private final int TAG_MY_WALLET_URL_CHECK = 1695702;
    private final int TAG_MODIFYPAYRESULTBYPAYTYPE = 1695703;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case 1:
                break;
            case 123456:
                AliPayResult aliPayResult = (AliPayResult) msg.obj;
                if (aliPayResult != null && aliPayResult.getResultStatus() == 9000) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                    requestParams.addBodyParameter("payChannel", "0");//0支付宝1微信
                    requestParams.addBodyParameter("payType", "0");//0押金2充值
                    if (aliPayResult.getResult() != null) {
                        requestParams.addBodyParameter("outTradeNo", aliPayResult.getResult().getAlipay_trade_app_pay_response().getOut_trade_no() + "");
                    }
                    dialogShow(true);
                    MFRunner.requestPost(2001, MFUrl.modifyPayResultByPayType, requestParams, this);
                } else if (aliPayResult != null && aliPayResult.getResultStatus() == 6001) {
                    MFUtil.showToast(mContext, "支付失败:操作已经取消");
                } else {
                    MFUtil.showToast(mContext, "支付失败请重试");
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_cardeposit, null);
            ButterKnife.bind(this, fragContentView);
            initView();
            checkDeposit();
            initInfoData();
        }
        return fragContentView;
    }

    private void checkDeposit() {
        if (!"".equals(SharedPreferenceTool.getPrefString(mContext, "wxoutTradeNo", ""))) {
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("payChannel", "1");//0支付宝1微信
            requestParams.addBodyParameter("payType", "0");//0押金2充值
            requestParams.addBodyParameter("outTradeNo", SharedPreferenceTool.getPrefString(mContext, "wxoutTradeNo", ""));
            dialogShow(true);
            SharedPreferenceTool.setPrefString(mContext, "wxoutTradeNo", "");
            MFRunner.requestPost(TAG_MODIFYPAYRESULTBYPAYTYPE, MFUrl.modifyPayResultByPayType, requestParams, this);
        }
    }

    private void initInfoData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams requestParams = new RequestParams();
        PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                PersonalCenterVo.class);
        if (personalCenterVo == null || TextUtils.isEmpty(personalCenterVo.getLoginCityCode())) {
            if (MFApp.getInstance().getmLocation() != null) {
                requestParams.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
            }
        } else {
            requestParams.addBodyParameter("cityCode", personalCenterVo.getLoginCityCode());
        }
        MFRunner.requestPost(TAG_GETDEPOSITCONFIG, MFUrl.GETDEPOSITCONFIG, requestParams, this);
    }

    private void initView() {
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText(R.string.my_deposit);
        alipayCheck.setSelected(true);
        alipay.setOnClickListener(this);
        wechat.setOnClickListener(this);
        tvRecharge.setOnClickListener(this);
//        PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
//                PersonalCenterVo.class);
//        if (personalCenterVo != null) {
//            tvDespositmoney.setText(personalCenterVo.getDeposit() + "元");
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (type == 1 && wechatCheck.isSelected()) {
//            initInfoDataCheck();
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WXPayEntryActivity.removedWXPayCallbackListener(this.getClass().getName());
    }

    /**
     * 二次检查押金是否已缴纳
     */
    private void initInfoDataCheck() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(TAG_MY_WALLET_URL_CHECK, MFUrl.MY_WALLET_URL, requestParams, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.alipay:
                type = 0;
                wechatCheck.setSelected(false);
                alipayCheck.setSelected(true);
                break;
            case R.id.wechat:
                type = 1;
                wechatCheck.setSelected(true);
                alipayCheck.setSelected(false);
                break;
            case R.id.tv_recharge:
                initInfoDataCheck();
                MobclickAgent.onEvent(mContext, "click_cardeposit_cardeposit");
                break;
        }
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {

    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        if (!isAdded()) {
            return;
        }
        dismmisDialog();
        switch (i) {
            case TAG_MY_WALLET_URL_CHECK:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<MyWalletBean> myWalletBeanRequestResultBeanCheck = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MyWalletBean>>() {
                }.getType());
                if (myWalletBeanRequestResultBeanCheck != null && myWalletBeanRequestResultBeanCheck.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MyWalletBean myWalletBean = myWalletBeanRequestResultBeanCheck.getData();
                    if (myWalletBean != null && myWalletBean.getDepositState() == 0) {
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                        params.addBodyParameter("payChannel", type + "");
                        PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                                PersonalCenterVo.class);
                        if (personalCenterVo == null || TextUtils.isEmpty(personalCenterVo.getLoginCityCode())) {
                            if (MFApp.getInstance().getmLocation() != null) {
                                params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
                            }
                        } else {
                            params.addBodyParameter("cityCode", personalCenterVo.getLoginCityCode());
                        }
                        dialogShow();
                        if (type == 0) {
                            MFRunner.requestPost(1000, MFUrl.BUILDDEPOSIT_URL, params, this);
                        } else {
                            MFRunner.requestPost(1001, MFUrl.BUILDDEPOSIT_URL, params, this);
                        }
                    } else {
                        MFUtil.showToast(mContext, "您已充值押金");
                    }
                } else if (myWalletBeanRequestResultBeanCheck != null && MFConstansValue.BACK_LOGOUT == myWalletBeanRequestResultBeanCheck.getCode()) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBeanCheck.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (myWalletBeanRequestResultBeanCheck != null && (MFConstansValue.BACK_SYSTEMERROR == myWalletBeanRequestResultBeanCheck.getCode() || MFConstansValue.BACK_BUSINESS == myWalletBeanRequestResultBeanCheck.getCode())) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBeanCheck.getMessage());
                }
                break;
            case 1000:
                RequestResultBean<ALiPayOrderVO> aliBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<ALiPayOrderVO>>() {
                }.getType());
                if (aliBean != null && aliBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MFRunner.payForAli(getActivity(), getHandler(), aliBean.getData());
                } else if (aliBean != null && MFConstansValue.BACK_LOGOUT == aliBean.getCode()) {
                    MFUtil.showToast(mContext, aliBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (aliBean != null && (MFConstansValue.BACK_SYSTEMERROR == aliBean.getCode() || MFConstansValue.BACK_BUSINESS == aliBean.getCode())) {
                    MFUtil.showToast(mContext, aliBean.getMessage());
                }
                break;
            case TAG_GETDEPOSITCONFIG:
                RequestResultBean<CarDepositVO> carDepositVORequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<CarDepositVO>>() {
                }.getType());
                if (carDepositVORequestResultBean != null && carDepositVORequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    if (carDepositVORequestResultBean.getData() != null) {
                        tvDespositmoney.setText("¥" + MFUtil.formatDoubleValue(carDepositVORequestResultBean.getData().getDepositAmount(), "0.#"));
                    }
                } else if (carDepositVORequestResultBean != null && MFConstansValue.BACK_LOGOUT == carDepositVORequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, carDepositVORequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (carDepositVORequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == carDepositVORequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == carDepositVORequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, carDepositVORequestResultBean.getMessage());
                }
                break;
            case 1001:
                RequestResultBean<WXPayBean> wx = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<WXPayBean>>() {
                }.getType());
                if (wx != null && wx.getCode() == MFConstansValue.BACK_SUCCESS) {
                    WXPayEntryActivity.addWXPayCallbackListener(this.getClass().getName(), this);
                    MFRunner.payForWX(mContext, wx.getData());
                    wxoutTradeNo = wx.getData().getOutTradeNo();
                    SharedPreferenceTool.setPrefString(mContext, "wxoutTradeNo", wxoutTradeNo);
                } else if (wx != null && MFConstansValue.BACK_LOGOUT == wx.getCode()) {
                    MFUtil.showToast(mContext, wx.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (wx != null && (MFConstansValue.BACK_SYSTEMERROR == wx.getCode() || MFConstansValue.BACK_BUSINESS == wx.getCode())) {
                    MFUtil.showToast(mContext, wx.getMessage());
                }
                break;
            case 2001:
                RequestResultBean sBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (sBean != null && MFConstansValue.BACK_SUCCESS == sBean.getCode()) {
                    MFUtil.showToast(mContext, sBean.getMessage());
                    PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                            PersonalCenterVo.class);
                    if (personalCenterVo != null && (personalCenterVo.getCertState() == 0 || personalCenterVo.getCertState() == 1)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                        startActivity(CertificationAct.class, bundle);
                    }
                    mContext.finish();
                } else if (sBean != null && MFConstansValue.BACK_LOGOUT == sBean.getCode()) {
                    MFUtil.showToast(mContext, sBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (sBean != null && (MFConstansValue.BACK_SYSTEMERROR == sBean.getCode() || MFConstansValue.BACK_BUSINESS == sBean.getCode())) {
                    MFUtil.showToast(mContext, sBean.getMessage());
                }
                break;
            case TAG_MODIFYPAYRESULTBYPAYTYPE:
                RequestResultBean checkBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (checkBean != null && MFConstansValue.BACK_SUCCESS == checkBean.getCode()) {
                    MFUtil.showToast(mContext, checkBean.getMessage());
                    PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                            PersonalCenterVo.class);
                    if (personalCenterVo != null && (personalCenterVo.getCertState() == 0 || personalCenterVo.getCertState() == 1)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                        startActivity(CertificationAct.class, bundle);
                    }
                    mContext.finish();
                } else if (checkBean != null && MFConstansValue.BACK_LOGOUT == checkBean.getCode()) {
                    MFUtil.showToast(mContext, checkBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (checkBean != null && (MFConstansValue.BACK_SYSTEMERROR == checkBean.getCode() || MFConstansValue.BACK_BUSINESS == checkBean.getCode())) {
//                    MFUtil.showToast(mContext, checkBean.getMessage());
                }
                break;
        }
    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
        dismmisDialog();
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    @Override
    public void OnWXpayBackListener(int status, String message, String transaction) {
        switch (status) {
            case 0:
                RequestParams requestParams = new RequestParams();
                requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                requestParams.addBodyParameter("payChannel", "1");//0支付宝1微信
                requestParams.addBodyParameter("payType", "0");//0押金2充值
                requestParams.addBodyParameter("outTradeNo", wxoutTradeNo);
                dialogShow(true);
                SharedPreferenceTool.setPrefString(mContext, "wxoutTradeNo", "");
                MFRunner.requestPost(2001, MFUrl.modifyPayResultByPayType, requestParams, this);
                break;
            default:
//                MFUtil.showToast(mContext, "支付失败请重试");
                break;
        }
    }
}
