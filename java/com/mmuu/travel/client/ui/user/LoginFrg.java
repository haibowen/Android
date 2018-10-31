package com.mmuu.travel.client.ui.user;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.user.UserBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PermissionHelper;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.ui.main.MainAct;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.widget.StatusView;
import com.mmuu.travel.client.widget.dialog.OneDialog;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 登录界面
 */

public class LoginFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    @BindView(R.id.v_status)
    StatusView vStatus;
    @BindView(R.id.iv_left_image)
    ImageView ivLeftImage;
    @BindView(R.id.rl_leftimg)
    RelativeLayout rlLeftimg;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.iv_logoicon)
    ImageView ivLogoicon;
    @BindView(R.id.tv_versioncode)
    TextView tvVersioncode;
    @BindView(R.id.ed_phone_num)
    EditText edPhoneNum;
    @BindView(R.id.v_line0)
    View vLine0;
    @BindView(R.id.rl_phonenum)
    RelativeLayout rlPhonenum;
    @BindView(R.id.tv_check1)
    TextView tvCheck1;
    @BindView(R.id.v_line1)
    View vLine1;
    @BindView(R.id.tv_check2)
    TextView tvCheck2;
    @BindView(R.id.v_line2)
    View vLine2;
    @BindView(R.id.tv_check3)
    TextView tvCheck3;
    @BindView(R.id.v_line3)
    View vLine3;
    @BindView(R.id.tv_check4)
    TextView tvCheck4;
    @BindView(R.id.v_line4)
    View vLine4;
    @BindView(R.id.ll_checkcode)
    LinearLayout llCheckcode;
    @BindView(R.id.ed_checkcode)
    EditText edCheckcode;
    @BindView(R.id.rl_checkcode)
    RelativeLayout rlCheckcode;
    @BindView(R.id.tv_get_check_code)
    TextView tvGetCheckCode;
    @BindView(R.id.tv_get_check_code_again)
    TextView tvGetCheckCodeAgain;
    @BindView(R.id.tv_age16tag)
    TextView tvAge16tag;
    @BindView(R.id.ll_user_agreement)
    LinearLayout llUserAgreement;
    @BindView(R.id.rl_tagtxt)
    RelativeLayout rlTagtxt;
    @BindView(R.id.tv_phonenum_code)
    TextView tvPhonenumCode;
    ArrayList<TextView> checkcodeViews;
    ArrayList<View> lineViews;
    ArrayList<View> cursorViews;
    @BindView(R.id.v_cursor1)
    View vCursor1;
    @BindView(R.id.v_cursor2)
    View vCursor2;
    @BindView(R.id.v_cursor3)
    View vCursor3;
    @BindView(R.id.v_cursor4)
    View vCursor4;
    Unbinder unbinder;
    private boolean isPhoneNumPage;
    private boolean isDuringTime;
    private String phoneNumOld = "";
    private CountDownTimer countDownTimer = null;
    private static final int TAG_BROADSMS = 1695704;
    private final int GET_CHECKCODE_REQUEST_TAG = 10000;
    private final int LOGIN_REQUEST_TAG = 10001;
    private static final int TAG_ADDPHONEDEVICEINFO = 1695703;
    private static final int WHAT_CURSOR = 1695701;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case WHAT_CURSOR: {
                int size = edCheckcode.getText().toString().length();
                if (size < cursorViews.size()) {
                    cursorViews.get(size).setVisibility(cursorViews.get(size).isShown() ? View.GONE : View.VISIBLE);
                }
                getHandler().sendEmptyMessageDelayed(WHAT_CURSOR, 500);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_login, null);
            unbinder = ButterKnife.bind(this, fragContentView);
            initView();
        }
        return fragContentView;
    }

    private void initPhoneNum(final int animtime) {
        this.isPhoneNumPage = true;
        tvGetCheckCode.setVisibility(View.VISIBLE);
        tvGetCheckCodeAgain.setVisibility(View.INVISIBLE);
        edPhoneNum.requestFocus();

        ObjectAnimator anim = ObjectAnimator.ofFloat(ivLeftImage, "alpha", 1f, 0f);
        anim.setDuration(animtime / 2);// 动画持续时间
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (ivLeftImage != null) {
                            ivLeftImage.setImageResource(R.drawable.title_leftimgclose_selector);
                            ObjectAnimator anim = ObjectAnimator.ofFloat(ivLeftImage, "alpha", 0f, 1f);
                            anim.setDuration(animtime / 2);// 动画持续时间
                            anim.start();
                        }
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.start();

        ObjectAnimator animatorIvLeftImage = ObjectAnimator.ofFloat(ivLeftImage,
                "TranslationX", 0);
        animatorIvLeftImage.setDuration(animtime);
        animatorIvLeftImage.start();
        ObjectAnimator animatorrlPhonenum = ObjectAnimator.ofFloat(rlPhonenum,
                "TranslationX", 0);
        animatorrlPhonenum.setDuration(animtime);
        animatorrlPhonenum.start();
        ObjectAnimator animatorrlTagtxt = ObjectAnimator.ofFloat(rlTagtxt,
                "TranslationX", 0);
        animatorrlTagtxt.setDuration(animtime);
        animatorrlTagtxt.start();
        ObjectAnimator animatortvPhonenumCode = ObjectAnimator.ofFloat(tvPhonenumCode,
                "TranslationX", ScreenUtil.getScreenWidth(mContext));
        animatortvPhonenumCode.setDuration(animtime);
        animatortvPhonenumCode.start();
        ObjectAnimator animatortvTitleRight = ObjectAnimator.ofFloat(tvTitleRight,
                "TranslationX", ScreenUtil.getScreenWidth(mContext));
        animatortvTitleRight.setDuration(animtime);
        animatortvTitleRight.start();

        ObjectAnimator animatorrlCheckcode = ObjectAnimator.ofFloat(rlCheckcode,
                "TranslationX", ScreenUtil.getScreenWidth(mContext));
        animatorrlCheckcode.setDuration(animtime);
        animatorrlCheckcode.start();
    }

    private void initCheckCode() {
        this.isPhoneNumPage = false;
        edCheckcode.requestFocus();
        tvGetCheckCode.setVisibility(View.INVISIBLE);
        tvGetCheckCodeAgain.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivLeftImage, "alpha", 1f, 0f);
        anim.setDuration(200);// 动画持续时间
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (ivLeftImage != null) {
                            ivLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
                            ObjectAnimator anim = ObjectAnimator.ofFloat(ivLeftImage, "alpha", 0f, 1f);
                            anim.setDuration(200);// 动画持续时间
                            anim.start();
                        }
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.start();
        ObjectAnimator animatorrlPhonenum = ObjectAnimator.ofFloat(rlPhonenum,
                "TranslationX", -ScreenUtil.getScreenWidth(mContext));
        animatorrlPhonenum.setDuration(400);
        animatorrlPhonenum.start();
        ObjectAnimator animatorrlTagtxt = ObjectAnimator.ofFloat(rlTagtxt,
                "TranslationX", -ScreenUtil.getScreenWidth(mContext));
        animatorrlTagtxt.setDuration(400);
        animatorrlTagtxt.start();
        ObjectAnimator animatortvTitleRight = ObjectAnimator.ofFloat(tvTitleRight,
                "TranslationX", 0);
        animatortvTitleRight.setDuration(400);
        animatortvTitleRight.start();

        ObjectAnimator animatorrlCheckcode = ObjectAnimator.ofFloat(rlCheckcode,
                "TranslationX", 0);
        animatorrlCheckcode.setDuration(400);
        animatorrlCheckcode.start();
        ObjectAnimator animatortvPhonenumCode = ObjectAnimator.ofFloat(tvPhonenumCode,
                "TranslationX", 0);
        animatortvPhonenumCode.setDuration(400);
        animatortvPhonenumCode.start();
    }

    private void initView() {
        initPhoneNum(0);
        rlLeftimg.setOnClickListener(this);
        ivLeftImage.setImageResource(R.drawable.title_leftimgclose_selector);
        tvTitleRight.setText("收不到验证码？");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvTitleRight.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector, null));
        } else {
            tvTitleRight.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector));
        }
        tvTitleRight.setOnClickListener(this);
        tvVersioncode.setText("V" + MFApp.versionName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvVersioncode.setLetterSpacing(0.3f);
        }
        edPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty((edPhoneNum.getText() + "").trim())) {
                    vLine0.setBackgroundColor(Color.parseColor("#dddddd"));
                } else {
                    vLine0.setBackgroundColor(Color.parseColor("#333333"));
                }
                if (s.toString().length() == 11) {
                    tvGetCheckCode.setClickable(true);
                    tvGetCheckCode.setBackgroundResource(R.drawable.login_button_bg);
                    tvGetCheckCode.setSelected(true);
                } else {
                    tvGetCheckCode.setClickable(false);
                    tvGetCheckCode.setSelected(false);
                    tvGetCheckCode.setBackgroundColor(Color.parseColor("#dddddd"));
                }
            }
        });
        if (checkcodeViews == null) {
            checkcodeViews = new ArrayList<>();
        }
        if (cursorViews == null) {
            cursorViews = new ArrayList<>();
        }
        if (lineViews == null) {
            lineViews = new ArrayList<>();
        }
        cursorViews.clear();
        cursorViews.add(vCursor1);
        cursorViews.add(vCursor2);
        cursorViews.add(vCursor3);
        cursorViews.add(vCursor4);
        checkcodeViews.clear();
        checkcodeViews.add(tvCheck1);
        checkcodeViews.add(tvCheck2);
        checkcodeViews.add(tvCheck3);
        checkcodeViews.add(tvCheck4);
        lineViews.clear();
        lineViews.add(vLine1);
        lineViews.add(vLine2);
        lineViews.add(vLine3);
        lineViews.add(vLine4);
        edCheckcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getHandler().removeMessages(WHAT_CURSOR);
                for (int i = 0; i < 4; i++) {
                    checkcodeViews.get(i).setText("");
                    lineViews.get(i).setBackgroundColor(Color.parseColor("#dddddd"));
                    cursorViews.get(i).setVisibility(View.GONE);
                }
                String str = s.toString().trim();
                for (int i = 0; i < str.length(); i++) {
                    checkcodeViews.get(i).setText(str.charAt(i) + "");
                    lineViews.get(i).setBackgroundColor(Color.parseColor("#333333"));
                }
                if (s.length() >= 4) {
                    attempLogin(edPhoneNum.getText().toString().trim(), str);
                } else {
                    lineViews.get(s.length()).setBackgroundColor(Color.parseColor("#333333"));
                    getHandler().sendEmptyMessage(WHAT_CURSOR);
                }
            }
        });
        edCheckcode.setText("");
        tvGetCheckCode.setOnClickListener(this);
        llUserAgreement.setOnClickListener(this);
        tvGetCheckCodeAgain.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MFStaticConstans.needRefOrder = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        MFStaticConstans.needRefOrder = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_leftimg:
                if (isPhoneNumPage) {
                    mContext.finish();
                } else {
                    initPhoneNum(400);
                }
                break;
            case R.id.ll_user_agreement:
                Bundle bundle = new Bundle();
                bundle.putString("url", MFUrl.USERAGREEMENT);
                bundle.putString("title", "用户服务协议");
                startActivity(WebAty.class, bundle);
                break;
            case R.id.tv_get_check_code:
                String mobilNumber = edPhoneNum.getText().toString().trim();
                checkPhonenum(mobilNumber);
                break;
            case R.id.tv_title_right:
                if (!TextUtils.isEmpty(edPhoneNum.getText().toString().trim())) {
                    showSoundDlg(edPhoneNum.getText().toString().trim());
                } else {
                    MFUtil.showToast(mContext, "请输入正确的手机号码");
                }
                break;
            case R.id.tv_get_check_code_again:
                String mobilNumber1 = edPhoneNum.getText().toString().trim();
                checkPhonenum(mobilNumber1);
                break;
        }
    }

    private void showSoundDlg(String mobilNumber) {
        OneDialog oneDialog = new OneDialog(mContext, "稍后我们将会通过电话的方式为您播报验证码，请您注意接听", "朕知道了");
        oneDialog.setTag(mobilNumber);
        oneDialog.setDialogClickListener(new DialogClickListener() {
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
                if (checkTimes()) {
                    d.dismiss();
                    return;
                }
                getSoundCode((String) ((OneDialog) d).getTag());
                d.dismiss();
            }
        });
        oneDialog.show();
    }

    private void getSoundCode(String phoneNumber) {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", phoneNumber);
        MFRunner.requestPost(TAG_BROADSMS, MFUrl.BROADSMS, params, this);
    }

    private boolean checkTimes() {
        String lasttimestr[] = SharedPreferenceTool.getPrefString(mContext, "codeTime", "0,0").split(",");//后面是次数
        /**
         * 上次的次数
         */
        int lasttimes = Integer.parseInt(lasttimestr[1]);
        long nowtime = System.currentTimeMillis();
        long lasttime = Long.parseLong(lasttimestr[0]);
        if ((nowtime - lasttime) / 1000 <= 10 * 60 && (lasttimes + 1) > 3) {
            MFUtil.showToast(mContext, "您操作太频繁，请10分钟后再试");
            return true;
        }
        if ((nowtime - lasttime) / 1000 <= 10 * 60) {
            SharedPreferenceTool.setPrefString(mContext, "codeTime", lasttime + "," + (lasttimes + 1));
        } else {
            SharedPreferenceTool.setPrefString(mContext, "codeTime", nowtime + "," + 1);
        }
        return false;
    }

    private boolean checkPhoneNumber(String phoneNumber) {

//        Pattern regex = Pattern
//                .compile("^(((13[0-9])|(14[0-9])|(15([0-3]|[5-9]))|(17([0-9]))|(18([0-9]))|(19([0-9])))\\d{8})$");
//        Matcher matcher = regex.matcher(phoneNumber);
        return true;
    }

    private void checkPhonenum(String mobilNumber) {
        if (checkPhoneNumber(mobilNumber)) {
            if (!MFUtil.checkNetwork(mContext)) {
                showNoNetworkDialog(true);
                return;
            }
            initCheckCode();
            tvPhonenumCode.setText("验证码已发送至 " + mobilNumber + "");
            if (!phoneNumOld.equals(mobilNumber) || phoneNumOld.equals(mobilNumber) && !isDuringTime) {
                MobclickAgent.onEvent(mContext, "click_getcodeagain");
                RequestParams params = new RequestParams();
                params.addBodyParameter("mobile", mobilNumber);
                MFRunner.requestPost(GET_CHECKCODE_REQUEST_TAG, MFUrl.getCheckCodeURL, params, this);
                tvGetCheckCodeAgain.setClickable(false);
                tvGetCheckCodeAgain.setBackgroundColor(Color.parseColor("#dddddd"));
                tvGetCheckCodeAgain.setSelected(false);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                countDownTimer = new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (tvGetCheckCodeAgain != null) {
                            isDuringTime = true;
                            tvGetCheckCodeAgain.setText(String.valueOf(millisUntilFinished / 1000) + "秒后重新获取");
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (tvGetCheckCodeAgain != null) {
                            isDuringTime = false;
                            tvGetCheckCodeAgain.setText("重新获取");
                            tvGetCheckCodeAgain.setClickable(true);
                            tvGetCheckCodeAgain.setBackgroundResource(R.drawable.login_button_bg);
                            tvGetCheckCodeAgain.setSelected(true);
                        }
                    }
                };
                countDownTimer.start();
                phoneNumOld = mobilNumber;
            }
        } else {
            MFUtil.showToast(mContext, "手机号码不正确");
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
        if (!isAdded()) {
            return;
        }
        dismmisDialog();
        switch (tag) {
            case LOGIN_REQUEST_TAG:
                String loginString = responseInfo.result.toString();
                RequestResultBean<UserBean> requestResultBean = GsonTransformUtil.getObjectFromJson(loginString, new TypeToken<RequestResultBean<UserBean>>() {
                }.getType());
                if (requestResultBean != null && MFConstansValue.BACK_SUCCESS == requestResultBean.getCode()) {
                    UserBean userBean = requestResultBean.getData();
                    if (userBean != null && userBean.getUser() != null) {
                        MFStaticConstans.setUserBean(userBean);
                        getUploadDeviceToken();

                        if (SharedPreferenceTool.getPrefBoolean(mContext, "deposit", true) && userBean.getUser().getDepositState() == 0) {
                            SharedPreferenceTool.setPrefBoolean(mContext, "deposit", false);
                            startActivity(CarDepositAct.class, null);
                            getActivity().finish();
                        } else if (SharedPreferenceTool.getPrefBoolean(mContext, "forWeb", true)) {
                            SharedPreferenceTool.setPrefBoolean(mContext, "forWeb", false);
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        } else {
                            SharedPreferenceTool.setPrefBoolean(mContext, "deposit", false);
                            Bundle mainBundle = new Bundle();
                            mainBundle.putBoolean("openLeftMenu", true);
                            MFStaticConstans.needRefUserInfo = true;
                            startActivity(MainAct.class, mainBundle);
                            getActivity().finish();
                        }
//                        getUserInfo();
                    }
                } else if (requestResultBean != null) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    edCheckcode.setText("");
                }
                break;
            case GET_CHECKCODE_REQUEST_TAG:
                String getCheckCodeInfoString = responseInfo.result.toString();
                RequestResultBean getCheckCodeInfo = GsonTransformUtil.getObjectFromJson(getCheckCodeInfoString, new TypeToken<RequestResultBean>() {
                }.getType());
                if (getCheckCodeInfo != null && getCheckCodeInfo.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MFUtil.showToast(mContext, "验证码发送成功，请注意查收");
                } else if (getCheckCodeInfo != null) {
                    MFUtil.showToast(mContext, getCheckCodeInfo.getMessage());
                }
                break;
            case TAG_BROADSMS:
                String getbroadsmsstring = responseInfo.result.toString();
                RequestResultBean requestResultBean_broadsms = GsonTransformUtil.getObjectFromJson(getbroadsmsstring, new TypeToken<RequestResultBean>() {
                }.getType());
                if (requestResultBean_broadsms != null && requestResultBean_broadsms.getCode() == MFConstansValue.BACK_SUCCESS) {
                } else if (requestResultBean_broadsms != null) {
                    MFUtil.showToast(mContext, requestResultBean_broadsms.getMessage());
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
            case LOGIN_REQUEST_TAG:
            case GET_CHECKCODE_REQUEST_TAG:
                MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
                dismmisDialog();
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        getHandler().removeMessages(WHAT_CURSOR);
        super.onDestroy();
    }

    private void getUploadDeviceToken() {
        String usertoken = MFStaticConstans.getUserBean().getToken();
        if (!MFUtil.checkNetwork(mContext) || TextUtils.isEmpty(usertoken) || TextUtils.isEmpty(SharedPreferenceTool.getPrefString(mContext,
                SharedPreferenceTool.KEY_XGTOKEN, ""))) {
            return;
        }
        RequestParams adrParams = new RequestParams();
        adrParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        adrParams.addBodyParameter("phoneType", "2");
        adrParams.addBodyParameter("deviceToken",
                SharedPreferenceTool.getPrefString(mContext,
                        SharedPreferenceTool.KEY_XGTOKEN, ""));
        MFRunner.requestPost(TAG_ADDPHONEDEVICEINFO, MFUrl.addphonedeviceinfo, adrParams, this);
    }

    private void attempLogin(String phoneNumber, String code) {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        if (!checkPermission(PermissionHelper.ACCESS_FINE_LOCATION_MODEL)) {
            MFUtil.showToast(mContext, "检测到没有定位权限，请设置后重试");
            return;
        }
        dialogShow("正在登录,请稍后...");
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("code", code);
        if (MFApp.getInstance().getmLocation() != null) {
            params.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
            XGPushManager.setTag(mContext, MFApp.getInstance().getmLocation().getAdCode());
        }
        params.addBodyParameter("phoneType", "2");
        params.addBodyParameter("deviceToken",
                SharedPreferenceTool.getPrefString(mContext,
                        SharedPreferenceTool.KEY_XGTOKEN, ""));
        MFRunner.requestPost(LOGIN_REQUEST_TAG, MFUrl.LOGIN_URL, params, this);
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }

    public void onKeyDown() {
        if (isPhoneNumPage) {
            mContext.finish();
        } else {
            initPhoneNum(400);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}