package com.mmuu.travel.client.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.mmuu.travel.client.bean.ADBean;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.ShareBean;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.order.OrderDetailVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ShareUtils;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.ui.user.CertificationAct;
import com.mmuu.travel.client.ui.user.LoginAct;
import com.mmuu.travel.client.ui.user.TripFeedbackDetailAct;
import com.mmuu.travel.client.widget.StatusView;
import com.mmuu.travel.client.widget.dialog.ADOrderEndDialog;
import com.mmuu.travel.client.widget.dialog.EndOrderReportDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.tauth.IUiListener;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 结束行程frg
 * JiaGuangWei on 2016/12/22 15:11
 */
public class RunEndFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {

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
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_creditadded)
    TextView tvCreditadded;
    @BindView(R.id.tv_reduction)
    TextView tvReduction;
    @BindView(R.id.tv_reduction0)
    TextView tvReduction0;
    @BindView(R.id.tv_reduction1)
    TextView tvReduction1;
    @BindView(R.id.tv_reduction2)
    TextView tvReduction2;
    @BindView(R.id.tv_savecarbon)
    TextView tvSavecarbon;
    @BindView(R.id.tv_savetaximoney)
    TextView tvSavetaximoney;
    @BindView(R.id.rl_rundetail)
    RelativeLayout rlRundetail;
    @BindView(R.id.rl_runshare)
    RelativeLayout rlRunshare;
    @BindView(R.id.tv_cerfication_getmoney)
    TextView tv_cerfication_getmoney;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private View runEndView;

    private OrderDetailVO orderDetailVO;
    private static final int TAG_INVITATION_URL = 1695701;
    private ShareBean shareBean = null;
    private Bitmap bitmap;
    private IUiListener iUiListener;
    private long exitTime = 0;
    private static final int TAG_GETADBYTYPE = 1695702;
    private static final int REQUESTCODE_CERTIFICATION = 1001;
    private ADBean adBean;
    private EndOrderReportDialog reportDialog;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (runEndView == null) {
            runEndView = inflater.inflate(R.layout.frg_runend, null);
            ButterKnife.bind(this, runEndView);
            initView();
//            getADData();
            initData();
        }
        return runEndView;
    }

    public void setIUiListener(IUiListener iUiListener) {
        this.iUiListener = iUiListener;
    }

    private void initData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        params.addBodyParameter("activityId", "4");//20171220产品说与行程详情分享一样，所以有1改成了7
        MFRunner.requestPost(TAG_INVITATION_URL, MFUrl.FINDACTIVITYINFO, params, this);
    }

    private void getADData() {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "4");
        PersonalCenterVo tempVoAD = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                PersonalCenterVo.class);
        if (tempVoAD != null && !TextUtils.isEmpty(tempVoAD.getLoginCityCode())) {
            params.addBodyParameter("cityName", tempVoAD.getCityName());
            params.addBodyParameter("cityCode", tempVoAD.getLoginCityCode());
        } else if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
            params.addBodyParameter("cityName", MFApp.getInstance().getmLocation().getCity());
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
        }
        MFRunner.requestPost(TAG_GETADBYTYPE, MFUrl.GETADBYTYPE, params, this);
    }

    private void initView() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleTitle.setText(R.string.runend);
        titleRightText.setVisibility(View.VISIBLE);
        titleRightText.setText("故障上报");
        orderDetailVO = (OrderDetailVO) getActivity().getIntent().getSerializableExtra("OrderDetailVO");

        PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                PersonalCenterVo.class);
        if (personalCenterVo != null && (personalCenterVo.getRealNameState() == 0)) {
            int runEndCertificationTimes = SharedPreferenceTool.getPrefInt(mContext, "runEndCertificationTimes", 0);
            if (runEndCertificationTimes < 5) {
                llBottom.setVisibility(View.VISIBLE);
                SharedPreferenceTool.setPrefInt(mContext, "runEndCertificationTimes", ++runEndCertificationTimes);
            } else {
                llBottom.setVisibility(View.GONE);
            }
        } else {
            llBottom.setVisibility(View.GONE);
        }

        if (orderDetailVO != null) {
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateOrderStatus(orderDetailVO.getOrderId());
                }
            }, 1000);

            if (orderDetailVO.getIsInvalidOrder() == 1) {
                getBaseHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showReportDialog();
                    }
                }, 350);
            } else {

                boolean showADDialog = false;
                if (!TextUtils.isEmpty(orderDetailVO.getMileage())) {
                    double m = Double.valueOf(orderDetailVO.getMileage());
                    if (m >= 100) {
                        showADDialog = true;
                    }
                }
                if (!TextUtils.isEmpty(orderDetailVO.getUseTime())) {
                    double t = Double.valueOf(orderDetailVO.getUseTime());
                    if (t >= 2) {
                        showADDialog = true;
                    }
                }
                if (showADDialog) {
                    getADData();
                }
            }
            if (!"null".equals(orderDetailVO.getBalanceAmount()) && !TextUtils.isEmpty(orderDetailVO.getBalanceAmount())) {
                double moneyPay = Double.parseDouble(orderDetailVO.getBalanceAmount());
                tvMoney.setText("余额支付 " + MFUtil.formatDoubleValue(moneyPay <= 0 ? 0 : moneyPay, "0.#") + " 元");
            }
            if (TextUtils.isEmpty(orderDetailVO.getIntegral()) || "0".equals(orderDetailVO.getIntegral())) {
                tvCreditadded.setText("信用+" + 0);
                tvCreditadded.setVisibility(View.GONE);
            } else {
                tvCreditadded.setText("信用+" + orderDetailVO.getIntegral());
                tvCreditadded.setVisibility(View.VISIBLE);
            }

            tvReduction.setText(null);
            if ("null".equals(orderDetailVO.getAmount()) ||
                    TextUtils.isEmpty(orderDetailVO.getAmount()) ||
                    Double.parseDouble(orderDetailVO.getAmount()) == 0
                    ) {
                tvReduction.setText("总费用" + MFUtil.formatDoubleValue(Double.parseDouble("0"), "0.#") + "元");
                tvReduction.setVisibility(View.VISIBLE);
            } else {
                tvReduction.setText("总费用" + MFUtil.formatDoubleValue(Double.parseDouble(orderDetailVO.getAmount()), "0.#") + "元");
                tvReduction.setVisibility(View.VISIBLE);
            }

            tvReduction0.setVisibility(View.GONE);
            tvReduction1.setText(null);
            tvReduction2.setText(null);
            if ("null".equals(orderDetailVO.getCityDiscountAmount()) ||
                    TextUtils.isEmpty(orderDetailVO.getCityDiscountAmount()) ||
                    Double.parseDouble(orderDetailVO.getCityDiscountAmount()) == 0
                    ) {
            } else {
                tvReduction0.setVisibility(View.VISIBLE);
                tvReduction1.setText("活动优惠" + MFUtil.formatDoubleValue(Double.parseDouble(orderDetailVO.getCityDiscountAmount()), "0.#") + "元");
                tvReduction1.setVisibility(View.VISIBLE);
            }
            if ("null".equals(orderDetailVO.getCouponAmount()) ||
                    TextUtils.isEmpty(orderDetailVO.getCouponAmount()) ||
                    Double.parseDouble(orderDetailVO.getCouponAmount()) == 0) {
            } else {
                tvReduction0.setVisibility(View.VISIBLE);
                tvReduction2.setText("出行券抵扣" + MFUtil.formatDoubleValue(Double.parseDouble(orderDetailVO.getCouponAmount()), "0.#") + "元");
                tvReduction2.setVisibility(View.VISIBLE);
            }

            tvSavecarbon.setText(MFUtil.formatDoubleValue(Double.parseDouble(orderDetailVO.getCarbonEmission()), "0.#") + "克");
            tvSavetaximoney.setText(MFUtil.formatDoubleValue(Double.parseDouble(orderDetailVO.getSaveBikeFfare()), "0.#") + "元");

            tv_cerfication_getmoney.setOnClickListener(this);
            titleLeftImage.setOnClickListener(this);
            rlRundetail.setOnClickListener(this);
            rlRunshare.setOnClickListener(this);
            titleRightText.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        if ((System.currentTimeMillis() - exitTime) < 800) {
            return;
        }
        exitTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.rl_runshare:
                showBottomDialog(shareBean);
                break;
            case R.id.tv_cerfication_getmoney:
                Bundle bundleCertificationAction = new Bundle();
                bundleCertificationAction.putInt("typeentrance", 2);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                startActivityForResult(CertificationAct.class, bundleCertificationAction, REQUESTCODE_CERTIFICATION);
                break;
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.rl_rundetail:
                if (orderDetailVO == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_runend_lookdetail");
                Bundle bundleSeedetail = new Bundle();
                bundleSeedetail.putString("orderId", orderDetailVO.getOrderId() + "");
                startActivity(RunDetailAct.class, bundleSeedetail);
                break;
            case R.id.title_right_text:
                if (orderDetailVO == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_runend_feelback");
                Intent intent = new Intent(mContext, TripFeedbackDetailAct.class);
                Bundle bundleProblem = new Bundle();
                bundleProblem.putString("pagetype", MFConstansValue.FEELBACK_TRIPBACK);
                bundleProblem.putString("bikeCode", orderDetailVO.getBikeCode() + "");
                bundleProblem.putString("orderId", orderDetailVO.getOrderId() + "");
                intent.putExtras(bundleProblem);
                mContext.startActivity(intent);
                break;
            case R.id.ll_shareqqspace:
                if (shareBean == null || iUiListener == null || bitmap == null || orderDetailVO == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_runend_shareqqspace");

                String urlQQspace = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                        , "orderId=" + orderDetailVO.getOrderId()
                );
                ShareUtils.getIntance(mContext).shareToTencent(urlQQspace, shareBean.getShareTitle(), shareBean.getShareDescription(), shareBean.getSharePic(), iUiListener);
                dialog.dismiss();
                break;
            case R.id.ll_shareweibo:
                if (shareBean == null || bitmap == null || orderDetailVO == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_runend_shareweibo");
                String urlWeibo = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                        , "orderId=" + orderDetailVO.getOrderId()
                );
                ShareUtils.getIntance(mContext).shareWeiboMultiMessage("暂无附带内容", shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), urlWeibo, "RunEndFrg");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixin:
                if (shareBean == null || bitmap == null || orderDetailVO == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_runend_shareweixin");
                String urlWeixin = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                        , "orderId=" + orderDetailVO.getOrderId()
                );
                ShareUtils.getIntance(mContext).shareToWeiXin(urlWeixin, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "RunDetailFrg");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixincircle:
                if (shareBean == null || bitmap == null || orderDetailVO == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_runend_shareweixincircle");
                String weixincircle = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                        , "orderId=" + orderDetailVO.getOrderId()
                );
                ShareUtils.getIntance(mContext).shareToWeiXinCircle(weixincircle, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "RunDetailFrg");
                dialog.dismiss();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUESTCODE_CERTIFICATION) {
            PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                    PersonalCenterVo.class);
            personalCenterVo.setRealNameState(1);
            SharedPreferenceTool.setPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN,
                    GsonTransformUtil.toJson(personalCenterVo));
            if (personalCenterVo != null && (personalCenterVo.getRealNameState() == 0)) {
                int runEndCertificationTimes = SharedPreferenceTool.getPrefInt(mContext, "runEndCertificationTimes", 0);
                if (runEndCertificationTimes < 5) {
                    llBottom.setVisibility(View.VISIBLE);
                    SharedPreferenceTool.setPrefInt(mContext, "runEndCertificationTimes", ++runEndCertificationTimes);
                } else {
                    llBottom.setVisibility(View.GONE);
                }
            } else {
                llBottom.setVisibility(View.GONE);
            }
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
            case TAG_INVITATION_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<ShareBean> shareBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<ShareBean>>() {
                }.getType());
                if (shareBeanRequestResultBean != null && shareBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    shareBean = shareBeanRequestResultBean.getData();
                    if (shareBean == null) {
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bitmap = ImageLoader.getInstance().loadImageSync((shareBean.getSharePic()));
                        }
                    }).start();
                } else if (shareBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == shareBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, shareBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (shareBeanRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == shareBeanRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == shareBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, shareBeanRequestResultBean.getMessage());
                }
                break;
            case TAG_GETADBYTYPE:
                if (!isAdded()) {
                    return;
                }
                String getAdDataInfoString = responseInfo.result.toString();
                RequestResultBean<ADBean> adBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(getAdDataInfoString, new TypeToken<RequestResultBean<ADBean>>() {
                }.getType());
                if (adBeanRequestResultBean == null || adBeanRequestResultBean.getData() == null || MFConstansValue.BACK_SUCCESS != adBeanRequestResultBean.getCode()) {
                    return;
                }
                adBean = adBeanRequestResultBean.getData();
                long lastShowTime = 0;
                if (!TextUtils.isEmpty(SharedPreferenceTool.getPrefString(mContext, "adtype4", ""))) {
                    String[] adInfo = SharedPreferenceTool.getPrefString(mContext, "adtype4", "").split("，");
                    if (adInfo != null && adInfo.length != 2) {
                        return;
                    }
                    Long adId = Long.parseLong(adInfo[0]);
                    if (adBean.getId() == adId) {
                        lastShowTime = Long.parseLong(adInfo[1]);
                    } else {
                        SharedPreferenceTool.setPrefString(mContext, "adtype4", "");
                    }
                }
                if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && (new Date().getTime() - lastShowTime) >
                        adBean.getActiveFrequency() * 1000 * 60) {
                    String imgrul = null;
                    if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && adBean.getPhotoUrl().contains("oss")) {
                        imgrul = adBean.getPhotoUrl().replace("oss", "img") + "@500w.jpg";
                    } else {
                        imgrul = adBean.getPhotoUrl();
                    }

                    ImageLoader.getInstance().loadImage(imgrul, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            if (!isAdded()) {
                                return;
                            }
                            SharedPreferenceTool.setPrefString(mContext, "adtype4", adBean.getId() + "，" + new Date().getTime());
                            sendStatistics("3003", "show", MFUtil.getValueForURL("cityCode", adBean.getAdUrl()), adBean.getId() + "");
                            final ADOrderEndDialog adDialog = new ADOrderEndDialog(mContext);
                            adDialog.setImageUrl(s);
                            adDialog.setAdBean(adBean);
                            adDialog.setBaseFragment(RunEndFrg.this);
                            adDialog.setCanceledOnTouchOutside(false);
                            getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isAdded()) {
                                        return;
                                    }
                                    adDialog.show();
                                }
                            }, 300);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }
                break;
            case 98765:
                RequestResultBean reportBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (reportBean != null && reportBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MFUtil.showToast(mContext, reportBean.getMessage());
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

    private void updateOrderStatus(String orderID) {

        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        // detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
        detailParams.addBodyParameter("orderId", orderID);
        //detailParams.addBodyParameter("status", "1");
        MFRunner.requestPost(1002, MFUrl.updateView, detailParams, this);
    }


    private void showReportDialog() {

        if (reportDialog == null) {
            reportDialog = new EndOrderReportDialog(mContext, "提交");
            reportDialog.setDialogClickListener(new DialogClickListener() {
                @Override
                public void onLeftClick(View v, Dialog d) {
                    d.dismiss();
                }

                @Override
                public void onRightClick(View v, Dialog d) {
                    if (!MFUtil.checkNetwork(mContext)) {
                        showNoNetworkDialog(true);
                        return;
                    }
                    d.dismiss();
                    reportOrder(((EndOrderReportDialog) d).getCheck());
                }
            });
        }

        if (reportDialog != null && !reportDialog.isShowing()) {
            reportDialog.show();
        }
    }


    private void reportOrder(String reason) {

        if (orderDetailVO != null) {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("orderId", orderDetailVO.getOrderId());
            detailParams.addBodyParameter("reason", reason);
            MFRunner.requestPost(98765, MFUrl.ORDERREASON, detailParams, this);
        }
    }

    private void showBottomDialog(ShareBean shareBean) {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_sharerbottom, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);


            TextView tv_titletxt = mDlgCallView
                    .findViewById(R.id.tv_titletxt);
            if (shareBean != null) {
//                tv_titletxt.setText(shareBean.getReward());
                tv_titletxt.setText("分享至");
            }
            View v_line = mDlgCallView
                    .findViewById(R.id.v_line);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                v_line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            LinearLayout ll_shareqqspace = mDlgCallView
                    .findViewById(R.id.ll_shareqqspace);
            LinearLayout ll_shareweibo = mDlgCallView
                    .findViewById(R.id.ll_shareweibo);
            LinearLayout ll_shareweixin = mDlgCallView
                    .findViewById(R.id.ll_shareweixin);
            LinearLayout ll_shareweixincircle = mDlgCallView
                    .findViewById(R.id.ll_shareweixincircle);
            ll_shareqqspace.setOnClickListener(this);
            ll_shareweibo.setOnClickListener(this);
            ll_shareweixin.setOnClickListener(this);
            ll_shareweixincircle.setOnClickListener(this);

            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgCallView);
        }
        dialog.show();
    }

}
