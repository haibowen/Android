package com.mmuu.travel.client.ui.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
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
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFOptions;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PhotoUtil;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 车辆丢失dialog
 * Created by XIXIHAHA on 2017/7/12.
 */

public class BikeReportFrg extends MFBaseFragment implements View.OnClickListener, TextWatcher, PublicRequestInterface {


    @BindView(R.id.dia_report_close)
    ImageView diaReportClose;
    @BindView(R.id.dia_report_temp_lable0)
    TextView diaReportTempLable0;
//    @BindView(R.id.dia_report_temp_lable1)
//    TextView diaReportTempLable1;
    @BindView(R.id.dia_report_report)
    TextView diaReportReport;
    @BindView(R.id.bike_problem_photo_one)
    ImageView bikeProblemPhotoOne;
    @BindView(R.id.bike_problem_photo_two)
    ImageView bikeProblemPhotoTwo;
    @BindView(R.id.bike_problem_photo_three)
    ImageView bikeProblemPhotoThree;
    @BindView(R.id.bike_problem_photo_four)
    ImageView bikeProblemPhotoFour;
    @BindView(R.id.bike_problem_text_one)
    TextView bikeProblemlable1;
    @BindView(R.id.bike_problem_text_two)
    TextView bikeProblemlable2;
    @BindView(R.id.bike_problem_text_three)
    TextView bikeProblemlable3;
    @BindView(R.id.bike_problem_text_four)
    TextView bikeProblemlable4;
    @BindView(R.id.dia_report_message_edit)
    EditText diaReportMessageEdit;
    @BindView(R.id.remark_count)
    TextView remarkCount;
    @BindView(R.id.ll_dialog)
    LinearLayout llDialog;
    @BindView(R.id.v_topbg)
    View vTopbg;
    Unbinder unbinder;
    private boolean hasProblemRemark;
    private boolean hasProblemPhoto;
    private int imgCount = 0;
    private int photoViewTag = 1;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private static final int TAG_UPIMGOK = 1695703;
    private static final int TAG_UPIMGFAIL = 1695704;
    private Dialog dialog;
    private String takePictureSavePath;
    private Uri imageUri;

    private A2B callAct;


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
                attempToSubmit(true);//提交数据
                break;
        }
    }


    private String bikeCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.dialog_reportbiek, null);
            // 设置为全屏状态
