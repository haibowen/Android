package com.mmuu.travel.client.ui.user;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * JGW 身份认证frg
 */

public class CertificationFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, TextWatcher {
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_idcardtxt)
    TextView tvIdcardtxt;
    @BindView(R.id.et_idcard)
    EditText etIdcard;
    @BindView(R.id.tv_certification)
    TextView tvCertification;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.iv_activity)
    ImageView ivActivity;
    @BindView(R.id.rl_animlay)
    RelativeLayout rlAnimlay;

    private Bundle bundle;
    private View certificationView;
    private static final int TAG_AUTHENTICATE_URL = 1695701;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (certificationView == null) {
            certificationView = inflater.inflate(R.layout.frg_certification, null);
            ButterKnife.bind(this, certificationView);
            initViews();
        }
        return certificationView;
    }

    private void initViews() {
        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                ObjectAnimator animatorrlTagtxt = ObjectAnimator.ofFloat(rlAnimlay,
                        "TranslationY", -height / 2);
                animatorrlTagtxt.setDuration(200);
                animatorrlTagtxt.start();
            }

            @Override
            public void keyBoardHide(int height) {
                ObjectAnimator animatorrlTagtxt = ObjectAnimator.ofFloat(rlAnimlay,
                        "TranslationY", 0);
                animatorrlTagtxt.setDuration(200);
                animatorrlTagtxt.start();
            }
        });

        navLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText(R.string.user_identification);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        etIdcard.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        tvCertification.setOnClickListener(this);
        etIdcard.requestFocus();
        tvCertification.setClickable(false);
        tvCertification.setSelected(false);
        tvCertification.setBackgroundColor(Color.parseColor("#dddddd"));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivActivity.getLayoutParams();
        params.width = ScreenUtil.getScreenWidth(mContext);
        params.height = 320 * params.width / 720;
        ivActivity.setLayoutParams(params);
        if (mContext.getIntent() != null) {
            bundle = mContext.getIntent().getExtras();
        }
    }

    private void requestData(String name, String idCard, String mobile, String from) {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("idCard", idCard);
        requestParams.addBodyParameter("mobile", mobile);
        requestParams.addBodyParameter("from", from);//来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动

        MFRunner.requestPost(TAG_AUTHENTICATE_URL, MFUrl.AUTHENTICATE_URL, requestParams, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_certification:
                String name = etName.getText().toString().trim();
                String idCardNumber = etIdcard.getText().toString().trim();
//                IDNumber number = new IDNumber();
//                Pattern pa = Pattern.compile("^[\u4e00-\u9fa5]*$");
//                Matcher matcher = pa.matcher(name);
//                if (!matcher.find()) {
//                    MFUtil.showToast(mContext, "请输入中文姓名");
//                    return;
//                } else if (name.length() < 2) {
//                    MFUtil.showToast(mContext, "名字不能少于2位哦");
//                    return;
//                } else if (!number.isIDnumber(idCardNumber)) {
//                    MFUtil.showToast(mContext, "请输入合法身份证号");
//                    return;
//                }
                if (bundle != null && bundle.containsKey("typeentrance")) {
                    MobclickAgent.onEvent(mContext, "click_certification_certification");
                    requestData(name, idCardNumber, MFStaticConstans.getUserBean().getUser().getMobile(), bundle.getInt("typeentrance") + "");
                }
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
            case TAG_AUTHENTICATE_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean sBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (sBean != null && MFConstansValue.BACK_SUCCESS == sBean.getCode()) {
                    MFUtil.showToast(mContext, sBean.getMessage());
                    mContext.setResult(Activity.RESULT_OK);
                    mContext.finish();
                } else if (sBean != null && MFConstansValue.BACK_LOGOUT == sBean.getCode()) {
                    MFUtil.showToast(mContext, sBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (sBean != null) {
                    MFUtil.showToast(mContext, sBean.getMessage());
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (checkContent()) {
            tvCertification.setClickable(true);
            tvCertification.setBackgroundResource(R.drawable.login_button_bg);
            tvCertification.setSelected(true);
        } else {
            tvCertification.setClickable(false);
            tvCertification.setSelected(false);
            tvCertification.setBackgroundColor(Color.parseColor("#dddddd"));
        }
    }

    private boolean checkContent() {
        String name = etName.getText().toString().trim();
        String idCardNumber = etIdcard.getText().toString().trim();
//        IDNumber number = new IDNumber();
//        Pattern pa = Pattern.compile("^[\u4e00-\u9fa5]*$");
//        Matcher matcher = pa.matcher(name);
//        if (!matcher.find()) {
//            return false;
//        } else if (name.length() <= 0) {
//            return false;
//        } else if (!number.isIDnumber(idCardNumber)) {
//            return false;
//        } else {
//            return true;
//        }
        return name.length() > 0 && idCardNumber.length() > 0;
    }


    /**
     * 监听键盘弹出高度变化
     * Created by liujinhua on 15/10/25.
     */
    public static class SoftKeyBoardListener {
        private View rootView;//activity的根视图
        int rootViewVisibleHeight;//纪录根视图的显示高度
        private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

        public SoftKeyBoardListener(Activity activity) {
            //获取activity的根视图
            rootView = activity.getWindow().getDecorView();

            //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //获取当前根视图在屏幕上显示的大小
                    Rect r = new Rect();
                    rootView.getWindowVisibleDisplayFrame(r);

                    int visibleHeight = r.height();
                    System.out.println("" + visibleHeight);
                    if (rootViewVisibleHeight == 0) {
                        rootViewVisibleHeight = visibleHeight;
                        return;
                    }

                    //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                    if (rootViewVisibleHeight == visibleHeight) {
                        return;
                    }

                    //根视图显示高度变小超过200，可以看作软键盘显示了
                    if (rootViewVisibleHeight - visibleHeight > 200) {
                        if (onSoftKeyBoardChangeListener != null) {
                            onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                        }
                        rootViewVisibleHeight = visibleHeight;
                        return;
                    }

                    //根视图显示高度变大超过200，可以看作软键盘隐藏了
                    if (visibleHeight - rootViewVisibleHeight > 200) {
                        if (onSoftKeyBoardChangeListener != null) {
                            onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                        }
                        rootViewVisibleHeight = visibleHeight;
                        return;
                    }

                }
            });
        }

        private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
            this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
        }

        public interface OnSoftKeyBoardChangeListener {
            void keyBoardShow(int height);

            void keyBoardHide(int height);
        }

        public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
            SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(activity);
            softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
        }
    }
}