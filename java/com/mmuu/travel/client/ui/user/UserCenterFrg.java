package com.mmuu.travel.client.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.ADBean;
import com.mmuu.travel.client.bean.MFBaseResBean;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.user.UserVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFOptions;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PhotoUtil;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.other.AdviseAct;
import com.mmuu.travel.client.ui.other.SetAct;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.widget.GradationScrollView;
import com.mmuu.travel.client.widget.dialog.OneDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * JGW 个人中心frg
 */

public class UserCenterFrg extends MFBaseFragment implements View.OnClickListener, GradationScrollView.ScrollViewListener, PublicRequestInterface {

    @BindView(R.id.iv_photo_bg)
    ImageView ivPhotoBg;
    @BindView(R.id.v_nullview)
    View vNullview;
    @BindView(R.id.iv_userimage)
    ImageView ivUserimage;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_userpoint)
    TextView tvUserpoint;
    @BindView(R.id.ll_point_login)
    LinearLayout llPointLogin;
    @BindView(R.id.ll_imggroup)
    LinearLayout llImggroup;
    @BindView(R.id.v_line0)
    View vLine0;
    @BindView(R.id.tv_reducecarbon)
    TextView tvReducecarbon;
    @BindView(R.id.tv_cumulative_travel_h)
    TextView tvCumulativeTravelH;
    @BindView(R.id.tv_savetaximoney)
    TextView tvSavetaximoney;
    @BindView(R.id.ll_accumulatetxt)
    LinearLayout llAccumulatetxt;
    @BindView(R.id.v_line1)
    View vLine1;
    @BindView(R.id.iv_phonebind)
    ImageView ivPhonebind;
    @BindView(R.id.v_identi_line1_1)
    View vIdentiLine11;
    @BindView(R.id.rl_phonebind)
    RelativeLayout rlPhonebind;
    @BindView(R.id.iv_deposit)
    ImageView ivDeposit;
    @BindView(R.id.rl_deposit)
    RelativeLayout rlDeposit;
    @BindView(R.id.iv_identification)
    ImageView ivIdentification;
    @BindView(R.id.rl_identification)
    RelativeLayout rlIdentification;
    @BindView(R.id.iv_starttrave)
    ImageView ivStarttrave;
    @BindView(R.id.rl_starttrave)
    RelativeLayout rlStarttrave;
    @BindView(R.id.ll_identification)
    LinearLayout llIdentification;
    @BindView(R.id.v_line2)
    View vLine2;
    @BindView(R.id.iv_mywallet)
    ImageView ivMywallet;
    @BindView(R.id.iv_wallet_right)
    ImageView ivWalletRight;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.rl_mywallet)
    RelativeLayout rlMywallet;
    @BindView(R.id.v_line3)
    View vLine3;
    @BindView(R.id.iv_myrun)
    ImageView ivMyrun;
    @BindView(R.id.iv_myrun_right)
    ImageView ivMyrunRight;
    @BindView(R.id.rl_myrun)
    RelativeLayout rlMyrun;
    @BindView(R.id.v_line4)
    View vLine4;
    @BindView(R.id.v_line5)
    View vLine5;
    @BindView(R.id.iv_invite)
    ImageView ivInvite;
    @BindView(R.id.iv_invite_right)
    ImageView ivInviteRight;
    @BindView(R.id.rl_invite)
    RelativeLayout rlInvite;
    @BindView(R.id.v_line6)
    View vLine6;
    @BindView(R.id.iv_userguide)
    ImageView ivUserguide;
    @BindView(R.id.iv_userguide_right)
    ImageView ivUserguideRight;
    @BindView(R.id.rl_userguide)
    RelativeLayout rlUserguide;
    @BindView(R.id.v_line7)
    View vLine7;
    @BindView(R.id.iv_userset)
    ImageView ivUserset;
    @BindView(R.id.rl_partner)
    RelativeLayout rlPartner;
    @BindView(R.id.v_line13)
    View vLine13;
    @BindView(R.id.iv_userset_right)
    ImageView ivUsersetRight;
    @BindView(R.id.rl_userset)
    RelativeLayout rlUserset;
    @BindView(R.id.v_line8)
    View vLine8;
    @BindView(R.id.rl_detailinfo)
    RelativeLayout rlDetailinfo;
    @BindView(R.id.cs_scrollview)
    GradationScrollView csScrollview;
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
    @BindView(R.id.v_identi_line1_2)
    View vIdentiLine12;
    @BindView(R.id.v_identi_line2_1)
    View vIdentiLine21;
    @BindView(R.id.v_identi_line2_2)
    View vIdentiLine22;
    @BindView(R.id.v_identi_line3_1)
    View vIdentiLine31;
    @BindView(R.id.v_identi_line3_2)
    View vIdentiLine32;
    @BindView(R.id.tv_deposit)
    TextView tvDeposit;
    @BindView(R.id.tv_identification)
    TextView tvIdentification;
    @BindView(R.id.tv_phonebind)
    TextView tvPhonebind;
    @BindView(R.id.iv_depositbg)
    ImageView ivDepositBg;
    @BindView(R.id.iv_identificationbg)
    ImageView ivIdentificationBg;
    @BindView(R.id.iv_adimg)
    ImageView ivAdimg;
    @BindView(R.id.tv_reducecarbon_txt)
    TextView tvReducecarbonTxt;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.v_line9)
    View vLine9;
    @BindView(R.id.rl_calltaxi)
    RelativeLayout rlCalltaxi;
    @BindView(R.id.rl_useradvise)
    RelativeLayout rlUseradvise;
    Unbinder unbinder;
    @BindView(R.id.ll_point_certification)
    LinearLayout llPointCertification;
    @BindView(R.id.tv_certification_point)
    TextView tvCertificationPoint;

    private Dialog dialog;
    private View userCenterView;
    private int ivPhotoBgheight;
    private String takePictureSavePath;
    private Uri imageUri;
    private String token;
    private static final int Tag_PERSONAL_CENTER_URL = 1695701;
    private static final int Tag_UPLOADAVATAR_URL = 1695702;
    private static final int TAG_GETADBYTYPE = 1695703;

    private boolean isfirst = false;
    private PersonalCenterVo personalCenterVo;
    private ADBean adBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (userCenterView == null) {
            userCenterView = inflater.inflate(R.layout.frg_usercenter, null);
            ButterKnife.bind(this, userCenterView);
            initScoroListeners();
            initViews();
            getADData();
            isfirst = true;
        }
        return userCenterView;
    }

    private void initData() {
        if (!MFUtil.checkNetwork(mContext) && isfirst) {
            showNoNetworkDialog(true);
            return;
        }
        if (isfirst) {
            dialogShow();
            isfirst = false;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
        }
        MFRunner.requestPost(Tag_PERSONAL_CENTER_URL, MFUrl.PERSONAL_CENTER_URL, params, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        token = MFStaticConstans.getUserBean().getToken();
        if (TextUtils.isEmpty(token)) {
            init_notlogin();
        } else {
            personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                    PersonalCenterVo.class);
            init_login(personalCenterVo);
            initData();

        }
    }

    private void getADData() {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "3");
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

    private void init_notlogin() {
        ivPhotoBg.setImageResource(R.drawable.usercenter_top_bg_n);
        ivUserimage.setImageResource(R.drawable.user_default_img_n);
        tvUserpoint.setText(R.string.click_login_txt);
        tvUsername.setText(R.string.not_login);
        tvCertificationPoint.setVisibility(View.GONE);
        llPointCertification.setVisibility(View.GONE);
        tvUsername.setTextColor(getResources().getColor(R.color.c666666_hint_font2));
        tvUserpoint.setCompoundDrawables(null, null, null, null);
        llImggroup.removeAllViews();
        llImggroup.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_usercenternologin, null);
        ImageView iv_nologin_rentcar = (ImageView) view.findViewById(R.id.iv_nologin_rentcar);
        ImageView iv_nologin_fee = (ImageView) view.findViewById(R.id.iv_nologin_fee);
        ImageView iv_nologin_gavecar = (ImageView) view.findViewById(R.id.iv_nologin_gavecar);
        ImageView iv_nologin_temporary = (ImageView) view.findViewById(R.id.iv_nologin_temporary);
        ImageView iv_nologin_startcar = (ImageView) view.findViewById(R.id.iv_nologin_startcar);
        ImageView iv_nologin_runwhere = (ImageView) view.findViewById(R.id.iv_nologin_runwhere);
        llImggroup.addView(view);
        int with = ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dip2px(mContext, 30);
        int heght = 577 * with / 995;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(with, heght);
        layoutParams.setMargins(0, ScreenUtil.dip2px(mContext, 10), 0, 0);
        iv_nologin_rentcar.setLayoutParams(layoutParams);
        iv_nologin_gavecar.setLayoutParams(layoutParams);
        iv_nologin_temporary.setLayoutParams(layoutParams);
        iv_nologin_startcar.setLayoutParams(layoutParams);
        iv_nologin_fee.setLayoutParams(layoutParams);
        iv_nologin_runwhere.setLayoutParams(layoutParams);
        rlDetailinfo.setVisibility(View.GONE);
    }

    private void init_login(PersonalCenterVo personalCenterVo) {
        if (personalCenterVo == null) {
            return;
        }
        if (!SharedPreferenceTool.getPrefBoolean(mContext, "usercenter_invitaion_tag", false)) {
            tvTag.setVisibility(View.VISIBLE);
        } else {
            tvTag.setVisibility(View.GONE);
        }
        if (personalCenterVo.getDriverState() == 0) {
            rlCalltaxi.setVisibility(View.GONE);
            vLine9.setVisibility(View.GONE);
        } else {
            rlCalltaxi.setVisibility(View.VISIBLE);
            vLine9.setVisibility(View.VISIBLE);
        }
        if (personalCenterVo.getIsShowPartner() == 1) {
            rlPartner.setVisibility(View.VISIBLE);
            vLine13.setVisibility(View.VISIBLE);
        } else {
            rlPartner.setVisibility(View.GONE);
            vLine13.setVisibility(View.GONE);
        }
        ivPhotoBg.setImageResource(R.drawable.usercenter_top_bg_y);
        ivUserimage.setImageResource(R.drawable.user_photo_selector);
        Drawable nav_up = ContextCompat.getDrawable(getContext(), R.drawable.user_point_raw);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tvUserpoint.setCompoundDrawables(null, null, nav_up, null);
        tvUserpoint.setText("信用积分：" + personalCenterVo.getCredScore());
        tvUsername.setTextColor(getResources().getColor(R.color.c333333_hint_font1));

        if (TextUtils.isEmpty(personalCenterVo.getHeadPic())) {
            ivUserimage.setImageResource(R.drawable.user_photo_selector);
        } else {
            String imgUrl = personalCenterVo.getHeadPic() + "?x-oss-process=image/circle,r_1000/format,png";
            ImageLoader.getInstance().displayImage(imgUrl, ivUserimage, MFOptions.Option_UserPhoto);
        }
        llImggroup.removeAllViews();
        llImggroup.setVisibility(View.GONE);
        rlDetailinfo.setVisibility(View.VISIBLE);

        if (1000 < personalCenterVo.getCarbonEmission().doubleValue()) {
            tvReducecarbonTxt.setText(R.string.reducecarbon_kg);
            double carbonEmission = personalCenterVo.getCarbonEmission().doubleValue() / 1000.0000;
            tvReducecarbon.setText(MFUtil.formatDoubleValue(carbonEmission, "0.0") + "");
        } else {
            tvReducecarbonTxt.setText(R.string.reducecarbon_g);
            tvReducecarbon.setText(MFUtil.formatDoubleValue(personalCenterVo.getCarbonEmission().doubleValue(), "0.0") + "");
        }

        tvCumulativeTravelH.setText(MFUtil.formatDoubleValue(Double.parseDouble(personalCenterVo.getSumTime()), "0.0") + "");
        tvSavetaximoney.setText(
                MFUtil.formatDoubleValue(personalCenterVo.getSaveBikeFfare().doubleValue(), "0.0") + "");

        ivPhonebind.setImageResource(R.drawable.user_bindphone_after);
        ivDeposit.setImageResource(R.drawable.user_deposit_after);
        ivIdentification.setImageResource(R.drawable.user_identification_after);
        vIdentiLine11.setSelected(false);
        vIdentiLine12.setSelected(false);
        vIdentiLine21.setSelected(false);
        vIdentiLine22.setSelected(false);
        vIdentiLine31.setSelected(false);
        vIdentiLine32.setSelected(false);
        ivIdentificationBg.setVisibility(View.INVISIBLE);
        ivIdentificationBg.clearAnimation();
        ivDepositBg.setVisibility(View.INVISIBLE);
        ivDepositBg.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.usercenter_rotate_bg);
        animation.setInterpolator(new LinearInterpolator());
        tvUsername.setText(MFUtil.convertPhoneNumber(personalCenterVo.getMobile()));
        if (personalCenterVo.getRealNameState() == 1 && personalCenterVo.getCertState() == 2) {
            tvUsername.setText(personalCenterVo.getName());
        }

        if (personalCenterVo.getRealNameState() == 0) {
            llPointCertification.setVisibility(View.VISIBLE);
            llPointCertification.setOnClickListener(this);
            tvCertificationPoint.setVisibility(View.VISIBLE);
        } else {
            llPointCertification.setVisibility(View.GONE);
            llPointCertification.setOnClickListener(null);
            tvCertificationPoint.setVisibility(View.GONE);
        }

        switch (personalCenterVo.getCertState()) {
            case 0://已经注册
                llIdentification.setVisibility(View.VISIBLE);
                vLine2.setVisibility(View.VISIBLE);
                ivPhonebind.setImageResource(R.drawable.user_bindcomplate);
                ivDepositBg.setVisibility(View.VISIBLE);
                ivDepositBg.setAnimation(animation);
                vIdentiLine11.setSelected(true);
                vIdentiLine12.setSelected(true);
                break;
            case 1:// 已经交押金
                ivPhonebind.setImageResource(R.drawable.user_bindcomplate);
                ivDeposit.setImageResource(R.drawable.user_bindcomplate);
                ivIdentificationBg.setVisibility(View.VISIBLE);
                ivIdentificationBg.setAnimation(animation);
                vIdentiLine11.setSelected(true);
                vIdentiLine12.setSelected(true);
                vIdentiLine21.setSelected(true);
                vIdentiLine22.setSelected(true);
                break;
            case 2://已经实名认证
                llIdentification.setVisibility(View.GONE);
                vLine2.setVisibility(View.GONE);
                break;
            default://
                llIdentification.setVisibility(View.VISIBLE);
                vLine2.setVisibility(View.VISIBLE);
                ivPhonebind.setImageResource(R.drawable.user_bindcomplate);
                ivDepositBg.setVisibility(View.VISIBLE);
                ivDepositBg.setAnimation(animation);
                vIdentiLine11.setSelected(true);
                vIdentiLine12.setSelected(true);
                break;
        }
        String startprice;
        if (personalCenterVo.getBalance().compareTo(BigDecimal.valueOf(0)) == -1) {
            startprice = "<font color='#999999'></font>" +
                    "<font color='#f6ab00'>" + MFUtil.formatDoubleValue(personalCenterVo.getBalance().doubleValue(), "0.0") + "</font>" + "<font color='#999999'>元</font>" +
                    "<font color='#f6ab00'>&nbsp;&nbsp;" + "请及时充值" + "</font>";
        } else {
            startprice = "<font color='#999999'></font>" +
                    "<font color='#f6ab00'>" + MFUtil.formatDoubleValue(personalCenterVo.getBalance().doubleValue(), "0.0") + "</font>" + "<font color='#999999'>元</font>";
        }
        tvBalance.setText(Html.fromHtml(startprice));

        ivAdimg.setOnClickListener(this);
        //广告
    }

    private void initViews() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setOnClickListener(this);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleTitle.setText(R.string.user_center);
        navLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        MFUtil.setViewsOnClick(this, titleLeftImage, ivUserimage, llPointLogin,
                rlPhonebind, rlDeposit, rlIdentification, rlStarttrave,
                rlMywallet, rlMyrun, rlInvite, rlUserguide, rlUserset,
                tvUsername, rlStarttrave, rlCalltaxi, rlPartner, rlUseradvise);
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initScoroListeners() {
        ViewTreeObserver vto0 = navLayout.getViewTreeObserver();
        vto0.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    navLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                            this);
                } else {
                    navLayout.getViewTreeObserver().removeGlobalOnLayoutListener(
                            this);
                }
                vNullview.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        navLayout.getHeight()));
            }
        });
        ViewTreeObserver vto = ivPhotoBg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivPhotoBg.getViewTreeObserver().removeOnGlobalLayoutListener(
                            this);
                } else {
                    ivPhotoBg.getViewTreeObserver().removeGlobalOnLayoutListener(
                            this);
                }

                ivPhotoBgheight = ivPhotoBg.getHeight() - 250;
                csScrollview.setScrollViewListener(UserCenterFrg.this);
            }
        });
    }

    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {   //设置标题的背景颜色
            navLayout.setBackgroundColor(Color.argb(0, 255, 255, 255));
        } else if (y > 0 && y <= ivPhotoBgheight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / ivPhotoBgheight;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            navLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {    //滑动到banner下面设置普通颜色
            navLayout.setBackgroundColor(Color.argb(255, 255, 255, 255));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.iv_userimage:
            case R.id.tv_username:
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    showBottomDialog();
                }
                break;
            case R.id.ll_point_certification:
                Bundle bundleCertificationAction = new Bundle();
                bundleCertificationAction.putInt("typeentrance", 1);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                startActivity(CertificationAct.class, bundleCertificationAction);
                break;
            case R.id.ll_point_login:
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class
                            , null);
                } else if (personalCenterVo != null) {
                    Bundle bundleCreditPoint = new Bundle();
                    bundleCreditPoint.putString("point", personalCenterVo.getCredScore() + "");
                    bundleCreditPoint.putString("message1", personalCenterVo.getMessage1() + "");
                    bundleCreditPoint.putString("message2", personalCenterVo.getMessage2() + "");
                    startActivity(CreditPointAct.class, bundleCreditPoint);
                }
                break;
            case R.id.rl_calltaxi:
                startActivity(TaxiAct.class, null);
                break;
            case R.id.rl_deposit:
                if (personalCenterVo != null && personalCenterVo.getCertState() == 0) {
                    MobclickAgent.onEvent(mContext, "click_usercenter_cardeposit");
                    startActivity(CarDepositAct.class, null);
                }
                break;
            case R.id.rl_identification:
                indentificationClick();
                break;
            case R.id.rl_starttrave:
                starttraveClick();
                break;
            case R.id.rl_mywallet:
                MobclickAgent.onEvent(mContext, "click_usercenter_mywallet");
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    Bundle userBundle = new Bundle();
                    UserVO userVO = MFStaticConstans.getUserBean().getUser();
                    userBundle.putSerializable("userVO", userVO);
                    startActivity(MyWalletAct.class, userBundle);
                }
                break;
            case R.id.rl_myrun:
                MobclickAgent.onEvent(mContext, "click_usercenter_myruns");
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    startActivity(MyRunsAct.class, null);
                }
                break;
            case R.id.rl_partner:
                MobclickAgent.onEvent(mContext, "click_usercenter_myruns");
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    startActivity(PartnerAccountAct.class, null);
                }
                break;
            case R.id.rl_invite:
                MobclickAgent.onEvent(mContext, "click_usercenter_invitefriend");
                SharedPreferenceTool.setPrefBoolean(mContext, "usercenter_invitaion_tag", true);
                startActivity(InviteFriendAct.class, null);
                break;
            case R.id.rl_userguide:
