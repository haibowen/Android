package com.mmuu.travel.client.ui.other;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.iflytek.cloud.thirdparty.B;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.MessageBeforeOrderVo;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.bike.BikeDetailVO;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.order.OrderDetailVO;
import com.mmuu.travel.client.bean.order.RoundroBinUnlockBikeVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.IMapUtils;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SystemUtils;
import com.mmuu.travel.client.ui.main.RunEndAct;
import com.mmuu.travel.client.ui.order.CurrentOrderAct;
import com.mmuu.travel.client.ui.user.CarDepositAct;
import com.mmuu.travel.client.ui.user.CertificationAct;
import com.mmuu.travel.client.ui.user.LoginAct;
import com.mmuu.travel.client.ui.user.UserCenterAct;
import com.mmuu.travel.client.widget.dialog.BackCarAbnormalDialog;
import com.mmuu.travel.client.widget.dialog.BackCarOutageDialog;
import com.mmuu.travel.client.widget.dialog.BluetoothDialog;
import com.mmuu.travel.client.widget.dialog.OneDialog;
import com.mmuu.travel.client.widget.dialog.ScanResult0Dialog;
import com.mmuu.travel.client.widget.dialog.ScanResultDialog;
import com.mmuu.travel.client.widget.dialog.TwoDialog;
import com.umeng.analytics.MobclickAgent;
import com.websocket.OnWebSocketCallback;
import com.websocket.TempWebSocketOpenCmdVo;
import com.websocket.WebSocketCallBackHelp;
import com.websocket.WebSocketCommandVO;
import com.websocket.WebSocketService;
import com.websocket.WebsocketBaseCmd;
import com.websocket.WebsocketResCmd;
import com.websocket.WebsocketSendBean;

import org.apache.http.NameValuePair;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.easelive.lockencrypt.LockService;
import cn.easelive.lockencrypt.MLog;

/**
 * Created by XIXIHAHA on 2017/2/8.
 */

public class CreateOrderFrg extends MFBaseFragment implements PublicRequestInterface, LockService.OnBleCallback, OnWebSocketCallback {

    private ImageView aminImage;
    private ProgressBar bar;
    private TextView bar1;
    private int state;

    private String code;
    private TextView barTitle;
    private ImageView iv_left_raw;

    private String bikeCode, orderId, BLEAddress;
    private BikeDetailVO currentBikeVo;
    private boolean toUnlock;


    private boolean canBack = false;
    private static final int TAG_ROUNDROBINUNLOCKBIKE = 1695701;

    int delayTiem = 1000;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> mLeDevices;
    private boolean isSuccessBlueUNlock;
    private boolean isScanning;
    private boolean isFind;
    private String bluetoothAddress;
    private int tryConnectCount = 0;
    private int addType = 0;
    private OrderDetailVO currentOrderDetail;
    TextView tv_bluetooth_tag;

    private boolean tempNoBLE = false;

    private long startTimeBLE;
    private int notifyCount = 0;

    private URI uri;
    private WebSocketClient mWebSocketClient;
    private static final String TAG = "JavaWebSocket";

    private TempWebSocketOpenCmdVo openCmdVo;//用于websocket命令中继2018-03-26


    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case 10000:
                RequestParams detailParams = new RequestParams();
                detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                detailParams.addBodyParameter("bikeCode", currentBikeVo.getBikeCode());
                //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
                //detailParams.addBodyParameter("bikeId", bikeVO.getId());
                detailParams.addBodyParameter("addType", "0");//BY_SCAN(0, "直接扫码"),BY_INSERVE(1,"预约车辆");
                detailParams.addBodyParameter("leftMileage", currentBikeVo.getAnticipatedMileage());//BY_SCAN(0, "直接扫码"),BY_INSERVE(1,"预约车辆");
                detailParams.addBodyParameter("noBLE", false ? "1" : "0");
                if (MFApp.getInstance().getmLocation() != null) {
                    detailParams.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
                    detailParams.addBodyParameter("location", MFApp.getInstance().getmLocation().getAddress());
                }
                MFRunner.requestPost(1002, MFUrl.reserveOrOrderBike, detailParams, this);

                break;
            case 1001:
                getHandler().removeMessages(1001);
                getHandler().removeMessages(1002);
                setProgress(1);
                getHandler().sendEmptyMessageDelayed(1001, delayTiem > 100 ? delayTiem : 800);
                break;
            case TAG_ROUNDROBINUNLOCKBIKE:
                getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                unlockTimer();
                getHandler().sendEmptyMessageDelayed(TAG_ROUNDROBINUNLOCKBIKE, 3000);
                break;
            case 1002:
                setProgress(1);
                getHandler().removeMessages(1001);
                getHandler().removeMessages(1002);
                getHandler().sendEmptyMessageDelayed(1002, 35);
                break;
            case 1003:
                getHandler().removeMessages(1001);
                getHandler().removeMessages(1002);
                getHandler().removeMessages(1003);
                setCreateResult(true);
//                dismiss();
                break;
            case 3001:
                //要求暂停蓝牙扫描
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    stopScan(true);
                }
                break;
            case 3002:
                //要求蓝牙连接超时操作
                getHandler().removeMessages(3002);
                if (mService != null && !mService.isStopTryConnect()) {
                    //4秒后没有回调 主动再次连接
                    tryConnectCount++;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (mService != null && !mService.isStopTryConnect()) {
                            mService.connect(bluetoothAddress);
                            getBaseHandler().sendEmptyMessageDelayed(3003, 15000);
                        } else {
                            preDoG2UnLock();
                            connectWebSocketService();
//                            G2UnLock(addType);20180326
                            bikeBleOperationLog(-3);
                            tryConnectCount = -999;
                        }
                    } else {
                        preDoG2UnLock();
                        connectWebSocketService();
//                        G2UnLock(addType);20180326
                        bikeBleOperationLog(-3);
                        tryConnectCount = -999;
                    }
                } else {
                    preDoG2UnLock();
                    connectWebSocketService();
//                    G2UnLock(addType);20180326
                    bikeBleOperationLog(-3);
                    tryConnectCount = -999;
                }
                break;
            case 3003:
                getBaseHandler().removeMessages(3003);
                if (mService != null) {
                    mService.isStopTryConnect();
                }
                if (isBLEback) {
                    return;
                }
                isBLEback = true;
                preDoG2UnLock();
                connectWebSocketService();
