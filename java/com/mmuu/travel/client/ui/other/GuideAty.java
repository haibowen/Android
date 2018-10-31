package com.mmuu.travel.client.ui.other;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseActivity;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.ui.other.adapter.GuidePageAdp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 引导页
 * JiaGuangWei on 2016/12/15 15:48
 */
public class GuideAty extends MFBaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;
    private GuideFrg0 guideFrg0;
    private GuideFrg1 guideFrg1;
    private GuideFrg2 guideFrg2;
    private GuidePageAdp guidePageAdp;
    private ArrayList<MFBaseFragment> list;
    private ImageView[] pointArry;
    private int padding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_guide);
        ButterKnife.bind(this);
        intiView();
    }

    private void intiView() {
        padding = ScreenUtil.dip2px(this, 2);
        guideFrg0 = new GuideFrg0();
        guideFrg1 = new GuideFrg1();
        guideFrg2 = new GuideFrg2();
        list = new ArrayList<MFBaseFragment>();
        list.add(guideFrg0);
        list.add(guideFrg1);
        list.add(guideFrg2);
        initPointGroup();
        FragmentManager fm = getSupportFragmentManager();
        guidePageAdp = new GuidePageAdp(fm);
        guidePageAdp.setList(list);
        vpGuide.setAdapter(guidePageAdp);
        vpGuide.setOffscreenPageLimit(3);
        vpGuide.addOnPageChangeListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            MFApp.getInstance().exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (position == 0) {

            if (guideFrg1 != null && guideFrg1.isAdded()) {
                guideFrg1.setAlpha(positionOffset);
            }

        } else if (position == 1) {
            if (guideFrg2 != null && guideFrg2.isAdded()) {
                guideFrg2.setAlpha(positionOffset);
            }
        } else if (position == 2) {

        }

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                guideFrg0.startAnim();
                guideFrg1.clearAnim();
                break;
            case 1:
                guideFrg1.startAnim();
                guideFrg1.showAnim();
                guideFrg2.clearAnim();
                break;
            case 2:
                guideFrg2.startAnim();
                guideFrg2.showAnim();
                break;
        }
        pointSetChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initPointGroup() {
        pointArry = new ImageView[list.size()];
        for (int i = 0; i < pointArry.length; i++) {
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams paramsPoint = new LinearLayout.LayoutParams(ScreenUtil.dip2px(this, 10), ScreenUtil.dip2px(this, 10));
            paramsPoint.setMargins(padding * 3, padding, padding * 3, padding);
            point.setLayoutParams(paramsPoint);
            point.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            pointArry[i] = point;
            if (i == 0) {
               /* pointArry[i].setPadding(5, 5, 5, 5);//节假日轮换字图标
                pointArry[i].setImageResource(imgTagIDs[0]);*/
//                pointArry[i].(padding * 3, padding, padding * 3, padding);
                pointArry[i].setImageResource(R.drawable.point_pre);
            } else {
//                pointArry[i].setPadding(padding * 3, padding, padding * 3, padding);
                pointArry[i].setImageResource(R.drawable.point_n);
            }
            llPoint.addView(pointArry[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void pointSetChange(int position) {
        for (int i = 0; i < pointArry.length; i++) {
            if (i == position) {
               /* pointArry[i].setPadding(5, 5, 5, 5);春节快乐时候使用
                pointArry[i].setImageResource(imgTagIDs[i]);*/
//                pointArry[i].setPadding(padding * 3, padding, padding * 3, padding);
                pointArry[i].setImageResource(R.drawable.point_pre);
            } else {
//                pointArry[i].setPadding(padding * 3, padding, padding * 3, padding);
                pointArry[i].setImageResource(R.drawable.point_n);
            }
        }
    }

}
