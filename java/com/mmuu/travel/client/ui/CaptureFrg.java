//package com.mmuu.travel.client.ui;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.AnimationDrawable;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.Settings;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.Result;
//import com.mmuu.travel.client.R;
//import com.mmuu.travel.client.base.MFBaseFragment;
//import com.mmuu.travel.client.bean.mfinterface.A2B;
//import com.mmuu.travel.client.bean.mfinterface.ChangePageInterface;
//import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
//import com.mmuu.travel.client.mfConstans.MFUrl;
//import com.mmuu.travel.client.tools.MFUtil;
//import com.mmuu.travel.client.ui.other.CreateOrderAct;
//import com.mmuu.travel.client.ui.other.WebAty;
//import com.mmuu.travel.client.widget.dialog.OneDialog;
//import com.mmuu.travel.client.widget.dialog.TwoDialog;
//import com.mmuu.travel.client.widget.dialog.UnLockDialog;
//import com.mmuu.travel.client.zxing.camera.CameraManager;
//import com.mmuu.travel.client.zxing.view.ViewfinderView;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
///**
// * Created by XIXIHAHA on 2016/12/15.
// */
//
//public class CaptureFrg extends MFBaseFragment implements
//        SurfaceHolder.Callback, View.OnClickListener, A2B {
//
//    private static final String TAG = CaptureFrg.class.getSimpleName();
//    @BindView(R.id.scan_to_edit_tv)
//    TextView scanToEditTv;
//    private String SCAN_TAG;
//    private String title;
//
//    @BindView(R.id.scan_to_edit)
//    RelativeLayout scanToEdit;
//    @BindView(R.id.scan_ele)
//    RelativeLayout scanEle;
//    @BindView(R.id.scan_to_el_tv)
//    TextView scanElTV;
//
//    // 相机控制
//    private CameraManager cameraManager;
//    private CaptureActivityHandler handler;
//    private ViewfinderView viewfinderView;
//    private boolean hasSurface;
//    private IntentSource source;
//    private Collection<BarcodeFormat> decodeFormats;
//    private Map<DecodeHintType, ?> decodeHints;
//    private String characterSet;
//    // 电量控制
//    private InactivityTimer inactivityTimer;
//    // 声音、震动控制
//    private BeepManager beepManager;
//
//
//    private TextView barTitle, tv_right;
//    private ImageView iv_left_raw;
//
//
//    private ChangePageInterface changePageInterface;
//
//    private UnLockDialog unLockDialog;
//
//    public ViewfinderView getViewfinderView() {
//        return viewfinderView;
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public CameraManager getCameraManager() {
//        return cameraManager;
//    }
//
//    public void drawViewfinder() {
//        viewfinderView.drawViewfinder();
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (fragContentView == null) {
//            fragContentView = inflater.inflate(R.layout.frg_scan_capture, null);
//
//            Window window = mContext.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            hasSurface = false;
//            inactivityTimer = new InactivityTimer(mContext);
//            beepManager = new BeepManager(mContext);
//
//            fragContentView.findViewById(R.id.nav_layout).setBackgroundColor(Color.parseColor("#1f1d1d"));
//
//
//            iv_left_raw = (ImageView) fragContentView.findViewById(R.id.title_left_image);
//            iv_left_raw.setVisibility(View.VISIBLE);
//            iv_left_raw.setImageResource(R.drawable.back_w);
//            barTitle = (TextView) fragContentView.findViewById(R.id.title_title);
//            barTitle.setText("扫码解锁");
//            barTitle.setTextColor(this.getResources().getColor(R.color.white));
//            tv_right = (TextView) fragContentView.findViewById(R.id.title_right_text);
//            tv_right.setVisibility(View.VISIBLE);
//            tv_right.setText("解锁帮助");
//            tv_right.setTextColor(Color.parseColor("#dddddd"));
//
//            iv_left_raw.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.finish();
//                }
//            });
//
//        }
//        ButterKnife.bind(this, fragContentView);
//        initView();
//        return fragContentView;
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // CameraManager必须在这里初始化，而不是在onCreate()中。
//        // 这是必须的，因为当我们第一次进入时需要显示帮助页，我们并不想打开Camera,测量屏幕大小
//        // 当扫描框的尺寸不正确时会出现bug
//        cameraManager = new CameraManager(mContext.getApplication());
//
//        viewfinderView = (ViewfinderView) fragContentView.findViewById(R.id.viewfinder_view);
//        viewfinderView.setCameraManager(cameraManager);
//
//        //handler = null;
//
//        SurfaceView surfaceView = (SurfaceView) fragContentView.findViewById(R.id.preview_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            // activity在paused时但不会stopped,因此surface仍旧存在；
//            // surfaceCreated()不会调用，因此在这里初始化camera
//            initCamera(surfaceHolder);
//        } else {
//            // 重置callback，等待surfaceCreated()来初始化camera
//            surfaceHolder.addCallback(this);
//        }
//
//        decodeFormats = null;
//        characterSet = null;
//        beepManager.updatePrefs();
//        inactivityTimer.onResume();
//        source = IntentSource.NONE;
//
//    }
//
//    @Override
//    public void onPause() {
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        inactivityTimer.onPause();
//        beepManager.close();
//        cameraManager.closeDriver();
//        if (!hasSurface) {
//            SurfaceView surfaceView = (SurfaceView) fragContentView.findViewById(R.id.preview_view);
//            SurfaceHolder surfaceHolder = surfaceView.getHolder();
//            surfaceHolder.removeCallback(this);
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroyView() {
//        inactivityTimer.shutdown();
//        super.onDestroyView();
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//
//    }
//
//    /**
//     * 扫描成功，处理反馈信息
//     *
//     * @param rawResult
//     * @param barcode
//     * @param scaleFactor
//     */
//    public void handleDecode(final Result rawResult, final Bitmap barcode, float scaleFactor) {
//        inactivityTimer.onActivity();
//        beepManager.playBeepSoundAndVibrate();
//        boolean fromLiveScan = barcode != null;
//        closeCamera();
//        String codeStr = rawResult.getText();
//        if (fromLiveScan && !TextUtils.isEmpty(codeStr) && codeStr.startsWith("https://www.mmuu.com/app/down.do?bikeCode")) {
//            int index = codeStr.indexOf("bikeCode=");
//            System.out.println(index);
//            if (codeStr.length() >= index + 18) {
//                String result = codeStr.substring(index + 9, index + 18);
////                result = "A" + result.substring(1);
//
//                if (TextUtils.isEmpty(SCAN_TAG)) {
//
//                    if (MFUtil.checkNetwork(mContext)) {
//                        System.out.println(result);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("code", result);
//                        startActivityForResult(CreateOrderAct.class, bundle, 10010);
//                    } else {
//
//                        TwoDialog netDilog = new TwoDialog(mContext, "网络故障，请确认网络正常后重试", "取消", "检查网络");
//                        netDilog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialogInterface) {
//                                getActivity().finish();
//                            }
//                        });
//                        netDilog.setDialogClickListener(new DialogClickListener() {
//                            @Override
//                            public void onLeftClick(View v, Dialog d) {
//                                d.dismiss();
//                            }
//
//                            @Override
//                            public void onRightClick(View v, Dialog d) {
//                                d.dismiss();
//                                Intent intent = null;
//                                // 先判断当前系统版本
//                                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
//                                    intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                                } else {
//                                    intent = new Intent();
//                                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
//                                }
//                                startActivity(intent);
//                            }
//                        });
//                        netDilog.show();
//
//                    }
//                } else {
//                    Intent key = new Intent();
//                    key.putExtra("BikeCode", result);
//                    key.putExtra("TAG", SCAN_TAG);
//                    getActivity().setResult(Activity.RESULT_OK, key);
//                    getActivity().finish();
//                }
//
//            } else {
//                showScanFailureDialog();
//            }
//        } else {
//            showScanFailureDialog();
//        }
//    }
//
//
//    private void initView() {
//        scanToEdit.setOnClickListener(this);
//        scanEle.setOnClickListener(this);
//
//        if (!TextUtils.isEmpty(title)) {
//            barTitle.setText(title);
//            tv_right.setText(null);
//            tv_right.setOnClickListener(null);
//            scanToEditTv.setText("输入编码");
//        } else {
//            scanToEditTv.setText("输入编码解锁");
//            tv_right.setOnClickListener(this);
//        }
//
//        AnimationDrawable anim0 = (AnimationDrawable) fragContentView.findViewById(R.id.scan_s_0).getBackground();
//        anim0.start();
//        AnimationDrawable anim1 = (AnimationDrawable) fragContentView.findViewById(R.id.scan_s_1).getBackground();
//        anim1.start();
//    }
//
//    /**
//     * 初始化Camera
//     *
//     * @param surfaceHolder
//     */
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        if (surfaceHolder == null) {
//            throw new IllegalStateException("No SurfaceHolder provided");
//        }
//        if (cameraManager.isOpen()) {
//            return;
//        }
//        try {
//            // 打开Camera硬件设备
//            cameraManager.openDriver(surfaceHolder);
//            // 创建一个handler来打开预览，并抛出一个运行时异常
//            if (handler == null) {
//                handler = new CaptureActivityHandler(this, decodeFormats,
//                        decodeHints, characterSet, cameraManager);
//            }
//        } catch (IOException ioe) {
//            Log.w(TAG, ioe);
//            displayFrameworkBugMessageAndExit();
//        } catch (RuntimeException e) {
//            Log.w(TAG, "Unexpected error initializing camera", e);
//            displayFrameworkBugMessageAndExit();
//        }
//    }
//
//    /**
//     * 显示底层错误信息并退出应用
//     */
//    private void displayFrameworkBugMessageAndExit() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle(getString(R.string.app_name));
//        builder.setMessage(getString(R.string.msg_camera_framework_bug));
//        builder.setPositiveButton(R.string.button_ok, new FinishListener(mContext));
//        builder.setOnCancelListener(new FinishListener(mContext));
//        builder.show();
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.scan_to_edit:
//                if (changePageInterface != null) {
//                    changePageInterface.onChangePage(1);
//                }
//                break;
//            case R.id.scan_ele:
//                switch (flashlightUtils()) {
//                    case 0:
//                        view.setSelected(false);
//                        scanElTV.setText("打开手电筒");
//                        break;
//                    case 1:
//                        view.setSelected(true);
//                        scanElTV.setText("关闭手电筒");
//                        break;
//                }
//                break;
//            case R.id.title_right_text:
//                Bundle bundle = new Bundle();
//                bundle.putString("url", MFUrl.UNLOCKHELP);
//                bundle.putString("title", "解锁帮助");
//                startActivity(WebAty.class, bundle);
//                break;
//        }
//
//    }
//
//    public ChangePageInterface getChangePageInterface() {
//        return changePageInterface;
//    }
//
//    public void setChangePageInterface(ChangePageInterface changePageInterface) {
//        this.changePageInterface = changePageInterface;
//    }
//
//
//    private void restartCamera() {
//        Log.d(TAG, "hasSurface " + hasSurface);
//
//        //viewfinderView.setVisibility(View.VISIBLE);
//
//        SurfaceView surfaceView = (SurfaceView) fragContentView.findViewById(R.id.preview_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        initCamera(surfaceHolder);
//
//        // 恢复活动监控器
//        inactivityTimer.onResume();
//    }
//
//    private void closeCamera() {
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        inactivityTimer.onPause();
//
//        // 关闭摄像头
//        cameraManager.closeDriver();
//    }
//
//    public String getSCAN_TAG() {
//        return SCAN_TAG;
//    }
//
//    public void setSCAN_TAG(String SCAN_TAG) {
//        this.SCAN_TAG = SCAN_TAG;
//    }
//
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    Camera camera;
//
//    /**
//     * 闪光灯开关
//     */
//    public int flashlightUtils() {
//        if (cameraManager != null && cameraManager.getCamera() != null) {
//            camera = cameraManager.getCamera();
//        }
//
//        if (camera == null) {
//            return 0;
//        }
//
//
//        Camera.Parameters parameters = camera.getParameters();
//        // String flashMode = parameters.getFlashMode();
//
//        if (isFlashlightOn()) {
//
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);// 关闭
//            camera.setParameters(parameters);
////            camera.release();
////            camera = null;
//            // Toast.makeText(mContext, "关闭手电筒", Toast.LENGTH_SHORT).show();
//            return 0;
//        } else {
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 开启
//            camera.setParameters(parameters);
//            // Toast.makeText(mContext, "开启手电筒", Toast.LENGTH_SHORT).show();
//            return 1;
//        }
//
//    }
//
//    /**
//     * 是否开启了闪光灯
//     *
//     * @return
//     */
//    public boolean isFlashlightOn() {
//        if (cameraManager != null) {
//            camera = cameraManager.getCamera();
//        }
//
//        if (camera == null) {
//            return false;
//        }
//
//        Camera.Parameters parameters = camera.getParameters();
//        String flashMode = parameters.getFlashMode();
//
//        return flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH);
//    }
//
//
//    private void showScanFailureDialog() {
//        //setUnLockDialogState(-1);
//        OneDialog errDialog = new OneDialog(mContext, "无效二维码", "朕知道了");
//        errDialog.setCanceledOnTouchOutside(false);
//        errDialog.setDialogClickListener(new DialogClickListener() {
//            @Override
//            public void onLeftClick(View v, Dialog d) {
//                d.dismiss();
//                getActivity().finish();
//            }
//
//            @Override
//            public void onRightClick(View v, Dialog d) {
//                d.dismiss();
//                //restartCamera();
//                //getActivity().finish();
//            }
//        });
//        errDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                getActivity().finish();
//            }
//        });
//        errDialog.show();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == 10010) {
////            if (data != null && data.getBooleanExtra("CreateResult", false)) {
//            Intent result = new Intent();
//            result.putExtra("CreateResult", data.getBooleanExtra("CreateResult", false));
//            getActivity().setResult(Activity.RESULT_OK, result);
//            getActivity().finish();
////            }
//        }
//    }
//
//    @Override
//    public int callB(int t, Object o) {
//        switch (flashlightUtils()) {
//            case 0:
//                scanEle.setSelected(false);
//                scanElTV.setText("打开手电筒");
//                break;
//            case 1:
//                scanEle.setSelected(true);
//                scanElTV.setText("关闭手电筒");
//                break;
//        }
//        return scanEle.isSelected() ? 1 : 0;
//    }
//}
