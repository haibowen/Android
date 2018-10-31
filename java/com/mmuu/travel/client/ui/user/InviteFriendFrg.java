package com.mmuu.travel.client.ui.user;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.mmuu.travel.client.bean.ShareBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ShareUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.tauth.IUiListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JGW 邀请好友frg
 */

public class InviteFriendFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.invitation_top_bg)
    ImageView invitationTopBg;
    @BindView(R.id.tv_money_number)
    TextView tvMoneyNumber;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_share_type)
    TextView tvShareType;
    @BindView(R.id.rl_invitation_top_bg)
    RelativeLayout rlInvitationTopBg;
    @BindView(R.id.tv_sharehint)
    TextView tvSharehint;
    @BindView(R.id.tv_getmoney)
    TextView tvGetmoney;
    @BindView(R.id.ll_shareweixin)
    LinearLayout llShareweixin;
    @BindView(R.id.ll_shareweixincircle)
    LinearLayout llShareweixincircle;
    @BindView(R.id.ll_shareweibo)
    LinearLayout llShareweibo;
    @BindView(R.id.ll_shareqqspace)
    LinearLayout llShareqqspace;
    @BindView(R.id.lr_sharebottom)
    LinearLayout lrSharebottom;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.ll_money_type)
    LinearLayout llMoneyType;
    private View InviteFriendView;
    private static final int TAG_INVITATION_URL = 1695701;
    private ShareBean shareBean = null;
    private Bitmap bitmap;
    private IUiListener iUiListener;
    private long exitTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (InviteFriendView == null) {
            InviteFriendView = inflater.inflate(R.layout.frg_invitefriend, null);
            ButterKnife.bind(this, InviteFriendView);
            initViews();
            initData();
        }
        return InviteFriendView;
    }

    private void initViews() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setOnClickListener(this);
        llShareqqspace.setOnClickListener(this);
        llShareweibo.setOnClickListener(this);
        llShareweixin.setOnClickListener(this);
        llShareweixincircle.setOnClickListener(this);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            vLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void initData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("activityId", "1");
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(TAG_INVITATION_URL, MFUrl.FINDACTIVITYINFO, params, this);

    }

    public void setIUiListener(IUiListener iUiListener) {
        this.iUiListener = iUiListener;
    }

    @Override
    public void onClick(View view) {
        if ((System.currentTimeMillis() - exitTime) < 800) {
            return;
        }
        exitTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.ll_shareqqspace:
                if (shareBean == null || iUiListener == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_invite_shareqqspace");
                String urlQQspace = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareToTencent(urlQQspace,
                        shareBean.getShareTitle(), shareBean.getShareDescription(), shareBean.getSharePic(), iUiListener);
                break;
            case R.id.ll_shareweibo:
                if (shareBean == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_invite_shareweibo");
                String urlWeibo = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareWeiboMultiMessage("暂无附带内容", shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32),
                        urlWeibo, "InviteFriendFrg");
                break;
            case R.id.ll_shareweixin:
                if (shareBean == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_invite_shareweixin");
                String urlWeixin = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareToWeiXin(urlWeixin, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "a");
                break;
            case R.id.ll_shareweixincircle:
                if (shareBean == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_invite_shareweixincircle");
                String weixincircle = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareToWeiXinCircle(weixincircle, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "a");
                break;
            default:
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
        switch (i) {
            case TAG_INVITATION_URL:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<ShareBean> shareBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<ShareBean>>() {
                }.getType());
                if (shareBeanRequestResultBean != null && shareBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    shareBean = shareBeanRequestResultBean.getData();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bitmap = ImageLoader.getInstance().loadImageSync((shareBean.getSharePic()));
                        }
                    }).start();
                    llMoneyType.setVisibility(View.VISIBLE);

                    tvType.setText(shareBean.getTypeName() + "");


                    if (!TextUtils.isEmpty(shareBean.getType())) {
                        if ("0".equals(shareBean.getType())) {
                            tvShareType.setVisibility(View.VISIBLE);
                            tvMoneyNumber.setText(shareBean.getAmount() + "");
                        } else if ("3".equals(shareBean.getType())) {
                            tvShareType.setVisibility(View.GONE);
                            tvMoneyNumber.setText(shareBean.getIntegral() + "");
                        }
                    }

                    tvSharehint.setText(shareBean.getDescription());
                    tvGetmoney.setText(shareBean.getReward());
                    titleTitle.setText(shareBean.getTitle());
                } else if (shareBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == shareBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, shareBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (shareBeanRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == shareBeanRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == shareBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, shareBeanRequestResultBean.getMessage());
//                    AMapUtils.op

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
}
