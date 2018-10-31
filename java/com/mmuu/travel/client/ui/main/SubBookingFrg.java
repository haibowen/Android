package com.mmuu.travel.client.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.bike.BikeDetailVO;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.bean.mfinterface.BookingBikeListener;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.order.CancelBookVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.IMapUtils;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.other.RechargeAct;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.ui.user.CarDepositAct;
import com.mmuu.travel.client.ui.user.CertificationAct;
import com.mmuu.travel.client.ui.user.LoginAct;
import com.mmuu.travel.client.ui.user.UserCenterAct;
import com.mmuu.travel.client.widget.dialog.ScanResult0Dialog;
import com.mmuu.travel.client.widget.dialog.TwoDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预约用车
 * Created by XIXIHAHA on 2016/12/26.
 */

public class SubBookingFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {


    @BindView(R.id.title_title)
    TextView itemTitle;
    @BindView(R.id.item_book_loca)
    TextView itemBookLoca;
    @BindView(R.id.item_book_licheng_tv)
    TextView itemBookLichengTv;
    @BindView(R.id.item_book_time)
    TextView itemBookTimeTv;
    @BindView(R.id.item_book_book)
    TextView itemBookBook;
    @BindView(R.id.item_book_dis)
    TextView itemBookDis;
    @BindView(R.id.item_book_rule)
    TextView itemBookRule;

    private BikeDetailVO bikeVO;

    private BookingBikeListener bookingBikeListener;
    private A2B a2B;

