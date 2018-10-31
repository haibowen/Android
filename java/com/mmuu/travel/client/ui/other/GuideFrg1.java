package com.mmuu.travel.client.ui.other;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JGW 启动页frg
 */
public class GuideFrg1 extends MFBaseFragment {
    @BindView(R.id.gui_context_layout)
    RelativeLayout contextLayout;

    @BindView(R.id.iv_guidebg)
    ImageView ivGuidebg;
    @BindView(R.id.tv_gui_text1)
    TextView textTitle;
    @BindView(R.id.tv_gui_text2)
    TextView textTitleSub;

    LottieAnimationView animationView;

    private View startPageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (startPageView == null) {
            startPageView = inflater.inflate(R.layout.frg_guide0, null);
            ButterKnife.bind(this, startPageView);
            ivGuidebg.setImageResource(R.drawable.guide1);
            textTitle.setText("超时提醒，不忘还车");
            textTitleSub.setText("订单超时提醒，再也不怕忘还车");
            animationView = (LottieAnimationView) startPageView.findViewById(R.id.animation_view);
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
        } catch (Exception e) {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (!isVisibleToUser && ivText != null) {
//            rlText.setVisibility(View.GONE);
//            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.guidetxt_anim_gone);
//            rlText.startAnimation(animation);
//        }
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


    public void setAlpha(float a) {
        if (contextLayout != null) {
            if (a > 0.9f) {
                a = 1f;
            }
            contextLayout.setAlpha(a);
        }
    }

    public void showAnim() {
        if (animationView != null && animationView.getTag() == null) {
            animationView.setAnimation("gui_clock.json");
            animationView.playAnimation();
            animationView.setTag(new Object());
        }
    }

    public void clearAnim() {
        if (animationView != null) {
            animationView.cancelAnimation();
            animationView.setTag(null);
        }
    }
}