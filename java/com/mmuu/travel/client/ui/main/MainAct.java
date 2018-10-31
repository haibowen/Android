package com.mmuu.travel.client.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseActivity;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.ui.user.LeftMenuFrg;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XIXIHAHA on 2016/12/12.
 * 地图主界面
 */

public class MainAct extends MFBaseActivity implements PublicRequestInterface, A2B {
    @BindView(R.id.base_contextlayout)
    FrameLayout contextLayout;
    @BindView(R.id.left_drawer)
    FrameLayout leftDrawerLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private MainFrg mainFrg;
    private LeftMenuFrg leftMenuFrg;
    private long exitTime = 0;
    private static final int TAG_ADDPHONEDEVICEINFO = 1695703;

    private A2B main2ACT, left2ACT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mf_main_act);
        ButterKnife.bind(this);
        initView();
        initXGPush();

    }

    private void initView() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 允许手势侧滑
        mainFrg = new MainFrg();
        leftMenuFrg = new LeftMenuFrg();
        mainFrg.setAct2Main(this);
        leftMenuFrg.setLeft2Act(this);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, mainFrg);
        transaction.replace(R.id.left_drawer, leftMenuFrg);
        transaction.commitAllowingStateLoss();
        drawerLayout.addDrawerListener(leftMenuFrg);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);// 允许手势侧滑
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 允许手势侧滑
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null && intent.getExtras().getBoolean("openLeftMenu")) {

            if (drawerLayout != null) {
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                            drawerLayout.openDrawer(Gravity.LEFT);
                        }
                    }
                }, 350);

            }
        }
        if (intent.getExtras() != null && intent.getExtras().getBoolean("closeLeftMenu")) {
            if (drawerLayout != null) {
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                }, 350);
            }
        }
    }

    private void initXGPush() {
        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
        // XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        Context context = getApplicationContext();
        XGPushManager.registerPush(context, new XGIOperateCallback() {

            @Override
            public void onSuccess(Object arg0, int arg1) {
                // TODO Auto-generated method stub
                SharedPreferenceTool.setPrefString(MainAct.this,
                        SharedPreferenceTool.KEY_XGTOKEN, arg0.toString());
                getUploadDeviceToken();
            }

            @Override
            public void onFail(Object arg0, int arg1, String arg2) {
                // TODO Auto-generated method stub
                System.err.println("xgpush register faluse");
            }
        });
    }


    private void getUploadDeviceToken() {
        String usertoken = MFStaticConstans.getUserBean().getToken();
        if (!MFUtil.checkNetwork(this) || TextUtils.isEmpty(usertoken) || TextUtils.isEmpty(SharedPreferenceTool.getPrefString(MainAct.this,
                SharedPreferenceTool.KEY_XGTOKEN, ""))) {
            return;
        }
        RequestParams adrParams = new RequestParams();
        adrParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        adrParams.addBodyParameter("phoneType", "2");
        adrParams.addBodyParameter("deviceToken",
                SharedPreferenceTool.getPrefString(MainAct.this,
                        SharedPreferenceTool.KEY_XGTOKEN, ""));
        MFRunner.requestPost(TAG_ADDPHONEDEVICEINFO, MFUrl.addphonedeviceinfo, adrParams, this);

        PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                PersonalCenterVo.class);
        if (personalCenterVo != null) {
            XGPushManager.setTag(this, personalCenterVo.getCityName());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                MFUtil.showToast(MainAct.this, getResources()
                        .getString(R.string.app_exit_tip));
                exitTime = System.currentTimeMillis();
            } else {
                MFApp.getInstance().exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {

    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {

    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {

    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    @Override
    public int callB(int type, Object o) {
        switch (type) {
            case 1:
                //mainFrg 打开侧滑菜单
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case -1:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            case -2:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
        }
        return 0;
    }
}
