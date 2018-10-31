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
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.user.VoucherBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.ui.user.adapter.VoucherAdp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 可用出行券
 * Created by HuangYuan on 2016/12/13.
 */

public class MyVoucherUsedFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    @BindView(R.id.pull_refresh_listview)
    PullToRefreshListView pullRefreshListview;

    View footView;
    private List<VoucherBean.DataBean> voucherVOs;
    private VoucherAdp adp;
    private int pageNo = 1;
    private final int REQUEST_VOUCHER_CODE = 1695701;

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_my_vouchernotuse, null);
            ButterKnife.bind(this, fragContentView);
            initView();
            initData(true);
        }
        return fragContentView;
    }

    /**
     */
    private void initData(boolean isshow) {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        if (isshow) {
            dialogShow();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageSize", 20 + "");
        params.addBodyParameter("pageNo", pageNo + "");
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        params.addBodyParameter("type", "beused");
        MFRunner.requestPost(REQUEST_VOUCHER_CODE, MFUrl.MY_VOUCHER_URL, params, this);
    }

    private void initView() {
        pullRefreshListview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullRefreshListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        voucherVOs = new ArrayList<VoucherBean.DataBean>();
        adp = new VoucherAdp(mContext, voucherVOs, "beused");
        footView = LayoutInflater.from(mContext).inflate(R.layout.voucher_footview, null);
        footView.setVisibility(View.GONE);
        pullRefreshListview.getRefreshableView().addFooterView(footView);
        pullRefreshListview.setAdapter(adp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.title_right_text:
                Bundle bundle = new Bundle();
                bundle.putString("url", MFUrl.CASHCOUPON);
                bundle.putString("title", "出行券使用规则");
                startActivity(WebAty.class, bundle);
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
        pullRefreshListview.onRefreshComplete();
        pullRefreshListview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefreshListview.onRefreshComplete();
            }
        }, 100);
        dismmisDialog();
        switch (i) {
            case REQUEST_VOUCHER_CODE:
                RequestResultBean<VoucherBean> requestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<VoucherBean>>() {
                }.getType());
                if (requestResultBean != null && requestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    if (requestResultBean != null && requestResultBean.getData() != null) {
                        if (pageNo == 1) {
                            voucherVOs = requestResultBean.getData().getList();
                        } else {
                            voucherVOs.addAll(requestResultBean.getData().getList());
                        }
                        adp.setDatas(voucherVOs);
                        if (pageNo >= requestResultBean.getData().getTotalPage()) {
//                            MFUtil.showToast(mContext, "没有更多数据了");
//                            footView.setVisibility(View.VISIBLE);
                            pullRefreshListview.setMode(PullToRefreshBase.Mode.DISABLED);
                        } else {
//                            footView.setVisibility(View.GONE);
                            pullRefreshListview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        }
                        View emptyView = LayoutInflater.from(mContext).inflate(R.layout.view_nodata, null);
                        ImageView iv_nodata = (ImageView) emptyView.findViewById(R.id.iv_nodata);
                        iv_nodata.setImageResource(R.drawable.nodata_nodata);
                        TextView textView = (TextView) emptyView.findViewById(R.id.tv_text_hint);
                        TextView tv_text_hintbutton = (TextView) emptyView.findViewById(R.id.tv_text_hintbutton);
                        tv_text_hintbutton.setVisibility(View.GONE);
                        textView.setText("您暂时还没有使用出行券");
                        pullRefreshListview.setEmptyView(emptyView);
                    }
                } else if (requestResultBean != null && MFConstansValue.BACK_LOGOUT == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (requestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == requestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == requestResultBean.getCode())) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
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
            case REQUEST_VOUCHER_CODE:
                MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
                dismmisDialog();
                break;
        }
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }
}
