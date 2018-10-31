package com.mmuu.travel.client.ui.user;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.ShareBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.ShareUtils;
import com.mmuu.travel.client.ui.other.CreditWebAty;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.widget.RoundIndicatorView;
import com.tencent.tauth.IUiListener;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 信用积分frg
 * JiaGuangWei on 2016/12/22 20:44
 */
public class CreditPointFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    //    @BindView(R.id.wave_view)
//    WaveView waveView;
    @BindView(R.id.rl_credit_record)
    RelativeLayout rlCreditRecord;
    @BindView(R.id.tv_drawtime)
    TextView tvDrawtime;
    @BindView(R.id.rl_draw)
    RelativeLayout rlDraw;
    @BindView(R.id.rl_share_credit)
    RelativeLayout rlShareCredit;
    @BindView(R.id.tv_credit_draw_rule)
    TextView tvCreditDrawRule;
    @BindView(R.id.credit_view)
    RoundIndicatorView creditView;
    @BindView(R.id.tv_credit_level)
    TextView tvCreditLevel;

    private View creditpointView;
    private View creditpointView2;
    //    private WaveHelper mWaveHelper;
    private IUiListener iUiListener;
    private static final int Tag_SHOWINTEGRALDRAWCOUNT = 1695701;
    private static final int Tag_SHOWINTEGRALDRAWCOUNT_RESUME = 1695703;
    private Dialog dialog;
    private Bitmap bitmap;
    private static final int TAG_INVITATION_URL = 1695702;
    private ShareBean shareBean = null;
    private boolean isfirst = true;
    private long exitTime = 0;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (creditpointView == null) {
            creditpointView = inflater.inflate(R.layout.frg_creditpoint, null);
            ButterKnife.bind(this, creditpointView);
            initView();
            initData();
            initShareUrl();
        }
        return creditpointView;
    }

    private void initData() {
        Bundle bundle = mContext.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("point")) {
            int point = Integer.parseInt(bundle.getString("point"));
            if (point >= 100) {
                tvCreditLevel.setText(mContext.getResources().getString(R.string.creditpoint_good));
            } else if (point < 100 && point >= 80) {
                tvCreditLevel.setText(mContext.getResources().getString(R.string.creditpoint_ok));
            } else {
                tvCreditLevel.setText(mContext.getResources().getString(R.string.creditpoint_bad));
            }
            creditView.setMaxNum(getMaxNum(point));
            creditView.setCurrentNumAnim(point, 1000);
            initShareView(point);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvCreditLevel.setVisibility(View.VISIBLE);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    TranslateAnimation translateAnimation = new TranslateAnimation(
                            0, 0, -ScreenUtil.dip2px(mContext, 20), 0);
                    AnimationSet animationSet = new AnimationSet(true);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.addAnimation(translateAnimation);
                    animationSet.setDuration(500);
                    animationSet.setInterpolator(new DecelerateInterpolator(1.5f));
                    animationSet.setFillAfter(true);
                    tvCreditLevel.startAnimation(animationSet);
                }
            }, 1000);
//            tvCreditDrawRule.setText(bundle.getString("message1") + "，" + bundle.getString("message2"));
            tvCreditDrawRule.setText(bundle.getString("message2"));
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initDataShowIntegralDrawCount();
                }
            }, 1300);
        }
    }

    private void initShareUrl() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("activityId", "5");
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(TAG_INVITATION_URL, MFUrl.FINDACTIVITYINFO, params, this);

    }

    private void initShareView(int point) {
        creditpointView2 = mContext.getLayoutInflater().from(mContext).inflate(R.layout.view_sharecreditpoint, null);
        RoundIndicatorView roundIndicatorView = (RoundIndicatorView) creditpointView2.findViewById(R.id.credit_view);
        TextView tv_credit_level = (TextView) creditpointView2.findViewById(R.id.tv_credit_level);
        roundIndicatorView.setMaxNum(getMaxNum(point));
        roundIndicatorView.setCurrentNumAnim(point, 0);
        if (point >= 100) {
            tv_credit_level.setText(mContext.getResources().getString(R.string.creditpoint_good));
        } else if (point < 100 && point >= 80) {
            tv_credit_level.setText(mContext.getResources().getString(R.string.creditpoint_ok));
        } else {
            tv_credit_level.setText(mContext.getResources().getString(R.string.creditpoint_bad));
        }
    }

    private void initDataShowIntegralDrawCount() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(Tag_SHOWINTEGRALDRAWCOUNT, MFUrl.SHOWINTEGRALDRAWCOUNT, params, this);
    }

    private void initView() {
        titleLeftImage.setImageResource(R.drawable.title_leftimgwhite_selector);
        titleLeftImage.setVisibility(View.VISIBLE);

        titleRightText.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtwhitecolorselector, null));
        } else {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtwhitecolorselector));
        }
        titleRightText.setText(R.string.creditrule);
        titleTitle.setTextColor(Color.parseColor("#FFFFFF"));
        titleTitle.setText(R.string.creditpoint);
        navLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        titleLeftImage.setOnClickListener(this);
        titleRightText.setOnClickListener(this);