//                MobclickAgent.onEvent(mContext, "click_usercenter_userguide");
//                Bundle bundle = new Bundle();
//                bundle.putString("url", MFUrl.INDEX);
//                bundle.putString("title", "用户指南");
//                startActivity(WebAty.class, bundle);
                startActivity(CostomerCenterAct.class, null);
                break;
            case R.id.rl_userset:
                MobclickAgent.onEvent(mContext, "click_usercenter_set");
                startActivity(SetAct.class, null);
                break;
            case R.id.iv_adimg:
                if (adBean != null && !TextUtils.isEmpty(adBean.getAdUrl())) {
                    Bundle adBundle = new Bundle();

                    if (!TextUtils.isEmpty(adBean.getAdUrl())) {
                        String url = MFUtil.getValueUrl(adBean.getAdUrl(), "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                                , "mobile=" + MFStaticConstans.getUserBean().getUser().getMobile());
                        adBundle.putString("url", url);
                    } else {
                        adBundle.putString("url", adBean.getAdUrl());
                    }
                    adBundle.putString("title", adBean.getTitle());
                    sendStatistics("3002", "click", MFUtil.getValueForURL("cityCode", adBean.getAdUrl()), adBean.getId() + "");
                    startActivity(WebAty.class, adBundle);
                }
                break;
            case R.id.rl_useradvise:
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    startActivity(AdviseAct.class, null);
                }
                break;
            default:
                break;
        }
    }

    private void starttraveClick() {
        if (personalCenterVo != null && personalCenterVo.getCertState() == 2) {
            Bundle bundle = new Bundle();
            bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
            startActivity(CertificationAct.class, bundle);
        } else if (personalCenterVo != null) {
            String message = "";
            if (personalCenterVo.getCertState() == 1) {
                message = "请先进行实名认证";
            } else if (personalCenterVo.getCertState() == 0) {
                message = "请您先交纳押金";
            }
            final OneDialog oneDialo = new OneDialog(mContext, message, "知道了");
            oneDialo.setDialogClickListener(new DialogClickListener() {
                @Override
                public void onLeftClick(View v, Dialog d) {
                    oneDialo.dismiss();
                }

                @Override
                public void onRightClick(View v, Dialog d) {
                    oneDialo.dismiss();
                }
            });
            oneDialo.show();
        }
    }

    private void indentificationClick() {
        if (personalCenterVo != null && personalCenterVo.getCertState() == 1) {
            MobclickAgent.onEvent(mContext, "click_usercenter_certification");
            Bundle bundle = new Bundle();
            bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
            startActivity(CertificationAct.class, bundle);
        } else if (personalCenterVo != null && personalCenterVo.getCertState() == 0) {
            final OneDialog oneDialo = new OneDialog(mContext, "请您先交纳押金", "知道了");
            oneDialo.setDialogClickListener(new DialogClickListener() {
                @Override
                public void onLeftClick(View v, Dialog d) {
                    oneDialo.dismiss();
                }

                @Override
                public void onRightClick(View v, Dialog d) {
                    oneDialo.dismiss();
                }
            });
            oneDialo.show();
        }
    }

    private void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_userfrg, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);
            TextView tv_camera_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_camera_txt);
            TextView tv_album_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_album_txt);
            TextView tv_exit_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_exit_txt);
            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.cancel_txt);
            tv_camera_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MFUtil.checkNetwork(mContext)) {
                        showNoNetworkDialog(true);
                        return;
                    }
                    if (!TextUtils.isEmpty(MFApp.getMiFengLocalForderPath())) {
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        String timeStamp = new SimpleDateFormat(
                                "yyyyMMdd_HHmmss").format(new Date());
                        File dirFil = new File(MFApp.getMiFengLocalForderPath()
                                + "imageCach/");
                        if (!dirFil.exists()) {
                            dirFil.mkdirs();
                        }
                        File makeFile = new File(MFApp.getMiFengLocalForderPath()
                                + "imageCach/", "xiaojia_"
                                + timeStamp + ".jpeg");
                        takePictureSavePath = makeFile.getAbsolutePath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(makeFile));
                        try {
                            UserCenterFrg.this.startActivityForResult(intent, 2);
                        } catch (Exception e) {
                            if (e.toString().contains("Permission")) {
                                MFUtil.showToast(mContext, "请检查蜜蜂出行相机权限是否开启");
                            }
                        }
                    } else {
                        MFUtil.showToast(mContext, "！存储设备部不可用");
                    }
                    dialog.dismiss();
                }
            });
            tv_album_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MFUtil.checkNetwork(mContext)) {
                        showNoNetworkDialog(true);
                        return;
                    }
                    try {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        UserCenterFrg.this.startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        MFUtil.showToast(mContext, "未检测到本地相册，请使用拍照功能");
                    }
                    dialog.dismiss();

                }
            });
            tv_album_txt.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
            tv_exit_txt.setVisibility(View.GONE);
            cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    if (data != null) {
                        imageUri = data.getData();
                        if (Build.VERSION.SDK_INT >= 19) {
                            imageUri = Uri.fromFile(new File(PhotoUtil.getPath(
                                    mContext, imageUri)));
                        }
                        imageUri = startPhotoZoom(imageUri);
                    }

                    break;
                // 如果是调用相机拍照时
                case 2:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        imageUri = startPhotoZoom(imageUri);
                    }
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        if (bitmap != null) {
                            //imageView.setImageBitmap(bitmap);
                            uploadImage(bitmap);
//                            MFUtil.showToast(mContext, "已经获取到bitmap");
                        }
                    }
                    break;
                default:
                    break;
            }

        }

    }

    /**
     * 头像裁剪 图片方法实现
     *
     * @param uriFrom
     */
    public Uri startPhotoZoom(Uri uriFrom) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
        return uriFrom;

    }

    private void uploadImage(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
            bitmap.recycle();//自由选择是否进行回收
            byte[] result = output.toByteArray();//转换成功了
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogShow();
            final String uploadName = MFApp.mifengOSSDirectory + "userPic" + "/" +
                    TimeDateUtil.longToDate(System.currentTimeMillis(), "yyyy/MM/dd") + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = MFConstansValue.OSS_BASEURL + uploadName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(MFConstansValue.OSS_BUCKETNAME, uploadName, result);
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                }
            });

            OSSAsyncTask asyncTask = MFApp.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                    params.addBodyParameter("avatarAddress", uploadUrl + "");
                    MFRunner.requestPost(Tag_UPLOADAVATAR_URL, MFUrl.UPLOADAVATAR_URL, params, UserCenterFrg.this);
                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                }
            });
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
            case TAG_GETADBYTYPE:
                if (!isAdded()) {
                    return;
                }
                String getAdDataInfoString = responseInfo.result.toString();
                final RequestResultBean<ADBean> adBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(getAdDataInfoString, new TypeToken<RequestResultBean<ADBean>>() {
                }.getType());
                if (adBeanRequestResultBean == null || adBeanRequestResultBean.getData() == null || MFConstansValue.BACK_SUCCESS != adBeanRequestResultBean.getCode()) {
                    return;
                }
                adBean = adBeanRequestResultBean.getData();
                long lastShowTime = 0;
                if (!TextUtils.isEmpty(SharedPreferenceTool.getPrefString(mContext, "adtype3", ""))) {
                    String[] adInfo = SharedPreferenceTool.getPrefString(mContext, "adtype3", "").split("，");
                    if (adInfo != null && adInfo.length != 2) {
                        return;
                    }
                    Long adId = Long.parseLong(adInfo[0]);
                    if (adBean.getId() == adId) {
                        lastShowTime = Long.parseLong(adInfo[1]);
                    } else {
                        SharedPreferenceTool.setPrefString(mContext, "adtype3", "");
                    }
                }
                if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && (new Date().getTime() - lastShowTime) >
                        adBean.getActiveFrequency() * 1000 * 60) {
                    ivAdimg.setVisibility(View.VISIBLE);
                    int with = ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dip2px(mContext, 30);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivAdimg.getLayoutParams();
                    layoutParams.height = 370 * with / 1320;
                    layoutParams.width = with;
                    ivAdimg.setLayoutParams(layoutParams);
                    String imgrul = null;
                    if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && adBean.getPhotoUrl().contains("oss")) {
                        imgrul = adBean.getPhotoUrl() + "?x-oss-process=image/rounded-corners,r_10/format,png";
                    } else {
                        imgrul = adBean.getPhotoUrl();
                    }
                    ImageLoader.getInstance().displayImage(imgrul, ivAdimg, MFOptions.OPTION_DEF);
                    sendStatistics("3002", "show", MFUtil.getValueForURL("cityCode", adBean.getAdUrl()), adBean.getId() + "");
                    SharedPreferenceTool.setPrefString(mContext, "adtype3", adBean.getId() + "，" + new Date().getTime());
                }
                break;
            case Tag_UPLOADAVATAR_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                String uploadimgString = responseInfo.result.toString();
                RequestResultBean<MFBaseResBean> uploadResultBean = GsonTransformUtil.getObjectFromJson(uploadimgString, new TypeToken<RequestResultBean<MFBaseResBean>>() {
                }.getType());
                if (uploadResultBean != null && MFConstansValue.BACK_SUCCESS == uploadResultBean.getCode()) {
                    initData();
                } else if (uploadResultBean != null && MFConstansValue.BACK_LOGOUT == uploadResultBean.getCode()) {
                    MFUtil.showToast(mContext, uploadResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (uploadResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == uploadResultBean.getCode() || MFConstansValue.BACK_BUSINESS == uploadResultBean.getCode())) {
                    MFUtil.showToast(mContext, uploadResultBean.getMessage());
                }
                break;
            case Tag_PERSONAL_CENTER_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                String personString = responseInfo.result.toString();
                RequestResultBean<PersonalCenterVo> personResultBean = GsonTransformUtil.getObjectFromJson(personString, new TypeToken<RequestResultBean<PersonalCenterVo>>() {
                }.getType());
                if (personResultBean != null && MFConstansValue.BACK_SUCCESS == personResultBean.getCode()) {
                    personalCenterVo = personResultBean.getData();
                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_KEY_PERSONRESULTBEAN, GsonTransformUtil.toJson(personalCenterVo));
                    init_login(personalCenterVo);
                } else if (personResultBean != null && MFConstansValue.BACK_LOGOUT == personResultBean.getCode()) {
                    MFUtil.showToast(mContext, personResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (personResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == personResultBean.getCode() || MFConstansValue.BACK_BUSINESS == personResultBean.getCode())) {
                    MFUtil.showToast(mContext, personResultBean.getMessage());//1002一样重新登录
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
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

}