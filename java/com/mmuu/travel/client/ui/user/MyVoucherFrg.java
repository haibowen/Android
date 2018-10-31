package com.mmuu.travel.client.ui.user;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.widget.CustomAnmation;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 出行券
 * Created by HuangYuan on 2016/12/13.
 */

public class MyVoucherFrg extends MFBaseFragment implements View.OnClickListener {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.tv_usedtxt)
    TextView tvUsedtxt;
    @BindView(R.id.tv_overtimetxt)
    TextView tvOvertimetxt;
    @BindView(R.id.v_tag)
    View vTag;
    @BindView(R.id.tv_usedable)
    TextView tvUsedable;
    @BindView(R.id.rl_slidecontent)
    RelativeLayout rlSlidecontent;
    @BindView(R.id.frl_contextlayout)
    FrameLayout frlContextlayout;
    Unbinder unbinder;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;

    private MyVoucherOverTimedFrg myVoucherOverTimedFrg;
    private MyVoucherUsedFrg myVoucherUsedFrg;
    private MyVoucherAbleFrg myVoucherAbleFrg;
    private Fragment currentFragment;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_voucher, null);
            unbinder = ButterKnife.bind(this, fragContentView);
            initView();
        }
        return fragContentView;
    }

    private float downY;//点击坐标点
    private float margintop_move;//一个方向累计移动的距离
    private boolean oldDiretion;//false为上滑
    private float titleH;//选择栏高度

    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                RelativeLayout.LayoutParams lP = (RelativeLayout.LayoutParams) frlContextlayout.getLayoutParams();
                margintop_move = lP.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getY() < ScreenUtil.dip2px(mContext, 64.5f)) {
                    downY = ev.getY();
                    return false;
                }
                boolean newDirecion = ev.getY() > downY;//判断新方向
                float slideY_move = ev.getY() - downY;//此单位时间内滑动的距离
                if (oldDiretion != newDirecion) {
                    margintop_move = ((RelativeLayout.LayoutParams) frlContextlayout.getLayoutParams()).topMargin;
                    oldDiretion = newDirecion;
                }
                margintop_move = slideY_move + margintop_move;
                if (margintop_move <= titleH && margintop_move >= 0) {
                    Animation animation = new CustomAnmation(frlContextlayout, margintop_move);
                    animation.setDuration(0);
                    frlContextlayout.startAnimation(animation);
                    Animation animation2 = new CustomAnmation(rlSlidecontent, margintop_move - titleH);
                    animation2.setDuration(0);
                    rlSlidecontent.startAnimation(animation2);
                } else if (margintop_move > titleH) {
                    Animation animation = new CustomAnmation(frlContextlayout, titleH);
                    animation.setDuration(0);
                    frlContextlayout.startAnimation(animation);
                    Animation animation2 = new CustomAnmation(rlSlidecontent, 0);
                    animation2.setDuration(0);
                    rlSlidecontent.startAnimation(animation2);
                } else if (margintop_move < 0) {
                    Animation animation = new CustomAnmation(frlContextlayout, 0);
                    animation.setDuration(0);
                    frlContextlayout.startAnimation(animation);
                    Animation animation2 = new CustomAnmation(rlSlidecontent, -titleH);
                    animation2.setDuration(0);
                    rlSlidecontent.startAnimation(animation2);
                }
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (oldDiretion)//下滑
                {
                    if (margintop_move > titleH / 4) {
                        Animation animation1 = new CustomAnmation(frlContextlayout, titleH);
                        animation1.setDuration(150);
                        frlContextlayout.startAnimation(animation1);
                        Animation animation2 = new CustomAnmation(rlSlidecontent, 0);
                        animation2.setDuration(150);
                        rlSlidecontent.startAnimation(animation2);
                    } else {
                        Animation animation1 = new CustomAnmation(frlContextlayout, 0);
                        animation1.setDuration(150);
                        frlContextlayout.startAnimation(animation1);
                        Animation animation2 = new CustomAnmation(rlSlidecontent, -titleH);
                        animation2.setDuration(150);
                        rlSlidecontent.startAnimation(animation2);
                    }
                } else {
                    if (margintop_move < titleH / 4 * 3) {
                        Animation animation1 = new CustomAnmation(frlContextlayout, 0);
                        animation1.setDuration(150);
                        frlContextlayout.startAnimation(animation1);
                        Animation animation2 = new CustomAnmation(rlSlidecontent, -titleH);
                        animation2.setDuration(150);
                        rlSlidecontent.startAnimation(animation2);
                    } else {
                        Animation animation1 = new CustomAnmation(frlContextlayout, titleH);
                        animation1.setDuration(150);
                        frlContextlayout.startAnimation(animation1);
                        Animation animation2 = new CustomAnmation(rlSlidecontent, 0);
                        animation2.setDuration(150);
                        rlSlidecontent.startAnimation(animation2);
                    }
                }
                break;
        }
        return false;
    }

    private void initView() {
        titleTitle.setText("出行券");
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleRightText.setText(R.string.instructions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector, null));
        } else {
            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector));
        }
        titleRightText.setVisibility(View.VISIBLE);
        titleRightText.setOnClickListener(this);
        titleLeftImage.setOnClickListener(this);
        tvUsedable.setOnClickListener(this);
        tvUsedtxt.setOnClickListener(this);
        tvOvertimetxt.setOnClickListener(this);

        ObjectAnimator animatorusedable = ObjectAnimator.ofFloat(vTag,
                "TranslationX", -(ScreenUtil.getScreenWidth(mContext) / 2 - ScreenUtil.dip2px(mContext, 230) / 6));
        animatorusedable.setDuration(200);
        animatorusedable.setInterpolator(new LinearInterpolator());
        animatorusedable.start();

        myVoucherOverTimedFrg = new MyVoucherOverTimedFrg();
        myVoucherUsedFrg = new MyVoucherUsedFrg();
        myVoucherAbleFrg = new MyVoucherAbleFrg();
        switchFragment(myVoucherAbleFrg);
        titleH = ScreenUtil.dip2px(mContext, 45.3f);
    }

    //正确的做法
    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frl_contextlayout, targetFragment);
            transaction.commitAllowingStateLoss();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commitAllowingStateLoss();
        }
        currentFragment = targetFragment;
        tvOvertimetxt.setTextColor(getResources().getColor(R.color.c999999_hint_font3));
        tvUsedtxt.setTextColor(getResources().getColor(R.color.c999999_hint_font3));
        tvUsedable.setTextColor(getResources().getColor(R.color.c999999_hint_font3));
        if (targetFragment.equals(myVoucherOverTimedFrg)) {
            tvOvertimetxt.setTextColor(getResources().getColor(R.color.c333333_hint_font1));
        }
        if (targetFragment.equals(myVoucherUsedFrg)) {
            tvUsedtxt.setTextColor(getResources().getColor(R.color.c333333_hint_font1));
        }
        if (targetFragment.equals(myVoucherAbleFrg)) {
            tvUsedable.setTextColor(getResources().getColor(R.color.c333333_hint_font1));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_usedtxt:
                ObjectAnimator animatorused = ObjectAnimator.ofFloat(vTag,
                        "TranslationX", -(ScreenUtil.getScreenWidth(mContext) / 2 - ScreenUtil.dip2px(mContext, 230) / 2));
                animatorused.setDuration(200);
                animatorused.setInterpolator(new LinearInterpolator());
                animatorused.start();
                switchFragment(myVoucherUsedFrg);
                break;
            case R.id.tv_overtimetxt:
                ObjectAnimator animatorovertime = ObjectAnimator.ofFloat(vTag,
                        "TranslationX", -(ScreenUtil.getScreenWidth(mContext) / 2 - ScreenUtil.dip2px(mContext, 230) / 6 * 5));
                animatorovertime.setDuration(200);
                animatorovertime.setInterpolator(new LinearInterpolator());
                animatorovertime.start();
                switchFragment(myVoucherOverTimedFrg);
                break;
            case R.id.tv_usedable:
                ObjectAnimator animatorusedable = ObjectAnimator.ofFloat(vTag,
                        "TranslationX", -(ScreenUtil.getScreenWidth(mContext) / 2 - ScreenUtil.dip2px(mContext, 230) / 6));
                animatorusedable.setDuration(200);
                animatorusedable.setInterpolator(new LinearInterpolator());
                animatorusedable.start();
                switchFragment(myVoucherAbleFrg);
                break;
            case R.id.title_right_text:
                MobclickAgent.onEvent(mContext, "click_myvoucher_useclear");
                Bundle bundle = new Bundle();
                bundle.putString("url", MFUrl.CASHCOUPON);
                bundle.putString("title", "出行券使用规则");
                startActivity(WebAty.class, bundle);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
