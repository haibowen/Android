package com.mmuu.travel.client.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.FeelBackBean;
import com.mmuu.travel.client.bean.MyRunsBean;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.RequestResultListBean;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFOptions;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PhotoUtil;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.other.ScanAct;
import com.mmuu.travel.client.ui.user.adapter.BikeProblemAdp;
import com.mmuu.travel.client.widget.FeedBackSubView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行程反馈、故障上报、车损上报
 * Created by HuangYuan on 2016/12/19.
 */

public class TripFeedbackDetailFrg extends MFBaseFragment implements View.OnClickListener, BikeProblemAdp.OnCheckedProblemOption, TextWatcher, PublicRequestInterface {

    @BindView(R.id.title_left_text)
    TextView titleLeftText;
    @BindView(R.id.title_title)
    TextView titleTitle;

    @BindView(R.id.feedback_submit)
    TextView feedbackSubmit;
    @BindView(R.id.bike_problem_photo_one)
    ImageView bikeProblemPhotoOne;
    @BindView(R.id.bike_problem_photo_two)
    ImageView bikeProblemPhotoTwo;
    @BindView(R.id.bike_problem_photo_three)
    ImageView bikeProblemPhotoThree;
    @BindView(R.id.bike_problem_photo_four)
    ImageView bikeProblemPhotoFour;
    @BindView(R.id.bike_problem_remark)
    EditText bikeProblemRemark;
    @BindView(R.id.get_bike_number)
    LinearLayout getBikeNumber;
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.scrollview)
    ScrollView scrollView;
    @BindView(R.id.scan_image)
    ImageView scanImage;
    @BindView(R.id.bike_number_text)
    TextView bikeNumberText;
    @BindView(R.id.remark_count)
    TextView remarkCount;
    @BindView(R.id.ll_texthint)
    LinearLayout llTexthint;
    @BindView(R.id.tv_changebikecode)
    TextView tvChangebikecode;
    @BindView(R.id.ll_takephoto)
    LinearLayout llTakephoto;

    private FeedBackSubView feedBackSubView;


    private List<FeelBackBean> problems = new ArrayList<>();
    private BikeProblemAdp adapter;
    private Map<String, Object> problemMap = new HashMap<>();

    private boolean hasBikeNumber;
    private boolean hasChooseProblem;
    private boolean hasProblemPhoto;
    private boolean hasProblemRemark;
    private static final int TAG_REPORTING = 1695701;
    private String takePictureSavePath;
    private Dialog dialog;
    private Uri imageUri;
    private static final int Tag_GETTRIPPROBLEMTERMS = 1695702;


    private int photoViewTag = 1;
    private int imgCount = 0;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private Bundle bundle = null;
    private static final int TAG_UPIMGOK = 1695703;
    private static final int TAG_UPIMGFAIL = 1695704;

    private String bikecodeOld = "";
    private String bikecodeNew = "";


    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case TAG_UPIMGFAIL:
                imgPaths.clear();
                imgCount = 0;
                dismmisDialog();
                MFUtil.showToast(mContext, "提交失败，请重试");
                break;
            case TAG_UPIMGOK:
                attempToSubmit(true);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_trip_feedback_detail, null);
            ButterKnife.bind(this, fragContentView);
            initView();
        }
        return fragContentView;
    }

    /**
     * 8为反馈故障上报
     * 2为故障上报原因
     *
     * @param type
     */
    private void initData(int type) {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("termsParentId", type + "");
//        requestParams.addBodyParameter("termsParentId", 10 + "");
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        MFRunner.requestPost(Tag_GETTRIPPROBLEMTERMS, MFUrl.GETTRIPPROBLEMTERMS, requestParams, this);

    }

    private void initView() {

        bundle = mContext.getIntent().getExtras();
        adapter = new BikeProblemAdp(mContext, problems);
        adapter.setOnCheckedProblemOption(this);
        titleLeftText.setOnClickListener(this);
        Drawable backDrawable = ContextCompat.getDrawable(mContext, R.drawable.title_leftimgblack_selector);
        backDrawable.setBounds(0, 0, backDrawable.getMinimumWidth(), backDrawable.getMinimumHeight());
        titleLeftText.setCompoundDrawables(backDrawable, null, null, null);
        titleLeftText.setCompoundDrawablePadding(20);
        titleLeftText.setTextColor(ContextCompat.getColor(mContext, R.color.black_666));
        titleLeftImage.setOnClickListener(this);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);

        bikeProblemPhotoOne.setVisibility(View.VISIBLE);
        feedbackSubmit.setOnClickListener(null);
        bikeProblemPhotoOne.setOnClickListener(this);
        getBikeNumber.setOnClickListener(this);
        bikeProblemPhotoTwo.setOnClickListener(this);
        bikeProblemPhotoThree.setOnClickListener(this);
        bikeProblemPhotoFour.setOnClickListener(this);
        feedbackSubmit.setClickable(true);
        bikeProblemRemark.addTextChangedListener(this);
        feedBackSubView = (FeedBackSubView) fragContentView.findViewById(R.id.ll_feedsubview);
        feedBackSubView.setA2B(new A2B() {
            @Override
            public int callB(int type, Object o) {
                hasChooseProblem = feedBackSubView.getCheckedFeelList() != null && feedBackSubView.getCheckedFeelList().size() > 0;
                setSubmitClickable();
                return 0;
            }
        });
        if (bundle != null && bundle.containsKey("bikeCode")) {
            bikecodeOld = bundle.getString("bikeCode");
            bikecodeNew = bikecodeOld;
            String code = "车辆编号：" + bikecodeOld;
            hasBikeNumber = true;
            bikeNumberText.setText(code);
            tvChangebikecode.setText("点击修改");
        } else {
            hasBikeNumber = false;
            bikeNumberText.setText("扫码或输入车辆编号");
            tvChangebikecode.setText("");
        }
        if (bundle == null || !bundle.containsKey("pagetype"))
            return;
        switch (bundle.get("pagetype") + "") {
            case MFConstansValue.FEELBACK_TRIPBACK:
                initIn_tripback();
                break;
            case MFConstansValue.FEELBACK_MAIN_BREAK:
                initIn_mainbreak();
                break;
            case MFConstansValue.FEELBACK_MAIN_NOTLOCK:
                initIn_mian_notlock();
                break;
            case MFConstansValue.FEELBACK_MAIN_STOPCAR:
                initInView_mainstopcar();
                break;

            case MFConstansValue.FEELBACK_BOOK_NOTLOCK:
                initIn_book_notlock();
                break;
            case MFConstansValue.FEELBACK_BOOK_BREAK:
                initIn_bookbreak();
                break;
            case MFConstansValue.FEELBACK_BOOK_NOTFINDCAR:
                initInViewBookNotfindCar();
                break;

            case MFConstansValue.FEELBACK_CURRENTORDER_NOTENDORDER:
                initInViewCurrentOrdernotendorder();
                break;
            case MFConstansValue.FEELBACK_CURRENTORDER_BREAK:
                initInViewCurrentOrderBreak();
                break;
            case MFConstansValue.FEELBACK_CURRENTORDER_COULDNOTSTART:
                initInViewCurrentNotstart();
                break;
            case MFConstansValue.FEELBACK_WEB_NOTLOCK:
                initInViewWebNotlock();
                break;
            case MFConstansValue.FEELBACK_WEB_COULDNOTSTART:
                initInViewWebNotstart();
                break;
            case MFConstansValue.FEELBACK_WEB_NOTFINDCAR:
                initInViewWebNotfindCar();
                break;
            case MFConstansValue.FEELBACK_WEB_NOTENDORDER:
                initInViewNotendOrder();
                break;
            case MFConstansValue.FEELBACK_MAIN_XUHANGBUZHUN:
                initInView_mainxuhangbuzhun();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_text:
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.get_bike_number:
                Bundle bundle = new Bundle();
                bundle.putString("TAG", "TripFeedbackDetailFrg");
                bundle.putString("Title", "获取车辆编号");
                Intent intent = new Intent(mContext, ScanAct.class);
                intent.putExtras(bundle);
                this.startActivityForResult(intent, 1001);
                break;
            case R.id.feedback_submit:
                dialogShow();
                imgCount = 0;
                imgPaths.clear();
                if (bikeProblemPhotoOne.getTag() != null) {
                    imgCount++;
                }
                if (bikeProblemPhotoTwo.getTag() != null) {
                    imgCount++;
                }
                if (bikeProblemPhotoThree.getTag() != null) {
                    imgCount++;
                }
                if (bikeProblemPhotoFour.getTag() != null) {
                    imgCount++;
                }
                if (imgCount <= 0) {
                    attempToSubmit(true);
                } else {
                    uploadImage((String) bikeProblemPhotoTwo.getTag());
                    uploadImage((String) bikeProblemPhotoThree.getTag());
                    uploadImage((String) bikeProblemPhotoFour.getTag());
                    uploadImage((String) bikeProblemPhotoOne.getTag());
                }
                break;
            case R.id.bike_problem_photo_one:
                photoViewTag = 1;
                showBottomDialog();
                break;
            case R.id.bike_problem_photo_two:
                photoViewTag = 2;
                showBottomDialog();
                break;
            case R.id.bike_problem_photo_three:
                photoViewTag = 3;
                showBottomDialog();
                break;
            case R.id.bike_problem_photo_four:
                photoViewTag = 4;
                showBottomDialog();
                break;
            case R.id.tv_callnumber:

//                Intent intentcall = new Intent(Intent.ACTION_DIAL, Uri
//                        .parse("tel:" + SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_CALLNUMBER, "13511010167")));
//                intentcall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intentcall);
//                mContext.overridePendingTransition(
//                        R.anim.activity_up, R.anim.activity_push_no_anim);
                break;
        }
    }

    private void attempToSubmit(boolean isShowDlg) {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        if (isShowDlg) {
            dialogShow();
        }
        if (!bundle.containsKey("pagetype")) {
            return;
        }
        switch (bundle.get("pagetype") + "") {
            //  type   0：未开始订单1：预约订单2：开锁后订单3：完成订单反馈
            //  父项id 1：开锁失败2：故障上报3：违停上报4：找不到车5：锁着无法结束订单6：无法启动车辆8反馈故障上报
            //TODO 预约订单 和订单中未处理
            case MFConstansValue.FEELBACK_BOOK_BREAK:
                requestBookBreak();
                break;
            case MFConstansValue.FEELBACK_BOOK_NOTFINDCAR:
                requestBookNotFindCar();
                break;
            case MFConstansValue.FEELBACK_BOOK_NOTLOCK:
                requestBookNotLock();
                break;
            case MFConstansValue.FEELBACK_CURRENTORDER_BREAK:
                requestCurrentorderBreak();
                break;
            case MFConstansValue.FEELBACK_CURRENTORDER_COULDNOTSTART:
                requestCurrentorderNotStartCar();
                break;
            case MFConstansValue.FEELBACK_CURRENTORDER_NOTENDORDER:
                requestCurrentorderNotendorder();
                break;

            case MFConstansValue.FEELBACK_TRIPBACK:
                requestTripbackData();
                break;

            case MFConstansValue.FEELBACK_MAIN_NOTLOCK:
                requestMainNotlock();
                break;
            case MFConstansValue.FEELBACK_MAIN_STOPCAR:
                requestMainStopCar();
                break;
            case MFConstansValue.FEELBACK_MAIN_BREAK:
                requestMianBreak();
                break;

            case MFConstansValue.FEELBACK_WEB_NOTLOCK:
                requestWebNotlock();
                break;
            case MFConstansValue.FEELBACK_WEB_NOTENDORDER:
                requestWebNotEndOrder();
                break;
            case MFConstansValue.FEELBACK_WEB_COULDNOTSTART:
                requestWebNotstart();
                break;
            case MFConstansValue.FEELBACK_WEB_NOTFINDCAR:
                requestWebNotFindCar();
                break;
            case MFConstansValue.FEELBACK_MAIN_XUHANGBUZHUN:
                requestBuZhun();
                break;
        }
    }

    private void requestMianBreak() {
        if (hasBikeNumber) {
            StringBuffer sb = new StringBuffer();
//            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }

            if (feedBackSubView.getVisibility() == View.VISIBLE && feedBackSubView.getCheckedFeelList() != null) {
                for (FeelBackBean b : feedBackSubView.getCheckedFeelList()) {
                    sb.append(b.getId() + ",");
                }
            }

            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "2");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestMainStopCar() {
        if (hasBikeNumber) {
            StringBuffer sb = new StringBuffer();
            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "3");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestWebNotFindCar() {
        if (hasBikeNumber) {
            StringBuffer sb = new StringBuffer();
            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "4");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestWebNotstart() {
        if (hasBikeNumber) {
            StringBuffer sb = new StringBuffer();
            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "6");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestWebNotEndOrder() {
        if (hasBikeNumber) {
            StringBuffer sb = new StringBuffer();
            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "5");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestWebNotlock() {
        if (hasBikeNumber) {
            StringBuffer sb = new StringBuffer();
            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "1");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestCurrentorderBreak() {
        if (bundle.containsKey("bikeCode") && bundle.containsKey("orderId")) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
                requestParams.addBodyParameter("orderId", bundle.get("orderId") + "");
                requestParams.addBodyParameter("type", "2");//上传入口
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
                requestParams.addBodyParameter("orderId", "");
                requestParams.addBodyParameter("type", "0");//上传入口
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "2");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    /**
     * 续航不准
     */
    private void requestBuZhun() {
        if (hasBikeNumber) {
            StringBuffer sb = new StringBuffer();
            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "11");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }


    private void requestBookBreak() {
        if (bundle.containsKey("bikeCode") && bundle.containsKey("orderId")) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
                requestParams.addBodyParameter("orderId", bundle.get("orderId") + "");
                requestParams.addBodyParameter("type", "1");//上传入口
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
                requestParams.addBodyParameter("orderId", "");
                requestParams.addBodyParameter("type", "0");//上传入口
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "2");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestBookNotFindCar() {
        if (bundle.containsKey("bikeCode") && bundle.containsKey("orderId")) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
                requestParams.addBodyParameter("orderId", bundle.get("orderId") + "");
                requestParams.addBodyParameter("type", "1");//上传入口
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
                requestParams.addBodyParameter("orderId", "");
                requestParams.addBodyParameter("type", "0");//上传入口
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "4");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestBookNotLock() {
        if (bundle.containsKey("bikeCode") && bundle.containsKey("orderId")) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
                requestParams.addBodyParameter("orderId", bundle.get("orderId") + "");
                requestParams.addBodyParameter("type", "1");//上传入口
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
                requestParams.addBodyParameter("orderId", "");
                requestParams.addBodyParameter("type", "0");//上传入口
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "1");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestCurrentorderNotStartCar() {
        if (bundle.containsKey("bikeCode") && bundle.containsKey("orderId")) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
                requestParams.addBodyParameter("orderId", bundle.get("orderId") + "");
                requestParams.addBodyParameter("type", "2");//上传入口
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
                requestParams.addBodyParameter("orderId", "");
                requestParams.addBodyParameter("type", "0");//上传入口
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "6");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestCurrentorderNotendorder() {
        if (bundle.containsKey("bikeCode") && bundle.containsKey("orderId")) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
                requestParams.addBodyParameter("orderId", bundle.get("orderId") + "");
                requestParams.addBodyParameter("type", "2");//上传入口
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
                requestParams.addBodyParameter("orderId", "");
                requestParams.addBodyParameter("type", "0");//上传入口
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "5");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestMainNotlock() {
        if (hasBikeNumber) {
            StringBuffer sbmain_notlock = new StringBuffer();
            problemMap.clear();
//                    problemMap.put("1", )
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sbmain_notlock.append(entry.getKey() + ",");
            }
            String items = sbmain_notlock.toString().length() <= 0 ? "" : sbmain_notlock.toString().substring(0, sbmain_notlock.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
            }
            requestParams.addBodyParameter("type", "0");//上传入口
            requestParams.addBodyParameter("orderId", "");
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "1");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    private void requestTripbackData() {
        if (bundle.containsKey("bikeCode") && bundle.containsKey("orderId")) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : problemMap.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
            String items = sb.toString().length() <= 0 ? "" : sb.toString().substring(0, sb.toString().length() - 1);
            RequestParams requestParams = new RequestParams();
            if (bikecodeNew == bikecodeOld) {//如果重新扫码与传进来的code相同则传入订单id
                requestParams.addBodyParameter("bikeCode", bikecodeOld);
                requestParams.addBodyParameter("orderId", bundle.get("orderId") + "");
                requestParams.addBodyParameter("type", "3");//上传入口
            } else {
                requestParams.addBodyParameter("bikeCode", bikecodeNew);
                requestParams.addBodyParameter("orderId", "");
                requestParams.addBodyParameter("type", "0");//上传入口
            }
            requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            requestParams.addBodyParameter("parentItemId", "8");//父id唯一
            requestParams.addBodyParameter("items", items);//子id分号
            requestParams.addBodyParameter("content", bikeProblemRemark.getText() + "");
            requestParams.addBodyParameter("images", listToString(imgPaths));
            MFRunner.requestPost(TAG_REPORTING, MFUrl.REPORTING, requestParams, this);
        }
    }

    @Override
    public void checkCount(Map<String, Object> checkedMap) {
        problemMap = checkedMap;

        hasChooseProblem = problemMap.size() > 0;

        setSubmitClickable();

    }

    private void setSubmitClickable() {
        if ((bundle != null && bundle.containsKey("pagetype") &&
                MFConstansValue.FEELBACK_MAIN_STOPCAR.equals(bundle.get("pagetype") + ""))) {
            if (hasBikeNumber && hasProblemPhoto) {
                feedbackSubmit.setClickable(true);
                feedbackSubmit.setOnClickListener(this);
                feedbackSubmit.setBackgroundResource(R.drawable.arc_orange_bg);
            } else {
                feedbackSubmit.setOnClickListener(null);
                feedbackSubmit.setClickable(false);
                feedbackSubmit.setBackgroundResource(R.drawable.arc_grey_bg);
            }
            return;
        }

        if (hasBikeNumber && (hasChooseProblem || hasProblemPhoto || hasProblemRemark)) {
            feedbackSubmit.setClickable(true);
            feedbackSubmit.setOnClickListener(this);
            feedbackSubmit.setBackgroundResource(R.drawable.arc_orange_bg);
        } else {
            feedbackSubmit.setOnClickListener(null);
            feedbackSubmit.setClickable(false);
            feedbackSubmit.setBackgroundResource(R.drawable.arc_grey_bg);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    if (data != null) {
                        imageUri = data.getData();
                        if (Build.VERSION.SDK_INT >= 19) {
                            imageUri = Uri.fromFile(new File(PhotoUtil.getPath(
                                    mContext, imageUri)));
                        }
                        switch (photoViewTag) {
                            case 1:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoOne, MFOptions.OPTION_DEF);
                                bikeProblemPhotoOne.setTag(imageUri.getPath());
                                bikeProblemPhotoTwo.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoTwo, MFOptions.OPTION_DEF);
                                bikeProblemPhotoTwo.setTag(imageUri.getPath());
                                bikeProblemPhotoThree.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoThree, MFOptions.OPTION_DEF);
                                bikeProblemPhotoThree.setTag(imageUri.getPath());
                                bikeProblemPhotoFour.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoFour, MFOptions.OPTION_DEF);
                                bikeProblemPhotoFour.setTag(imageUri.getPath());
                                break;
                        }
//                        imageUri = startPhotoZoom(imageUri);
                    }

                    break;
                // 如果是调用相机拍照时
                case 2:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        switch (photoViewTag) {
                            case 1:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoOne, MFOptions.OPTION_DEF);
                                bikeProblemPhotoOne.setTag(imageUri.getPath());
                                bikeProblemPhotoTwo.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoTwo, MFOptions.OPTION_DEF);
                                bikeProblemPhotoTwo.setTag(imageUri.getPath());
                                bikeProblemPhotoThree.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoThree, MFOptions.OPTION_DEF);
                                bikeProblemPhotoThree.setTag(imageUri.getPath());
                                bikeProblemPhotoFour.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoFour, MFOptions.OPTION_DEF);
                                bikeProblemPhotoFour.setTag(imageUri.getPath());
                                break;
                        }
