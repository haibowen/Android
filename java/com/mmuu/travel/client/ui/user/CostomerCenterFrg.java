package com.mmuu.travel.client.ui.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.ui.other.AdviseAct;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.widget.StatusView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XIXIHAHA on 2017/7/18.
 * 客服中心
 */

public class CostomerCenterFrg extends MFBaseFragment implements View.OnClickListener {


    @BindView(R.id.v_status)
    StatusView vStatus;
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
    @BindView(R.id.title_left_image_mark)
    TextView titleLeftImageMark;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.ll_carrepair)
    LinearLayout llCarrepair;
    @BindView(R.id.ll_carstop)
    LinearLayout llCarstop;
    @BindView(R.id.ll_topfunction)
    LinearLayout llTopfunction;
    @BindView(R.id.tv_carproblem)
    TextView tvCarproblem;
    @BindView(R.id.tv_orderproblem)
    TextView tvOrderproblem;
    @BindView(R.id.tv_useguide)
    TextView tvUseguide;
    @BindView(R.id.ll_class)
    LinearLayout llClass;
    @BindView(R.id.tv_advice)
    TextView tvAdvice;
    @BindView(R.id.tv_costomer_kefu)
    TextView tvCostomerKefu;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.costomer_ll_0)
    LinearLayout costomerLl0;
    @BindView(R.id.costomer_ll_1)
    LinearLayout costomerLl1;
    @BindView(R.id.costomer_ll_3)
    LinearLayout costomerLl3;
    @BindView(R.id.costomer_ll_4)
    LinearLayout costomerLl4;
    @BindView(R.id.ll_carproblem)
    LinearLayout llCarproblem;
    @BindView(R.id.costomer_l2_1)
    LinearLayout costomerL21;
    @BindView(R.id.ll_orderproblem)
    LinearLayout llOrderproblem;
    @BindView(R.id.costomer_l3_0)
    LinearLayout costomerL30;
    @BindView(R.id.costomer_l3_1)
    LinearLayout costomerL31;
    @BindView(R.id.costomer_l3_3)
    LinearLayout costomerL33;
    @BindView(R.id.ll_useguide)
    LinearLayout llUseguide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_costomer_center, null);
            ButterKnife.bind(this, fragContentView);
            initView();
//            loadingDialog = new LoadingFragmentDialog();
        }
        return fragContentView;
    }


    private void initView() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleTitle.setText(mContext.getResources().getString(R.string.user_userguide));

        tvCarproblem.setSelected(true);
        tvOrderproblem.setSelected(false);
        tvUseguide.setSelected(false);
        llCarproblem.setVisibility(View.VISIBLE);
        llOrderproblem.setVisibility(View.GONE);
        llUseguide.setVisibility(View.GONE);

        titleLeftImage.setOnClickListener(this);
        llCarrepair.setOnClickListener(this);
        llCarstop.setOnClickListener(this);
        tvCarproblem.setOnClickListener(this);
        tvOrderproblem.setOnClickListener(this);
        tvUseguide.setOnClickListener(this);
        tvAdvice.setOnClickListener(this);
        tvCostomerKefu.setOnClickListener(this);


        costomerLl0.setOnClickListener(this);
        costomerLl1.setOnClickListener(this);
        costomerLl3.setOnClickListener(this);
        costomerLl4.setOnClickListener(this);
        costomerL21.setOnClickListener(this);
        costomerL30.setOnClickListener(this);
        costomerL31.setOnClickListener(this);
        costomerL33.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.ll_carrepair:
                Bundle bundlecarrepair = new Bundle();
                bundlecarrepair.putString("pagetype", MFConstansValue.FEELBACK_MAIN_BREAK);
                startActivity(TripFeedbackDetailAct.class, bundlecarrepair);
                break;
            case R.id.ll_carstop:
                Bundle bundlecarstop = new Bundle();
                bundlecarstop.putString("pagetype", MFConstansValue.FEELBACK_MAIN_STOPCAR);
                startActivity(TripFeedbackDetailAct.class, bundlecarstop);
                break;
            case R.id.tv_carproblem:
                tvCarproblem.setSelected(true);
                tvOrderproblem.setSelected(false);
                tvUseguide.setSelected(false);
                llCarproblem.setVisibility(View.VISIBLE);
                llOrderproblem.setVisibility(View.GONE);
                llUseguide.setVisibility(View.GONE);
                break;
            case R.id.tv_orderproblem:
                tvCarproblem.setSelected(false);
                tvOrderproblem.setSelected(true);
                tvUseguide.setSelected(false);
                llCarproblem.setVisibility(View.GONE);
                llOrderproblem.setVisibility(View.VISIBLE);
                llUseguide.setVisibility(View.GONE);
                break;
            case R.id.tv_useguide:
                tvCarproblem.setSelected(false);
                tvOrderproblem.setSelected(false);
                tvUseguide.setSelected(true);
                llCarproblem.setVisibility(View.GONE);
                llOrderproblem.setVisibility(View.GONE);
                llUseguide.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_advice:
                startActivity(AdviseAct.class, null);
                break;
            case R.id.tv_costomer_kefu:
                try {
                    if (isInServiceTime()) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                .parse("tel:" + SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_CALLNUMBER, "13511010167")));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        mContext.overridePendingTransition(
                                R.anim.activity_up, R.anim.activity_push_no_anim);
                    } else {
                        showOutServiceTimeDialog();
                    }
                } catch (Exception e) {
                    MFUtil.showToast(mContext, "请检查是否开启电话权限");
                }
                break;


            case R.id.costomer_ll_0:
                Bundle bundle = new Bundle();
                bundle.putString("pagetype", MFConstansValue.FEELBACK_WEB_NOTLOCK);
                startActivity(TripFeedbackDetailAct.class, bundle);
                break;
            case R.id.costomer_ll_1:
                Bundle bundle11 = new Bundle();
                bundle11.putString("pagetype", MFConstansValue.FEELBACK_WEB_COULDNOTSTART);
                startActivity(TripFeedbackDetailAct.class, bundle11);
                break;
            case R.id.costomer_ll_3:
                Bundle bundle13 = new Bundle();
                bundle13.putString("pagetype", MFConstansValue.FEELBACK_MAIN_XUHANGBUZHUN);
                startActivity(TripFeedbackDetailAct.class, bundle13);
                break;
            case R.id.costomer_ll_4:
                Bundle bundle14 = new Bundle();
                bundle14.putString("pagetype", MFConstansValue.FEELBACK_MAIN_BREAK);
                startActivity(TripFeedbackDetailAct.class, bundle14);
                break;
            case R.id.costomer_l2_1:

                Intent intent1 = new Intent(mContext, TripFeedbackCannotReturnAct.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("pagetype", MFConstansValue.FEELBACK_CURRENTORDER_NOTENDORDER);
                bundle1.putInt("showCallKeFu", 2);
                intent1.putExtras(bundle1);
                startActivity(intent1);

                break;
            case R.id.costomer_l3_0:
                Bundle bundle30 = new Bundle();
                bundle30.putString("url", MFUrl.GETCASHCOUPON);
                bundle30.putString("title", "出行券说明");
                startActivity(WebAty.class, bundle30);
                break;
            case R.id.costomer_l3_1:
                Bundle bundle31 = new Bundle();
                bundle31.putString("url", MFUrl.CREDITSCORE);
                bundle31.putString("title", "信用积分说明");
                startActivity(WebAty.class, bundle31);
                break;
            case R.id.costomer_l3_3:
                MobclickAgent.onEvent(mContext, "click_usercenter_userguide");
                Bundle bundle40 = new Bundle();
                bundle40.putString("url", MFUrl.INDEX);
                bundle40.putString("title", "用户指南");
                startActivity(WebAty.class, bundle40);
                break;

        }
    }
