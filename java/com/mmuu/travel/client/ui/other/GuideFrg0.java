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
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.mfConstans.MFOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JGW 启动页frg
 */
public class GuideFrg0 extends MFBaseFragment {

    @BindView(R.id.iv_guidebg)
    ImageView ivGuidebg;
    @BindView(R.id.tv_gui_text1)
    TextView textTitle;
    @BindView(R.id.tv_gui_text2)
    TextView textTitleSub;
    private View startPageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (startPageView == null) {
            startPageView = inflater.inflate(R.layout.frg_guide0, null);
            ButterKnife.bind(this, startPageView);
            ivGuidebg.setImageResource(R.drawable.guide0);
            textTitle.setText("蓝牙解锁，更方便");
            textTitleSub.setText("新增蓝牙解锁，无需等待，更快捷方便");
            startAnim();
        }
        return startPageView;
    }

    public void startAnim() {
        try {
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
//            ivText.setImageResource(0);
            if (oldBitmap != null && !oldBitmap.isRecycled()) {
                oldBitmap.recycle();
                oldBitmap = null;//回收之后再次使用会出现问题
            }
        }
        super.onDestroy();
    }
}