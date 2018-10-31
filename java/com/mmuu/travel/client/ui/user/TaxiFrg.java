package com.mmuu.travel.client.ui.user;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.RequestResultListBean;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.taxi.MainOrderBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.widget.WheelView;
import com.mmuu.travel.client.widget.dialog.OneDialog;
import com.mmuu.travel.client.widget.dialog.TwoDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 出租车项目
 */

public class TaxiFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, TextWatcher {
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.v_button_bg)
    View vButtonBg;
    @BindView(R.id.tv_now)
    TextView tvNow;
    @BindView(R.id.tv_future)
    TextView tvFuture;
    @BindView(R.id.ll_selected)
    LinearLayout llSelected;
    @BindView(R.id.iv_time_icon)
    ImageView ivTimeIcon;
    @BindView(R.id.rl_timeselected)
    RelativeLayout rlTimeselected;
    @BindView(R.id.v_line3)
    View vLine3;
    @BindView(R.id.ll_tag_icon)
    LinearLayout llTagIcon;
    @BindView(R.id.ed_startplace)
    EditText edStartplace;
    @BindView(R.id.tv_orderstatu)
    TextView tvOrderstatu;
    @BindView(R.id.v_line4)
    View vLine4;
    @BindView(R.id.ed_endplace)
    EditText edEndplace;
    @BindView(R.id.v_line5)
    View vLine5;
    @BindView(R.id.rl_place)
    RelativeLayout rlPlace;
    @BindView(R.id.tv_calltaxi)
    TextView tvCalltaxi;
    Unbinder unbinder;
    @BindView(R.id.ll_place)
    LinearLayout llPlace;
    @BindView(R.id.tv_selectedtime)
    TextView tvSelectedtime;
    @BindView(R.id.tv_startplace)
    TextView tvStartplace;
    @BindView(R.id.tv_endplace)
    TextView tvEndplace;
    private WheelView wvaDate;
    private WheelView wvaHour;
    private WheelView wvaMinute;
    private Dialog dialog;
    private String year = "";
    private String date = "";
    private String hour = "";
    private String minite = "";
    private String[] nowTime = null;
    private final int TAG_GETRUNNINGORDEROFUSER = 1695701;
    private final int TAG_CANCELORDER = 1695702;
    private List<MainOrderBean.ResultBean> orderBeanList;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_taxi, null);
            unbinder = ButterKnife.bind(this, fragContentView);
            initView();
            getOrderingInfo();
        }
        return fragContentView;
    }

    private void getOrderingInfo() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
//        MFRunner.requestPost(TAG_GETRUNNINGORDEROFUSER, MFUrl.GETRUNNINGORDEROFUSER, params, this);
    }

    private void getCancelInfo() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");

        params.addBodyParameter("orderNo", orderBeanList.get(0).getOrderNo() + "");
