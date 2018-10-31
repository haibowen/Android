package com.mmuu.travel.client.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.tools.SharedPreferenceTool;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 无法结束订单反馈页面
 * Created by XIXIHAHA on 2017/7/31.
 */

public class TripFeedbackCannotReturnFrg extends MFBaseFragment implements View.OnClickListener {


    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.tv_callnumber)
    TextView tvCallnumber;


//    private LoadingFragmentDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_trip_cannot_return, null);
            ButterKnife.bind(this, fragContentView);
            initView();
        }
        return fragContentView;
    }


    private void initView() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleTitle.setText("无法结束订单");

        titleLeftImage.setOnClickListener(this);

        tvCallnumber.setVisibility(View.VISIBLE);
        tvCallnumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_callnumber:
                if (isInServiceTime()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel:" + SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_CALLNUMBER, "13511010167")));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    mContext.overridePendingTransition(R.anim.activity_up, R.anim.activity_push_no_anim);
                } else {
                    showOutServiceTimeDialog();
                }

//                if (loadingDialog == null) {
//                    loadingDialog = new LoadingFragmentDialog();
//                }
//
//                //判断版本若为6.0申请权限
//                view.setClickable(false);
//                if (Build.VERSION.SDK_INT < 23) {
//                    init();
//                } else {
//                    //6.0
//                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                        //该权限已经有了
//                        init();
//                    } else {
//                        view.setClickable(true);
//                        //申请该权限
//                        ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, 0x1111);
//                    }
//                }


                break;
        }
    }


//    private void init() {
//
//        if (!NetUtils.hasDataConnection(mContext)) {
//            Toast.makeText(mContext, "当前没有网络连接", Toast.LENGTH_SHORT).show();
//            if (tvCallnumber != null) {
//                tvCallnumber.setClickable(true);
//            }
//            return;
//        }
//
//        loadingDialog.show(mContext.getFragmentManager(), "");
//        if (MFApp.isKFSDK) {
//            loadingDialog.dismiss();
//            if (tvCallnumber != null) {
//                tvCallnumber.setClickable(true);
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
//                        if (tvCallnumber != null) {
//                            tvCallnumber.setClickable(true);
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
//                        if (tvCallnumber != null) {
//                            tvCallnumber.setClickable(true);
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
//                    if (tvCallnumber != null) {
//                        tvCallnumber.setClickable(true);
//                    }
//                    startActivity(LoginAct.class, null);
//                }
//            }
//        }.start();
//    }
//
//    private void startChatActivity(String peerId) {
//        Intent chatIntent = new Intent(mContext, ChatActivity.class);
//        chatIntent.putExtra("PeerId", peerId);
//        startActivity(chatIntent);
//    }

}