//                G2UnLock(addType);20180326
                bikeBleOperationLog(-3);
                tryConnectCount = -999;
                break;
            case 4001:
                //websocketservice 启动计时
                disconnectWebSocketService();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_createorder, null);
            initView();
            initHandler();
            openCmdVo = new TempWebSocketOpenCmdVo();
            if (getActivity().getIntent().getExtras() != null) {
                code = getActivity().getIntent().getExtras().getString("code");
                bikeCode = code;
                openCmdVo.setBikeCode(bikeCode);
            }

            if (getActivity().getIntent().getExtras() != null) {
                if (getActivity().getIntent().getExtras().getBoolean("toUnlock", false)) {
                    toUnlock = getActivity().getIntent().getExtras().getBoolean("toUnlock", false);
                    bikeCode = (getActivity().getIntent().getExtras().getString("bikeCode"));
                    orderId = (getActivity().getIntent().getExtras().getString("orderId"));
                    BLEAddress = (getActivity().getIntent().getExtras().getString("BLEAddress"));

                    openCmdVo.setBikeCode(bikeCode);
                    openCmdVo.setOrderId(orderId);
                    openCmdVo.setBLEAddress(BLEAddress);
                }
            }

            if (toUnlock) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragContentView.setAlpha(1);
                        setProgress(-1);
                        setUnLockDialogState(31);
                        addType = 3;
                        if (MFUtil.isSupportBluetooth(mContext)) {
                            getBikeDetail(bikeCode);
                        } else {
                            tempNoBLE = !TextUtils.isEmpty(BLEAddress);
                            openCmdVo.setNoBLE(tempNoBLE);
                            preDoG2UnLock();
                            connectWebSocketService();
//                            doUnLock(tempNoBLE);20180326
                        }
                    }
                }, 1000);

            } else {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setProgress(-1);
                        getHandler().sendEmptyMessageDelayed(1001, 500);
                        getUnFinishOrder(code);
                    }
                }, 1000);
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogShow();
                    }
                }, 200);
            }
        }
        fragContentView.setAlpha(0);
        return fragContentView;
    }

    private void initView() {

        aminImage = fragContentView.findViewById(R.id.dia_scan_iv_amin);

        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(mContext, R.anim.unlock_alpha);
        aminImage.startAnimation(alphaAnimation);


        bar = fragContentView.findViewById(R.id.dia_scan_pb);
        bar1 = fragContentView.findViewById(R.id.dia_scan_tv);

        fragContentView.findViewById(R.id.nav_layout).setBackgroundColor(Color.parseColor("#1f1d1d"));

        iv_left_raw = fragContentView.findViewById(R.id.title_left_image);
        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setImageResource(R.drawable.title_leftimgwhite_selector);
        tv_bluetooth_tag = fragContentView.findViewById(R.id.tv_bluetooth_tag);
        tv_bluetooth_tag.setVisibility(View.GONE);

        barTitle = fragContentView.findViewById(R.id.title_title);
        barTitle.setText("正在解锁");
        barTitle.setTextColor(this.getResources().getColor(R.color.white));
        iv_left_raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canBack) {
                    return;
                }
                setCreateResult(false);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBindLOckService();
        disconnectWebSocketService();
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {
        switch (i) {
            case 1000:
                dialogShow();
                break;
            case 1001:
                //dialogShow();
                break;
            case 1002:
                break;
            case 1901:
                //showUnLockDialog();
                break;
        }
    }


    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        switch (i) {
            case 1901:
                if (!isAdded()) {
                    return;
                }
                //是否有未完成的订单
                //dismmisDialog();
                RequestResultBean<OrderDetailVO> unFinish = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<OrderDetailVO>>() {
                }.getType());

                if (unFinish != null) {
                    switch (unFinish.getCode()) {
                        case 0:
                            String code = null;
                            List<NameValuePair> list = requestParams.getBodyParams();
                            for (NameValuePair nvp : list) {
                                if ("bikeCode".equals(nvp.getName())) {
                                    code = nvp.getValue();
                                    break;
                                }
                            }

                            if (unFinish.getData() == null || TextUtils.isEmpty(unFinish.getData().getOrderId())) {
                                //走下单流程
                                addType = 0;
                                checkUser(code);
                            } else {
                                if (("3".equals(unFinish.getData().getState()) || "0".equals(unFinish.getData().getState())) && "0".equals(unFinish.getData().getViewType())) {
                                    setCreateResult(false);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("OrderDetailVO", unFinish.getData());
                                    startActivity(RunEndAct.class, bundle);
                                    getActivity().finish();
                                } else if ("1".equals(unFinish.getData().getState()) || "2".equals(unFinish.getData().getState())) {
                                    //解锁动画
                                    fragContentView.setAlpha(1);
                                    setUnLockDialogState(31);

                                    //开锁
                                    addType = 1;
                                    currentOrderDetail = unFinish.getData();
                                    if (currentOrderDetail.getBikeCode().equals(code)) {
                                        if (MFUtil.isSupportBluetooth(mContext)) {
                                            getBikeDetail(code);
                                        } else {
                                            if (!TextUtils.isEmpty(currentOrderDetail.getBluetoothAddress())) {
                                                openCmdVo.setNoBLE(true);
                                                openCmdVo.setIsInBLE(0);
                                                connectWebSocketService();
//                                                unlockBikeInOrder(unFinish.getData(), code, 0, true);20180326
                                            } else {
                                                openCmdVo.setNoBLE(false);
                                                openCmdVo.setIsInBLE(0);
                                                connectWebSocketService();
//                                                unlockBikeInOrder(unFinish.getData(), code, 0);20180326
                                            }
                                        }
                                    } else {
                                        showBookFailureDialog("您有未完成的订单，结束该订单后才能租用其他小蜜蜂");
                                    }
                                } else {
                                    addType = 0;
                                    checkUser(code);
                                }
                            }
                            break;
                        default:
                            dismmisDialog();
                            showBookFailureDialog(unFinish.getMessage());
                            break;
                    }
                } else {
                    dismmisDialog();
                    showBookFailureDialog("网络数据请求错误，请重试");
                }

                break;
            case 1000:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<MessageBeforeOrderVo> check = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MessageBeforeOrderVo>>() {
                }.getType());
                if (check != null) {
                    switch (check.getCode()) {
                        case 0:
                            if (check.getData() != null) {
                                int delay = check.getData().getDelayTime();
                                if (delay > 0) {
                                    delayTiem = delay * 1000 / 100;
                                }
                            }
                            List<NameValuePair> list = requestParams.getBodyParams();
                            for (NameValuePair nvp : list) {
                                if ("bikeCode".equals(nvp.getName())) {

                                    //获取用户积分1.4.0开始
                                    RequestParams params = new RequestParams();
                                    params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                                    params.addBodyParameter("bikeCode", nvp.getValue());
                                    MFRunner.requestPost(2001, MFUrl.SCOREINFINENOTICE, params, this);
                                    //延后一部
//                                    getBikeDetail(nvp.getValue());
                                    break;
                                }
                            }
                            break;
                        case 1000:
                            showBookFailureDialog(check.getMessage());
                            //MFUtil.showToast(mContext, check.getMessage());
                            break;
                        case 1003:
                            //提示message,跳转支付押金界面
                            startActivity(CarDepositAct.class, null);
                            MFUtil.showToast(mContext, check.getMessage());
                            setCreateResult(false);
                            break;
                        case 1004:
                            //提示message,跳转支付充值界面
                            if (!isAdded()) {
                                return;
                            }
                            TwoDialog rechergeDialog = new TwoDialog(mContext, check.getMessage(), "取消", "充值");
                            rechergeDialog.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    d.dismiss();
                                    setCreateResult(false);
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    d.dismiss();
                                    setCreateResult(false);
                                    startActivity(RechargeAct.class, null);
                                    getActivity().finish();
                                }
                            });
                            rechergeDialog.show();
                            break;
                        case 1005:
                            //提示message,跳转到进行中订单
                            showBookFailureDialog(check.getMessage());
                            break;
                        case 1006:
                            //提示message,跳转新手引导页面
                            startActivity(UserCenterAct.class, null);
                            getActivity().finish();
                            MFUtil.showToast(mContext, check.getMessage());
                            setCreateResult(false);
                            break;
                        case 1007:
                            //提示message,跳转认证页面
                            Bundle bundle = new Bundle();
                            bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                            startActivity(CertificationAct.class, bundle);
                            getActivity().finish();
                            MFUtil.showToast(mContext, check.getMessage());
                            setCreateResult(false);
                            break;
                        case 1016:
                            //失踪车辆处理
                            dismmisDialog();
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);
                            setCreateResult(false, -2);
                            break;
                        default:
                            showBookFailureDialog(check.getMessage());
                            //MFUtil.showToast(mContext, check.getMessage());
                            break;
                    }
                } else {
                    showBookFailureDialog("网络数据请求错误，请重试");
                }

                break;
            case 1001:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<BikeDetailVO> detail = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<BikeDetailVO>>() {
                }.getType());
                if (detail != null && detail.getCode() == 0) {
                    BikeDetailVO bikeVO = detail.getData();
                    if (bikeVO != null) {
                        currentBikeVo = bikeVO;
                        if (currentBikeVo != null && !TextUtils.isEmpty(currentBikeVo.getBluetoothAddress())) {
                            tv_bluetooth_tag.setVisibility(View.VISIBLE);
                        } else {
                            tv_bluetooth_tag.setVisibility(View.GONE);
                        }
//                        if (MFApp.getInstance().getmLocation() != null) {
//                            if (!MFApp.getInstance().getmLocation().getAdCode().equals(bikeVO.getCityCode())) {
//                                showDifferentCityDialog();
//                                return;
//                            }
//                        }
                        if (addType == 1) {
                            //有订单（预约解锁）
                            if (MFUtil.isSupportBluetooth(mContext, bikeVO.getBluetoothAddress())) {
                                bluetoothAddress = bikeVO.getBluetoothAddress();
                                openCmdVo.setBLEAddress(bluetoothAddress);
                                bindLockService();
                            } else {
                                if (!TextUtils.isEmpty(currentOrderDetail.getBluetoothAddress())) {
                                    openCmdVo.setOrderId(currentOrderDetail.getOrderId());
                                    openCmdVo.setBikeCode(code);
                                    openCmdVo.setIsInBLE(0);
                                    openCmdVo.setNoBLE(true);
                                    connectWebSocketService();
//                                    unlockBikeInOrder(currentOrderDetail, code, 0, true);20180326
                                } else {
                                    openCmdVo.setOrderId(currentOrderDetail.getOrderId());
                                    openCmdVo.setBikeCode(code);
                                    openCmdVo.setIsInBLE(0);
                                    openCmdVo.setNoBLE(false);
                                    connectWebSocketService();
//                                    unlockBikeInOrder(currentOrderDetail, code, 0);20180326
                                }
                            }
                            return;
                        } else if (addType == 3) {
//                            addType = 1;
                            //有订单（临停解锁）
                            if (MFUtil.isSupportBluetooth(mContext, bikeVO.getBluetoothAddress())) {
                                bluetoothAddress = bikeVO.getBluetoothAddress();
                                openCmdVo.setBLEAddress(bluetoothAddress);
                                bindLockService();
                            } else {
                                if (!TextUtils.isEmpty(bikeVO.getBluetoothAddress())) {
                                    openCmdVo.setNoBLE(true);
                                    connectWebSocketService();
//                                    doUnLock(true);20180326
                                } else {
                                    tempNoBLE = !TextUtils.isEmpty(BLEAddress);
                                    openCmdVo.setNoBLE(tempNoBLE);
                                    connectWebSocketService();
//                                    doUnLock(tempNoBLE);20180326
                                }
                            }
                            return;
                        }
                        //无订单（直接扫码）

                        if (!TextUtils.isEmpty(bikeVO.getGpsX())) {
                            if (true || (TextUtils.isEmpty(bikeVO.getGpsX()) || Double.valueOf(bikeVO.getGpsX()) <= 0.5) || IMapUtils.isContains(new LatLng(Double.valueOf(bikeVO.getGpsX()), Double.valueOf(bikeVO.getGpsY())))) {
                                showBookingDialog(bikeVO);
                            } else {
                                TwoDialog twoDialog = new TwoDialog(mContext, "这辆小蜜蜂已经超出运营范围，租用后骑回运营范围内还车正常收费，否则会罚款100元，是否解锁租用？", "取消", "继续租用");
                                twoDialog.setTag(bikeVO);
                                twoDialog.setDialogClickListener(new DialogClickListener() {
                                    @Override
                                    public void onLeftClick(View v, Dialog d) {
                                        setCreateResult(false);
                                        d.dismiss();
                                    }

                                    @Override
                                    public void onRightClick(View v, Dialog d) {
                                        showBookingDialog((BikeDetailVO) ((TwoDialog) d).getTag());
                                        d.dismiss();
                                    }
                                });
                                twoDialog.show();
                            }
                        } else {
                            showBookingDialog(bikeVO);
                        }
                    } else {
                        currentBikeVo = null;
                        showBookFailureDialog("网络数据请求错误，请重试");
                    }

                } else {
                    currentBikeVo = null;
                    showBookFailureDialog("网络数据请求错误，请重试");
                }
                break;
            case 1002:
                if (!isAdded()) {
                    dismmisDialog();
                    return;
                }
                RequestResultBean booking = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (booking != null) {
                    switch (booking.getCode()) {
                        case 0:
                            if (openCmdVo != null && openCmdVo.isHasSendCmd()) {
                                return;
                            }
                            getHandler().sendEmptyMessage(TAG_ROUNDROBINUNLOCKBIKE);
                            break;
                        case 1003:
                            dismmisDialog();
                            //提示message,跳转支付押金界面
                            startActivity(CarDepositAct.class, null);
                            setCreateResult(false);
                            MFUtil.showToast(mContext, booking.getMessage());

                            break;
                        case 1004:
                            dismmisDialog();
                            //提示message,跳转支付充值界面
                            if (!isAdded()) {
                                return;
                            }
                            TwoDialog rechergeDialog = new TwoDialog(mContext, booking.getMessage(), "取消", "充值");
                            rechergeDialog.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    d.dismiss();
                                    setCreateResult(false);
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    d.dismiss();
                                    setCreateResult(false);
                                    startActivity(RechargeAct.class, null);
                                    getActivity().finish();
                                }
                            });
                            rechergeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    if (((TwoDialog) dialogInterface).isBackCancel()) {
                                        setCreateResult(false);
                                    }
                                }
                            });
                            rechergeDialog.show();
                            break;
                        case 1005:
                            dismmisDialog();
                            setCreateResult(false);
                            MFUtil.showToast(mContext, booking.getMessage());
                            //提示message,跳转到进行中订单
                            break;
                        case 1006:
                            dismmisDialog();
                            //提示message,跳转新手引导页面
                            startActivity(UserCenterAct.class, null);
                            setCreateResult(false);
                            MFUtil.showToast(mContext, booking.getMessage());
                            break;
                        case 1007:
                            dismmisDialog();
                            //提示message,跳转认证页面
                            Bundle bundle = new Bundle();
                            bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                            startActivity(CertificationAct.class, bundle);
                            setCreateResult(false);
                            MFUtil.showToast(mContext, booking.getMessage());
                            break;
                        case 1008:
                            if (!isAdded()) {
                                dismmisDialog();
                                return;
                            }
                            dismmisDialog();
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);
                            TwoDialog failureUnLock = new TwoDialog(mContext, booking.getMessage(), "更换车辆", "重试");
                            failureUnLock.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    d.dismiss();
                                    setCreateResult(false);
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    d.dismiss();
                                    setCreateResult(false, 2);
                                }
                            });
                            failureUnLock.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    if (((TwoDialog) dialogInterface).isBackCancel()) {
                                        setCreateResult(false);
                                    }
                                }
                            });
                            failureUnLock.show();
                            //showBookFailureDialog(booking.getMessage());
                            break;
                        case 10:
                            dismmisDialog();
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            String noBLE = null;
                            List<NameValuePair> list = requestParams.getBodyParams();
                            for (NameValuePair nvp : list) {
                                if ("noBLE".equals(nvp.getName())) {
                                    noBLE = nvp.getValue();
                                    break;
                                }
                            }

                            if (!TextUtils.isEmpty(noBLE) && "1".equals(noBLE)) {
                                showBluetoothDialog();
                            } else {
                                showBookFailureDialog(booking.getMessage());
                            }
                            break;
                        default:
                            dismmisDialog();
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            showBookFailureDialog(booking.getMessage());
                            //MFUtil.showToast(mContext, booking.getMessage());
                            break;
                    }
                } else {
                    dismmisDialog();
                    showBookFailureDialog("网络数据请求错误，请重试");
                }
                break;
            case 1003:
                //蓝牙锁定车辆
                if (!isAdded()) {
                    return;
                }
                RequestResultBean lockBike = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (lockBike != null && lockBike.getCode() == 1026) {
                    bindLockService();
                } else {
                    preDoG2UnLock();
                    connectWebSocketService();
//                    G2UnLock(0);20180326
                }
                break;
            case 1004:
                //蓝牙开锁回调
                if (!isAdded()) {
                    return;
                }
                RequestResultBean notify = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (notify != null && notify.getCode() == 0) {
                    MFUtil.showToast(mContext, "解锁成功");
                    updataOpenLog(3, true);
                    setUnLockDialogState(100);
                } else {
                    notifyCount++;
                    if (notifyCount < 3) {
                        String morderId = "0";
                        List<NameValuePair> list = requestParams.getBodyParams();
                        for (NameValuePair nvp : list) {
                            if ("orderId".equals(nvp.getName())) {
                                morderId = nvp.getValue();
                                break;
                            }
                        }
                        notifyUnlockBikeInBLE(morderId);
                    } else {
                        getBaseHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                preDoG2UnLock();
                                connectWebSocketService();
//                                G2UnLock(addType);20180326
                            }
                        }, 2000);
                    }
                }
                break;
            case TAG_ROUNDROBINUNLOCKBIKE:
                if (!isAdded()) {
                    getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                    return;
                }
                dismmisDialog();
                RequestResultBean<RoundroBinUnlockBikeVO> roundroBinUnlockBikeVO = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<RoundroBinUnlockBikeVO>>() {
                }.getType());
                if (roundroBinUnlockBikeVO != null && MFConstansValue.BACK_SUCCESS == roundroBinUnlockBikeVO.getCode()) {
                    if (roundroBinUnlockBikeVO.getData() == null) {
                        //失败
                        if (!isAdded()) {
                            dismmisDialog();
                            return;
                        }
                        dismmisDialog();
                        getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                        MobclickAgent.onEvent(mContext, "Unlock_failed");
                        setUnLockDialogState(-1);
                        updataOpenLog(3, false);
                        showUnlockFailureDialog();
                        return;
                    }
                    switch (roundroBinUnlockBikeVO.getData().getFlow()) {
                        case 1://预约订单中解锁失败
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            if (!isAdded()) {
                                dismmisDialog();
                                return;
                            }
                            dismmisDialog();

                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);

                            String noBLE1 = null;
                            List<NameValuePair> list = requestParams.getBodyParams();
                            for (NameValuePair nvp : list) {
                                if ("noBLE".equals(nvp.getName())) {
                                    noBLE1 = nvp.getValue();
                                    break;
                                }
                            }

                            if (!TextUtils.isEmpty(noBLE1) && "1".equals(noBLE1)) {
                                showBluetoothDialog();
                            } else {
                                TwoDialog failureUnLock = new TwoDialog(mContext, "解锁失败", "更换车辆", "重试");
//                            TwoDialog failureUnLock = new TwoDialog(mContext, roundroBinUnlockBikeVO.getMessage(), "更换车辆", "重试");
                                failureUnLock.setDialogClickListener(new DialogClickListener() {
                                    @Override
                                    public void onLeftClick(View v, Dialog d) {
                                        d.dismiss();
                                        setCreateResult(false);
                                    }

                                    @Override
                                    public void onRightClick(View v, Dialog d) {
                                        d.dismiss();
                                        setCreateResult(false, 2);
                                    }
                                });
                                failureUnLock.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        if (((TwoDialog) dialogInterface).isBackCancel()) {
                                            setCreateResult(false);
                                        }
                                    }
                                });
                                failureUnLock.show();
                                //showBookFailureDialog(booking.getMessage());
                            }
                            break;
                        case 2://开锁成功
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            MFUtil.showToast(mContext, "解锁成功");
                            updataOpenLog(3, true);
                            setUnLockDialogState(100);
                            break;
                        case 9://正在开锁
                            //不移除定时器
                            break;
                        case 10://解锁失败
                            if (!isAdded()) {
                                dismmisDialog();
                                return;
                            }
                            dismmisDialog();
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);

                            String noBLE10 = null;
                            List<NameValuePair> list10 = requestParams.getBodyParams();
                            for (NameValuePair nvp : list10) {
                                if ("noBLE".equals(nvp.getName())) {
                                    noBLE10 = nvp.getValue();
                                    break;
                                }
                            }

                            if (!TextUtils.isEmpty(noBLE10) && "1".equals(noBLE10)) {
                                showBluetoothDialog();
                            } else {
                                TwoDialog failureUnLock10 = new TwoDialog(mContext, "解锁失败", "更换车辆", "重试");
//                            TwoDialog failureUnLock10 = new TwoDialog(mContext, roundroBinUnlockBikeVO.getMessage(), "更换车辆", "重试");
                                failureUnLock10.setDialogClickListener(new DialogClickListener() {
                                    @Override
                                    public void onLeftClick(View v, Dialog d) {
                                        d.dismiss();
                                        setCreateResult(false);
                                    }

                                    @Override
                                    public void onRightClick(View v, Dialog d) {
                                        d.dismiss();
                                        setCreateResult(false, 2);
                                    }
                                });
                                failureUnLock10.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        if (((TwoDialog) dialogInterface).isBackCancel()) {
                                            setCreateResult(false);
                                        }
                                    }
                                });
                                failureUnLock10.show();
                                //showBookFailureDialog(booking.getMessage());
                            }
                            break;

                        //临时停车解锁
                        case 14:
                            //成功
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            MFUtil.showToast(mContext, "解锁成功");
                            updataOpenLog(3, true);
                            setUnLockDialogState(100);
                            break;
                        case 15:
                            //开锁中
                            break;
                        case 16:
                            //失败
                            if (!isAdded()) {
                                dismmisDialog();
                                return;
                            }
                            dismmisDialog();
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);

                            String noBLE16 = null;
                            List<NameValuePair> list16 = requestParams.getBodyParams();
                            for (NameValuePair nvp : list16) {
                                if ("noBLE".equals(nvp.getName())) {
                                    noBLE16 = nvp.getValue();
                                    break;
                                }
                            }

                            if (!TextUtils.isEmpty(noBLE16) && "1".equals(noBLE16)) {
                                showBluetoothDialog();
                            } else {
                                showUnlockFailureDialog();
                            }
                            break;
                        case 24:
                            //车辆离线
                            if (!isAdded()) {
                                dismmisDialog();
                                return;
                            }
                            dismmisDialog();
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);

                            String noBLE24 = null;
                            List<NameValuePair> list24 = requestParams.getBodyParams();
                            for (NameValuePair nvp : list24) {
                                if ("noBLE".equals(nvp.getName())) {
                                    noBLE24 = nvp.getValue();
                                    break;
                                }
                            }

                            if (!TextUtils.isEmpty(noBLE24) && "1".equals(noBLE24)) {
                                showBluetoothDialog();
                            } else {
                                MFUtil.showToast(mContext, "车辆暂不可用，请更换其它小蜜蜂");
                                showUnlockFailureDialog();
                            }
                            break;
                        case 25:
                            //车辆断电
                            if (!isAdded()) {
                                dismmisDialog();
                                return;
                            }
                            dismmisDialog();
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);
//                            MFUtil.showToast(mContext, "车辆断电，请联系客服");
                            BackCarOutageDialog outageDialog = new BackCarOutageDialog(mContext);
                            outageDialog.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    d.cancel();
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    d.cancel();
                                    callCustomerServices();
                                }
                            });
                            outageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    setCreateResult(false);
                                }
                            });
                            outageDialog.show();
                            break;
                        case 26:
                            //临停解锁离线
                            if (!isAdded()) {
                                dismmisDialog();
                                return;
                            }
                            dismmisDialog();
                            getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                            MobclickAgent.onEvent(mContext, "Unlock_failed");
                            setUnLockDialogState(-1);
                            updataOpenLog(3, false);
                            String noBLE26 = null;
                            List<NameValuePair> list26 = requestParams.getBodyParams();
                            for (NameValuePair nvp : list26) {
                                if ("noBLE".equals(nvp.getName())) {
                                    noBLE26 = nvp.getValue();
                                    break;
                                }
                            }

                            if (!TextUtils.isEmpty(noBLE26) && "1".equals(noBLE26)) {
                                showBluetoothDialog();
                            } else {
                                MFUtil.showToast(mContext, "解锁失败，请联系客服");
                                BackCarAbnormalDialog abnormalDialog = new BackCarAbnormalDialog(mContext);
                                abnormalDialog.setDialogClickListener(new DialogClickListener() {
                                    @Override
                                    public void onLeftClick(View v, Dialog d) {
                                        d.cancel();
                                    }

                                    @Override
                                    public void onRightClick(View v, Dialog d) {
                                        callCustomerServices();
                                    }
                                });
                                abnormalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        setCreateResult(false);
                                    }
                                });
                                abnormalDialog.show();
                            }
                            break;
                    }
                } else if (roundroBinUnlockBikeVO != null && MFConstansValue.BACK_LOGOUT == roundroBinUnlockBikeVO.getCode()) {
                    getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                    MFUtil.showToast(mContext, roundroBinUnlockBikeVO.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (roundroBinUnlockBikeVO != null && (MFConstansValue.BACK_SYSTEMERROR == roundroBinUnlockBikeVO.getCode() || MFConstansValue.BACK_BUSINESS == roundroBinUnlockBikeVO.getCode())) {
                    getHandler().removeMessages(TAG_ROUNDROBINUNLOCKBIKE);
                    MFUtil.showToast(mContext, roundroBinUnlockBikeVO.getMessage());
                }
                break;
            case 2001:

                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                String personString = responseInfo.result.toString();
                RequestResultBean<String> personResultBean = GsonTransformUtil.getObjectFromJson(personString, new TypeToken<RequestResultBean<PersonalCenterVo>>() {
                }.getType());

                if (personResultBean != null) {
                    switch (personResultBean.getCode()) {
                        case 0:
                            List<NameValuePair> list = requestParams.getBodyParams();
                            for (NameValuePair nvp : list) {
                                if ("bikeCode".equals(nvp.getName())) {
                                    if (TextUtils.isEmpty(personResultBean.getMessage())) {
                                        getBikeDetail(nvp.getValue());
                                    } else {
                                        dismmisDialog();
                                        TwoDialog nextDia = new TwoDialog(mContext, personResultBean.getMessage(), "取消", "继续租车");
                                        nextDia.setTag(nvp.getValue());
                                        nextDia.setDialogClickListener(new DialogClickListener() {
                                            @Override
                                            public void onLeftClick(View v, Dialog d) {

                                                d.dismiss();
                                                setCreateResult(false);
                                            }

                                            @Override
                                            public void onRightClick(View v, Dialog d) {
                                                d.dismiss();
                                                getBikeDetail((String) ((TwoDialog) d).getTag());
                                            }
                                        });
                                        nextDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                if (((TwoDialog) dialogInterface).isBackCancel()) {
                                                    setCreateResult(false);
                                                }
                                            }
                                        });
                                        nextDia.show();
                                    }
                                    break;
                                }
                            }
                            break;
                        default:
                            dismmisDialog();
                            showBookFailureDialog(personResultBean.getMessage());
                            break;
                    }

                } else {
                    dismmisDialog();
                    showBookFailureDialog("网络数据请求错误，请重试");
                }
                break;
            case 2010:
                if (!isAdded()) {
                    dismmisDialog();
                    return;
                }
                RequestResultBean unlockBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());


                if (unlockBean != null) {
                    switch (unlockBean.getCode()) {
                        case 0:
                            if (openCmdVo != null && openCmdVo.isHasSendCmd()) {

                            } else {
                                getHandler().sendEmptyMessage(TAG_ROUNDROBINUNLOCKBIKE);
                            }
                            setUnLockDialogState(31);
                            break;
                        case 1018:
                            //原本就未锁车
                            setUnLockDialogState(100);
                            updataOpenLog(3, true);
                            MFUtil.showToast(mContext, unlockBean.getMessage());
                            break;
                        default:
                            showUnlockFailureDialog();
                            break;
                    }
                } else {
                    showUnlockFailureDialog();
                }
                break;
        }
    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        switch (i) {
            case 1901:
            case 1001:
                showBookFailureDialog("网络数据请求错误，请重试");
                dismmisDialog();
                break;
            case 1002:
                showBookFailureDialog("网络数据请求错误，请重试");
                dismmisDialog();
                break;
            case 1003:
                MFUtil.showToast(mContext, "获取1003失败");
                preDoG2UnLock();
                connectWebSocketService();
//                G2UnLock(0);20180326
                break;
            case 1004:
                notifyCount++;
                if (notifyCount < 3) {
                    List<NameValuePair> list = requestParams.getBodyParams();
                    for (NameValuePair nvp : list) {
                        if ("orderId".equals(nvp.getName())) {
                            final String morderId = nvp.getValue();
                            getBaseHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    notifyUnlockBikeInBLE(morderId);
                                }
                            }, 1000);
                            break;
                        }
                    }
                } else {
                    preDoG2UnLock();
                    connectWebSocketService();
//                    G2UnLock(addType);20180326
                }
                break;
            case 2010:
                showUnlockFailureDialog();
                break;
            default:
                showBookFailureDialog("网络数据请求错误，请重试");
                dismmisDialog();
                break;
        }
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    private void getUnFinishOrder(String mbikeCode) {
        if (!isAdded()) {
            return;
        }
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            startActivity(LoginAct.class, null);
            setCreateResult(false);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("bikeCode", mbikeCode);
            //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
            MFRunner.requestPost(1901, MFUrl.getUnFinishOrder, detailParams, this);
        }
    }

    public void initSockect(final String bikeCode) {
        try {
            uri = new URI(MFUrl.WEBSOKETADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (null == mWebSocketClient) {
            mWebSocketClient = new WebSocketClient(uri, new Draft_6455(), null, 60) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    String text = GsonTransformUtil.toJson(new WebsocketSendBean("0", bikeCode, MFStaticConstans.getUserBean().getUser().getId(), SystemUtils.getVersionName(mContext)));
                    mWebSocketClient.send(text);
                    Log.i(TAG, "onOpen: " + text);
                }

                @Override
                public void onMessage(String s) {
                    if ("\"链接成功\"".equals(s)) {
                        getHandler().sendEmptyMessage(10000);
                    }

                    Log.i(TAG, "onMessage: " + s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
//                    getHandler().sendEmptyMessage(10000);
                    Log.i(TAG, "onClose: " + s);
                }

                @Override
                public void onError(Exception e) {
//                    getHandler().sendEmptyMessage(10000);
//                    MFUtil.showToast(mContext, "错误发生");
                    Log.i(TAG, "onError: " + e);
                }
            };
            mWebSocketClient.connect();
        }
    }


    private void checkUser(String bikeCode) {
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            startActivity(LoginAct.class, null);
            setCreateResult(false);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("bikeCode", bikeCode);
            // detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
            MFRunner.requestPost(1000, MFUrl.getMessageBeforeOrder, detailParams, this);
        }
    }

    private void getBikeDetail(String bikeCode) {
        if (TextUtils.isEmpty(bikeCode)) {
            setCreateResult(false);
            return;
        }
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            startActivity(LoginAct.class, null);
            setCreateResult(false);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("bikeCode", bikeCode);
            //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
            MFRunner.requestPost(1001, MFUrl.getBikeDetail, detailParams, this);
        }
    }

    /**
     * 下单，创建订单
     *
     * @param bikeVO
     */
    private void reserveOrOrderBike(BikeDetailVO bikeVO) {
        reserveOrOrderBike(bikeVO, false);
    }

    private void reserveOrOrderBike(BikeDetailVO bikeVO, boolean noBLE) {
        this.tempNoBLE = noBLE;
        if (bikeVO == null) {
            setCreateResult(false);
            return;
        }
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            startActivity(LoginAct.class, null);
            setCreateResult(false);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("bikeCode", bikeVO.getBikeCode());
            //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
            //detailParams.addBodyParameter("bikeId", bikeVO.getId());
            detailParams.addBodyParameter("addType", "0");//BY_SCAN(0, "直接扫码"),BY_INSERVE(1,"预约车辆");
            detailParams.addBodyParameter("leftMileage", bikeVO.getAnticipatedMileage());//BY_SCAN(0, "直接扫码"),BY_INSERVE(1,"预约车辆");
            detailParams.addBodyParameter("noBLE", noBLE ? "1" : "0");
            if (MFApp.getInstance().getmLocation() != null) {
                detailParams.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
                detailParams.addBodyParameter("location", MFApp.getInstance().getmLocation().getAddress());
            }
            MFRunner.requestPost(1002, MFUrl.reserveOrOrderBike, detailParams, this);
        }
    }

    /**
     * \
     *
     * @param mOrderId
     * @param bikeCode
     * @param isInBLE  是否是蓝牙 0:不是,1:是
     */
    private void unlockBikeInOrder(String mOrderId, String bikeCode, int isInBLE, boolean noBLE) {
        this.tempNoBLE = noBLE;
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        detailParams.addBodyParameter("bikeCode", bikeCode);
        detailParams.addBodyParameter("isInBLE", isInBLE + "");
        //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
        detailParams.addBodyParameter("orderId", mOrderId + "");
        detailParams.addBodyParameter("noBLE", noBLE ? "1" : "0");
        if (MFApp.getInstance().getmLocation() != null) {
            detailParams.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
            detailParams.addBodyParameter("location", MFApp.getInstance().getmLocation().getAddress());
        }
        MFRunner.requestPost(1002, MFUrl.unlockBikeInOrder, detailParams, this);
    }

    /**
     * \
     *
     * @param orderId
     * @param bikeCode
     * @param isInBLE  是否是蓝牙 0:不是,1:是
     */
    private void unlockBikeInOrder(String orderId, String bikeCode, int isInBLE) {
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        detailParams.addBodyParameter("isInBLE", isInBLE + "");
        detailParams.addBodyParameter("bikeCode", bikeCode);
        //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
        detailParams.addBodyParameter("orderId", orderId);
        if (MFApp.getInstance().getmLocation() != null) {
            detailParams.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
            detailParams.addBodyParameter("location", MFApp.getInstance().getmLocation().getAddress());
        }
        MFRunner.requestPost(1002, MFUrl.unlockBikeInOrder, detailParams, this);
    }


    /**
     * 蓝牙解锁前锁定车辆
     *
     * @param mbikeCode
     */
    private void lockBikeinBLE(String mbikeCode) {
        fragContentView.setAlpha(1);
        setUnLockDialogState(31);

        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        detailParams.addBodyParameter("bikeCode", mbikeCode);
        MFRunner.requestPost(1003, MFUrl.LOCKBIKEINBLE, detailParams, this);
    }


    /**
     * 2.0.0蓝牙解锁结果通知(直接扫码蓝牙解锁)
     */
    private void notifyUnlockBikeInBLE(String mOrderId) {
        RequestParams notifyParams = new RequestParams();
        notifyParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        notifyParams.addBodyParameter("bikeCode", currentBikeVo.getBikeCode());
        notifyParams.addBodyParameter("orderId", mOrderId);
        notifyParams.addBodyParameter("addType", addType + "");
        notifyParams.addBodyParameter("state", isSuccessBlueUNlock ? "1" : "0");
        if (MFApp.getInstance().getmLocation() != null) {
            notifyParams.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
            notifyParams.addBodyParameter("location", MFApp.getInstance().getmLocation().getAddress());
        }
        MFRunner.requestPost(1004, MFUrl.NOTIFYUNLOCKBIKEINBLE, notifyParams, this);
    }


    /**
     * 定时器轮询调起真实解锁状态
     */
    private void unlockTimer() {
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        detailParams.addBodyParameter("noBLE", tempNoBLE ? "1" : "0");
        MFRunner.requestPost(TAG_ROUNDROBINUNLOCKBIKE, MFUrl.roundRobinUnlockBike, detailParams, this);
    }

    private void showBookingDialog(final BikeDetailVO bikeVO) {

        if (!isAdded()) {
            return;
        }

        setUnLockDialogState(30);

        if (!TextUtils.isEmpty(bikeVO.getAnticipatedMileage())) {

            double d = Double.valueOf(bikeVO.getAnticipatedMileage());
            int s = ((int) (d / 1000));

            if (s == 0) {
                ScanResult0Dialog result0Dialog = new ScanResult0Dialog(mContext, "此小蜜蜂电量不足，请更换其他小蜜蜂", bikeVO.getAnticipatedMileage(), "确定");
                result0Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (((ScanResult0Dialog) dialogInterface).getTag() == null) {
                            setCreateResult(false);
                        }
                    }
                });
                result0Dialog.setDialogClickListener(new DialogClickListener() {
                    @Override
                    public void onLeftClick(View v, Dialog d) {
                        ((ScanResult0Dialog) d).setTag(new Object());
                        d.dismiss();
                        setCreateResult(false);
                    }

                    @Override
                    public void onRightClick(View v, Dialog d) {
                        ((ScanResult0Dialog) d).setTag(new Object());
                        d.dismiss();
                        bluetoothAddress = bikeVO.getBluetoothAddress();
                        if (MFUtil.isSupportBluetooth(mContext, bikeVO.getBluetoothAddress())) {
                            lockBikeinBLE(bikeVO.getBikeCode());
//                            bindLockService();
                        } else {
                            if (!TextUtils.isEmpty(bikeVO.getBluetoothAddress())) {
                                openCmdVo.setNoBLE(true);
                                addType = 0;
                                preDoG2UnLock();
                                connectWebSocketService();
//                                G2UnLock(0, true);20180326
                            } else {
                                openCmdVo.setNoBLE(false);
                                addType = 0;
                                preDoG2UnLock();
                                connectWebSocketService();
//                                G2UnLock(0);20180326
                            }
//                            fragContentView.setAlpha(1);
//                            setUnLockDialogState(31);
//                            reserveOrOrderBike(bikeVO);
                        }
                    }
                });
                result0Dialog.show();
                return;
            }
        }


        ScanResultDialog resultDialog = new ScanResultDialog(mContext, "预计剩余续航里程", bikeVO.getAnticipatedMileage(), "取消", "解锁");
        resultDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dialogInterface == null || ((ScanResultDialog) dialogInterface).getTag() == null) {
                    setCreateResult(false);
                }
            }
        });
        resultDialog.setDialogClickListener(new DialogClickListener() {
            @Override
            public void onLeftClick(View v, Dialog d) {
                ((ScanResultDialog) d).setTag(new Object());
                d.dismiss();
                setCreateResult(false);
            }

            @Override
            public void onRightClick(View v, Dialog d) {
                ((ScanResultDialog) d).setTag(new Object());
                d.dismiss();
                bluetoothAddress = bikeVO.getBluetoothAddress();
                if (MFUtil.isSupportBluetooth(mContext, bikeVO.getBluetoothAddress())) {
                    lockBikeinBLE(bikeVO.getBikeCode());
//                    bindLockService();
                } else {
                    if (!TextUtils.isEmpty(bikeVO.getBluetoothAddress())) {
                        openCmdVo.setNoBLE(true);
                        preDoG2UnLock();
                        connectWebSocketService();
//                        G2UnLock(0, true);2018326
                    } else {
                        openCmdVo.setNoBLE(false);
                        preDoG2UnLock();
                        connectWebSocketService();
//                        G2UnLock(0);20180326
                    }
//                    fragContentView.setAlpha(1);
//                    setUnLockDialogState(31);
//                    reserveOrOrderBike(bikeVO);
                }
            }
        });
        resultDialog.show();
    }

    private void G2UnLock(int unLockType) {
        G2UnLock(unLockType, false);
    }

    /**
     * @param unLockType
     * @param noBLE      true--》有蓝牙
     */
    private void G2UnLock(int unLockType, boolean noBLE) {
        preDoG2UnLock();
        switch (unLockType) {
            case 0:
                reserveOrOrderBike(currentBikeVo, noBLE);
                break;
            case 1:
                unlockBikeInOrder(currentOrderDetail.getOrderId(), currentOrderDetail.getBikeCode(), 0);
                break;
            case 3:
                doUnLock();
                break;
        }
    }

    /**
     * 关闭LockService
     */
    private void preDoG2UnLock() {
        unBindLOckService();
        fragContentView.setAlpha(1);
        setUnLockDialogState(31);
    }


    private void showBookFailureDialog(String mes) {
        if (!isAdded()) {
            return;
        }
        setUnLockDialogState(-1);
        updataOpenLog(3, false);
        OneDialog errDialog = new OneDialog(mContext, mes, "我知道了");
        errDialog.setCanceledOnTouchOutside(false);
        errDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setCreateResult(false);
//                restartCamera();
//                if (unLockDialog != null && unLockDialog.isShowing()) {
//                    unLockDialog.cancel();
//                }
            }
        });
        errDialog.setDialogClickListener(new DialogClickListener() {
            @Override
            public void onLeftClick(View v, Dialog d) {

            }

            @Override
            public void onRightClick(View v, Dialog d) {
                d.dismiss();
            }
        });
        errDialog.show();
    }


    private void showUnlockFailureDialog() {
        TwoDialog unlockFaild = new TwoDialog(mContext, "解锁失败，请重试", "取消", "重试");
        unlockFaild.setDialogClickListener(new DialogClickListener() {
            @Override
            public void onLeftClick(View v, Dialog d) {
                d.dismiss();
                setCreateResult(false);
            }

            @Override
            public void onRightClick(View v, Dialog d) {
                d.dismiss();
                setCreateResult(false, 2);
            }
        });
        unlockFaild.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (((TwoDialog) dialogInterface).isBackCancel()) {
                    setCreateResult(false);
                }
            }
        });
        unlockFaild.show();
    }


    private void showDifferentCityDialog() {
        if (!isAdded()) {
            return;
        }
        setUnLockDialogState(-1);
        updataOpenLog(3, false);
        OneDialog errDialog = new OneDialog(mContext, "您当前解锁的小蜜蜂不是当前城市车辆，请检查小蜜蜂编号，或更换其它小蜜蜂。", "好的");
        errDialog.setCanceledOnTouchOutside(false);
        errDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setCreateResult(false, 2);
            }
        });
        errDialog.setDialogClickListener(new DialogClickListener() {
            @Override
            public void onLeftClick(View v, Dialog d) {

            }

            @Override
            public void onRightClick(View v, Dialog d) {
                d.dismiss();
                setCreateResult(false, 2);
            }
        });
        errDialog.show();
    }


    private void doUnLock() {
        doUnLock(false);
    }

    private void doUnLock(boolean noBLE) {
        tempNoBLE = noBLE;
        RequestParams unlockParams = new RequestParams();
        unlockParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        unlockParams.addBodyParameter("orderId", orderId);
        unlockParams.addBodyParameter("bikeCode", bikeCode);
        unlockParams.addBodyParameter("noBLE", noBLE ? "1" : "0");
        MFRunner.requestPost(2010, MFUrl.UNLOCKBIKEINORDER, unlockParams, this);
    }


    private void showScanFailureDialog() {
        setUnLockDialogState(-1);
        OneDialog errDialog = new OneDialog(mContext, "扫码失败", "我知道了");
        errDialog.setCanceledOnTouchOutside(false);
        errDialog.setDialogClickListener(new DialogClickListener() {
            @Override
            public void onLeftClick(View v, Dialog d) {

            }

            @Override
            public void onRightClick(View v, Dialog d) {
                d.dismiss();
//                restartCamera();
                setCreateResult(false);
            }
        });
        errDialog.show();
    }

    private void setUnLockDialogState(int state) {
        setState(state);
    }

    private void setProgress(int pro) {

        if (bar != null && pro == -1) {
            bar.setProgress(1);
            bar1.setText(1 + "%");
        } else if (bar != null) {
            if (bar.getProgress() >= 100) {
                getHandler().removeMessages(1001);
                getHandler().removeMessages(1002);
                getHandler().sendEmptyMessageDelayed(1003, 500);
            }

            bar.setProgress(bar.getProgress() + 1);
            bar1.setText(bar.getProgress() + "%");
//            if (bar.getProgress() > 5) {
//                bar.setProgress(100);
//                bar1.setText(100 + "%");
//            }
        }
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case 100:
                canBack = true;
                getHandler().removeMessages(1001);
                getHandler().removeMessages(1002);
                getHandler().sendEmptyMessageDelayed(1003, 500);//不走1002，直接跳转
                break;
            case -1:
                canBack = true;
                getHandler().removeMessages(1001);
                getHandler().removeMessages(1002);
                break;
            case 30:
                getHandler().removeMessages(1001);
                getHandler().removeMessages(1002);
                break;
            case 31:
                getHandler().sendEmptyMessageDelayed(1001, 1000);
                break;
        }
    }


    private void setCreateResult(boolean suc) {
        setCreateResult(suc, 0);
    }

    private void setCreateResult(boolean suc, int nextStop) {
        canBack = true;
        if (!isAdded()) {
            return;
        }
        getHandler().removeCallbacksAndMessages(null);
        if (suc) {
            startActivity(CurrentOrderAct.class, null, true);
            if (!toUnlock) {
                mContext.sendBroadcast(new Intent("DestroyActReceiver"));
            }
        }

        if (!suc) {
            MFStaticConstans.needRefOrder2Sce = true;
        }

        Intent result = new Intent();
        result.putExtra("CreateResult", suc);
        result.putExtra("nextStop", nextStop);
        result.putExtra("bikeCode", code);
        getActivity().setResult(Activity.RESULT_OK, result);
        getActivity().finish();
    }


    public boolean isCanBack() {
        return canBack;
    }

    public void setCanBack(boolean canBack) {
        this.canBack = canBack;
    }

    private LockService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            tryConnectCount = 0;
            mService = ((LockService.LocalBinder) rawBinder).getService();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (!mService.initialize(CreateOrderFrg.this, "0000")) {
                    MFUtil.showToast(mContext, "0000");
                    bikeBleOperationLog(-4);
                    connectWebSocketService();
                    preDoG2UnLock();
//                    G2UnLock(addType);20180326
                }
            }
            operationBluetooth();
        }


        @Override

        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
            connectWebSocketService();
            preDoG2UnLock();