//            mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            unbinder = ButterKnife.bind(this, fragContentView);
            initView();
        }
        return fragContentView;
    }


    private void initView() {
        bikeProblemPhotoOne.setVisibility(View.VISIBLE);
        bikeProblemPhotoOne.setOnClickListener(this);
        bikeProblemPhotoTwo.setOnClickListener(this);
        bikeProblemPhotoThree.setOnClickListener(this);
        bikeProblemPhotoFour.setOnClickListener(this);
        diaReportClose.setOnClickListener(this);
        vTopbg.setOnClickListener(this);
//        llDialog.setClickable(false);
//        llDialog.setOnClickListener(this);
        diaReportMessageEdit.addTextChangedListener(this);
        diaReportReport.setOnClickListener(this);
        bikeProblemlable1.setVisibility(View.VISIBLE);
    }


    private void reSetImage() {
        if (imgPaths != null) {
            imgPaths.clear();
        }
        bikeProblemPhotoOne.setVisibility(View.VISIBLE);
        bikeProblemPhotoOne.setImageResource(R.drawable.feelback_commit_selector);
        bikeProblemPhotoOne.setTag(null);
        bikeProblemPhotoTwo.setVisibility(View.GONE);
        bikeProblemPhotoTwo.setImageResource(R.drawable.feelback_commit_selector);
        bikeProblemPhotoTwo.setTag(null);
        bikeProblemPhotoThree.setVisibility(View.GONE);
        bikeProblemPhotoThree.setImageResource(R.drawable.feelback_commit_selector);
        bikeProblemPhotoThree.setTag(null);
        bikeProblemPhotoFour.setVisibility(View.GONE);
        bikeProblemPhotoFour.setImageResource(R.drawable.feelback_commit_selector);
        bikeProblemPhotoFour.setTag(null);

        bikeProblemlable1.setVisibility(View.VISIBLE);
        bikeProblemlable2.setVisibility(View.INVISIBLE);
        bikeProblemlable3.setVisibility(View.INVISIBLE);
        bikeProblemlable4.setVisibility(View.INVISIBLE);

        diaReportMessageEdit.setText(null);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dia_report_report:
                if (!MFUtil.checkNetwork(mContext)) {
                    showNoNetworkDialog(true);
                    return;
                }
                if (diaReportMessageEdit != null && TextUtils.isEmpty(diaReportMessageEdit.getText().toString())) {
                    MFUtil.showToast(mContext, "还没有告诉我们小蜜蜂的具体位置哦");
                    return;
                }
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
                dialogShow(true);
                if (imgCount <= 0) {
                    attempToSubmit(true);   //提交数据
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
            case R.id.dia_report_close:
            case R.id.v_topbg:
                if (!MFUtil.checkNetwork(mContext)) {
                    if (callAct != null) {
                        reSetImage();
                        callAct.callB(3, bikeCode);
                    }
                    return;
                }
                attempToSubmit(false);
                break;
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
                    diaReportMessageEdit.setText(inputAfterText);
                    CharSequence text = diaReportMessageEdit.getText();
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
//        setSubmitClickable();
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


    private void uploadImage(String path) {
        if (path != null && !TextUtils.isEmpty(path)) {
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
                    if (!isAdded()) {
                        return;
                    }
                    imgCount--;
                    if (imgCount == 0) {
                        getHandler().sendEmptyMessage(TAG_UPIMGOK);
                    }
                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                    if (!isAdded()) {
                        return;
                    }
                    dismmisDialog();
                    getHandler().sendEmptyMessage(TAG_UPIMGFAIL);
                }
            });
        }
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
                        BikeReportFrg.this.startActivityForResult(intent, 2);
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
                    BikeReportFrg.this.startActivityForResult(intent, 1);
                    dialog.dismiss();

                }
            });
            tv_album_txt.setVisibility(View.GONE);
            tv_album_txt.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
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
                                bikeProblemlable1.setVisibility(View.INVISIBLE);
                                bikeProblemlable2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoTwo, MFOptions.OPTION_DEF);
                                bikeProblemPhotoTwo.setTag(imageUri.getPath());
                                bikeProblemPhotoThree.setVisibility(View.VISIBLE);
                                bikeProblemlable2.setVisibility(View.INVISIBLE);
                                bikeProblemlable3.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoThree, MFOptions.OPTION_DEF);
                                bikeProblemPhotoThree.setTag(imageUri.getPath());
                                bikeProblemPhotoFour.setVisibility(View.VISIBLE);
                                bikeProblemlable1.setVisibility(View.INVISIBLE);
                                bikeProblemlable3.setVisibility(View.INVISIBLE);
                                bikeProblemlable4.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoFour, MFOptions.OPTION_DEF);
                                bikeProblemPhotoFour.setTag(imageUri.getPath());
                                bikeProblemlable1.setVisibility(View.INVISIBLE);
                                bikeProblemlable4.setVisibility(View.INVISIBLE);
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
                                bikeProblemlable1.setVisibility(View.INVISIBLE);
                                bikeProblemlable2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoTwo, MFOptions.OPTION_DEF);
                                bikeProblemPhotoTwo.setTag(imageUri.getPath());
                                bikeProblemPhotoThree.setVisibility(View.VISIBLE);
                                bikeProblemlable2.setVisibility(View.INVISIBLE);
                                bikeProblemlable3.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoThree, MFOptions.OPTION_DEF);
                                bikeProblemPhotoThree.setTag(imageUri.getPath());
                                bikeProblemPhotoFour.setVisibility(View.VISIBLE);
                                bikeProblemlable1.setVisibility(View.INVISIBLE);
                                bikeProblemlable3.setVisibility(View.INVISIBLE);
                                bikeProblemlable4.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                ImageLoader.getInstance().displayImage("file://" + imageUri.getPath(), bikeProblemPhotoFour, MFOptions.OPTION_DEF);
                                bikeProblemPhotoFour.setTag(imageUri.getPath());
                                bikeProblemlable1.setVisibility(View.INVISIBLE);
                                bikeProblemlable4.setVisibility(View.INVISIBLE);
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
//        if (bikeProblemPhotoOne.getTag() != null || bikeProblemPhotoTwo.getTag() != null || bikeProblemPhotoThree.getTag() != null || bikeProblemPhotoFour.getTag() != null) {
//            hasProblemPhoto = true;
//            setSubmitClickable();
//        }
    }

    public String getBikeCode() {
        return bikeCode;
    }

    public void setBikeCode(String bikeCode) {
        this.bikeCode = bikeCode;
    }

    public A2B getCallAct() {
        return callAct;
    }

    public void setCallAct(A2B callAct) {
        this.callAct = callAct;
    }


    private void attempToSubmit(boolean b) {

        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        requestParams.addBodyParameter("bikeCode", bikeCode);
        if (MFApp.getInstance().getmLocation() != null) {
            requestParams.addBodyParameter("userGps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
            requestParams.addBodyParameter("location", MFApp.getInstance().getmLocation().getAddress());
        }
        requestParams.addBodyParameter("type", "0");//父id唯一
        requestParams.addBodyParameter("parentItemId", "9");//父id唯一
        requestParams.addBodyParameter("content", diaReportMessageEdit.getText() + "");
        requestParams.addBodyParameter("images", listToString(imgPaths));
        if (b) {
            MFRunner.requestPost(1001, MFUrl.REPORTING, requestParams, this);
        } else {
            MFRunner.requestPost(2001, MFUrl.REPORTING, requestParams, this);
        }
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
            case 1001:
                RequestResultBean reportBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean>() {
                }.getType());
                if (reportBean != null && reportBean.getCode() == 0) {
                    MFUtil.showToast(mContext, "提交成功");
                    if (callAct != null) {
                        reSetImage();
                        callAct.callB(3, bikeCode);
                    }
                } else if (reportBean != null) {
                    MFUtil.showToast(mContext, reportBean.getMessage());
                } else {
                    MFUtil.showToast(mContext, "提交失败，请重试");
                }
                break;
            case 2001:
                if (callAct != null) {
                    reSetImage();
                    callAct.callB(3, bikeCode);
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {
        if (!isAdded()) {
            return;
        }
        switch (tag) {
            case 1001:
                MFUtil.showToast(mContext, "提交失败，请重试");
                dismmisDialog();
                break;
            case 2001:
                dismmisDialog();
                if (callAct != null) {
                    reSetImage();
                    callAct.callB(3, bikeCode);
                }
                break;
        }

    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }


}