//    private void init() {
//
//        if (!NetUtils.hasDataConnection(mContext)) {
//            Toast.makeText(mContext, "当前没有网络连接", Toast.LENGTH_SHORT).show();
//            if (costomerKefu != null) {
//                costomerKefu.setClickable(true);
//            }
//            return;
//        }
//
//        loadingDialog.show(mContext.getFragmentManager(), "");
//        if (MFApp.isKFSDK) {
//            loadingDialog.dismiss();
//            if (costomerKefu != null) {
//                costomerKefu.setClickable(true);
//            }
//            getPeers();
//        } else {
//            startKFService();
//        }
//    }
//
//    private void getPeers() {
//        IMChatManager.getInstance().getPeers(new GetPeersListener() {
//            @Override
//            public void onSuccess(List<Peer> peers) {
//                System.out.println("获取技能组成功");
//                if (peers.size() > 1) {
//                    PeerDialog dialog = new PeerDialog();
//                    Bundle b = new Bundle();
//                    b.putSerializable("Peers", (Serializable) peers);
//                    b.putString("type", "init");
//                    dialog.setArguments(b);
//                    dialog.show(mContext.getFragmentManager(), "");
//
//                } else if (peers.size() == 1) {
//                    startChatActivity(peers.get(0).getId());
//                } else {
//                    startChatActivity("");
//                }
//            }
//
//            @Override
//            public void onFailed() {
//                System.out.println("获取技能组失败了");
//            }
//        });
//    }
//
//    private void startKFService() {
//
//        new Thread() {
//            @Override
//            public void run() {
//                IMChatManager.getInstance().setOnInitListener(new InitListener() {
//                    @Override
//                    public void oninitSuccess() {
//                        if (costomerKefu != null) {
//                            costomerKefu.setClickable(true);
//                        }
//                        MFApp.isKFSDK = true;
//                        loadingDialog.dismiss();
//                        getPeers();
//                        Log.d("MFApp", "sdk初始化成功");
//
//                    }
//
//                    @Override
//                    public void onInitFailed() {
//                        if (costomerKefu != null) {
//                            costomerKefu.setClickable(true);
//                        }
//                        MFApp.isKFSDK = false;
//                        loadingDialog.dismiss();
//                        MFUtil.showToast(mContext, "客服初始化失败");
//                        Log.d("MFApp", "sdk初始化失败, 请填写正确的accessid");
//                    }
//                });
//
//
//                if (!TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
//                    //初始化IMSdk,填入相关参数
//                    if (!TextUtils.isEmpty(MFStaticConstans.getUserBean().getUser().getName())) {
//                        IMChatManager.getInstance().init(MFApp.getInstance(), "action", "35a0e770-622c-11e7-9bd9-21ea7208aea7", MFStaticConstans.getUserBean().getUser().getName(), "MF_" + MFStaticConstans.getUserBean().getUser().getMobile());
//                    } else {
//                        IMChatManager.getInstance().init(MFApp.getInstance(), "action", "35a0e770-622c-11e7-9bd9-21ea7208aea7", MFStaticConstans.getUserBean().getUser().getMobile(), "MF_" + MFStaticConstans.getUserBean().getUser().getMobile());
//                    }
//                } else {
//                    if (costomerKefu != null) {
//                        costomerKefu.setClickable(true);
//                    }
//                    startActivity(LoginAct.class, null);
//                }
//            }
//        }.start();
//
//    }
//
//    private void startChatActivity(String peerId) {
//        Intent chatIntent = new Intent(mContext, ChatActivity.class);
//        chatIntent.putExtra("PeerId", peerId);
//        startActivity(chatIntent);
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0x1111:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    init();
                }
                break;
        }
    }
}