//        mWaveHelper = new WaveHelper(waveView);
//        waveView.setWaveColor(Color.parseColor("#68FFFFFF"), Color.parseColor("#ccFFFFFF"));
//        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        tvDrawtime.setSelected(true);

        rlCreditRecord.setOnClickListener(this);
        rlDraw.setOnClickListener(this);
        rlShareCredit.setOnClickListener(this);
    }

    public void setIUiListener(IUiListener iUiListener) {
        this.iUiListener = iUiListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isfirst) {
            initDataShowIntegralDrawCountResume();
        }
        isfirst = false;
//        mWaveHelper.start();
    }

    private void initDataShowIntegralDrawCountResume() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(Tag_SHOWINTEGRALDRAWCOUNT_RESUME, MFUrl.SHOWINTEGRALDRAWCOUNT, params, this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mWaveHelper.cancel();
    }

    @Override
    public void onClick(View view) {
        if ((System.currentTimeMillis() - exitTime) < 800) {
            return;
        }
        exitTime = System.currentTimeMillis();
        bitmap = getShareBitmap(mContext);
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.rl_share_credit:
                showBottomDialog();
                break;
            case R.id.rl_credit_record:
                Bundle bundle_before = mContext.getIntent().getExtras();
                Bundle recordBundle = new Bundle();
                if (bundle_before != null && bundle_before.containsKey("message2")) {
                    recordBundle.putString("message2", bundle_before.getString("message2") + "");
                }
                startActivity(CreditDetailAct.class, recordBundle);
                break;
            case R.id.rl_draw:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else {
                    if (shareBean == null) {
                        return;
                    }
                    Bundle drawBundle = new Bundle();
                    String url = MFUtil.getValueUrl(shareBean.getShareUrl(), "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                    );
                    drawBundle.putString("url", url);
                    drawBundle.putString("title", "信用抽奖");
                    startActivity(CreditWebAty.class, drawBundle);
                }
                break;
            case R.id.title_right_text:
                Bundle bundle = new Bundle();
                bundle.putString("url", MFUrl.CREDITRULES);
                bundle.putString("title", "蜜蜂信用");
                startActivity(WebAty.class, bundle);
                break;
            case R.id.ll_shareqqspace:
                if (iUiListener == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditpoint_shareqqspace");
                ShareUtils.getIntance(mContext).shareCreditPointToQQSpace(saveMyBitmap(bitmap), iUiListener);
                dialog.dismiss();
                break;
            case R.id.ll_shareweibo:
                if (bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditpoint_shareweibo");
                ShareUtils.getIntance(mContext).shareCreditPointWeiboMultiMessage(saveMyBitmap(bitmap), "CreditPointFrg");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixin:
                if (bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditpoint_shareweixin");
                ShareUtils.getIntance(mContext).shareCreditPointToWeiXin(bitmap, null, "a");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixincircle:
                if (bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditpoint_shareweixincircle");
                ShareUtils.getIntance(mContext).shareCreditPointToWeiXinCircle(bitmap, null, "a");
                dialog.dismiss();
                break;
        }
    }

    private int getMaxNum(int point) {
        int maxNumber = 200;
        if (point <= 100) {
            maxNumber = 200;
        } else if (100 < point && point <= 105) {
            maxNumber = 200;
        } else if (105 < point && point <= 110) {
            maxNumber = 204;
        } else if (110 < point && point <= 115) {
            maxNumber = 205;
        } else if (115 < point && point <= 120) {
            maxNumber = 207;
        } else if (120 < point && point <= 130) {
            maxNumber = 217;
        } else if (130 < point && point <= 140) {
            maxNumber = 227;
        } else if (140 < point && point <= 150) {
            maxNumber = 236;
        } else if (150 < point && point <= 160) {
            maxNumber = 243;
        } else if (160 < point && point <= 170) {
            maxNumber = 251;
        } else if (170 < point && point <= 180) {
            maxNumber = 259;
        } else if (180 < point && point <= 190) {
            maxNumber = 267;
        } else if (190 < point && point <= 200) {
            maxNumber = 274;
        } else if (200 < point && point <= 210) {
            maxNumber = 280;
        } else if (210 < point && point <= 220) {
            maxNumber = 285;
        } else if (220 < point && point <= 240) {
            maxNumber = 304;
        } else if (240 < point && point <= 260) {
            maxNumber = 329;
        } else if (260 < point && point <= 280) {
            maxNumber = 345;
        } else if (280 < point && point <= 300) {
            maxNumber = 363;
        } else if (300 < point && point <= 320) {
            maxNumber = 378;
        } else if (320 < point && point <= 340) {
            maxNumber = 391;
        } else if (340 < point && point <= 360) {
            maxNumber = 406;
        } else if (360 < point && point <= 380) {
            maxNumber = 420;
        } else if (380 < point) {
            maxNumber = point * 10 / 9;
        }
        return maxNumber;
    }

    private void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgSharepoint = inflater.inflate(R.layout.dlg_sharerbottom, null);
            final int cFullFillWidth = 10000;
            mDlgSharepoint.setMinimumWidth(cFullFillWidth);


            TextView tv_titletxt = (TextView) mDlgSharepoint
                    .findViewById(R.id.tv_titletxt);
            tv_titletxt.setText("晒信用分");
            View v_line = mDlgSharepoint
                    .findViewById(R.id.v_line);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                v_line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            LinearLayout ll_shareweixin = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareweixin);
            LinearLayout ll_shareweixincircle = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareweixincircle);
            LinearLayout ll_shareweibo = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareweibo);
            LinearLayout ll_shareqqspace = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareqqspace);
            ll_shareweixin.setOnClickListener(this);
            ll_shareweixincircle.setOnClickListener(this);
            ll_shareweibo.setOnClickListener(this);
            ll_shareqqspace.setOnClickListener(this);
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgSharepoint);
        }
        dialog.show();
    }

    public Bitmap getShareBitmap(Context context) {
        creditpointView2.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtil.dip2px(mContext, 375), ScreenUtil.dip2px(mContext, 543)));
        creditpointView2.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        creditpointView2.layout(0, 0, creditpointView2.getMeasuredWidth(),
                creditpointView2.getMeasuredHeight());
        creditpointView2.buildDrawingCache();
        return creditpointView2.getDrawingCache();
    }

    public String saveMyBitmap(Bitmap bmp) {
        String path = "";
        File dirFil = new File(MFApp.getMiFengLocalForderPath()
                + "imageCach/");
        if (!dirFil.exists()) {
            dirFil.mkdirs();
        }
        File f = new File(MFApp.getMiFengLocalForderPath()
                + "imageCach/", "xiaojia_creditPoint.jpeg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            path = f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
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
            case Tag_SHOWINTEGRALDRAWCOUNT:
                if (!isAdded()) {
                    return;
                }
                String dataString = responseInfo.result.toString();
                final RequestResultBean<String> resultBean = GsonTransformUtil.getObjectFromJson(dataString, new TypeToken<RequestResultBean<String>>() {
                }.getType());
                if (resultBean != null && MFConstansValue.BACK_SUCCESS == resultBean.getCode()) {
                    String drawTime = resultBean.getData();
                    tvDrawtime.setText(TextUtils.isEmpty(drawTime) ? "0" : drawTime);
                    tvDrawtime.setVisibility(View.VISIBLE);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(500);
                    scaleAnimation.setInterpolator(new OvershootInterpolator());
                    tvDrawtime.startAnimation(scaleAnimation);
                    tvDrawtime.setVisibility(View.VISIBLE);
                } else if (resultBean != null && MFConstansValue.BACK_LOGOUT == resultBean.getCode()) {
                    MFUtil.showToast(mContext, resultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (resultBean != null && (MFConstansValue.BACK_SYSTEMERROR == resultBean.getCode() || MFConstansValue.BACK_BUSINESS == resultBean.getCode())) {
                    MFUtil.showToast(mContext, resultBean.getMessage());//1002一样重新登录
                }
                break;
            case Tag_SHOWINTEGRALDRAWCOUNT_RESUME:
                if (!isAdded()) {
                    return;
                }
                String datastringresume = responseInfo.result.toString();
                final RequestResultBean<String> stringRequestResultBean = GsonTransformUtil.getObjectFromJson(datastringresume, new TypeToken<RequestResultBean<String>>() {
                }.getType());
                if (stringRequestResultBean != null && MFConstansValue.BACK_SUCCESS == stringRequestResultBean.getCode()) {
                    String drawTime = stringRequestResultBean.getData();
                    tvDrawtime.setText(TextUtils.isEmpty(drawTime) ? "0" : drawTime);
                    tvDrawtime.setVisibility(View.VISIBLE);
                } else if (stringRequestResultBean != null && MFConstansValue.BACK_LOGOUT == stringRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, stringRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (stringRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == stringRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == stringRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, stringRequestResultBean.getMessage());
                }
                break;
            case TAG_INVITATION_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<ShareBean> shareBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<ShareBean>>() {
                }.getType());
                if (shareBeanRequestResultBean != null && shareBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    shareBean = shareBeanRequestResultBean.getData();
                } else if (shareBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == shareBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, shareBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (shareBeanRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == shareBeanRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == shareBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, shareBeanRequestResultBean.getMessage());
                }
        }
    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }
}
