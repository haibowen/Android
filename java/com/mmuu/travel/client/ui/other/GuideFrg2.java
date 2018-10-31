package com.mmuu.travel.client.ui.other;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.tools.SystemUtils;
import com.mmuu.travel.client.ui.main.MainAct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JGW 启动页frg
 */
public class GuideFrg2 extends MFBaseFragment {

    @BindView(R.id.gui_context_layout)
    RelativeLayout contextLayout;
    @BindView(R.id.iv_guidebg)
    ImageView ivGuidebg;
    @BindView(R.id.tv_gui_text1)
    TextView textTitle;
    @BindView(R.id.tv_gui_text2)
    TextView textTitleSub;

    @BindView(R.id.lay_gui_bottom)
    LinearLayout bottomLayout;
    @BindView(R.id.iv_gui_next)
    ImageView bottomNext;

    private View startPageView;
    private LottieAnimationView animationView;
    private int point = 0;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case 6668:
                bottomLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (startPageView == null) {
            startPageView = inflater.inflate(R.layout.frg_guide0, null);
            ButterKnife.bind(this, startPageView);
            ivGuidebg.setImageResource(R.drawable.guide2);
            animationView = startPageView.findViewById(R.id.animation_view);
            textTitle.setText("临时锁车，想停就停");
            textTitleSub.setText("短时间办事更方便");
            bottomLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    point = bottomLayout.getLayoutParams().height;

                    if (bottomLayout.getTag() == null) {
                        showOrDismmisTop(true);
                        getHandler().sendEmptyMessageDelayed(6668, 200);
                    }
                    bottomLayout.setTag("");
                }
            });


            bottomNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferenceTool.setPrefInt(mContext, MFConstansValue.SHARE_KEY_VERIONCODE, SystemUtils.getVersionCode(mContext));
                    startActivity(MainAct.class, null);
                    mContext.finish();
                }
            });
        }
        return startPageView;
    }

    public void startAnim() {
        try {
            setAlpha(1f);
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.guidetxt_anim_visable);
            animation.setInterpolator(new DecelerateInterpolator());
//            rlText.startAnimation(animation);
//            rlText.setVisibility(View.VISIBLE);
            getBaseHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showOrDismmisTop(false);
                }
            }, 350);
        } catch (Exception e) {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && bottomLayout != null) {
            showOrDismmisTop(true);
//            rlText.setVisibility(View.GONE);
//            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.guidetxt_anim_gone);
//            rlText.startAnimation(animation);
        }
    }

    @Override
    public void onDestroy() {
        if (ivGuidebg != null && ivGuidebg.getDrawable() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) ivGuidebg.getDrawable()).getBitmap();
            ivGuidebg.setImageResource(0);
            if (oldBitmap != null && !oldBitmap.isRecycled()) {
                oldBitmap.recycle();
                oldBitmap = null;//回收之后再次使用会出现问题
            }
        }
        super.onDestroy();
    }


    private void showOrDismmisTop(boolean dismmis) {

        if (dismmis) {
            if (bottomLayout.getY() == 0) {
                ObjectAnimator translationUp = ObjectAnimator.ofFloat(bottomLayout, "Y", +bottomLayout.getHeight());
                translationUp.setDuration(350);
                translationUp.start();
            }
        } else {
            if (bottomLayout.getY() != 0) {
                AnimationSet animSet = new AnimationSet(false);
                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(500);
                ObjectAnimator translationUp = ObjectAnimator.ofFloat(bottomLayout, "Y", 0);
                translationUp.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setAlpha(1f);
                    }
                });
                translationUp.setDuration(350);
                translationUp.start();
                bottomLayout.setAnimation(animation);
                bottomLayout.startAnimation(animation);
                //subKeepFrg.showData(bikeVO);
            }
        }
    }

    public void setAlpha(float a) {
        if (contextLayout != null) {
            if (a > 0.9f) {
                a = 1f;
            }
            contextLayout.setAlpha(a);
        }
    }

    public void showAnim() {
//        if (animationView != null && animationView.getTag() == null) {
//            animationView.setAnimation("gui_lock.json");
//            animationView.playAnimation();
//            animationView.setTag(new Object());
//        }
    }

    public void clearAnim() {
//        if (animationView != null) {
//            animationView.cancelAnimation();
//            animationView.setTag(null);
//        }
    }
}