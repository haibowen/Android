//package com.mmuu.travel.client.ui.user;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.PublicRequestInterface;
//import com.mmuu.travel.client.R;
//import com.mmuu.travel.client.base.MFBaseFragment;
//import com.mmuu.travel.client.bean.RequestResultBean;
//import com.mmuu.travel.client.bean.aliorder.AliPayResult;
//import com.mmuu.travel.client.bean.mfinterface.WXPayCallBackListener;
//import com.mmuu.travel.client.bean.wxorder.WXPayBean;
//import com.mmuu.travel.client.mfConstans.MFConstansValue;
//import com.mmuu.travel.client.mfConstans.MFStaticConstans;
//import com.mmuu.travel.client.mfConstans.MFUrl;
//import com.mmuu.travel.client.tools.GsonTransformUtil;
//import com.mmuu.travel.client.tools.MFRunner;
//import com.mmuu.travel.client.tools.MFUtil;
//import com.mmuu.travel.client.wxapi.WXPayEntryActivity;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.umeng.analytics.MobclickAgent;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * 充值
// * Created by HuangYuan on 2016/12/13.
// */
//
//public class RechargeFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, WXPayCallBackListener {
//
//    @BindView(R.id.title_left_image)
//    ImageView titleLeftImage;
//    @BindView(R.id.title_title)
//    TextView titleTitle;
//
//    @BindView(R.id.money_number)
//    EditText moneyNumber;
//    @BindView(R.id.ten_rmb)
//    TextView tenRmb;
//    @BindView(R.id.twenty_rmb)
//    TextView twentyRmb;
//    @BindView(R.id.fifty_rmb)
//    TextView fiftyRmb;
//    @BindView(R.id.one_hundred_rmb)
//    TextView oneHundredRmb;
//
//    @BindView(R.id.alipay_check)
//    TextView alipayCheck;
//    @BindView(R.id.wechat_check)
//    TextView wechatCheck;
//    @BindView(R.id.recharge)
//    TextView recharge;
//    @BindView(R.id.alipay)
//    LinearLayout alipay;
//    @BindView(R.id.wechat)
//    LinearLayout wechat;
//    private Activity context;
//    private View rootView;
//
//    private String rechargeMoney = "10";
//    private InputMethodManager imm;
//    private String wxoutTradeNo = "";
//
//    @Override
//    protected void baseHandMessage(Message msg) {
//        super.baseHandMessage(msg);
//
//        switch (msg.what) {
//            case 123456:
//                AliPayResult aliPayResult = (AliPayResult) msg.obj;
//                if (aliPayResult != null && aliPayResult.getResultStatus() == 9000) {
//                    RequestParams requestParams = new RequestParams();
//                    requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
//                    requestParams.addBodyParameter("payChannel", "0");//0支付宝1微信
//                    requestParams.addBodyParameter("payType", "2");//0押金2充值
//                    if (aliPayResult.getResult() != null) {
//                        requestParams.addBodyParameter("outTradeNo", aliPayResult.getResult().getAlipay_trade_app_pay_response().getOut_trade_no() + "");
//                    }
//                    dialogShow();
//                    MFRunner.requestPost(2001, MFUrl.modifyPayResultByPayType, requestParams, this);
//                } else if (aliPayResult != null && aliPayResult.getResultStatus() == 6001) {
//                    MFUtil.showToast(mContext, "支付失败:操作已经取消");
//                } else {
//                    MFUtil.showToast(mContext, "支付失败请重试");
//                }
//                break;
//        }
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getActivity();
//        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.frg_recharger, null);
//        }
//        ButterKnife.bind(this, rootView);
//        initView();
//        return rootView;
//    }
//
//    private void initView() {
//        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
//        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
//        titleLeftImage.setVisibility(View.VISIBLE);
//        titleLeftImage.setOnClickListener(this);
//
//        titleTitle.setText(R.string.recharge);
//
//        tenRmb.setOnClickListener(this);
//        tenRmb.setSelected(true);
//        twentyRmb.setOnClickListener(this);
//        fiftyRmb.setOnClickListener(this);
//        oneHundredRmb.setOnClickListener(this);
//
//        alipay.setOnClickListener(this);
//        wechat.setOnClickListener(this);
//
//        alipayCheck.setSelected(true);
//
//        recharge.setOnClickListener(this);
//
//        moneyNumber.setOnClickListener(this);
//        moneyNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s) && Integer.parseInt(s.toString()) > 10000) {
//                    MFUtil.showToast(mContext, "每次最大充值额度为10000元");
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        WXPayEntryActivity.removedWXPayCallbackListener(this.getClass().getName());
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.title_left_image:
//                context.finish();
//                break;
//            case R.id.ten_rmb:
//                cleanCheckedState(true);
//                moneyNumber.setText("");
//                tenRmb.setSelected(true);
//                rechargeMoney = "10";
//                break;
//            case R.id.twenty_rmb:
//                cleanCheckedState(true);
//                moneyNumber.setText("");
//                twentyRmb.setSelected(true);
//                rechargeMoney = "20";
//                break;
//            case R.id.fifty_rmb:
//                cleanCheckedState(true);
//                moneyNumber.setText("");
//                fiftyRmb.setSelected(true);
//                rechargeMoney = "50";
//                break;
//            case R.id.one_hundred_rmb:
//                cleanCheckedState(true);
//                moneyNumber.setText("");
//                oneHundredRmb.setSelected(true);
//                rechargeMoney = "100";
//                break;
//            case R.id.wechat:
//                alipayCheck.setSelected(false);
//                wechatCheck.setSelected(true);
//                break;
//            case R.id.alipay:
//                alipayCheck.setSelected(true);
//                wechatCheck.setSelected(false);
//                break;
//            case R.id.recharge:
//                MobclickAgent.onEvent(mContext, "click_charge_charge");
//                String money = getRechargeMoney();
//                gotoPayment(money);
//                break;
//            case R.id.money_number:
//                cleanCheckedState(false);
//                rechargeMoney = "";
//                break;
//        }
//    }
//
//
//    private String getRechargeMoney() {
//
//        if (TextUtils.isEmpty(moneyNumber.getText().toString().trim())) {
//            return rechargeMoney;
//        } else {
//            return moneyNumber.getText().toString().trim();
//        }
//    }
//
//    private void gotoPayment(String money) {
//        if (TextUtils.isEmpty(money) || "0".equals(money)) {
//            MFUtil.showToast(mContext, "请选择或输入正确的充值金额");
//            return;
//        }
//        if (Integer.parseInt(money.toString()) > 10000) {
//            MFUtil.showToast(mContext, "每次最大充值额度为10000元");
//            return;
//        }
//        if (!MFUtil.checkNetwork(mContext)) {
//            MFUtil.showToast(mContext, getResources().getString(R.string.nonet));
//            return;
//        }
//        if (wechatCheck.isSelected()) {
//            dialogShow();
//            RequestParams aliParams = new RequestParams();
//            aliParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
//            aliParams.addBodyParameter("amount", money);
//            aliParams.addBodyParameter("payChannel", "1");//支付渠道:0支付宝，1微信
//            MFRunner.requestPost(1002, MFUrl.buildRecharge, aliParams, this);
//        } else {
//            dialogShow();
//            RequestParams aliParams = new RequestParams();
//            aliParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
//            aliParams.addBodyParameter("amount", money);
//            aliParams.addBodyParameter("payChannel", "0");//支付渠道:0支付宝，1微信
//            MFRunner.requestPost(1001, MFUrl.buildRecharge, aliParams, this);
//        }
//    }
//
//    private void cleanCheckedState(boolean isHideSoftInput) {
//        tenRmb.setSelected(false);
//        twentyRmb.setSelected(false);
//        fiftyRmb.setSelected(false);
//        oneHundredRmb.setSelected(false);
//        if (isHideSoftInput) {
//            if (imm == null) {
//                imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            }
//            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
//            moneyNumber.clearFocus();
//        }
//    }
//
//
//    @Override
//    public void onStart(int i, RequestParams requestParams) {
//
//    }
//
//    @Override
//    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {
//
//    }
//
//    @Override
//    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
//        if (!isAdded()) {
//            return;
//        }
//        dismmisDialog();
//        switch (i) {
//            case 1001:
//                RequestResultBean<String> ali = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<String>>() {
//                }.getType());
//                if (ali != null && ali.getCode() == MFConstansValue.BACK_SUCCESS) {
//                    MFRunner.payForAli(getActivity(), getHandler(), ali.getData());
//                } else if (ali != null && MFConstansValue.BACK_LOGOUT == ali.getCode()) {
//                    MFUtil.showToast(mContext, ali.getMessage());
//                    MFStaticConstans.setUserBean(null);
//                    startActivity(LoginAct.class, null);
//                    mContext.finish();
//                } else if (ali != null && (MFConstansValue.BACK_SYSTEMERROR == ali.getCode() || MFConstansValue.BACK_BUSINESS == ali.getCode())) {
//                    MFUtil.showToast(mContext, ali.getMessage());
//                }
//                break;
//            case 1002:
//                RequestResultBean<WXPayBean> wx = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<WXPayBean>>() {
//                }.getType());
//                if (wx != null && wx.getData() != null && wx.getCode() == MFConstansValue.BACK_SUCCESS) {
//                    WXPayEntryActivity.addWXPayCallbackListener(this.getClass().getName(), this);
//                    MFRunner.payForWX(mContext, wx.getData());
//                    wxoutTradeNo = wx.getData().getOutTradeNo();
//                } else if (wx != null && MFConstansValue.BACK_LOGOUT == wx.getCode()) {
//                    MFUtil.showToast(mContext, wx.getMessage());
//                    MFStaticConstans.setUserBean(null);
//                    startActivity(LoginAct.class, null);
//                    mContext.finish();
//                } else if (wx != null && (MFConstansValue.BACK_SYSTEMERROR == wx.getCode() || MFConstansValue.BACK_BUSINESS == wx.getCode())) {
//                    MFUtil.showToast(mContext, wx.getMessage());
//                }
//                break;
//            case 2001:
//                RequestResultBean sBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
//                }.getType());
//                if (sBean != null && MFConstansValue.BACK_SUCCESS == sBean.getCode()) {
//                    MFUtil.showToast(mContext, sBean.getMessage());
//                    mContext.finish();
//                } else if (sBean != null && MFConstansValue.BACK_LOGOUT == sBean.getCode()) {
//                    MFUtil.showToast(mContext, sBean.getMessage());
//                    MFStaticConstans.setUserBean(null);
//                    startActivity(LoginAct.class, null);
//                    mContext.finish();
//                } else if (sBean != null && (MFConstansValue.BACK_SYSTEMERROR == sBean.getCode() || MFConstansValue.BACK_BUSINESS == sBean.getCode())) {
//                    MFUtil.showToast(mContext, sBean.getMessage());
//                }
//        }
//
//    }
//
//    @Override
//    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
//        if (!isAdded()) {
//            return;
//        }
//        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
//        dismmisDialog();
//    }
//
//    @Override
//    public void onCancel(int i, RequestParams requestParams) {
//
//    }
//
//    @Override
//    public void OnWXpayBackListener(int status, String message, String transaction) {
//        switch (status) {
//            case 0:
//                RequestParams requestParams = new RequestParams();
//                requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
//                requestParams.addBodyParameter("payChannel", "1");//0支付宝1微信
//                requestParams.addBodyParameter("payType", "2");//0押金2充值
//                requestParams.addBodyParameter("outTradeNo", wxoutTradeNo);
//                dialogShow();
//                MFRunner.requestPost(2001, MFUrl.modifyPayResultByPayType, requestParams, this);
//                break;
//
//            default:
////                if (!TextUtils.isEmpty(message)) {
////                    MFUtil.showToast(mContext, message);
////                }
//                break;
//        }
//
//
//    }
//}