//        MFRunner.requestPost(TAG_CANCELORDER, MFUrl.CANCELORDER, params, this);
    }

    private void initView() {
        titleTitle.setText("预约出租车");
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setOnClickListener(this);
        tvFuture.setOnClickListener(this);
        tvNow.setOnClickListener(this);
        rlTimeselected.setOnClickListener(this);
        edEndplace.setOnClickListener(this);
        edStartplace.setOnClickListener(this);

        edEndplace.addTextChangedListener(this);
        edStartplace.addTextChangedListener(this);
        tvEndplace.setOnClickListener(this);
        tvStartplace.setOnClickListener(this);
        tvCalltaxi.setOnClickListener(this);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog != null) {
            dialog.dismiss();
        }
        getOrderingInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.rl_timeselected:
                Date date = new Date();
                long l = 24 * 60 * 60 * 1000; //每天的毫秒数
                //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
                //减8个小时的毫秒值是为了解决时区的问题。
                long border = date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000 + 23 * 60 * 60 * 1000 + 30 * 60 * 1000;
                if (date.getTime() > border) {
                    MFUtil.showToast(mContext, "只可预约当天用车");
                } else {
                    showBottomDialog();
                }
                break;
            case R.id.tv_now:
                startAnimation(true, 300);
                break;
            case R.id.tv_future:
                startAnimation(false, 300);
                break;

            case R.id.tv_sure:
                tvSelectedtime.setText(wvaDate.getSeletedItem() + wvaHour.getSeletedItem() + wvaMinute.getSeletedItem());
                dialog.dismiss();

                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_calltaxi:
                if ("取消订单".equals(tvCalltaxi.getText().toString())) {
//                    TwoDialog taxiDia = new TwoDialog(mContext, "有正在进行中订单", "如要重新预约请取消后再次呼叫出租车\n一天内最多可以取消5次", "点错了", "确定");
                    TwoDialog taxiDia = new TwoDialog(mContext, "有正在进行中订单", "确认是否取消当前用车", "点错了", "确定");
                    taxiDia.setDialogClickListener(new DialogClickListener() {
                        @Override
                        public void onLeftClick(View v, Dialog d) {
                            d.dismiss();
                        }

                        @Override
                        public void onRightClick(View v, Dialog d) {
                            d.dismiss();
                            if (orderBeanList == null || orderBeanList.size() <= 0) {
                                return;
                            }
                            getCancelInfo();
                        }
                    });
                    taxiDia.show();
                } else if ("呼叫出租车".equals(tvCalltaxi.getText().toString())) {
                    callTaxi();
                }
                break;
        }

    }

    private void startAnimation(final boolean isNow, int time) {
        int vButtonBgnumber = 0;
        int rlTimeselectednumber = 0;
        int llPlacenumber = -57;
        tvFuture.setSelected(false);
        tvNow.setSelected(true);
        if (!isNow) {
            vButtonBgnumber = ScreenUtil.dip2px(mContext, 121);
            rlTimeselectednumber = 1;
            llPlacenumber = 0;
            tvFuture.setSelected(true);
            tvNow.setSelected(false);
        }
        ObjectAnimator animatorBg = ObjectAnimator.ofFloat(vButtonBg,
                "TranslationX", vButtonBgnumber);
        animatorBg.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorBg.setDuration(time);
        animatorBg.start();

//        ObjectAnimator animatorTimeselected = ObjectAnimator.ofFloat(rlTimeselected, "scaleY", rlTimeselectednumber);
//        rlTimeselected.setPivotY(0);
//        animatorTimeselected.setDuration(time);
//        animatorTimeselected.setInterpolator(new AccelerateDecelerateInterpolator());
//        animatorTimeselected.start();

        final ObjectAnimator animatorllPlace = ObjectAnimator.ofFloat(llPlace,
                "TranslationY", ScreenUtil.dip2px(mContext, llPlacenumber));
        animatorllPlace.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorllPlace.setDuration(time);
        animatorllPlace.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!isNow) {
                    rlTimeselected.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isNow) {
                    rlTimeselected.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorllPlace.start();
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {
        switch (i) {
            case 1001:
                dialogShow();
                break;
        }
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
                RequestResultBean taxiBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (taxiBean != null && taxiBean.getCode() == 0) {
                    getOrderingInfo();
                    OneDialog taxiDia = new OneDialog(mContext, taxiBean.getMessage(), "确定");
                    taxiDia.setDialogClickListener(new DialogClickListener() {
                        @Override
                        public void onLeftClick(View v, Dialog d) {

                        }

                        @Override
                        public void onRightClick(View v, Dialog d) {

                            d.dismiss();
                        }
                    });
                    taxiDia.show();
                } else if (taxiBean != null && taxiBean.getCode() == 1200) {
                    OneDialog taxiDia = new OneDialog(mContext, taxiBean.getMessage(), "确定");
                    taxiDia.setDialogClickListener(new DialogClickListener() {
                        @Override
                        public void onLeftClick(View v, Dialog d) {
                        }

                        @Override
                        public void onRightClick(View v, Dialog d) {
                            d.dismiss();
                        }
                    });
                    taxiDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mContext.finish();
                        }
                    });
                    taxiDia.show();
                } else if (taxiBean != null) {
                    MFUtil.showToast(mContext, taxiBean.getMessage());
                } else {
                    MFUtil.showToast(mContext, "呼叫失败，请重试");
                }
                break;
            case TAG_GETRUNNINGORDEROFUSER:
                RequestResultListBean<MainOrderBean.ResultBean> mainOrderBeanRequestResultBean = GsonTransformUtil.getObjectListFromJson(responseInfo.result.toString(), new TypeToken<RequestResultListBean<MainOrderBean.ResultBean>>() {
                }.getType());
                if (mainOrderBeanRequestResultBean != null && MFConstansValue.TAXI_BACK_SUCCESS == mainOrderBeanRequestResultBean.getCode()) {
                    orderBeanList = orderBeanList == null ? new ArrayList<MainOrderBean.ResultBean>() : orderBeanList;
                    orderBeanList.clear();
                    orderBeanList.addAll(mainOrderBeanRequestResultBean.getData());
                    if (orderBeanList.size() > 0) {
                        tvCalltaxi.setBackgroundResource(R.drawable.arc_orange_bg);
                        tvCalltaxi.setTextColor(Color.parseColor("#ffffff"));
                        tvCalltaxi.setOnClickListener(this);
                        MainOrderBean.ResultBean resultBean = orderBeanList.get(0);
                        tvNow.setOnClickListener(null);
                        tvFuture.setOnClickListener(null);
                        tvStartplace.setVisibility(View.VISIBLE);
                        tvEndplace.setVisibility(View.VISIBLE);
                        tvStartplace.setText(resultBean.getStartLocation());
                        tvEndplace.setText(resultBean.getEndLocation());
                        tvCalltaxi.setText("取消订单");
                        rlTimeselected.setOnClickListener(null);
                        if (orderBeanList.get(0).getOrderState() == 5) {
                            tvOrderstatu.setText("司机已接单");
                        }
                        if (orderBeanList.get(0).getCallType() == 1) {
                            startAnimation(true, 300);
                        } else {
                            startAnimation(false, 300);
                            tvSelectedtime.setText(TimeDateUtil.longToDate(resultBean.getAskTime(), "MM月dd日 HH点mm分"));
                        }
                    } else {
                        initData();
                    }
                } else if (mainOrderBeanRequestResultBean != null && MFConstansValue.TAXI_BACK_LOGOUT == mainOrderBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, mainOrderBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (mainOrderBeanRequestResultBean != null && (MFConstansValue.TAXI_BACK_SYSTEMERROR == mainOrderBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, mainOrderBeanRequestResultBean.getMessage());
                }
                break;
            case TAG_CANCELORDER:
                RequestResultBean requestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (requestResultBean != null && MFConstansValue.TAXI_BACK_SUCCESS == requestResultBean.getCode()) {
                    initData();
                } else if (requestResultBean != null && MFConstansValue.TAXI_BACK_LOGOUT == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (requestResultBean != null && (MFConstansValue.TAXI_BACK_SYSTEMERROR == requestResultBean.getCode())) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                }
                break;
        }
    }

    private void initData() {
        tvStartplace.setVisibility(View.GONE);
        tvEndplace.setVisibility(View.GONE);
        edEndplace.setText("");
        edStartplace.setText("");
        tvSelectedtime.setText("");
        tvNow.setOnClickListener(this);
        tvFuture.setOnClickListener(this);

        edEndplace.setHint("您要去哪");
        edStartplace.setHint("您现在在哪");
        tvSelectedtime.setHint("请选择用车时间");
        tvCalltaxi.setText("呼叫出租车");
        rlTimeselected.setOnClickListener(this);
        tvOrderstatu.setText("");
        tvFuture.setSelected(false);
        tvNow.setSelected(true);
        startAnimation(true, 0);
//                    addCancelNum();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheetTaxi);
        }
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mDlgCallView = inflater.inflate(R.layout.dlg_taxitime, null);
        final int cFullFillWidth = 10000;
        mDlgCallView.setMinimumWidth(cFullFillWidth);

        mDlgCallView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mDlgCallView.findViewById(R.id.tv_sure).setOnClickListener(this);

        wvaDate = (WheelView) mDlgCallView.findViewById(R.id.wheel_date);
        wvaHour = (WheelView) mDlgCallView.findViewById(R.id.wheel_hour);
        wvaMinute = (WheelView) mDlgCallView.findViewById(R.id.wheel_minute);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) wvaDate.getLayoutParams();
        params.width = ScreenUtil.getScreenWidth(mContext) / 3;
        wvaDate.setLayoutParams(params);
        wvaHour.setLayoutParams(params);
        wvaMinute.setLayoutParams(params);
        wvaDate.setOffset(1);
        wvaHour.setOffset(1);
        wvaMinute.setOffset(1);

        wvaDate.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                if (item.equals(nowTime[0])) {
                    changeHours(false);
                } else if (wvaHour.getItems().size() < 24) {
                    changeHours(true);
                }
            }
        });
        wvaHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                if (wvaDate.getSeletedItem().equals(nowTime[0]) && item.startsWith(nowTime[1]) && item.length() - nowTime[1].length() == 1) {
                    if (selectedIndex == 1) {
                        changeMinutes(false);
                    } else {
                        changeMinutes(true);
                    }
                } else {
                    changeMinutes(true);
                }
            }
        });
        wvaMinute.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
            }
        });

        getNowTime();

        wvaDate.setItems(getDay());
        wvaHour.setItems(getHour(Integer.valueOf(nowTime[1])));
        wvaMinute.setItems(getMinute(Integer.valueOf(nowTime[2])));

        wvaHour.setSeletion(0);
        wvaMinute.setSeletion(0);
        wvaDate.setSeletion(0);

        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(lp);
        dialog.setContentView(mDlgCallView);

        dialog.show();
    }

    private List<String> getMinute(int minute) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = minute; i < 60; i++) {
            strings.add(i + "分");
        }
        return strings;
    }

    private List<String> getHour(int hour) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = hour; i < 24; i++) {
            strings.add(i + "点");
        }
        return strings;
    }

    private List<String> getDay() {
        ArrayList<String> strings = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
//        calendar.setTime(new Date(System.currentTimeMillis() + 30 * 60 * 1000));
//        calendar.add(Calendar.DAY_OF_MONTH, -3);
//        for (int i = 0; i < 7; i++) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        String dateNowStr = sdf.format(date);
        strings.add(dateNowStr + "");
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }
        return strings;
    }


    private void getNowTime() {

//        long time = System.currentTimeMillis();
        long time = System.currentTimeMillis() + 30 * 60 * 1000;
        Date now = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        String dateNowStr = sdf.format(now);
        nowTime = new String[3];
        nowTime[0] = dateNowStr;
        nowTime[1] = now.getHours() + "";
        nowTime[2] = now.getMinutes() + "";
        year = now.getYear()+1900 + "年";
    }

    private void changeHours(boolean from0) {
        String l = wvaHour.getSeletedItem();
        if (wvaDate.getSeletedItem().equals(nowTime[0])) {
            wvaHour.setItems(getHour(Integer.valueOf(nowTime[1])));
            wvaHour.setSeletion(0);
            changeMinutes(false);
        } else {
            wvaHour.setItems(getHour(0));
        }
        if (from0) {
            wvaHour.setItems(getHour(0));
        }
        if (!l.equals(wvaHour.getSeletedItem())) {
            changeMinutes(false);
        }
    }

    private void changeMinutes(boolean from0) {
        if (!from0 && wvaHour.getSeletedItem().indexOf(nowTime[1]) == 0 && wvaHour.getSeletedItem().length() - nowTime[1].length() == 1) {
            wvaMinute.setItems(getMinute(Integer.valueOf(nowTime[2])));
            wvaMinute.setSeletion(0);
        } else {
            wvaMinute.setItems(getMinute(0));
        }
        if (from0) {
            wvaMinute.setItems(getMinute(0));
        }
    }


    private void callTaxi() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        String startAdr = edStartplace.getText().toString();
        String endAdr = edEndplace.getText().toString();
        String bookTime = tvSelectedtime.getText().toString();


        if (TextUtils.isEmpty(startAdr)) {
            MFUtil.showToast(mContext, "请填写您的出发地");
            return;
        }
        if (TextUtils.isEmpty(endAdr)) {
            MFUtil.showToast(mContext, "请填写您的目的地");
            return;
        }


        RequestParams params = new RequestParams();
        if (tvNow.isSelected()) {
            params.addBodyParameter("callType", "1");
        } else {
            params.addBodyParameter("callType", "2");


            if (TextUtils.isEmpty(bookTime)) {
                MFUtil.showToast(mContext, "请选择出行时间");
                return;
            }


            StringBuffer sb = new StringBuffer();

            date = wvaDate.getSeletedItem();
            sb.append(year);
            sb.append(date);
            sb.append(" ");
            hour = wvaHour.getSeletedItem();
            if (!TextUtils.isEmpty(hour)) {
                if (hour.length() == 2) {
                    sb.append("0");
                    sb.append(hour.substring(0, 1));
                } else if (hour.length() == 3) {
                    sb.append(hour.substring(0, 2));
                } else {
                    MFUtil.showToast(mContext, "请选择出行时间");
                    return;
                }
            }
            sb.append(":");
            minite = wvaMinute.getSeletedItem();
            if (!TextUtils.isEmpty(minite)) {
                if (minite.length() == 2) {
                    sb.append("0");
                    sb.append(minite.substring(0, 1));
                } else if (minite.length() == 3) {
                    sb.append(minite.substring(0, 2));
                } else {
                    MFUtil.showToast(mContext, "请选择出行时间");
                    return;
                }
            }

            String sDt = sb.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            Date dt2 = null;
            try {
                dt2 = sdf.parse(sDt);
                bookTime = dt2.getTime() + "";
            } catch (ParseException e) {
                e.printStackTrace();
            }
//继续转换得到秒数的long型
            params.addBodyParameter("askTime", bookTime);
        }

        if (MFApp.getInstance().getmLocation() != null) {
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
            params.addBodyParameter("district", MFApp.getInstance().getmLocation().getDistrict());
            params.addBodyParameter("inCityCode", MFApp.getInstance().getmLocation().getCityCode());
            params.addBodyParameter("inCityName", MFApp.getInstance().getmLocation().getCity());
            params.addBodyParameter("createGps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
            params.addBodyParameter("createLocation", MFApp.getInstance().getmLocation().getAddress());
        }

        params.addBodyParameter("startLocation", startAdr);
        params.addBodyParameter("endLocation", endAdr);
        params.addBodyParameter("userMobile", MFStaticConstans.getUserBean().getUser().getMobile() + "");
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
//        MFRunner.requestPost(1001, MFUrl.CALLTAXI, params, this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edStartplace.getText() + "") && !TextUtils.isEmpty(edEndplace.getText() + "")) {
            tvCalltaxi.setBackgroundResource(R.drawable.arc_orange_bg);
            tvCalltaxi.setTextColor(Color.parseColor("#ffffff"));
            tvCalltaxi.setOnClickListener(this);
        } else {
            tvCalltaxi.setBackgroundResource(R.drawable.taxi_calltaxino_bg);
            tvCalltaxi.setTextColor(Color.parseColor("#999999"));
            tvCalltaxi.setOnClickListener(null);
        }
    }

    //    /**
//     * 获取当天还可以取消订单次数
//     *
//     * @return
//     */
//    private int getCancelNum() {
//        int num = 0;
//        String data = SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_CANCELORDER_DATE, "");
//        if (!TextUtils.isEmpty(data)) {
//            String[] datas = data.split(";");
//            if (datas != null && datas.length > 0) {
//                String now = TimeDateUtil.longToDate(System.currentTimeMillis(), "yyyy-MM-dd");
//                for (String s : datas) {
//                    if (now.equals(s)) {
//                        num++;
//                    }
//                }
//                if (num == 0) {
//                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_CANCELORDER_DATE, "");
//                }
//            }
//        }
//        return num;
//    }

//    private void addCancelNum() {
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_CANCELORDER_DATE, ""));
//        buffer.append(TimeDateUtil.longToDate(System.currentTimeMillis(), "yyyy-MM-dd")).append(";");
//        SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_CANCELORDER_DATE, buffer.toString());
//    }
}