    private int dis = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.item_main_checkorder, null);
            ButterKnife.bind(this, fragContentView);
            itemBookBook.setOnClickListener(this);
            itemBookRule.setOnClickListener(this);
        }
        return fragContentView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.item_book_book:
                if (bikeVO == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_main_reserve");

                CancelBookVO cancelBookVO = (CancelBookVO) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(mContext, MFStaticConstans.getUserBean().getUser().getId() + "cancelBook", ""), CancelBookVO.class);
                if (cancelBookVO != null && cancelBookVO.getDate() == TimeDateUtil.longTolong(System.currentTimeMillis()) && cancelBookVO.getCancelTimes() >= 5) {
                    MFUtil.showToast(mContext, "今天取消预约已经超过5次，无法再使用预约功能");
                    return;
                }

                checkUser();
                if (a2B != null) {
                    a2B.callB(0, null);
                }
                break;
            case R.id.item_book_rule:
                if (bikeVO == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("url", MFUrl.HOWCHARGE + "?cityCode=" + bikeVO.getCityCode());
                bundle.putString("title", "计费说明");
                startActivity(WebAty.class, bundle);
                break;
        }
    }

    public void showData(BikeDetailVO bikeVO) {
        this.bikeVO = bikeVO;
        if (bikeVO == null) {
            return;
        }
        itemTitle.setText("NO." + bikeVO.getBikeCode());
        itemBookLoca.setText(TextUtils.isEmpty(bikeVO.getLocation()) ? "地理位置获取失败" : bikeVO.getLocation());
        int dis = (int) Math.floor(Double.valueOf(bikeVO.getAnticipatedMileage()) / 1000);
        itemBookLichengTv.setText(dis + "");
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {
        switch (i) {
            case 1000:
                dialogShow();
                break;
            case 1001:
                dialogShow();
                break;
            case 1002:
                break;
        }
    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        switch (i) {
            case 1000:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean check = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (check != null) {
                    switch (check.getCode()) {
                        case 0:
                            if (bikeVO == null) {
                                dismmisDialog();
                                return;
                            }
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                            params.addBodyParameter("bikeCode", bikeVO.getBikeCode());
                            MFRunner.requestPost(2001, MFUrl.SCOREINFINENOTICE, params, this);
//                             getBikeDetail(bikeVO);
                            break;
                        case 1000:
                            MFUtil.showToast(mContext, check.getMessage());
                            break;
                        case 1003:
                            //提示message,跳转支付押金界面
                            startActivity(CarDepositAct.class, null);
                            MFUtil.showToast(mContext, check.getMessage());
                            break;
                        case 1004:
                            //提示message,跳转支付充值界面
                            startActivity(RechargeAct.class, null);
                            MFUtil.showToast(mContext, check.getMessage());
                            break;
                        case 1005:
                            //提示message,跳转到进行中订单
                            MFUtil.showToast(mContext, check.getMessage());
                            break;
                        case 1006:
                            //提示message,跳转新手引导页面
                            startActivity(UserCenterAct.class, null);
                            MFUtil.showToast(mContext, check.getMessage());
                            break;
                        case 1007:
                            //提示message,跳转认证页面
                            Bundle bundle = new Bundle();
                            bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                            startActivity(CertificationAct.class, bundle);
                            MFUtil.showToast(mContext, check.getMessage());
                            break;
                        case 1010:
                            //失信用户
                            TwoDialog dialog = new TwoDialog(mContext, check.getMessage(), "确认", "联系客服");
                            dialog.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    d.dismiss();
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    d.dismiss();
                                    if (isInServiceTime()) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                                .parse("tel:" + SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_CALLNUMBER, "13511010167")));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        mContext.overridePendingTransition(
                                                R.anim.activity_up, R.anim.activity_push_no_anim);
                                    } else {
                                        showOutServiceTimeDialog();
                                    }
                                }
                            });
                            dialog.show();
                            break;
                        default:
                            MFUtil.showToast(mContext, check.getMessage());
                            break;
                    }
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


                        if (!TextUtils.isEmpty(bikeVO.getAnticipatedMileage())) {

                            double d = Double.valueOf(bikeVO.getAnticipatedMileage());
                            int s = ((int) (d / 1000));

                            if (s == 0) {
                                ScanResult0Dialog result0Dialog = new ScanResult0Dialog(mContext, "此小蜜蜂电量不足，请更换其它小蜜蜂", bikeVO.getAnticipatedMileage(), "确定");
                                result0Dialog.setDialogClickListener(new DialogClickListener() {
                                    @Override
                                    public void onLeftClick(View v, Dialog d) {
                                        ((ScanResult0Dialog) d).setTag(new Object());
                                        d.dismiss();
                                    }

                                    @Override
                                    public void onRightClick(View v, Dialog d) {
                                        ((ScanResult0Dialog) d).setTag(new Object());
                                        d.dismiss();
                                    }
                                });
                                result0Dialog.show();
                                return;
                            }
                        }
                        // bikeVO.getButtery
                        if (!TextUtils.isEmpty(bikeVO.getGpsX())) {
                            if (true || (TextUtils.isEmpty(bikeVO.getGpsX()) || Double.valueOf(bikeVO.getGpsX()) <= 0.5) || IMapUtils.isContains(new LatLng(Double.valueOf(bikeVO.getGpsX()), Double.valueOf(bikeVO.getGpsY())))) {
                                reserveOrOrderBike(bikeVO);
                            } else {
                                TwoDialog twoDialog = new TwoDialog(mContext, "这辆小蜜蜂已经超出运营范围，租用后骑回运营范围内还车正常收费，否则会罚款100元，是否继续预约？", "取消", "继续预约");
                                twoDialog.setTag(bikeVO);
                                twoDialog.setDialogClickListener(new DialogClickListener() {
                                    @Override
                                    public void onLeftClick(View v, Dialog d) {
                                        d.dismiss();
                                    }

                                    @Override
                                    public void onRightClick(View v, Dialog d) {
                                        reserveOrOrderBike((BikeDetailVO) ((TwoDialog) d).getTag());
                                        d.dismiss();
                                    }
                                });
                                twoDialog.show();
                            }

                        } else {
                            reserveOrOrderBike(bikeVO);
                        }


                    }
                } else {
                    MFUtil.showToast(mContext, "数据错误");
                }
                break;
            case 1002:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean booking = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (booking != null) {
                    switch (booking.getCode()) {
                        case 0:
                            if (bookingBikeListener != null) {
                                bookingBikeListener.onBookingBike(1, bikeVO);
                            }
                            break;
                        case 1003:
                            //提示message,跳转支付押金界面
                            startActivity(CarDepositAct.class, null);
                            MFUtil.showToast(mContext, booking.getMessage());
                            break;
                        case 1004:
                            //提示message,跳转支付充值界面
                            startActivity(RechargeAct.class, null);
                            MFUtil.showToast(mContext, booking.getMessage());
                            break;
                        case 1005:
                            //提示message,跳转到进行中订单
                            break;
                        case 1006:
                            //提示message,跳转新手引导页面
                            startActivity(UserCenterAct.class, null);
                            MFUtil.showToast(mContext, booking.getMessage());
                            break;
                        case 1007:
                            //提示message,跳转认证页面
                            Bundle bundle = new Bundle();
                            bundle.putInt("typeentrance", 0);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                            startActivity(CertificationAct.class, bundle);
                            MFUtil.showToast(mContext, booking.getMessage());
                            break;
                        case 1008:
                            break;
                        default:
                            MFUtil.showToast(mContext, booking.getMessage());
                            break;
                    }
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
                                        getBikeDetail(bikeVO);
                                    } else {
                                        dismmisDialog();
                                        TwoDialog nextDia = new TwoDialog(mContext, personResultBean.getMessage(), "取消", "继续租车");
                                        nextDia.setTag(nvp.getValue());
                                        nextDia.setDialogClickListener(new DialogClickListener() {
                                            @Override
                                            public void onLeftClick(View v, Dialog d) {
                                                d.dismiss();
                                            }

                                            @Override
                                            public void onRightClick(View v, Dialog d) {
                                                d.dismiss();
                                                getBikeDetail(bikeVO);
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
                            MFUtil.showToast(mContext, personResultBean.getMessage());
                            break;
                    }

                } else {
                    dismmisDialog();
                    MFUtil.showToast(mContext, "数据错误");
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
            case 1001:
                dismmisDialog();
                break;
            default:
                dismmisDialog();
                break;
        }
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    private void checkUser() {
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            startActivity(LoginAct.class, null);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("bikeCode", bikeVO.getBikeCode());
            // detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
            MFRunner.requestPost(1000, MFUrl.getMessageBeforeOrder, detailParams, this);
        }
    }


    private void getBikeDetail(BikeDetailVO bikeVO) {

        if (bikeVO == null) {
            return;
        }
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            startActivity(LoginAct.class, null);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("bikeCode", bikeVO.getBikeCode());
            //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
            MFRunner.requestPost(1001, MFUrl.getBikeDetail, detailParams, this);
        }
    }


    private void reserveOrOrderBike(BikeDetailVO bikeVO) {

        if (bikeVO == null) {
            return;
        }
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            startActivity(LoginAct.class, null);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            detailParams.addBodyParameter("bikeCode", bikeVO.getBikeCode());
            //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
            //detailParams.addBodyParameter("bikeId", bikeVO.getId());
            detailParams.addBodyParameter("addType", "1");//BY_SCAN(0, "直接扫码"),BY_INSERVE(1,"预约车辆");
            detailParams.addBodyParameter("leftMileage", bikeVO.getAnticipatedMileage());//BY_SCAN(0, "直接扫码"),BY_INSERVE(1,"预约车辆");
            if (MFApp.getInstance().getmLocation() != null) {
                detailParams.addBodyParameter("gps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
                detailParams.addBodyParameter("location", MFApp.getInstance().getmLocation().getAddress());
            }
            MFRunner.requestPost(1002, MFUrl.reserveOrOrderBike, detailParams, this);
        }

    }


    public BookingBikeListener getBookingBikeListener() {
        return bookingBikeListener;
    }

    public void setBookingBikeListener(BookingBikeListener bookingBikeListener) {
        this.bookingBikeListener = bookingBikeListener;
    }

    public void showDis(float dis, long dur) {
        this.dis = (int) dis;
        if (dis == -1) {
            itemBookDis.setText(null);
            itemBookTimeTv.setText(null);
            return;
        } else if (dis == -2) {

        } else {

        }


        StringBuffer sb = new StringBuffer();
        if (itemBookDis != null) {

            SpannableString disText = null;
            if (dis > 1000) {
                DecimalFormat df = new DecimalFormat("0.00");
                sb.append(df.format(dis / 1000));

                sb.append("公里");
                disText = new SpannableString(sb.toString());
                disText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle11), 0, sb.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                disText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle10), sb.length() - 2, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {
                if (dis != -2) {
                    sb.append((int) dis);
                } else {
                    sb.append("-");
                }
                sb.append("米");
                disText = new SpannableString(sb.toString());
                disText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle11), 0, sb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                disText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle10), sb.length() - 1, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            itemBookDis.setText(disText, TextView.BufferType.SPANNABLE);


            sb.delete(0, sb.length());
//            sb.append(",步行约");
            if (dur > 3600) {
                sb.append(dur / 3600).append("小时").append((int) Math.ceil((dur % 3600) * 1.0 / 60)).append("分钟");
            } else {
                if (dur != -2) {
                    sb.append((int) Math.ceil((dur % 3600) * 1.0 / 60)).append("分钟");
                } else {
                    sb.append("-").append("分钟");
                }
            }


            SpannableString styledText = new SpannableString(sb.toString());
            int i = sb.toString().indexOf("小时");
            int p = sb.toString().indexOf("分钟");
            if (i > 0) {
                styledText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle11), 0, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                styledText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle10), i, i + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (p > 0) {
                    styledText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle11), i + 2, p, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle10), p, p + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                styledText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle11), 0, p, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                styledText.setSpan(new TextAppearanceSpan(mContext, R.style.timesstyle10), p, p + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            itemBookTimeTv.setText(styledText, TextView.BufferType.SPANNABLE);
        }
    }

    public void setA2B(A2B a2B) {
        this.a2B = a2B;
    }


    public int getDis() {
        return dis;
    }
}
