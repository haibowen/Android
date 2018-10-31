package com.mmuu.travel.client.ui.user;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.user.CarDepositVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.ui.other.RechargeAct;
import com.mmuu.travel.client.widget.StatusView;
import com.mmuu.travel.client.widget.dialog.TwoDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的钱包
 * Created by HuangYuan on 2016/12/13.
 */

public class MyWalletFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    @BindView(R.id.v_status)
    StatusView vStatus;
    @BindView(R.id.title_left_text)
    TextView titleLeftText;
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.title_left_image_mark)
    TextView titleLeftImageMark;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.tv_mywallet)
    TextView tvMywallet;
    @BindView(R.id.iv_mywallet_activity)
    ImageView ivMywalletActivity;
    @BindView(R.id.rl_mywallet)
    RelativeLayout rlMywallet;
    @BindView(R.id.iv_balance)
    ImageView ivBalance;
    @BindView(R.id.tv_balance_txt)
    TextView tvBalanceTxt;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.rl_balance)
    RelativeLayout rlBalance;
    @BindView(R.id.iv_voucher)
    ImageView ivVoucher;
    @BindView(R.id.tv_voucher_txt)
    TextView tvVoucherTxt;
    @BindView(R.id.tv_voucher)
    TextView tvVoucher;
    @BindView(R.id.iv_wallet_raw)
    ImageView ivWalletRaw;
    @BindView(R.id.rl_voucher)
    RelativeLayout rlVoucher;
    @BindView(R.id.iv_deposit)
    ImageView ivDeposit;
    @BindView(R.id.tv_deposit_txt)
    TextView tvDepositTxt;
    @BindView(R.id.rl_deposit)
    RelativeLayout rlDeposit;
    @BindView(R.id.v_line0)
    View vLine0;
    @BindView(R.id.tv_deposit_button)
    TextView tvDepositButton;
    private View rootView;
    private final int TAG_MY_WALLET_URL = 1695701;//获取我的钱包信息
    private final int TAG_GETDEPOSITCONFIG = 1695703;//获取押金配置
    private final int TAG_CHECKCITY = 1695704;//检查城市是否开通
    private final int TAG_CHECKCITYWTIHRECHARGE = 1695705;
    private final int TAG_MY_WALLET_URL_CHECK = 1695706;//得知未交押金，二次获取我的钱包信息检查是否押金的确未交
    private MyWalletBean myWalletBean = null;
    private double depositAmount;
    private boolean isfirst = true;
    private int checkCity = 0;//0未知；1已开通；-1未开通

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frg_my_wallet, null);
            ButterKnife.bind(this, rootView);
            initView();
            initDepositData();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkCity(false);
        initInfoData();
    }

    private void initDepositData() {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
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

    private void initInfoData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        if (isfirst) {
            dialogShow();
            isfirst = false;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(TAG_MY_WALLET_URL, MFUrl.MY_WALLET_URL, requestParams, this);
    }

    private void initView() {
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setOnClickListener(this);
        titleRightText.setText(R.string.detail);
        titleRightText.setVisibility(View.VISIBLE);
        titleRightText.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector, null));
        } else {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector));
        }
        if (SharedPreferenceTool.getPrefInt(mContext, MFConstansValue.SP_KEY_HASRECHARGEACTIVITY, 0) == 0) {
            ivMywalletActivity.setVisibility(View.GONE);
        } else {
            int with = ScreenUtil.getScreenWidth(mContext);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivMywalletActivity.getLayoutParams();
            layoutParams.width = with - ScreenUtil.dip2px(mContext, 14);
            layoutParams.height = layoutParams.width / 2;
            ivMywalletActivity.setLayoutParams(layoutParams);
            ivMywalletActivity.setVisibility(View.VISIBLE);
        }
        ivMywalletActivity.setOnClickListener(this);
        tvRecharge.setOnClickListener(this);
        rlVoucher.setOnClickListener(this);
        tvDepositButton.setOnClickListener(this);
    }

    /**
     * 请求数据后刷新页面
     */
    private void refreshUI() {
        if (myWalletBean == null) {
            return;
        }
        String residualCount = MFUtil.formatDoubleValue(myWalletBean.getBalance().doubleValue(), "0.#") + "元";
        tvBalance.setText(residualCount);
        tvVoucher.setText(myWalletBean.getCouponCount() + "张");
        switch (myWalletBean.getDepositState()) {// 0未缴纳 1正常 2退款中 3冻结
            case 0:
                tvDepositButton.setText("缴纳");
                tvDepositTxt.setText(Html
                        .fromHtml("<font  color=\"#999999\" size=\"60\">缴纳</font>"
                                + "<font  color=\"#ff785d\" size=\"60\">" + MFUtil.formatDoubleValue(Double.parseDouble(depositAmount + ""), "0.#") + "元</font>"
                                + "<font  color=\"#999999\" size=\"60\">押金后</font><br>"
                                + "<font  color=\"#999999\" size=\"60\">可享受小蜜蜂为您提供的服务</font>"
                        ));
                break;
            default:
                tvDepositButton.setText("查看");
                tvDepositTxt.setText(
                        Html.fromHtml("<font  color=\"#999999\" size=\"60\">已缴纳押金，可享受小蜜蜂为您提供的服务</font>"));
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_voucher:
                MobclickAgent.onEvent(mContext, "click_mywallet_myvoucher");
                startActivity(MyVoucherAct.class, null);
                break;
            case R.id.tv_deposit_button:
                depositClick();
                break;
            case R.id.iv_mywallet_activity:
            case R.id.tv_recharge:
                if (myWalletBean == null) {
                    return;
                }
                if (myWalletBean.getDepositState() == 0) {
                    TwoDialog twoDialog1 = new TwoDialog(mContext, "您还未充值押金，请充值押金后在进行余额充值", "取消", "充值押金");
                    twoDialog1.setDialogClickListener(new DialogClickListener() {
                        @Override
                        public void onLeftClick(View v, Dialog d) {
                            d.dismiss();
                        }

                        @Override
                        public void onRightClick(View v, Dialog d) {
                            MobclickAgent.onEvent(mContext, "click_mywallet_cardeposit");
                            startActivity(CarDepositAct.class, null);
                            d.dismiss();
                        }
                    });
                    twoDialog1.show();
                } else {
                    MobclickAgent.onEvent(mContext, "click_mywallet_charge");
                    switch (checkCity) {
                        case 0:
                            checkCity(true);
                            break;
                        case -1:
                            String messge = "您所在的城市未开通，暂无小蜜蜂可以使用" + "\r\n" + "<font color='#ff0000'><br>请确认是否充值？</font>";
                            TwoDialog rechargeDia = new TwoDialog(mContext, messge, "取消", "确认充值");
                            rechargeDia.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    d.dismiss();
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    d.dismiss();
                                    startActivity(RechargeAct.class, null);
                                }
                            });
                            rechargeDia.show();
                            break;
                        case 1:
                            startActivity(RechargeAct.class, null);
                            break;
                    }
                }
                break;
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.title_right_text:
                MobclickAgent.onEvent(mContext, "click_mywallet_costdetail");
                startActivity(CostDetailAct.class, null);
                break;
        }

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

    private void depositClick() {
        if (myWalletBean == null) {
            return;
        }
        switch (myWalletBean.getDepositState()) {// 0未缴纳 1正常 2退款中 3冻结
            case 0:
                initInfoDataCheck();
                break;
            default:
                startActivity(MyDepositAct.class, null);
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
        switch (i) {
            case TAG_MY_WALLET_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<MyWalletBean> myWalletBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MyWalletBean>>() {
                }.getType());
                if (myWalletBeanRequestResultBean != null && myWalletBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    myWalletBean = myWalletBeanRequestResultBean.getData();
                    refreshUI();
                } else if (myWalletBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == myWalletBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (myWalletBeanRequestResultBean != null) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBean.getMessage());
                }
                break;
            case TAG_MY_WALLET_URL_CHECK:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<MyWalletBean> myWalletBeanRequestResultBeanCheck = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MyWalletBean>>() {
                }.getType());
                if (myWalletBeanRequestResultBeanCheck != null && myWalletBeanRequestResultBeanCheck.getCode() == MFConstansValue.BACK_SUCCESS) {
                    myWalletBean = myWalletBeanRequestResultBeanCheck.getData();
                    refreshUI();
                    if (myWalletBean != null && myWalletBean.getDepositState() == 0) {
                        MobclickAgent.onEvent(mContext, "click_mywallet_cardeposit");
                        startActivity(CarDepositAct.class, null);
                    } else {
                        MFUtil.showToast(mContext, "您已充值押金");
                    }
                } else if (myWalletBeanRequestResultBeanCheck != null && MFConstansValue.BACK_LOGOUT == myWalletBeanRequestResultBeanCheck.getCode()) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBeanCheck.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (myWalletBeanRequestResultBeanCheck != null) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBeanCheck.getMessage());
                }
                break;

            case TAG_GETDEPOSITCONFIG:
                RequestResultBean<CarDepositVO> carDepositVORequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<CarDepositVO>>() {
                }.getType());
                if (carDepositVORequestResultBean != null && carDepositVORequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    if (carDepositVORequestResultBean.getData() != null) {
                        depositAmount = carDepositVORequestResultBean.getData().getDepositAmount();
                        refreshUI();
                    }
                } else if (carDepositVORequestResultBean != null && MFConstansValue.BACK_LOGOUT == carDepositVORequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, carDepositVORequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (carDepositVORequestResultBean != null) {
                    MFUtil.showToast(mContext, carDepositVORequestResultBean.getMessage());
                }
                break;
            case TAG_CHECKCITY:
                RequestResultBean<String> cityBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<String>>() {
                }.getType());

                if (cityBean != null) {
                    switch (cityBean.getCode()) {
                        case 0:
                            checkCity = 1;
                            break;
                        case 1011:
                            checkCity = -1;
                            break;
                        default:
                            MFUtil.showToast(mContext, cityBean.getMessage());
                            break;
                    }
                }
                break;
            case TAG_CHECKCITYWTIHRECHARGE:
                RequestResultBean<String> cityBean2 = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<String>>() {
                }.getType());

                if (cityBean2 != null) {
                    switch (cityBean2.getCode()) {
                        case 1011:
                            checkCity = -1;
                            String messge = "您所在的城市未开通，暂无小蜜蜂可以使用" + "\n" + "<font color='#ff0000'>请确认是否充值？</font>";
                            TwoDialog rechargeDia = new TwoDialog(mContext, messge, "取消", "确认充值");
                            rechargeDia.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    d.dismiss();
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    d.dismiss();
                                    startActivity(RechargeAct.class, null);
                                }
                            });
                            break;
                        case 0:
                            checkCity = 1;
                            startActivity(RechargeAct.class, null);
                            break;
                        default:
                            MFUtil.showToast(mContext, cityBean2.getMessage());
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        switch (i) {
            case TAG_MY_WALLET_URL:
                MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
                dismmisDialog();
                break;
        }
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    private void checkCity(boolean button) {
        RequestParams params = new RequestParams();

        if (MFApp.getInstance().getmLocation() != null) {
            params.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
        }
        if (button) {
            MFRunner.requestPost(TAG_CHECKCITYWTIHRECHARGE, MFUrl.CHECKCITYBEFORERECHARGE, params, this);
        } else {
            MFRunner.requestPost(TAG_CHECKCITY, MFUrl.CHECKCITYBEFORERECHARGE, params, this);
        }
    }
}
