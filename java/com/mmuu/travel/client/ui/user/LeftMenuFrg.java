package com.mmuu.travel.client.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.mmuu.travel.client.bean.ADBean;
import com.mmuu.travel.client.bean.MFBaseResBean;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.bean.user.UserVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFOptions;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PhotoUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.other.AdviseAct;
import com.mmuu.travel.client.ui.other.SetAct;
import com.mmuu.travel.client.ui.other.WebAty;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XIXIHAHA on 2017/12/6.
 * <p>
 * 左侧菜单栏
 */
public class LeftMenuFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, DrawerLayout.DrawerListener {

    @BindView(R.id.left_menu_context_layout)
    RelativeLayout leftMenuContextLayout;
    @BindView(R.id.left_menu_userphone)
    TextView userphone;
    @BindView(R.id.left_menu_userimage)
    ImageView userimage;
    @BindView(R.id.left_menu_userjifeng)
    TextView userjifeng;
    @BindView(R.id.left_menu_userrenzheng)
    TextView userrenzheng;
    @BindView(R.id.left_menu_userinfo)
    View userinfo;
    @BindView(R.id.left_menu_new_tag)
    TextView newTag;
    @BindView(R.id.left_menu_layout1)
    RelativeLayout leftMenuLayout1;
    @BindView(R.id.left_menu_layout2)
    RelativeLayout leftMenuLayout2;
    @BindView(R.id.left_menu_layout3)
    RelativeLayout leftMenuLayout3;
    @BindView(R.id.left_menu_layout4)
    RelativeLayout leftMenuLayout4;
    @BindView(R.id.left_menu_layout6)
    LinearLayout leftMenuLayout6;
    @BindView(R.id.left_menu_layout7)
    LinearLayout leftMenuLayout7;
    @BindView(R.id.left_menu_ad)
    ImageView leftMenuAd;
    @BindView(R.id.left_menu_lable10)
    TextView leftMenuLable10;
    @BindView(R.id.left_menu_lable11)
    TextView leftMenuLable11;
    @BindView(R.id.left_menu_lable20)
    TextView leftMenuLable20;
    @BindView(R.id.left_menu_lable21)
    TextView leftMenuLable21;
    @BindView(R.id.left_menu_lable30)
    TextView leftMenuLable30;
    @BindView(R.id.left_menu_lable4)
    TextView left_menu_lable4;

    private boolean isfirst = false;
    private PersonalCenterVo personalCenterVo;
    private ADBean adBean;

    private Dialog dialog;
    private int ivPhotoBgheight;
    private String takePictureSavePath;
    private Uri imageUri;


