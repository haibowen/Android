package com.mmuu.travel.client.ui.user;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.user.PartnerAccountBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.widget.dialog.PartnerQuitDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 采蜜人账户
 */

public class PartnerAccountFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    Unbinder unbinder;
    @BindView(R.id.partner_hasgetmenoy)
    TextView partnerHasgetmenoy;
    @BindView(R.id.tv_partner_quit)
    TextView tv_partner_quit;
    @BindView(R.id.partner_menoy)
    TextView partnerMenoy;
    @BindView(R.id.partner_withdraw)
    TextView partnerWithdraw;
    @BindView(R.id.partner_fuli)
    RelativeLayout partnerFuli;
    @BindView(R.id.partner_hongli)
    RelativeLayout partnerHongli;
    @BindView(R.id.partner_quit)
    LinearLayout partnerQuit;
    private final int TAG_PARTNERDETAIL_URL = 1695701;
    private PartnerAccountBean partnerAccountBean;
    private final int TAG_QUITPARTNER = 1695702;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_partneraccount, null);
            unbinder = ButterKnife.bind(this, fragContentView);
            initView();
        }
        return fragContentView;
    }

    private void initInfoData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
//        MFRunner.requestPost(TAG_PARTNERDETAIL_URL, MFUrl.PARTNERDETAIL, requestParams, this);
    }

    private void initView() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText("采蜜人账户");

        partnerWithdraw.setOnClickListener(this);
        partnerFuli.setOnClickListener(this);
        partnerHongli.setOnClickListener(this);
        partnerQuit.setOnClickListener(this);
        partnerQuit.setClickable(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        initInfoData();
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
            case R.id.partner_fuli:
                startActivity(PartnerBoonAct.class, null);
                break;
            case R.id.partner_hongli:
                startActivity(PartnerRewardAct.class, null);
                break;
            case R.id.partner_withdraw:
                if (partnerAccountBean == null) {
                    return;
                }

                if (Double.parseDouble(partnerAccountBean.getGrantAmount()) == 0) {
                    MFUtil.showToast(mContext, "没有可提取金额");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("grantAmount", partnerAccountBean.getGrantAmount());
                startActivity(PartnerWithdrawAct.class, bundle);
                break;
            case R.id.partner_quit:
                if (partnerAccountBean != null) {
                    if (partnerAccountBean.getIsPartner() == 0) {
                        MFUtil.showToast(mContext, "您已退出采蜜人");
                        return;
                    }
                    String titleDia = "审核通过后，系统将返还您\n现金" + partnerAccountBean.getQuitAmount() + "元";
                    PartnerQuitDialog quitDialog = new PartnerQuitDialog(mContext, titleDia, null, "确认退出", "我再想想");
                    quitDialog.setDialogClickListener(new DialogClickListener() {
                        @Override
                        public void onLeftClick(View v, Dialog d) {
                            d.dismiss();
                            if (Double.parseDouble(partnerAccountBean.getQuitAmount()) == 0) {
                                getQuitData();
                                return;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("grantAmount", partnerAccountBean.getQuitAmount());
                            bundle.putString("type", "quit");
                            startActivity(PartnerWithdrawAct.class, bundle);
                        }

                        @Override
                        public void onRightClick(View v, Dialog d) {
                            d.dismiss();
                        }
                    });
                    quitDialog.show();
                }
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
        dismmisDialog();
        switch (i) {
            case TAG_PARTNERDETAIL_URL:
                RequestResultBean<PartnerAccountBean> accountBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<PartnerAccountBean>>() {
                }.getType());
                if (accountBeanRequestResultBean != null && accountBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    partnerAccountBean = accountBeanRequestResultBean.getData();
                    if (partnerAccountBean.getIsPartner() == 0) {
                        tv_partner_quit.setText("已退出采蜜人");
                    } else {
                        tv_partner_quit.setText("采蜜人退出申请");
                    }
                    partnerHasgetmenoy.setText(partnerAccountBean.getTotalGrantAmount() + "元");
                    partnerMenoy.setText(partnerAccountBean.getGrantAmount() + "");
                    if (Double.parseDouble(partnerAccountBean.getGrantAmount()) == 0) {
                        partnerWithdraw.setBackgroundResource(R.drawable.arc_grey_bg);
                        partnerWithdraw.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        partnerWithdraw.setBackgroundResource(R.drawable.partner_withdraw_bu_bg);
                        partnerWithdraw.setTextColor(getResources().getColor(R.color.black_333));
                    }
                    partnerQuit.setClickable(true);
                } else if (accountBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == accountBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, accountBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (accountBeanRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == accountBeanRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == accountBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, accountBeanRequestResultBean.getMessage());
                }
                break;
            case TAG_QUITPARTNER:
                String quitpartnerString = responseInfo.result.toString();
                RequestResultBean quitrRequestResultBean = GsonTransformUtil.getObjectFromJson(quitpartnerString, new TypeToken<RequestResultBean>() {
                }.getType());
                if (quitrRequestResultBean != null && MFConstansValue.BACK_SUCCESS == quitrRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
                    MobclickAgent.onEvent(mContext, "quit_partner");
                    initInfoData();
                } else if (quitrRequestResultBean != null && MFConstansValue.BACK_LOGOUT == quitrRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (quitrRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == quitrRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == quitrRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
                } else {
                    MFUtil.showToast(mContext, quitrRequestResultBean.getMessage());
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getQuitData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        params.addBodyParameter("IDCard", "");
        params.addBodyParameter("account", "");
        params.addBodyParameter("userName", "");
//        MFRunner.requestPost(TAG_QUITPARTNER, MFUrl.QUITPARTNER, params, this);
    }
}
