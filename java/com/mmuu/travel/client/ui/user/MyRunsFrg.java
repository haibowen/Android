package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.bean.MyRunsItemBean;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.MyRunsBean;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.user.adapter.MyrunsAdp;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的行程
 * Created by HuangYuan on 2016/12/19.
 */

public class MyRunsFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.pull_refresh_listview)
    PullToRefreshListView pullRefreshlistview;

    private ArrayList<MyRunsItemBean> myRunsItemBeans;
    private MyrunsAdp adapter;
    private int pageNo = 1;
    private static final int TAG_STROKELIST_URL = 1695701;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_my_trips, null);
            ButterKnife.bind(this, fragContentView);
            initView();
            if (!MFUtil.checkNetwork(mContext)) {
                View emptyView = LayoutInflater.from(mContext).inflate(R.layout.view_nodata, null);
                ImageView iv_nodata = (ImageView) emptyView.findViewById(R.id.iv_nodata);
                iv_nodata.setImageResource(R.drawable.nodata_nonet);
                TextView textView = (TextView) emptyView.findViewById(R.id.tv_text_hint);
                TextView tv_text_hintbutton = (TextView) emptyView.findViewById(R.id.tv_text_hintbutton);
                tv_text_hintbutton.setOnClickListener(this);
                textView.setText("无网络链接，点击");
                tv_text_hintbutton.setVisibility(View.VISIBLE);
                pullRefreshlistview.setEmptyView(emptyView);
                return fragContentView;
            }
            initData(true);
        }
        return fragContentView;
    }

    private void initView() {
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setOnClickListener(this);
//        titleTitle.setText(R.string.user_myrun);
//        titleRightText.setText(R.string.journey_feedback);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector, null));
//        } else {
//            titleRightText.setTextColor(getResources().getColorStateList(R.color.title_righttxtblackcolorselector));
//        }
//        titleRightText.setVisibility(View.VISIBLE);
//        titleRightText.setOnClickListener(this);
        pullRefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullRefreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 1;
                initData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;
                initData(false);
            }
        });
//        pullRefreshlistview.getRefreshableView().addHeaderView();
        adapter = new MyrunsAdp(mContext, myRunsItemBeans);
        pullRefreshlistview.setAdapter(adapter);
    }

    private void initData(boolean isShowDlg) {
        if (!MFUtil.checkNetwork(mContext)) {
            pullRefreshlistview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullRefreshlistview.onRefreshComplete();
                }
            }, 100);
            showNoNetworkDialog(true);
            return;
        }
        if (isShowDlg)
            dialogShow();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", String.valueOf(MFStaticConstans.getUserBean().getUser().getId()));
        requestParams.addBodyParameter("pageNo", pageNo + "");
        requestParams.addBodyParameter("pageSize", 100 + "");
        MFRunner.requestPost(TAG_STROKELIST_URL, MFUrl.STROKELIST_URL, requestParams, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_text_hintbutton:
                dialogShow();
                if (!MFUtil.checkNetwork(mContext)) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                        }
                    }, 120);
                    return;
                }
                initData(true);
                break;
            case R.id.title_right_text:
                MobclickAgent.onEvent(mContext, "click_myrun_feelback");
                startActivity(TripFeedbackAct.class, null);
                break;
        }
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {

    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        if (!isAdded()) {
            return;
        }
        pullRefreshlistview.onRefreshComplete();
        pullRefreshlistview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefreshlistview.onRefreshComplete();
            }
        }, 50);
        dismmisDialog();
        switch (i) {
            case TAG_STROKELIST_URL:
                RequestResultBean<MyRunsBean> myRunsBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MyRunsBean>>() {
                }.getType());
                if (myRunsBeanRequestResultBean != null && myRunsBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MyRunsBean myRunsBean = myRunsBeanRequestResultBean.getData();
                    if (myRunsBean != null && myRunsBean.getData() != null) {


                        if (pageNo >= myRunsBean.getTotalPage()) {
//                            MFUtil.showToast(mContext, "没有更多数据了");
                            pullRefreshlistview.setMode(PullToRefreshBase.Mode.DISABLED);
                        } else {
                            pullRefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        }

                        if (pageNo == 1) {
                            myRunsItemBeans = translateDate((ArrayList<MyRunsBean.DataBean>) myRunsBean.getData());
                        } else {
                            if (myRunsItemBeans == null) {
                                pageNo = 1;
                                myRunsItemBeans = translateDate((ArrayList<MyRunsBean.DataBean>) myRunsBean.getData());
                            } else {
                                myRunsItemBeans.addAll(translateDate((ArrayList<MyRunsBean.DataBean>) myRunsBean.getData()));
                            }
                        }
                        adapter.replaceData(myRunsItemBeans);
                        adapter.notifyDataSetChanged();

                        View emptyView = LayoutInflater.from(mContext).inflate(R.layout.view_nodata, null);
                        ImageView iv_nodata = (ImageView) emptyView.findViewById(R.id.iv_nodata);
                        iv_nodata.setImageResource(R.drawable.nodata_nodata);
                        TextView textView = (TextView) emptyView.findViewById(R.id.tv_text_hint);
                        TextView tv_text_hintbutton = (TextView) emptyView.findViewById(R.id.tv_text_hintbutton);
                        tv_text_hintbutton.setVisibility(View.GONE);
                        textView.setText("您暂时还没有行程记录，快去选一辆蜜蜂飞驰吧");
                        pullRefreshlistview.setEmptyView(emptyView);
                    }
                } else if (myRunsBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == myRunsBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, myRunsBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (myRunsBeanRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == myRunsBeanRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == myRunsBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, myRunsBeanRequestResultBean.getMessage());
                }
                break;
        }
    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
        dismmisDialog();
        pullRefreshlistview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefreshlistview.onRefreshComplete();
            }
        }, 100);
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    private ArrayList<MyRunsItemBean> translateDate(ArrayList<MyRunsBean.DataBean> datas) {
        if (datas.size() <= 0) {
            return null;
        }
        ArrayList<MyRunsItemBean> myRunsItemBeans = new ArrayList<>();
        ArrayList<MyRunsBean.DataBean> dataBeen = new ArrayList<>();
        dataBeen.add(datas.get(0));
        MyRunsItemBean myRunsItemBean = new MyRunsItemBean(TimeDateUtil.longToDate(datas.get(0).getPlaceOrderTime(), "yyyy-MM-dd"),
                dataBeen);
        for (int j = 0; j < datas.size(); j++) {
            if (j == 0) {
            } else if (myRunsItemBean.getDateStr().equals(TimeDateUtil.longToDate(datas.get(j).getPlaceOrderTime(), "yyyy-MM-dd"))) {
                dataBeen.add(datas.get(j));
            } else {
                myRunsItemBeans.add(myRunsItemBean);

                dataBeen = new ArrayList<>();
                dataBeen.add(datas.get(j));
                myRunsItemBean = new MyRunsItemBean(TimeDateUtil.longToDate(datas.get(j).getPlaceOrderTime(), "yyyy-MM-dd"),
                        dataBeen);
            }
            if (j == datas.size() - 1) {
                myRunsItemBeans.add(myRunsItemBean);
            }
        }
        return myRunsItemBeans;
    }
}
