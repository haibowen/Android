package com.mmuu.travel.client.ui.main;

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
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.ShareBean;
import com.mmuu.travel.client.bean.order.RunDetailBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ShareUtils;
import com.mmuu.travel.client.ui.user.LoginAct;
import com.mmuu.travel.client.ui.user.TripFeedbackDetailAct;
import com.mmuu.travel.client.widget.StatusView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.tauth.IUiListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行程详情frg
 */
public class RunDetailFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {

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
    @BindView(R.id.tv_carcode)
    TextView tvCarcode;
    @BindView(R.id.tv_mileage)
    TextView tvMileage;
    @BindView(R.id.tv_mileage_m)
    TextView tvMileageM;
    @BindView(R.id.tv_reducecarbon)
    TextView tvReducecarbon;
    @BindView(R.id.tv_runtime)
    TextView tvRuntime;
    @BindView(R.id.tv_runmoney)
    TextView tvRunmoney;
    @BindView(R.id.ll_runinfo)
    LinearLayout llRuninfo;
    @BindView(R.id.tv_starttime)
    TextView tvStarttime;
    @BindView(R.id.tv_endtime)
    TextView tvEndtime;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.v_line_coupon)
    View vLineCoupon;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.rl_coupon)
    RelativeLayout rlCoupon;
    @BindView(R.id.v_line_activity_coupon)
    View vLineActivityCoupon;
    @BindView(R.id.tv_activity_coupon)
    TextView tvActivityCoupon;
    @BindView(R.id.rl_activity_coupon)
    RelativeLayout rlActivityCoupon;
    private View runDetailView;
    private static final int TAG_STROKEDETAIL_URL = 1695701;
    private static final int TAG_INVITATION_URL = 1695702;
    private RunDetailBean runDetailBean;
    private Dialog dialog;
    private ShareBean shareBean = null;
    private Bitmap bitmap;
    private IUiListener iUiListener;
    private Bundle bundle;
    private long exitTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (runDetailView == null) {
            runDetailView = inflater.inflate(R.layout.frg_rundetail, null);
            ButterKnife.bind(this, runDetailView);
            initView();
            initData();
            initShare();
        }
        return runDetailView;
    }

    public void setIUiListener(IUiListener iUiListener) {
        this.iUiListener = iUiListener;
    }

    private void initShare() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("activityId", "4");
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(TAG_INVITATION_URL, MFUrl.FINDACTIVITYINFO, params, this);
    }

    private void initData() {
        if (mContext.getIntent() != null) {
            bundle = mContext.getIntent().getExtras();
        }
        if (bundle == null || !bundle.containsKey("orderId")) {
            return;
        }
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        params.addBodyParameter("orderId", bundle.get("orderId") + "");
        MFRunner.requestPost(TAG_STROKEDETAIL_URL, MFUrl.STROKEDETAIL, params, this);
    }

    private void initView() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText(R.string.rundetail);
        titleRightText.setText("行程反馈");
        titleRightText.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector, null));
        } else {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector));
        }
        tvShare.setOnClickListener(this);
        titleRightText.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if ((System.currentTimeMillis() - exitTime) < 800) {
            return;
        }
        exitTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_share:
                showBottomDialog();
                break;
            case R.id.ll_shareqqspace:
                if (shareBean == null || iUiListener == null || bitmap == null || bundle == null || !bundle.containsKey("orderId")) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_rundetail_shareqqspace");
                String urlQQspace = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "orderId=" + bundle.get("orderId")
                );
                ShareUtils.getIntance(mContext).shareToTencent(urlQQspace, shareBean.getShareTitle(), shareBean.getShareDescription(), shareBean.getSharePic(), iUiListener);
                dialog.dismiss();
                break;
            case R.id.ll_shareweibo:
                if (shareBean == null || bitmap == null || bundle == null || !bundle.containsKey("orderId")) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_rundetail_shareweibo");
                String urlWeibo = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "orderId=" + bundle.get("orderId")
                );
                ShareUtils.getIntance(mContext).shareWeiboMultiMessage("暂无附带内容", shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32),
                        urlWeibo, "RunDetailFrg");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixin:
                if (shareBean == null || bitmap == null || bundle == null || !bundle.containsKey("orderId")) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_rundetail_shareweixin");
                String urlWeixin = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "orderId=" + bundle.get("orderId")
                );
                ShareUtils.getIntance(mContext).shareToWeiXin(urlWeixin, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "RunDetailFrg");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixincircle:
                if (shareBean == null || bitmap == null || bundle == null || !bundle.containsKey("orderId")) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_rundetail_shareweixincircle");
                String urlWeixincircle = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "orderId=" + bundle.get("orderId")
                );
                ShareUtils.getIntance(mContext).shareToWeiXinCircle(urlWeixincircle, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "RunDetailFrg");
                dialog.dismiss();
                break;
            case R.id.title_right_text:
                if (bundle == null || !bundle.containsKey("orderId") || runDetailBean == null) {
                    return;
                }
                Intent intent = new Intent(mContext, TripFeedbackDetailAct.class);
                Bundle bundle = new Bundle();
                bundle.putString("pagetype", MFConstansValue.FEELBACK_TRIPBACK);
                bundle.putString("bikeCode", runDetailBean.getBikeCode() + "");
                bundle.putString("orderId", bundle.get("orderId") + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
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
            case TAG_INVITATION_URL:
                if (!isAdded()) {
                    return;
                }
                RequestResultBean<ShareBean> shareBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<ShareBean>>() {
                }.getType());
                if (shareBeanRequestResultBean != null && shareBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    shareBean = shareBeanRequestResultBean.getData();
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
            case TAG_STROKEDETAIL_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                String systemInfoString = responseInfo.result.toString();
                RequestResultBean<RunDetailBean> runDetailBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(systemInfoString, new TypeToken<RequestResultBean<RunDetailBean>>() {
                }.getType());
                if (runDetailBeanRequestResultBean != null && MFConstansValue.BACK_SUCCESS == runDetailBeanRequestResultBean.getCode()) {
                    runDetailBean = runDetailBeanRequestResultBean.getData();
                    tvCarcode.setText("No：" + runDetailBean.getBikeCode() + "");

                    Double mileage = Double.parseDouble(TextUtils.isEmpty(runDetailBean.getMileage()) ? "0" : runDetailBean.getMileage());
                    if (mileage < 1000) {
                        tvMileage.setText(MFUtil.formatDoubleValue(mileage, "0.#") + "");
                        tvMileageM.setText("米");
                    } else {
                        tvMileage.setText(MFUtil.formatDoubleValue((mileage / 1000.000), "0.#") + "");
                        tvMileageM.setText("公里");
                    }
                    tvReducecarbon.setText(MFUtil.formatDoubleValue(Double.parseDouble(TextUtils.isEmpty(runDetailBean.getCarbonEmission()) ? "0" : runDetailBean.getCarbonEmission()), "0.#") + "");
                    tvRuntime.setText(MFUtil.formatDoubleValue(Double.parseDouble(TextUtils.isEmpty(runDetailBean.getSumTime()) ? "0" : runDetailBean.getSumTime()), "0") + "");
                    tvRunmoney.setText(
                            MFUtil.formatDoubleValue(Double.parseDouble(TextUtils.isEmpty(runDetailBean.getAmount()) ? "0" : runDetailBean.getAmount()), "0.0") + "");
                    tvStarttime.setText(runDetailBean.getChargeTime().length() == 21 ? runDetailBean.getChargeTime().substring(0, 19) : runDetailBean.getChargeTime() + "");
                    tvEndtime.setText(runDetailBean.getEndTime().length() == 21 ? runDetailBean.getEndTime().substring(0, 19) : runDetailBean.getEndTime() + "");
                    tvCoupon.setText(MFUtil.formatDoubleValue(Double.parseDouble(TextUtils.isEmpty(runDetailBean.getCouponAmount()) ? "0" : runDetailBean.getCouponAmount()), "0.0") + "元");
                    tvMoney.setText(MFUtil.formatDoubleValue(Double.parseDouble(
                            TextUtils.isEmpty(runDetailBean.getBalanceAmount()) ? "0" : runDetailBean.getBalanceAmount()), "0.0") + "元");


                    Double coupon = Double.parseDouble(TextUtils.isEmpty(runDetailBean.getCouponAmount()) ? "0" : runDetailBean.getCouponAmount());
                    if (coupon == 0) {
                        rlCoupon.setVisibility(View.GONE);
                        vLineCoupon.setVisibility(View.GONE);
                    } else {
                        rlCoupon.setVisibility(View.VISIBLE);
                        vLineCoupon.setVisibility(View.VISIBLE);
                        tvCoupon.setText(MFUtil.formatDoubleValue(coupon, "0.0") + "元");
                    }
                    Double activityCoupon = Double.parseDouble(TextUtils.isEmpty(runDetailBean.getActivityAmount()) ? "0" : runDetailBean.getActivityAmount());
                    if (activityCoupon == 0) {
                        rlActivityCoupon.setVisibility(View.GONE);
                        vLineActivityCoupon.setVisibility(View.GONE);
                    } else {
                        rlActivityCoupon.setVisibility(View.VISIBLE);
                        vLineActivityCoupon.setVisibility(View.VISIBLE);
                        tvActivityCoupon.setText(MFUtil.formatDoubleValue(activityCoupon, "0.0") + "元");
                    }
                } else if (runDetailBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == runDetailBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, runDetailBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (runDetailBeanRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == runDetailBeanRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == runDetailBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, runDetailBeanRequestResultBean.getMessage());
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

    private void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_sharerbottom, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);


            TextView tv_titletxt = mDlgCallView
                    .findViewById(R.id.tv_titletxt);
            tv_titletxt.setText("分享至");
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