//                        imageUri = startPhotoZoom(imageUri);
                    }
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        if (bitmap != null) {
                            switch (photoViewTag) {
                                case 1:
                                    bikeProblemPhotoOne.setImageBitmap(bitmap);
                                    bikeProblemPhotoOne.setTag(bitmap);
                                    bikeProblemPhotoTwo.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    bikeProblemPhotoTwo.setImageBitmap(bitmap);
                                    bikeProblemPhotoTwo.setTag(bitmap);
                                    bikeProblemPhotoThree.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    bikeProblemPhotoThree.setImageBitmap(bitmap);
                                    bikeProblemPhotoThree.setTag(bitmap);
                                    bikeProblemPhotoFour.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    bikeProblemPhotoFour.setImageBitmap(bitmap);
                                    bikeProblemPhotoFour.setTag(bitmap);
                                    break;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        if (bikeProblemPhotoOne.getTag() != null || bikeProblemPhotoTwo.getTag() != null || bikeProblemPhotoThree.getTag() != null || bikeProblemPhotoFour.getTag() != null) {
            hasProblemPhoto = true;
            setSubmitClickable();
        }
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                bikecodeNew = data.getStringExtra("BikeCode");
                if (TextUtils.isEmpty(bikecodeNew)) {
                    bikecodeNew = bikecodeOld;
                    return;
                }
                hasBikeNumber = true;
                tvChangebikecode.setText("点击修改");
                String code = "车辆编号：" + bikecodeNew;
                bikeNumberText.setText(code == null ? mContext.getResources().getString(R.string.scan_or_input_bike_number) : code);
                setSubmitClickable();
            }
        }
    }

    private void uploadImage(String path) {
        if (path != null && !TextUtils.isEmpty(path)) {
//            ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
//            bitmap.recycle();//自由选择是否进行回收
//            byte[] result = output.toByteArray();//转换成功了
//            try {
//                output.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            final String uploadName = MFApp.mifengOSSDirectory + "reportPic" + "/" +
                    TimeDateUtil.longToDate(System.currentTimeMillis(), "yyyy/MM/dd") + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = MFConstansValue.OSS_BASEURL + uploadName;
            imgPaths.add(uploadUrl);
            PutObjectRequest putObjectRequest = new PutObjectRequest(MFConstansValue.OSS_BUCKETNAME, uploadName, path);
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {

                }
            });

            OSSAsyncTask asyncTask = MFApp.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    imgCount--;
                    if (imgCount == 0) {
                        getHandler().sendEmptyMessage(TAG_UPIMGOK);
                    }
                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                    getHandler().sendEmptyMessage(TAG_UPIMGFAIL);
                }
            });
        }
    }

    // 输入表情前EditText中的文本
    private String inputAfterText;
    // 是否重置了EditText的内容
    private boolean resetText;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!resetText) {
// 这里用s.toString()而不直接用s是因为如果用s，
// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
// inputAfterText也就改变了，那么表情过滤就失败了
            inputAfterText = s.toString();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!resetText) {
            if (count >= 2) {// 表情符号的字符长度最小为2
//                CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                CharSequence input = s.subSequence(start, start + count <= s.length() ? start + count : s.length());
                if (containsEmoji(input.toString())) {
                    resetText = true;
                    Toast.makeText(mContext, "不支持输入表情符号！", Toast.LENGTH_SHORT).show();
                    // 是表情符号就将文本还原为输入表情符号之前的内容
                    bikeProblemRemark.setText(inputAfterText);
                    CharSequence text = bikeProblemRemark.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                }
            }
        } else {
            resetText = false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        remarkCount.setText(s.toString().length() + "/140字");
        hasProblemRemark = s.toString().length() > 0;
        setSubmitClickable();
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 头像裁剪 图片方法实现
     *
     * @param uriFrom
     */
    public Uri startPhotoZoom(Uri uriFrom) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
        return uriFrom;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
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
            case TAG_REPORTING:
                RequestResultBean<MyRunsBean> myRunsBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<MyRunsBean>>() {
                }.getType());
                if (myRunsBeanRequestResultBean != null && myRunsBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    MFUtil.showToast(mContext, "反馈成功");
                    mContext.finish();
                } else if (myRunsBeanRequestResultBean != null && MFConstansValue.BACK_LOGOUT == myRunsBeanRequestResultBean.getCode()) {
                    MFUtil.showToast(mContext, myRunsBeanRequestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (myRunsBeanRequestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == myRunsBeanRequestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == myRunsBeanRequestResultBean.getCode())) {
                    MFUtil.showToast(mContext, myRunsBeanRequestResultBean.getMessage());
                }
                break;
            case Tag_GETTRIPPROBLEMTERMS:
                RequestResultListBean<FeelBackBean> resultBean = GsonTransformUtil.getObjectListFromJson(responseInfo.result.toString(), new TypeToken<RequestResultListBean<FeelBackBean>>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                    List<FeelBackBean> feelBackBeanList = resultBean.getData();
                    adapter.replaceData(feelBackBeanList);
                    adapter.notifyDataSetChanged();

                    int padd = ScreenUtil.dip2px(mContext, 15);
                    feedBackSubView.setPaddingWith(padd);
                    int with = (ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dip2px(mContext, 15) * 5) / 4;
                    feedBackSubView.setItemWith(with);
                    feedBackSubView.setItemHeight(ScreenUtil.dip2px(mContext, 29));
                    feedBackSubView.reInitLayout();
                    feedBackSubView.setFeelBeanList((ArrayList<FeelBackBean>) feelBackBeanList);
                } else if (resultBean != null && MFConstansValue.BACK_LOGOUT == resultBean.getCode()) {
                    MFUtil.showToast(mContext, resultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (resultBean != null && (MFConstansValue.BACK_SYSTEMERROR == resultBean.getCode() || MFConstansValue.BACK_BUSINESS == resultBean.getCode())) {
                    MFUtil.showToast(mContext, resultBean.getMessage());
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

    private void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_userfrg, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);
            TextView tv_camera_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_camera_txt);
            TextView tv_album_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_album_txt);
            TextView tv_exit_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_exit_txt);
            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.cancel_txt);
            tv_camera_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MFUtil.checkNetwork(mContext)) {
                        showNoNetworkDialog(true);
                        return;
                    }
                    if (!TextUtils.isEmpty(MFApp.getMiFengLocalForderPath())) {
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        String timeStamp = new SimpleDateFormat(
                                "yyyyMMdd_HHmmss").format(new Date());
                        File dirFil = new File(MFApp.getMiFengLocalForderPath()
                                + "imageCach/");
                        if (!dirFil.exists()) {
                            dirFil.mkdirs();
                        }
                        File makeFile = new File(MFApp.getMiFengLocalForderPath()
                                + "imageCach/", "xiaojia_"
                                + timeStamp + ".jpeg");
                        takePictureSavePath = makeFile.getAbsolutePath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(makeFile));
                        TripFeedbackDetailFrg.this.startActivityForResult(intent, 2);
                    } else {
                        MFUtil.showToast(mContext, "！存储设备部不可用");
                    }
                    dialog.dismiss();
                }
            });
            tv_album_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MFUtil.checkNetwork(mContext)) {
                        showNoNetworkDialog(true);
                        return;
                    }
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    TripFeedbackDetailFrg.this.startActivityForResult(intent, 1);
                    dialog.dismiss();

                }
            });
            tv_album_txt.setVisibility(View.GONE);
            tv_camera_txt.setBackgroundResource(R.drawable.actionsheet_single_selector);
            tv_exit_txt.setVisibility(View.GONE);
            cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;

            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgCallView);
        }
        dialog.show();
    }

    /**
     * @Description:把list转换为一个用逗号分隔的字符串
     */
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    private void initInViewNotendOrder() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_notendorder);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View viewnotendorder = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notendorder, null);
        TextView tv_callnumber = (TextView) viewnotendorder.findViewById(R.id.tv_callnumber);
        tv_callnumber.setOnClickListener(this);
        llTexthint.removeAllViews();
        llTexthint.addView(viewnotendorder);
    }

    private void initInViewWebNotfindCar() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_notfindcar);
        llTakephoto.setVisibility(View.GONE);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View viewnotfindcar = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notfindcar, null);
        llTexthint.removeAllViews();
        llTexthint.addView(viewnotfindcar);
    }

    private void initInViewBookNotfindCar() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_notfindcar);
        llTakephoto.setVisibility(View.GONE);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View viewnotfindcar = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notfindcar, null);
        llTexthint.removeAllViews();
        llTexthint.addView(viewnotfindcar);
    }

    private void initInViewWebNotstart() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_notstart);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View viewnotstartcar = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notstartcar, null);
        llTexthint.removeAllViews();
        llTexthint.addView(viewnotstartcar);
    }

    private void initInViewWebNotlock() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_unlock);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View viewwebnotlock = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notlock, null);
        llTexthint.removeAllViews();
        llTexthint.addView(viewwebnotlock);
    }

    private void initInViewCurrentNotstart() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_notstart);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View view_currentorder_notstartcar = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notstartcar, null);
        llTexthint.removeAllViews();
        llTexthint.addView(view_currentorder_notstartcar);
    }

    private void initInViewCurrentOrderBreak() {
        initData(10);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.journey_feedback);
        feedBackSubView.setVisibility(View.VISIBLE);
        getBikeNumber.setVisibility(View.VISIBLE);
    }

    private void initInViewCurrentOrdernotendorder() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_notendorder);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View viewcurrentorder_notendorder = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notendorder, null);
        TextView tv_callnumber = (TextView) viewcurrentorder_notendorder.findViewById(R.id.tv_callnumber);
        tv_callnumber.setOnClickListener(this);
        llTexthint.removeAllViews();
        llTexthint.addView(viewcurrentorder_notendorder);
    }

    private void initInView_mainstopcar() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_stopwrong);
        getBikeNumber.setVisibility(View.VISIBLE);
    }

    private void initInView_mainxuhangbuzhun() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_xuhangbuzhun);
        getBikeNumber.setVisibility(View.VISIBLE);
    }

    private void initIn_mian_notlock() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_unlock);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notlock, null);
        llTexthint.removeAllViews();
        llTexthint.addView(view);
    }

    private void initIn_book_notlock() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.bike_problem_unlock);
        getBikeNumber.setVisibility(View.VISIBLE);
        llTexthint.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_problem_notlock, null);
        llTexthint.removeAllViews();
        llTexthint.addView(view);
    }

    private void initIn_mainbreak() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.fault_submit);
        feedBackSubView.setVisibility(View.VISIBLE);
        initData(10);
    }

    private void initIn_bookbreak() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        titleTitle.setText(R.string.fault_submit);
        feedBackSubView.setVisibility(View.VISIBLE);
        initData(10);
    }

    private void initIn_tripback() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftText.setVisibility(View.GONE);
        feedBackSubView.setVisibility(View.VISIBLE);
//        titleLeftText.setText(R.string.modify_choose_order);
        titleTitle.setText(R.string.journey_feedback);
        initData(10);
    }
}
