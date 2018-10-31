package com.mmuu.travel.client.ui.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
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
import com.mmuu.travel.client.mfConstans.MFOptions;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.widget.CustomAlphaAnmation;
import com.mmuu.travel.client.widget.StatusView;
import com.mmuu.travel.client.widget.dialog.OneDialog;
import com.mmuu.travel.client.widget.dialog.TwoDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * JGW 押金权益界面frg
 */

public class MyDepositFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    View rootView;
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
    @BindView(R.id.tv_exitdeposit)
    TextView tvExitdeposit;
    private final int TAG_MY_WALLET_URL = 1695701;//获取我已缴纳押金
    private final int TAG_APPLYDEPOSITBACK = 1695702;//退押金

    private final int TAG_MY_WALLET_URL_CHECK = 1695706;//得知未交押金，二次获取我的钱包信息检查是否押金的确未交
    private final int TAG_GETDEPOSITCONFIGCHECK = 1695707;//退押金前检查押金金额配置
    @BindView(R.id.iv_wave1)
    ImageView ivWeve1;
    @BindView(R.id.iv_wave2)
    ImageView ivWeve2;
    @BindView(R.id.iv_wave3)
    ImageView ivWeve3;
    Unbinder unbinder;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    private MyWalletBean myWalletBean = null;
    private static final int WHAT_WAVE1 = 1095703;
    private static final int WHAT_WAVE2 = 1095704;
    private static final int WHAT_WAVE3 = 1095705;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case WHAT_WAVE1:
                startAnimation(ivWeve1, 1, 0f, 3000);
                break;
            case WHAT_WAVE2:
                startAnimation(ivWeve2, 1, 0f, 3000);
                break;
            case WHAT_WAVE3:
                startAnimation(ivWeve3, 1, 0f, 3000);
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frg_mydeposit, null);
            unbinder = ButterKnife.bind(this, rootView);
            initView();
            initInfoData();
        }
        return rootView;
    }

    public void initView() {
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText("我的押金");

        String imageUri = "drawable://" + R.drawable.icon_mydeposit_bg;
        Bitmap bm = ImageLoader.getInstance().loadImageSync(imageUri, MFOptions.OPTION_DEF);
        BitmapDrawable bd = new BitmapDrawable(bm);
        rlBg.setBackground(bd);

        tvExitdeposit.setOnClickListener(this);
        startAnimation(ivWeve1, 0, 0, 0);
        startAnimation(ivWeve2, 0, 0, 0);
        startAnimation(ivWeve3, 0, 0, 0);
        getHandler().sendEmptyMessage(WHAT_WAVE1);
        getHandler().sendEmptyMessageDelayed(WHAT_WAVE2, 1000);
        getHandler().sendEmptyMessageDelayed(WHAT_WAVE3, 2000);
    }

    private void startAnimation(View view, float fromalpha, float toalpha, int time) {

        ScaleAnimation scaleAnimationrlScan = new ScaleAnimation(1, 2.6f, 1, 2.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimationrlScan.setRepeatCount(AnimationSet.INFINITE);
        scaleAnimationrlScan.setInterpolator(new DecelerateInterpolator());
        CustomAlphaAnmation alphaAnimation = new CustomAlphaAnmation(fromalpha, toalpha);
        alphaAnimation.setRepeatCount(AnimationSet.INFINITE);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        AnimationSet animatorSet = new AnimationSet(true);
        animatorSet.setDuration(time);
        animatorSet.addAnimation(scaleAnimationrlScan);
        animatorSet.addAnimation(alphaAnimation);
        animatorSet.setRepeatMode(AnimationSet.RESTART);
        view.startAnimation(animatorSet);
    }

    private void initInfoData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(TAG_MY_WALLET_URL, MFUrl.MY_WALLET_URL, requestParams, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_exitdeposit:
                depositClick();
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

    private void initDepositData(int tag) {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
        if (TAG_GETDEPOSITCONFIGCHECK == tag) {
            dialogShow();
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
        MFRunner.requestPost(tag, MFUrl.GETDEPOSITCONFIG, requestParams, this);
    }

    private void depositClick() {
        if (myWalletBean == null) {
            return;
        }
        switch (myWalletBean.getDepositState()) {// 0未缴纳 1正常 2退款中 3冻结
            case 0:
                initInfoDataCheck();
                break;
            case 1:
                initDepositData(TAG_GETDEPOSITCONFIGCHECK);
                break;
            case 2:
                OneDialog oneDialog2 = new OneDialog(mContext, "通知", "正在退款中，退款时间为1-7个工作日，在退款期间押金将被冻结，无法进行操作,您的账号将无法继续租用车辆", "我知道了");
                oneDialog2.setDialogClickListener(new DialogClickListener() {
                    @Override
                    public void onLeftClick(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void onRightClick(View v, Dialog d) {
                        d.dismiss();
                    }
                });
                oneDialog2.show();
                break;
            case 3:
                TwoDialog oneDialog3 = new TwoDialog(mContext, "通知", "押金已经冻结，无法进行操作，冻结期间您的账号无法租用车辆，如有问题请咨询客服", "咨询客服", "我知道了");
                oneDialog3.setDialogClickListener(new DialogClickListener() {
                    @Override
                    public void onLeftClick(View v, Dialog d) {
                        try {
                            if (isInServiceTime()) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                        .parse("tel:" + SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_CALLNUMBER, "13511010167")));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                mContext.overridePendingTransition(
                                        R.anim.activity_up, R.anim.activity_push_no_anim);
                            } else {
                                showOutServiceTimeDialog();
                            }
                        } catch (Exception e) {
                            MFUtil.showToast(mContext, "请检查是否开启电话权限");
                        } finally {
                            d.dismiss();
                        }
                    }

                    @Override
                    public void onRightClick(View v, Dialog d) {
                        d.dismiss();
                    }
                });
                oneDialog3.show();
                break;
            case 4:
                TwoDialog twoDialog2 = new TwoDialog(mContext, "由于您存在用车不当行为，您的押金已经被冻结，详细信息请联系客服电话咨询", "取消", "联系客服");
                twoDialog2.setDialogClickListener(new DialogClickListener() {
                    @Override
                    public void onLeftClick(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void onRightClick(View v, Dialog d) {
                        try {
                            if (isInServiceTime()) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                        .parse("tel:" + SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_CALLNUMBER, "13511010167")));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                mContext.overridePendingTransition(
                                        R.anim.activity_up, R.anim.activity_push_no_anim);
                            } else {
                                showOutServiceTimeDialog();
                            }
                        } catch (Exception e) {
                            MFUtil.showToast(mContext, "请检查是否开启电话权限");
                        } finally {
                            d.dismiss();
                        }
                    }
                });
                twoDialog2.show();
                break;
        }
    }

    @Override
    public void onStart(int tag, RequestParams params) {

    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {
        switch (tag) {
            case TAG_MY_WALLET_URL://按钮显示内容
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<MyWalletBean> myWalletBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MyWalletBean>>() {
                }.getType());
                if (myWalletBeanRequestResultBean != null && myWalletBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    myWalletBean = myWalletBeanRequestResultBean.getData();
                    if (myWalletBean == null) {
                        return;
                    }
                    switch (myWalletBean.getDepositState()) {// 0未缴纳 1正常 2退款中 3冻结
                        case 0:
                            tvExitdeposit.setText("您还未交纳押金");
                        case 1:
                            tvExitdeposit.setText("退回押金" +
                                    MFUtil.formatDoubleValue(Double.parseDouble(myWalletBean.getDeposit() + ""), "0.#") + "元");
                            break;
                        case 2:
                            tvExitdeposit.setText("押金正在退款中");
                            break;
                        case 3:
                            tvExitdeposit.setText("押金已冻结");
                            break;
                    }
                } else if (myWalletBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == myWalletBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (myWalletBeanRequestResultBean != null) {
                    MFUtil.showToast(mContext, myWalletBeanRequestResultBean.getMessage());
                }
                break;
            case TAG_MY_WALLET_URL_CHECK://二次检查押金是否已经充值
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<MyWalletBean> myWalletBeanRequestResultBeanCheck = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MyWalletBean>>() {
                }.getType());
                if (myWalletBeanRequestResultBeanCheck != null && myWalletBeanRequestResultBeanCheck.getCode() == MFConstansValue.BACK_SUCCESS) {
                    myWalletBean = myWalletBeanRequestResultBeanCheck.getData();
                    if (myWalletBean != null && myWalletBean.getDepositState() == 0) {
                        MobclickAgent.onEvent(mContext, "click_mywallet_cardeposit");
                        startActivity(CarDepositAct.class, null);
                    } else {
                        tvExitdeposit.setText("退回押金" +
                                MFUtil.formatDoubleValue(Double.parseDouble(myWalletBean.getDeposit() + ""), "0.#") + "元");
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
            case TAG_APPLYDEPOSITBACK:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean requestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (requestResultBean != null && requestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    mContext.finish();
                } else if (requestResultBean != null && MFConstansValue.BACK_LOGOUT == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (requestResultBean != null && 1012 == requestResultBean.getCode()) {
                    TwoDialog oneDialog = new TwoDialog(mContext, "由于您存在用车不当行为，您的押金已被冻结，详细信息请联系客服电话咨询", "取消", "咨询客服");
                    oneDialog.setDialogClickListener(new DialogClickListener() {
                        @Override
                        public void onLeftClick(View v, Dialog d) {
                            d.dismiss();
                        }

                        @Override
                        public void onRightClick(View v, Dialog d) {
                            try {
                                if (isInServiceTime()) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                            .parse("tel:" + SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_CALLNUMBER, "13511010167")));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    mContext.overridePendingTransition(
                                            R.anim.activity_up, R.anim.activity_push_no_anim);
                                } else {
                                    showOutServiceTimeDialog();
                                }
                            } catch (Exception e) {
                                MFUtil.showToast(mContext, "请检查是否开启电话权限");
                            } finally {
                                d.dismiss();
                            }
                        }
                    });
                    oneDialog.show();
                } else if (requestResultBean != null && 1025 == requestResultBean.getCode()) {
                    startActivity(CommitAliCountAct.class, null);
                } else if (requestResultBean != null) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                }
                break;
            case TAG_GETDEPOSITCONFIGCHECK:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<CarDepositVO> carDepositVORequestResultBeanCheck = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<CarDepositVO>>() {
                }.getType());
                if (carDepositVORequestResultBeanCheck != null && carDepositVORequestResultBeanCheck.getCode() == MFConstansValue.BACK_SUCCESS) {
                    if (carDepositVORequestResultBeanCheck.getData() != null) {
                        Double depositAmount = carDepositVORequestResultBeanCheck.getData().getDepositAmount();
                        Double depositnum = Double.parseDouble(myWalletBean.getDeposit() + "");
                        String msg = "押金退还时间为1-7个工作日，在退款期间您的账号将无法继续租用车辆，请问是否继续退还押金";
                        if (depositAmount > depositnum) {
                            msg = "小蜜蜂押金已调整至" + MFUtil.formatDoubleValue(Double.parseDouble(depositAmount + ""), "0.#") + "元，本次退押金后，下次使用时需缴纳" + MFUtil.formatDoubleValue(Double.parseDouble(depositAmount + ""), "0.#") + "元押金，请知晓。";
                        }
                        TwoDialog twoDialog1 = new TwoDialog(mContext, "通知", msg, "取消", "退还押金");
                        twoDialog1.setDialogClickListener(new DialogClickListener() {
                            @Override
                            public void onLeftClick(View v, Dialog d) {
                                d.dismiss();
                            }

                            @Override
                            public void onRightClick(View v, Dialog d) {
                                MobclickAgent.onEvent(mContext, "click_mywallet_exitcardeposit");
                                getDepositBack();
                                d.dismiss();
                            }
                        });
                        twoDialog1.show();
                    }
                } else if (carDepositVORequestResultBeanCheck != null && MFConstansValue.BACK_LOGOUT == carDepositVORequestResultBeanCheck.getCode()) {
                    MFUtil.showToast(mContext, carDepositVORequestResultBeanCheck.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (carDepositVORequestResultBeanCheck != null && (MFConstansValue.BACK_SYSTEMERROR == carDepositVORequestResultBeanCheck.getCode() || MFConstansValue.BACK_BUSINESS == carDepositVORequestResultBeanCheck.getCode())) {
                    MFUtil.showToast(mContext, carDepositVORequestResultBeanCheck.getMessage());
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        if (!isAdded()) {
            return;
        }
        switch (tag) {
            case TAG_MY_WALLET_URL:
                MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
                dismmisDialog();
                break;
        }
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getDepositBack() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(TAG_APPLYDEPOSITBACK, MFUrl.APPLYDEPOSITBACK, params, this);
    }
}
