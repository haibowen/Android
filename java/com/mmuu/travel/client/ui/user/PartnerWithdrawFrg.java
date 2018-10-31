package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.IDNumber;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XIXIHAHA on 2017/8/30.
 * <p>
 * 采蜜人提现--支付宝
 */
public class PartnerWithdrawFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, TextWatcher {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.partner_withdraw_menoy)
    TextView partnerWithdrawMenoy;
    @BindView(R.id.partner_withdraw_zhifubao)
    EditText partnerWithdrawZhifubao;
    @BindView(R.id.partner_withdraw_name)
    EditText partnerWithdrawName;
    @BindView(R.id.partner_withdraw_cardid)
    EditText partnerWithdrawCardid;
    @BindView(R.id.tv_withdraw_menoy)
    TextView tvWithdrawMenoy;
    @BindView(R.id.partner_withdraw_submit)
    TextView partnerWithdrawSubmit;
    Unbinder unbinder;
    private final int TAG_WITHDRAW = 1695701;
    private final int TAG_QUITPARTNER = 1695702;
    private Bundle bundle = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_partnerwithdraw, null);
            unbinder = ButterKnife.bind(this, fragContentView);
            bundle = mContext.getIntent().getExtras();
            initView();
        }
        return fragContentView;
    }

    private void initView() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setOnClickListener(this);

        if (bundle != null && bundle.containsKey("type") && "quit".equals(bundle.getString("type"))) {
            titleTitle.setText("退出采蜜人");
        } else {
            titleTitle.setText("金额提取");
        }

        if (bundle != null && bundle.containsKey("grantAmount")) {
            tvWithdrawMenoy.setText(bundle.getString("grantAmount"));
        }
        partnerWithdrawZhifubao.addTextChangedListener(this);
        partnerWithdrawName.addTextChangedListener(this);
        partnerWithdrawCardid.addTextChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.partner_withdraw_submit:
                String idCardNumber = partnerWithdrawCardid.getText().toString().trim();
                IDNumber number = new IDNumber();
                if (!number.isIDnumber(idCardNumber)) {
                    MFUtil.showToast(mContext, "请输入合法身份证号");
                    return;
                }
                submitData();
                break;
        }

    }

    private void submitData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        params.addBodyParameter("IDCard", partnerWithdrawCardid.getText() + "");
        params.addBodyParameter("account", partnerWithdrawZhifubao.getText() + "");
        params.addBodyParameter("userName", partnerWithdrawName.getText() + "");
//        if (bundle != null && bundle.containsKey("type") && "quit".equals(bundle.getString("type"))) {
//            MFRunner.requestPost(TAG_QUITPARTNER, MFUrl.QUITPARTNER, params, this);
//        } else {
//            MFRunner.requestPost(TAG_WITHDRAW, MFUrl.WITHDRAW, params, this);
//        }

    }

    @Override
    public void onStart(int tag, RequestParams params) {

    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {
        if (!isAdded()) {
            return;
        }
        dismmisDialog();
        switch (tag) {
            case TAG_WITHDRAW:
                String stringjson = responseInfo.result.toString();
                RequestResultBean requestResultBean = GsonTransformUtil.getObjectFromJson(stringjson, new TypeToken<RequestResultBean>() {
                }.getType());
                if (requestResultBean != null && MFConstansValue.BACK_SUCCESS == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    startActivity(PartnerRewardAct.class, null);
                    mContext.finish();
                } else if (requestResultBean != null && MFConstansValue.BACK_LOGOUT == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (requestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == requestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == requestResultBean.getCode())) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                } else if (requestResultBean != null) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                }
                break;
            case TAG_QUITPARTNER:
                String quitpartnerString = responseInfo.result.toString();
                RequestResultBean quitrRequestResultBean = GsonTransformUtil.getObjectFromJson(quitpartnerString, new TypeToken<RequestResultBean>() {
                }.getType());
                if (quitrRequestResultBean != null && MFConstansValue.BACK_SUCCESS == quitrRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
                    MobclickAgent.onEvent(mContext, "quit_partner");
                    mContext.finish();
                } else if (quitrRequestResultBean != null && MFConstansValue.BACK_LOGOUT == quitrRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (quitrRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == quitrRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == quitrRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
                } else if (quitrRequestResultBean != null) {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        if (!isAdded()) {
            return;
        }
        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
        dismmisDialog();
    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(partnerWithdrawZhifubao.getText()) &&
                !TextUtils.isEmpty(partnerWithdrawCardid.getText()) &&
                !TextUtils.isEmpty(partnerWithdrawName.getText())) {
            partnerWithdrawSubmit.setBackgroundResource(R.drawable.arc_orange_bg);
            partnerWithdrawSubmit.setOnClickListener(this);
        } else {
            partnerWithdrawSubmit.setBackgroundResource(R.drawable.arc_grey_bg);
            partnerWithdrawSubmit.setOnClickListener(null);
        }
    }
}
