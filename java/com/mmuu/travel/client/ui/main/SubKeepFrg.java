package com.mmuu.travel.client.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.order.CancelBookVO;
import com.mmuu.travel.client.bean.order.OrderDetailVO;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.widget.dialog.CancelBookOrderDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XIXIHAHA on 2016/12/26.
 */

public class SubKeepFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, A2B {

    @BindView(R.id.item_keep_loca)
    TextView itemKeepLoca;
    @BindView(R.id.item_keep_flute)
    TextView itemKeepFlute;
    @BindView(R.id.item_keep_time)
    TextView itemKeepTime;
    @BindView(R.id.item_keep_cancel)
    TextView itemKeepCancel;

    private long times;
    private OrderDetailVO orderDetailVO;
    private CancelBookVO tempVO;

    private boolean cando = true;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);

        switch (msg.what) {
            case 2001:
                getHandler().removeMessages(2001);
                times--;
                if (times > 0) {
                    itemKeepTime.setText(checkTime(times));
                    getHandler().sendEmptyMessageDelayed(2001, 1000);
                } else {
                    getHandler().sendEmptyMessageDelayed(2002, 1000);
                }

                break;
            case 2002:
                getHandler().removeCallbacksAndMessages(null);
                if (isAdded()) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                break;
            case 5001:
                cando = true;
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.item_main_booking, null);
            ButterKnife.bind(this, fragContentView);
            initView();
            cando = true;
        }
        return fragContentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        MFStaticConstans.needRefOrder = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        MFStaticConstans.needRefOrder = true;
    }

    @Override
    public void onDestroyView() {
        getHandler().removeCallbacksAndMessages(null);

        super.onDestroyView();
    }

    private void initView() {
        itemKeepFlute.setOnClickListener(this);
        itemKeepCancel.setOnClickListener(this);
    }

    public void showData(OrderDetailVO bikeVO) {
        MFStaticConstans.hasToOrder = true;
        if (bikeVO == null) {
            return;
        }

        tempVO = (CancelBookVO) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(mContext, MFStaticConstans.getUserBean().getUser().getId() + "cancelBook", ""), CancelBookVO.class);
        if (tempVO == null) {
            tempVO = new CancelBookVO();
            tempVO.addId(bikeVO.getOrderId());
            tempVO.setDate(TimeDateUtil.longTolong(System.currentTimeMillis()));
        } else if (tempVO.getDate() == TimeDateUtil.longTolong(System.currentTimeMillis())) {
            tempVO.addId(bikeVO.getOrderId());
        } else {
            tempVO.clearIds();
            tempVO.addId(bikeVO.getOrderId());
            tempVO.setDate(TimeDateUtil.longTolong(System.currentTimeMillis()));
        }

        SharedPreferenceTool.setPrefString(mContext, MFStaticConstans.getUserBean().getUser().getId() + "cancelBook", GsonTransformUtil.toJson(tempVO));

        orderDetailVO = bikeVO;
        itemKeepLoca.setText(bikeVO.getLocation());
        int dis = (int) Math.ceil(Double.valueOf(bikeVO.getAnticipatedMileage() == null ? "0" : bikeVO.getAnticipatedMileage()) / 1000);

        long timess = 0;
        if (!TextUtils.isEmpty(bikeVO.getServerDate()) && !TextUtils.isEmpty(bikeVO.getPlaceOrderTime())) {
            timess = 5 + 10 * 60 - (Long.valueOf(bikeVO.getServerDate()) - Long.valueOf(bikeVO.getPlaceOrderTime())) / 1000;
        }

        if (timess > 0) {
            times = timess;
            getHandler().sendEmptyMessageDelayed(2001, 100);
        } else {
            getHandler().sendEmptyMessageDelayed(2002, 1000);
        }


    }


    private String checkTime(long times) {

        StringBuffer sb = new StringBuffer();
        int min = (int) (times / 60);
        if (min > 9) {
            sb.append(min);
        } else {
            sb.append(0);
            sb.append(min);
        }
        sb.append(":");
        int sec = (int) (times % 60);
        if (sec > 9) {
            sb.append(sec);
        } else {
            sb.append(0);
            sb.append(sec);
        }
        return sb.toString();
    }

    @Override
    public void onClick(View view) {

        if (orderDetailVO == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.item_keep_flute:
                MobclickAgent.onEvent(mContext, "click_reserveafter_blow");
//                if (!cando) {
//                    MFUtil.showToast(mContext, "鸣笛操作不能太频繁哦～会影响别人的");
//                    return;
//                } else {
                findEbikeWhistle(orderDetailVO.getBikeCode());
//                }
                break;
//            case R.id.item_keep_light_ly:
//                ctrLight(orderDetailVO.getBikeCode());
//                break;
            case R.id.item_keep_cancel:
                StringBuffer sb = new StringBuffer();
                sb.append("当日最多可取消5次预约，" +
                        "还剩");
                CancelBookVO cancelBookVO = (CancelBookVO) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(mContext, MFStaticConstans.getUserBean().getUser().getId() + "cancelBook", ""), CancelBookVO.class);
                if (cancelBookVO == null) {
                    sb.append(5);
                } else if (cancelBookVO != null) {
                    if (cancelBookVO.getDate() == TimeDateUtil.longTolong(System.currentTimeMillis())) {
                        sb.append(5 - cancelBookVO.getCancelTimes() + 1);
                    } else {
                        cancelBookVO.clearIds();
                        sb.append(5);
                    }
                }

                sb.append("次");

                CancelBookOrderDialog cancelBookOrderDialog = new CancelBookOrderDialog(mContext, sb.toString(), null, null);
                cancelBookOrderDialog.setDialogClickListener(new DialogClickListener() {
                    @Override
                    public void onLeftClick(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void onRightClick(View v, Dialog d) {
                        String s = ((CancelBookOrderDialog) d).getCheck();
                        if (TextUtils.isEmpty(s)) {
                            MFUtil.showToast(mContext, "请选择取消原因");
                            return;
                        }
                        if (!MFUtil.checkNetwork(mContext)) {
                            showNoNetworkDialog(true);
                            return;
                        }
                        d.dismiss();
                        MobclickAgent.onEvent(mContext, "click_reserveafter_cancel");
                        cancelOrder(orderDetailVO, ((CancelBookOrderDialog) d).getCheck());
                    }
                });
                cancelBookOrderDialog.show();
                sb = null;
                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (hasHandler()) {
//            getHandler().removeCallbacksAndMessages(null);
//        }
    }

    /**
     * 鸣笛找车
     */
    private void findEbikeWhistle(String bikeCode) {
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("bikeCode", bikeCode);
        MFRunner.requestPost(1001, MFUrl.findEbikeWhistle, detailParams, this);
    }

    /**
     * 车辆闪灯
     */
    private void ctrLight(String bikeCode) {
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("bikeCode", bikeCode);
        MFRunner.requestPost(1001, MFUrl.ctrLight, detailParams, this);
    }


    private void cancelOrder(OrderDetailVO orderDetailVO, String reason) {
        if (orderDetailVO == null) {
            return;
        }
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("bikeCode", orderDetailVO.getBikeCode());
        detailParams.addBodyParameter("orderId", orderDetailVO.getOrderId());
        detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        detailParams.addBodyParameter("reason", reason);
        MFRunner.requestPost(1002, MFUrl.cancelReserveOrder, detailParams, this);
    }


    @Override
    public void onStart(int i, RequestParams requestParams) {
        dialogShow();
    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        if (!isAdded()) {
            return;
        }
        dismmisDialog();
        switch (i) {
            case 1001:
                RequestResultBean<String> req = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<String>>() {
                }.getType());
                if (req != null) {
                    MFUtil.showToast(mContext, req.getMessage());
                    if (req.getCode() == 0) {
                        cando = false;
                        getHandler().sendEmptyMessageDelayed(5001, 60000);
                    }
                }
                break;
            case 1002:
                RequestResultBean<String> requ = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<String>>() {
                }.getType());
                if (requ != null) {
                    MFUtil.showToast(mContext, requ.getMessage());
                    if (requ.getCode() == 0) {
                        getHandler().sendEmptyMessageDelayed(2002, 1000);
                    }
                }
                break;

        }
    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        dismmisDialog();
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    @Override
    public int callB(int type, Object o) {

        switch (type) {
            case 1:
                if (itemKeepCancel != null) {
                    onClick(itemKeepCancel);
                }
                break;
            default:
                break;
        }

        return 0;
    }
}