    private A2B left2Act;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_left_menu, null);
            ButterKnife.bind(this, fragContentView);
            initView();
            isfirst = true;

        }
        return fragContentView;
    }


    private void initView() {

        leftMenuContextLayout.setOnClickListener(this);
        userimage.setOnClickListener(this);
        userjifeng.setOnClickListener(this);
        userrenzheng.setOnClickListener(this);
        leftMenuLayout1.setOnClickListener(this);
        leftMenuLayout2.setOnClickListener(this);
        leftMenuLayout3.setOnClickListener(this);
        leftMenuLayout4.setOnClickListener(this);
        leftMenuLayout6.setOnClickListener(this);
        leftMenuLayout7.setOnClickListener(this);
        leftMenuAd.setOnClickListener(this);
        getBaseHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getADData();
            }
        }, 300);

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
        if (MFStaticConstans.needRefUserInfo) {
            MFStaticConstans.needRefUserInfo = false;
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_menu_userimage:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    showBottomDialog();
                }
                break;
            case R.id.left_menu_userjifeng:

                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    if (personalCenterVo != null) {
                        getBaseHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Bundle actBundle = new Bundle();
                                String url = MFUtil.getValueUrl(MFUrl.CREDITPOINT, "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                                        , "mobile=" + MFStaticConstans.getUserBean().getUser().getMobile(), "token=" + MFStaticConstans.getUserBean().getToken()
                                        , "version=" + MFApp.versionName, "credScore=" + personalCenterVo.getCredScore());
                                actBundle.putString("url", url);
                                startActivity(WebAty.class, actBundle);
                            }
                        }, 500);
                    } else {
                        initData();
                    }
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_userrenzheng:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundleCertificationAction = new Bundle();
                            bundleCertificationAction.putInt("typeentrance", 1);//	来源 0:默认注册后的认证,1:用户中心实名认证活动,2:结束订单页(显示5次)实名认证活动
                            startActivity(CertificationAct.class, bundleCertificationAction);
                        }
                    }, 500);
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_layout1:
                MobclickAgent.onEvent(mContext, "click_usercenter_mywallet");
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle userBundle = new Bundle();
                            UserVO userVO = MFStaticConstans.getUserBean().getUser();
                            userBundle.putSerializable("userVO", userVO);
                            startActivity(MyWalletAct.class, userBundle);
                        }
                    }, 500);
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_layout2:
                MobclickAgent.onEvent(mContext, "click_usercenter_myruns");
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(MyRunsAct.class, null);
                        }
                    }, 500);
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_layout3:
                SharedPreferenceTool.setPrefBoolean(mContext, "usercenter_invitaion_tag", true);
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle actBundle = new Bundle();
                            String url = MFUtil.getValueUrl(MFUrl.INTEGRALPRIZE, "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                                    , "mobile=" + MFStaticConstans.getUserBean().getUser().getMobile(), "token=" + MFStaticConstans.getUserBean().getToken()
                                    , "version=" + MFApp.versionName);
                            actBundle.putString("url", url);
                            actBundle.putBoolean("showTitlrBar", false);
                            startActivity(WebAty.class, actBundle);
                        }
                    }, 500);
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_layout4:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(CostomerCenterAct.class, null);
                        }
                    }, 500);
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_layout6:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MobclickAgent.onEvent(mContext, "click_usercenter_set");
                            startActivity(SetAct.class, null);
                        }
                    }, 500);
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_layout7:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    getBaseHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(AdviseAct.class, null);
                        }
                    }, 500);
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
            case R.id.left_menu_ad:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    if (adBean != null && !TextUtils.isEmpty(adBean.getAdUrl())) {
                        getBaseHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Bundle adBundle = new Bundle();
                                if (!TextUtils.isEmpty(adBean.getAdUrl())) {
                                    String url = MFUtil.getValueUrl(adBean.getAdUrl(), "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                                            , "mobile=" + MFStaticConstans.getUserBean().getUser().getMobile());
                                    adBundle.putString("url", url);
                                } else {
                                    adBundle.putString("url", adBean.getAdUrl());
                                }
                                adBundle.putString("title", adBean.getTitle());
                                sendStatistics("3002", "click", MFUtil.getValueForURL("cityCode", adBean.getAdUrl()), adBean.getId() + "");
                                startActivity(WebAty.class, adBundle);
                            }
                        }, 500);
                    }
                }
                if (left2Act != null) {
                    left2Act.callB(-2, null);
                }
                break;
        }
    }

    private void getADData() {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "3");
        PersonalCenterVo tempVoAD = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                PersonalCenterVo.class);
        if (tempVoAD != null && !TextUtils.isEmpty(tempVoAD.getLoginCityCode())) {
            params.addBodyParameter("cityName", tempVoAD.getCityName());
            params.addBodyParameter("cityCode", tempVoAD.getLoginCityCode());
        } else if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
            params.addBodyParameter("cityName", MFApp.getInstance().getmLocation().getCity());
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
        }
        MFRunner.requestPost(1020, MFUrl.GETADBYTYPE, params, this);
    }

    public void initData() {
        if (!MFUtil.checkNetwork(mContext) && isfirst) {
            showNoNetworkDialog(true);
            return;
        }
        isfirst = false;
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
        }
        MFRunner.requestPost(1001, MFUrl.PERSONAL_CENTER_URL, params, this);
    }


    private void showUserInfo(PersonalCenterVo personalCenterVo) {

        if (!SharedPreferenceTool.getPrefBoolean(mContext, "usercenter_invitaion_tag", false)) {
            newTag.setVisibility(View.VISIBLE);
        } else {
            newTag.setVisibility(View.GONE);
        }


        if (personalCenterVo == null) {
            userphone.setText("----");
            userimage.setImageResource(R.drawable.user_photo_selector);
            userrenzheng.setVisibility(View.VISIBLE);
            userinfo.setVisibility(View.VISIBLE);
            leftMenuLable10.setText("--");
            leftMenuLable20.setText("--");
            leftMenuLable30.setText("--");
            left_menu_lable4.setText(null);
//            initData();
            return;
        }
        userphone.setText(MFUtil.convertPhoneNumber(personalCenterVo.getMobile()));
        if (personalCenterVo.getRealNameState() == 1 && personalCenterVo.getCertState() == 2) {
            userphone.setText(personalCenterVo.getName());
        }

        if (TextUtils.isEmpty(personalCenterVo.getHeadPic())) {
            userimage.setImageResource(R.drawable.user_photo_selector);
        } else {
            String imgUrl = personalCenterVo.getHeadPic() + "?x-oss-process=image/circle,r_1000/format,png";
            ImageLoader.getInstance().displayImage(imgUrl, userimage, MFOptions.Option_UserPhoto);
        }

        userjifeng.setText("信用积分：" + personalCenterVo.getCredScore());

        if (personalCenterVo.getRealNameState() == 0) {
            userrenzheng.setVisibility(View.VISIBLE);
            userinfo.setVisibility(View.VISIBLE);
        } else {
            userrenzheng.setVisibility(View.GONE);
            userinfo.setVisibility(View.GONE);
        }

        if (1000 < personalCenterVo.getCarbonEmission().doubleValue()) {
            leftMenuLable11.setText(R.string.reducecarbon_kg_new);
            double carbonEmission = personalCenterVo.getCarbonEmission().doubleValue() / 1000.0000;
            leftMenuLable10.setText(MFUtil.formatDoubleValue(carbonEmission, "0.#") + "");
        } else {
            leftMenuLable11.setText(R.string.reducecarbon_g_new);
            leftMenuLable10.setText(MFUtil.formatDoubleValue(personalCenterVo.getCarbonEmission().doubleValue(), "0.#") + "");
        }

        if (!TextUtils.isEmpty(personalCenterVo.getMileage())) {
            double mileage = 0;
            try {
                mileage = Double.valueOf(personalCenterVo.getMileage());
            } catch (Exception e) {
                mileage = 0;
            }

            if (mileage > 1000) {
                leftMenuLable20.setText(
                        MFUtil.formatDoubleValue(mileage / 1000.0, "0.#") + "");
                leftMenuLable21.setText("累计(公里)");
            } else {
                leftMenuLable20.setText(((int) mileage) + "");
                leftMenuLable21.setText("累计(米)");
            }

        }

        leftMenuLable30.setText(
                MFUtil.formatDoubleValue(personalCenterVo.getSaveBikeFfare().doubleValue(), "0.#") + "");

        String startprice;
        if (personalCenterVo.getBalance().compareTo(BigDecimal.valueOf(0)) == -1) {
            startprice = "<font color='#999999'></font>" +
                    "<font color='#f6ab00'>" + MFUtil.formatDoubleValue(personalCenterVo.getBalance().doubleValue(), "0.#") + "</font>" + "<font color='#999999'>元</font>" +
                    "<font color='#f6ab00'>&nbsp;&nbsp;" + "请及时充值" + "</font>";
        } else {
            startprice = "<font color='#999999'></font>" +
                    "<font color='#f6ab00'>" + MFUtil.formatDoubleValue(personalCenterVo.getBalance().doubleValue(), "0.#") + "</font>" + "<font color='#999999'>元</font>";
        }
        left_menu_lable4.setText(Html.fromHtml(startprice));


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
                        try {
                            LeftMenuFrg.this.startActivityForResult(intent, 2);
                        } catch (Exception e) {
                            if (e.toString().contains("Permission")) {
                                MFUtil.showToast(mContext, "请检查蜜蜂出行相机权限是否开启");
                            }
                        }
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
                    try {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        LeftMenuFrg.this.startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        MFUtil.showToast(mContext, "未检测到本地相册，请使用拍照功能");
                    }
                    dialog.dismiss();

                }
            });
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
                        imageUri = startPhotoZoom(imageUri);
                    }

                    break;
                // 如果是调用相机拍照时
                case 2:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        imageUri = startPhotoZoom(imageUri);
                    }
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        if (bitmap != null) {
                            //imageView.setImageBitmap(bitmap);
                            uploadImage(bitmap);
//                            MFUtil.showToast(mContext, "已经获取到bitmap");
                        }
                    }
                    break;
                default:
                    break;
            }

        }

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

    private void uploadImage(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
            bitmap.recycle();//自由选择是否进行回收
            byte[] result = output.toByteArray();//转换成功了
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogShow();
            final String uploadName = MFApp.mifengOSSDirectory + "userPic" + "/" +
                    TimeDateUtil.longToDate(System.currentTimeMillis(), "yyyy/MM/dd") + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = MFConstansValue.OSS_BASEURL + uploadName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(MFConstansValue.OSS_BUCKETNAME, uploadName, result);
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                }
            });

            OSSAsyncTask asyncTask = MFApp.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
                    params.addBodyParameter("avatarAddress", uploadUrl + "");
                    MFRunner.requestPost(1010, MFUrl.UPLOADAVATAR_URL, params, LeftMenuFrg.this);
                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                }
            });
        }
    }


    @Override
    public void onStart(int tag, RequestParams params) {

    }

    @Override
    public void onLoading(int tag, RequestParams params, long total, long current, boolean isUploading) {

    }

    @Override
    public void onSuccess(int tag, RequestParams params, ResponseInfo responseInfo) {
        switch (tag) {
            case 1001:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                String personString = responseInfo.result.toString();
                RequestResultBean<PersonalCenterVo> personResultBean = GsonTransformUtil.getObjectFromJson(personString, new TypeToken<RequestResultBean<PersonalCenterVo>>() {
                }.getType());
                if (personResultBean != null && MFConstansValue.BACK_SUCCESS == personResultBean.getCode()) {
                    personalCenterVo = personResultBean.getData();
                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_KEY_PERSONRESULTBEAN, GsonTransformUtil.toJson(personalCenterVo));
                    showUserInfo(personalCenterVo);
                } else if (personResultBean != null && MFConstansValue.BACK_LOGOUT == personResultBean.getCode()) {
                    MFUtil.showToast(mContext, personResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
//                    mContext.finish();
                } else if (personResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == personResultBean.getCode() || MFConstansValue.BACK_BUSINESS == personResultBean.getCode())) {
                    MFUtil.showToast(mContext, personResultBean.getMessage());//1002一样重新登录
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
//                    mContext.finish();
                }
                break;
            case 1010:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                String uploadimgString = responseInfo.result.toString();
                RequestResultBean<MFBaseResBean> uploadResultBean = GsonTransformUtil.getObjectFromJson(uploadimgString, new TypeToken<RequestResultBean<MFBaseResBean>>() {
                }.getType());
                if (uploadResultBean != null && MFConstansValue.BACK_SUCCESS == uploadResultBean.getCode()) {
                    initData();
                } else if (uploadResultBean != null && MFConstansValue.BACK_LOGOUT == uploadResultBean.getCode()) {
                    MFUtil.showToast(mContext, uploadResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
//                    mContext.finish();
                } else if (uploadResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == uploadResultBean.getCode() || MFConstansValue.BACK_BUSINESS == uploadResultBean.getCode())) {
                    MFUtil.showToast(mContext, uploadResultBean.getMessage());
                }
                break;
            case 1020:
                if (!isAdded()) {
                    return;
                }
                String getAdDataInfoString = responseInfo.result.toString();
                final RequestResultBean<ADBean> adBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(getAdDataInfoString, new TypeToken<RequestResultBean<ADBean>>() {
                }.getType());
                if (adBeanRequestResultBean == null || adBeanRequestResultBean.getData() == null || MFConstansValue.BACK_SUCCESS != adBeanRequestResultBean.getCode()) {
                    return;
                }
                adBean = adBeanRequestResultBean.getData();
                long lastShowTime = 0;
                if (!TextUtils.isEmpty(SharedPreferenceTool.getPrefString(mContext, "adtype3", ""))) {
                    String[] adInfo = SharedPreferenceTool.getPrefString(mContext, "adtype3", "").split("，");
                    if (adInfo != null && adInfo.length != 2) {
                        return;
                    }
                    Long adId = Long.parseLong(adInfo[0]);
                    if (adBean.getId() == adId) {
                        lastShowTime = Long.parseLong(adInfo[1]);
                    } else {
                        SharedPreferenceTool.setPrefString(mContext, "adtype3", "");
                    }
                }
                if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && (new Date().getTime() - lastShowTime) >
                        adBean.getActiveFrequency() * 1000 * 60) {
                    leftMenuAd.setVisibility(View.VISIBLE);
//                    int with = ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dip2px(mContext, 30);
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivAdimg.getLayoutParams();
//                    layoutParams.height = 370 * with / 1320;
//                    layoutParams.width = with;
//                    leftMenuAd.setLayoutParams(layoutParams);
                    String imgrul = null;
                    if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && adBean.getPhotoUrl().contains("oss")) {
                        imgrul = adBean.getPhotoUrl() + "?x-oss-process=image/rounded-corners,r_10/format,png";
                    } else {
                        imgrul = adBean.getPhotoUrl();
                    }
                    ImageLoader.getInstance().displayImage(imgrul, leftMenuAd, MFOptions.OPTION_DEF);
                    sendStatistics("3002", "show", MFUtil.getValueForURL("cityCode", adBean.getAdUrl()), adBean.getId() + "");
                    SharedPreferenceTool.setPrefString(mContext, "adtype3", adBean.getId() + "，" + new Date().getTime());
                }
                break;
        }
    }

    @Override
    public void onFailure(int tag, RequestParams params, HttpException error, String msg) {

    }

    @Override
    public void onCancel(int tag, RequestParams params) {

    }


    public void refreshUserInfo() {
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            personalCenterVo = null;
            showUserInfo(null);
        } else {
//            if (personalCenterVo == null) {
            personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                    PersonalCenterVo.class);
            showUserInfo(personalCenterVo);
//            }
        }
    }


    public A2B getLeft2Act() {
        return left2Act;
    }

    public void setLeft2Act(A2B left2Act) {
        this.left2Act = left2Act;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        refreshUserInfo();
        initData();
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
