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
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JGW 提交支付宝账号frg
 */

public class CommitAliCountFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, TextWatcher {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.ed_alicount)
    EditText edAlicount;
    @BindView(R.id.ed_aliname)
    EditText edAliname;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private final int TAG_APPLYDEPOSITBACKBYACCOUNT = 1695701;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_commitalicount, null);
            ButterKnife.bind(this, fragContentView);
            initView();
        }
        return fragContentView;
    }

    private void initView() {
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText("押金提现");
        edAlicount.addTextChangedListener(this);
        edAliname.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_commit:
                if (!TextUtils.isEmpty(edAliname.getText().toString().trim()) &&
                        !TextUtils.isEmpty(edAlicount.getText().toString().trim())) {
                    commitData();
                }
                break;
        }
    }

    private void commitData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        params.addBodyParameter("account", edAlicount.getText().toString().trim() + "");
        params.addBodyParameter("userName", edAliname.getText().toString().trim() + "");
        MFRunner.requestPost(TAG_APPLYDEPOSITBACKBYACCOUNT, MFUrl.APPLYDEPOSITBACKBYACCOUNT, params, this);
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
        dismmisDialog();
        switch (i) {
            case TAG_APPLYDEPOSITBACKBYACCOUNT:
                RequestResultBean requestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (requestResultBean != null && requestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    mContext.finish();
                } else if (requestResultBean != null && MFConstansValue.BACK_LOGOUT == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (requestResultBean != null) {
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
        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
        dismmisDialog();
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edAlicount.getText()) &&
                !TextUtils.isEmpty(edAliname.getText())) {
            tvCommit.setBackgroundResource(R.drawable.arc_orange_bg);
            tvCommit.setOnClickListener(this);
        } else {
            tvCommit.setBackgroundResource(R.drawable.arc_grey_bg);
            tvCommit.setOnClickListener(null);
        }
    }
}
