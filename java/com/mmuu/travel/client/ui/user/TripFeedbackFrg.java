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
import com.mmuu.travel.client.ui.user.adapter.TripFeedbackAdp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行程反馈列表
 */

public class TripFeedbackFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.pull_refresh_listview)
    PullToRefreshListView pullRefreshlistview;

    private List<MyRunsBean.DataBean> datas;
    private int pageNo = 1;
    private TripFeedbackAdp adapter;
    private static final int TAG_STROKELIST_URL = 1695701;
    private View heardView;
    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_trip_feedback, null);
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
        requestParams.addBodyParameter("pageSize", 10 + "");
        MFRunner.requestPost(TAG_STROKELIST_URL, MFUrl.STROKELIST_URL, requestParams, this);

    }

    private void initView() {
        titleTitle.setText(getResources().getString(R.string.journey_feedback));
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setOnClickListener(this);
        pullRefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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
        heardView = inflater.inflate(R.layout.headvie_tripback, null);
        pullRefreshlistview.getRefreshableView().addHeaderView(heardView);
        adapter = new TripFeedbackAdp(mContext, datas);
        pullRefreshlistview.setAdapter(adapter);
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
        }, 100);

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
                            pullRefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else {
                            pullRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
                        }

                        if (pageNo == 1) {
                            datas = myRunsBean.getData();
                        } else {
                            datas.addAll(myRunsBean.getData());
                        }
                        adapter.replaceData(datas);
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
}