//            G2UnLock(addType);20180326
        }

    };

    private boolean isBLEback = false;

    @Override
    public void onConnected() {
        getHandler().removeMessages(3002);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mService.mTxCharState("onConnected");
            tryConnectCount = -999;
            if (mService != null) {
                if (mService.openCmd()) {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isBLEback) {
                                isBLEback = true;
                                preDoG2UnLock();
                                connectWebSocketService();
//                                G2UnLock(addType);20180326
                            }
                        }
                    }, 10000);
                } else {
                    preDoG2UnLock();
                    connectWebSocketService();
//                    G2UnLock(addType);20180326
                }
            } else {
                preDoG2UnLock();
                connectWebSocketService();
//                G2UnLock(addType);20180326
            }
        }
    }

    @Override
    public void onDisconnected() {
        getHandler().removeMessages(3002);
        tryConnectCount++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (tryConnectCount >= 3) {
                getHandler().removeMessages(3003);
                if (isBLEback) {
                    return;
                }
                preDoG2UnLock();
                connectWebSocketService();
//                G2UnLock(addType);20180326
                bikeBleOperationLog(-3);
                tryConnectCount = -999;
                unBindLOckService();
            }
        }
    }

    @Override
    public void onDeviceReady(LockService.BleInfo bleInfo) {
//        MFUtil.showToast(mContext, "onDeviceReady");
    }

    @Override
    public void onOpenLock(int i, Date date) {
        getBaseHandler().removeMessages(3003);
        if (isBLEback) {
            return;
        }
        isBLEback = true;
        bikeBleOperationLog(i);
        switch (i) {
            case 2:
            case 6:
            case 0:
                //
                isSuccessBlueUNlock = true;
                if (addType == 0) {
                    notifyUnlockBikeInBLE("0");
                } else if (addType == 1) {
                    if (currentOrderDetail != null && "1".equals(currentOrderDetail.getState())) {
                        notifyUnlockBikeInBLE(currentOrderDetail.getOrderId());
                    } else {
                        openCmdVo.setOrderId(currentOrderDetail.getOrderId());
                        openCmdVo.setBikeCode(currentOrderDetail.getBikeCode());
                        openCmdVo.setIsInBLE(1);
                        connectWebSocketService();
//                        unlockBikeInOrder(currentOrderDetail.getOrderId(), currentOrderDetail.getBikeCode(), 1);20180326
                    }
                } else if (addType == 3) {
                    openCmdVo.setOrderId(orderId);
                    openCmdVo.setBikeCode(bikeCode);
                    openCmdVo.setIsInBLE(1);
                    connectWebSocketService();
//                    unlockBikeInOrder(orderId, bikeCode, 1);20180326
                } else {
                    isSuccessBlueUNlock = false;
                    preDoG2UnLock();
                    connectWebSocketService();
//                    G2UnLock(addType);20180326
                }
                updataOpenLog(1, true);
                break;
            default:
                updataOpenLog(1, false);
                isSuccessBlueUNlock = false;
                preDoG2UnLock();
                connectWebSocketService();
//                G2UnLock(addType);20180326
                break;
        }
        unBindLOckService();
    }

    @Override
    public void onCloseLock(int i, long l, Date date) {
//        MFUtil.showToast(mContext, "onCloseLock");
    }

    private void bindLockService() {
        tryConnectCount = 0;
        notifyCount = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (mService == null) {
//            dialogShow();
                startTimeBLE = System.currentTimeMillis();
                Intent bindIntent = new Intent(mContext, LockService.class);
                mContext.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            } else {
                updataOpenLog(1, false);
                bikeBleOperationLog(-5);
                preDoG2UnLock();
                connectWebSocketService();
//                G2UnLock(addType);20180326
            }
        } else {
            updataOpenLog(1, false);
            bikeBleOperationLog(-6);
            preDoG2UnLock();
            connectWebSocketService();
//            G2UnLock(addType);20180326
        }
    }

    private void unBindLOckService() {
        if (mService != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mService.disconnectAndClose();
            }
            mContext.unbindService(mServiceConnection);
            mService.stopSelf();
            mService = null;
        }
    }

    /**
     * 操作蓝牙
     *
     * @param
     */
    private void operationBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            mLeDevices = new ArrayList<>();
        }
        if (mBluetoothAdapter == null) {
            preDoG2UnLock();
            connectWebSocketService();
//            G2UnLock(addType);20180326
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            preDoG2UnLock();
            connectWebSocketService();
//            G2UnLock(addType);20180326
        } else {
            startScan();
        }

    }

    private void stopScan() {
        stopScan(false);
    }

    private void stopScan(boolean disconnect) {
        if (isScanning) {
            isScanning = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
        if (disconnect && mService != null) {//20180118zhangyu
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mService.closeProxy();
                mService.setStopTryConnect(true);
                mService.disconnect();
            }
        }
        if (!isFind) {
            bikeBleOperationLog(-2);
            preDoG2UnLock();
            connectWebSocketService();
//            G2UnLock(addType);20180326
        }
    }

    private void startScan() {
        isFind = false;
        isBLEback = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mService.mTxCharState("startScan");
            mService.setStopTryConnect(false);
            if (!isScanning) {
                isScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
                getBaseHandler().sendEmptyMessageDelayed(3001, 5000);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {


        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                MLog.i("LockService", "name: " + device.getName() + "\tmac: " + device.getAddress());
            }

            if (device != null) {
                if (device.getAddress().equalsIgnoreCase(bluetoothAddress) && !isFind) {
                    isFind = true;
                    getBaseHandler().removeMessages(3001);
                    stopScan();
                    if (mService != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            mService.mTxCharState("onLeScan");
                        }
                        getBaseHandler().sendEmptyMessageDelayed(3002, 200);
                    } else {
                        MFUtil.showToast(mContext, "扫描地址成功，但mservice为null");
                        connectWebSocketService();
                        preDoG2UnLock();
//                        G2UnLock(addType);20180326
                    }
                }
            }
        }
    };

    private BluetoothDialog bluetoothDialog;

    private void showBluetoothDialog() {
        if (!isAdded()) {
            return;
        }
        if (bluetoothDialog == null) {
            bluetoothDialog = new BluetoothDialog(mContext, "解锁失败", "无法解锁？可尝试开启手机蓝牙后再解锁", "开启手机蓝牙");
            bluetoothDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setCreateResult(false);
                }
            });
            bluetoothDialog.setDialogClickListener(new DialogClickListener() {
                @Override
                public void onLeftClick(View v, Dialog d) {

                }

                @Override
                public void onRightClick(View v, Dialog d) {
                    d.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        bluetoothDialog.show();
    }


    private void bikeBleOperationLog(int resCode) {
        RequestParams unlockParams = new RequestParams();
        unlockParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        if (TextUtils.isEmpty(orderId)) {
            unlockParams.addBodyParameter("orderId", "0");
        } else {
            unlockParams.addBodyParameter("orderId", orderId);
        }
        unlockParams.addBodyParameter("bikeCode", bikeCode);
        if (currentBikeVo != null) {
            unlockParams.addBodyParameter("cityCode", currentBikeVo.getCityCode());
        }
        unlockParams.addBodyParameter("opType", "unlock");
        unlockParams.addBodyParameter("resCode", resCode + "");
        unlockParams.addBodyParameter("clientType", "0");
        MFRunner.requestPost(-111, MFUrl.BIKEBLEOPERATIONLOG, unlockParams, this);
    }


    @Override
    public void onWebsocketOpen() {
        Intent s = new Intent(mContext, WebSocketService.class);
        WebSocketCommandVO socketCommandVO = new WebSocketCommandVO();
        socketCommandVO.setCommand(0);
        socketCommandVO.setBikeCode(bikeCode);
        socketCommandVO.setUserId(MFStaticConstans.getUserBean().getUser().getId());
        socketCommandVO.setVersionCode(MFApp.versionName);
        s.putExtra("WebSocketCommandVO", socketCommandVO);
        mContext.startService(s);
    }

    @Override
    public void onWebsocketMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            WebsocketBaseCmd temCmd = (WebsocketBaseCmd) GsonTransformUtil.fromJson2(message, WebsocketBaseCmd.class);
            if (temCmd != null && temCmd.getModule() == 1) {
                switch (temCmd.getCode()) {
                    case 0:
                        openCmdVo.setHasSendCmd(true);
                        switch (addType) {
                            case 0:
                                reserveOrOrderBike(currentBikeVo, openCmdVo.isNoBLE());
                                break;
                            case 1:
                                unlockBikeInOrder(currentOrderDetail.getOrderId(), openCmdVo.getBikeCode(), openCmdVo.getIsInBLE());
                                break;
                            case 3:
                                doUnLock();
                        }
                        break;
                    default:

                        break;
                }

            } else if (temCmd != null && temCmd.getModule() == 2) {
                WebsocketBaseCmd<WebsocketResCmd> temCmd2 = GsonTransformUtil.fromJson2(message, new TypeToken<WebsocketBaseCmd<WebsocketResCmd>>() {
                }.getType());

                switch (temCmd.getCode()) {
                    case 0:
                        WebsocketResCmd tempcmd3 = temCmd2.getData();
                        if (tempcmd3 != null) {
                            openCmdVo.setHasReceiveCmd(true);
                            getBaseHandler().removeMessages(4001);
                            MFUtil.showToast(mContext, tempcmd3.getMessage());
                            Intent s = new Intent(mContext, WebSocketService.class);
                            WebSocketCommandVO socketCommandVO = new WebSocketCommandVO();
                            socketCommandVO.setCommand(-2);
                            socketCommandVO.setBikeCode(bikeCode);
                            socketCommandVO.setUserId(MFStaticConstans.getUserBean().getUser().getId());
                            socketCommandVO.setVersionCode(MFApp.versionName);
                            s.putExtra("WebSocketCommandVO", socketCommandVO);
//                            mContext.startService(s);
                            mContext.stopService(s);
                            updataOpenLog(2, true);
                            getBaseHandler().sendEmptyMessage(1003);
                        }
                        break;
                    default:
                        break;
                }
            } else {
            }
        }
    }

    @Override
    public void onWebsocketClose(String s) {
        if (openCmdVo != null && !openCmdVo.isHasReceiveCmd()) {
            openCmdVo.setHasReceiveCmd(true);
            openCmdVo.setHasSendCmd(false);
            switch (addType) {
                case 0:
                    reserveOrOrderBike(currentBikeVo, openCmdVo.isNoBLE());
                    break;
                case 1:
                    unlockBikeInOrder(currentOrderDetail.getOrderId(), openCmdVo.getBikeCode(), openCmdVo.getIsInBLE());
                    break;
                case 3:
                    doUnLock();
            }
        }
    }

    @Override
    public void onWebsocketError(Exception e) {

    }

    private void updataOpenLog(int type, boolean isSuccess) {
        RequestParams unlockParams = new RequestParams();
        switch (type) {
            case 1:
                unlockParams.addBodyParameter("opType", "蓝牙开锁");
                break;
            case 2:
                unlockParams.addBodyParameter("opType", "websocket开锁");
                break;
            case 3:
                unlockParams.addBodyParameter("opType", "2G开锁");
                break;
            case 4:
                unlockParams.addBodyParameter("opType", "websocket 启动时长");
                break;
        }
//        unlockParams.addBodyParameter("openLockTime", openLockTime + "");
        unlockParams.addBodyParameter("isSuccess", isSuccess ? "开锁成功" : "开始失败");
        MFRunner.requestPost(-111, MFUrl.BIKEOPELOG, unlockParams, this);
    }


    private void connectWebSocketService() {
        getBaseHandler().sendEmptyMessageDelayed(4001, 50000);
        WebSocketCallBackHelp.addWebSoctketCallBcak("CreateOrder", this);
        Intent s = new Intent(mContext, WebSocketService.class);
        WebSocketCommandVO socketCommandVO = new WebSocketCommandVO();
        socketCommandVO.setCommand(-1);
        s.putExtra("WebSocketCommandVO", socketCommandVO);
        mContext.startService(s);
    }

    /**
     * 断开websocket
     */
    private void disconnectWebSocketService() {
        Intent s = new Intent(mContext, WebSocketService.class);
        mContext.stopService(s);
    }

}