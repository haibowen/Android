package com.mmuu.travel.client.ui.main;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.RequestResultListBean;
import com.mmuu.travel.client.bean.bike.AreaPoint;
import com.mmuu.travel.client.bean.bike.BikeIconVO;
import com.mmuu.travel.client.bean.bike.BikeVO;
import com.mmuu.travel.client.bean.bike.GpsXy;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.bean.mfinterface.DialogItemClickListener;
import com.mmuu.travel.client.bean.mfinterface.LocationListener;
import com.mmuu.travel.client.bean.order.OrderDetailVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.overlay.WalkRouteOverlay;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.IMapUtils;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PermissionHelper;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.SensorHandle;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.ui.order.CurrentOrderAct;
import com.mmuu.travel.client.ui.other.ScanAct;
import com.mmuu.travel.client.ui.other.adapter.CheckBookPageAdp;
import com.mmuu.travel.client.ui.user.CostomerCenterAct;
import com.mmuu.travel.client.ui.user.POISearchAct;
import com.mmuu.travel.client.ui.user.TripFeedbackDetailAct;
import com.mmuu.travel.client.widget.MyViewPage;
import com.mmuu.travel.client.widget.dialog.HelpBottomDialog;
import com.mmuu.travel.client.widget.dialog.NavigateDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XIXIHAHA on 2017/2/7.
 */

public class BookIngFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, LocationListener, AMap.OnCameraChangeListener, LocationSource, OnRouteSearchListener {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.main_bottom_loca)
    LinearLayout mainBottomLoca;
    @BindView(R.id.frg_book_maplayout)
    FrameLayout mianMapLayout;
    @BindView(R.id.main_bottom_layout)
    RelativeLayout mainBottomLayout;
    @BindView(R.id.mian_top_check_vp)
    MyViewPage mianTopCheckVp;
    @BindView(R.id.main_bottom_help)
    LinearLayout mainBottomHelp;
    @BindView(R.id.main_bottom_ref)
    LinearLayout mainBottomRef;
    @BindView(R.id.main_bottom_scan)
    ImageView mainBottomScan;
    @BindView(R.id.main_top_check_layout)
    RelativeLayout mainTopCheckLayout;


    private SubKeepFrg subKeepFrg;
    //private SupportMapFragment frgMainMap;
    private MapView mapView;
    private AMap aMap;

    private MarkerOptions locaMarker = null;

    private LocationSource.OnLocationChangedListener locationChangedListener;
    private SensorHandle sensorHandle;

    private RouteSearch mRouteSearch;
    private WalkRouteOverlay walkRouteOverlay;
    private OrderDetailVO orderDetailVO;

    private String locaTag = "BookIngFrg";

    private int isHasGetBikeIcons = 0;

    private A2B a2B;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case 5001:
                getHandler().removeMessages(5001);
                getUnFinishOrder("");
                break;
            case 5004:
                //车辆图标
                if (msg.getData() != null) {
                    isHasGetBikeIcons++;
                    getBikeIcons(msg.getData().getString("cityCode"));
                }
                break;
            case 6666://跳动中心标志
                //startJumpAnimation();
                break;
            case 6668:
                mianTopCheckVp.setVisibility(View.VISIBLE);
                break;
            case 2050:
                Log.e("addArea -start 2050", System.currentTimeMillis() + "");
                addArea(false);
                break;
            case 2060:
                //重新规划路径
                if (!isAdded()) {
                    return;
                }

                MFApp.getInstance().addLocationListener(locaTag, BookIngFrg.this);
                MFApp.getInstance().startLocation();

                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_booking, null);
            // frgMainMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg_main_map);
            ButterKnife.bind(this, fragContentView);
            initView();
            initMapView(savedInstanceState);
            //showData();

            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MFApp.getInstance().addLocationListener(locaTag, BookIngFrg.this);
                    MFApp.getInstance().startLocation();
                }
            }, 2500);

        }

        return fragContentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        MFApp.getInstance().addLocationListener(locaTag, this);
        getHandler().sendEmptyMessageDelayed(5001, 800);
        if (sensorHandle != null) {
            sensorHandle.onResume();
        }
        if (mapView != null) {
            mapView.onResume();
        }
        MFStaticConstans.needRefOrder = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        MFApp.getInstance().removeLocationListener(locaTag);
        getHandler().removeMessages(5001);
        if (sensorHandle != null) {
            sensorHandle.onPause();
        }
        if (mapView != null) {
            mapView.onPause();
        }
        MFStaticConstans.needRefOrder = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getHandler().removeCallbacksAndMessages(null);
        if (mapView != null) {
            mapView.onDestroy();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.main_bottom_help:
                MobclickAgent.onEvent(mContext, "click_reserve_feelback");
                toHelp();
                break;
            case R.id.main_bottom_loca:
                //aMap.setOnCameraChangeListener(this);
                MFApp.getInstance().addLocationListener(locaTag, this);
                MFApp.getInstance().startLocation();
                break;
            case R.id.main_bottom_ref:
                //showLineRecode();
                // aMap.setOnCameraChangeListener(this);
                // mainTopCheckLayout.setVisibility(View.GONE);
                break;
            case R.id.main_bottom_scan:
                if (!checkPermission(PermissionHelper.ACCESS_FINE_LOCATION_MODEL)) {
                    MFUtil.showToast(mContext, "检测到没有定位权限，请设置后重试");
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_reserveafter_scan");
                Bundle scanBundle = new Bundle();
                scanBundle.putBoolean("needAnim", true);
                startActivityForResult(ScanAct.class, scanBundle, 1001);
                break;
            case R.id.title_left_image:
                if (isAdded()) {
                    getActivity().finish();
                }
                break;
            case R.id.title_right_image:
                startActivityForResult(POISearchAct.class, null, 4004);
                break;
            case R.id.title_right_text:
                if (a2B != null) {
                    a2B.callB(1, null);
                }
//                toNavi();
                break;
        }
    }

    private void initView() {
//        titleTitle.setText(getResources().getText(R.string.app_name));
        // aMap = frgMainMap.getMap();
        //centerLocaView.setVisibility(View.INVISIBLE);
        mainBottomHelp.setOnClickListener(this);
        mainBottomLoca.setOnClickListener(this);
        mainBottomRef.setOnClickListener(this);
        mainBottomScan.setOnClickListener(this);
        titleLeftImage.setOnClickListener(this);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setVisibility(View.VISIBLE);
        initViewpage();
    }

    private void initViewpage() {
        mianTopCheckVp = (MyViewPage) fragContentView.findViewById(R.id.mian_top_check_vp);
        mianTopCheckVp.setScrollble(false);
        CheckBookPageAdp CheckBookPageAdp = new CheckBookPageAdp(getChildFragmentManager());
        ArrayList<MFBaseFragment> subList = new ArrayList<MFBaseFragment>();
        subKeepFrg = new SubKeepFrg();
        setA2B(subKeepFrg);
        subList.add(subKeepFrg);
        CheckBookPageAdp.setList(subList);
        mianTopCheckVp.setAdapter(CheckBookPageAdp);
        mainTopCheckLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mainTopCheckLayout.getTag() == null) {
                    showOrDismmisTop(true, null);
                    getHandler().sendEmptyMessageDelayed(6668, 200);
                }
                mainTopCheckLayout.setTag("");
            }
        });

    }


    private void initMapView(Bundle savedInstanceState) {

        AMapOptions aOptions = new AMapOptions();
        mianMapLayout.removeAllViews();
        if (MFApp.getInstance().getmLocation() != null) {
            aOptions.camera(new CameraPosition(new LatLng(MFApp.getInstance().getmLocation().getLatitude(), MFApp.getInstance().getmLocation().getLongitude()), 16f, 0, 0));
        } else {
        }

        mapView = new MapView(mContext, aOptions);
        mapView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mianMapLayout.addView(mapView, mParams);
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);


        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.icon_loca));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 87, 128, 233));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(50, 87, 128, 233));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(15f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(0);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        aMap.setOnMapClickListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                                        @Override
                                        public void onMapLoaded() {
                                            getHandler().sendEmptyMessageDelayed(2050, 1);
                                            if (MFApp.getInstance().getmLocation() != null) {
                                                onLocation(MFApp.getInstance().getmLocation());
                                                MFApp.getInstance().addLocationListener(locaTag, BookIngFrg.this);
                                                MFApp.getInstance().startLocation();
                                            }

                                            sensorHandle = new SensorHandle(mContext, new SensorEventListener() {
                                                @Override
                                                public void onSensorChanged(SensorEvent sensorEvent) {
                                                    if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                                                        float degree = sensorEvent.values[0];
                                                        float bearing = aMap.getCameraPosition().bearing;
                                                        if (degree + bearing > 360) {
                                                            aMap.setMyLocationRotateAngle(-(degree + bearing - 360));// 设置小蓝点旋转角度
                                                        } else {
                                                            aMap.setMyLocationRotateAngle(-(degree + bearing));//
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onAccuracyChanged(Sensor sensor, int i) {

                                                }
                                            });

                                            if (sensorHandle != null) {
                                                sensorHandle.onResume();
                                            }
                                        }
                                    }
        );
    }


    private void showOrDismmisTop(boolean dismmis, BikeVO bikeVO) {

        if (dismmis) {
            if (mianTopCheckVp.getY() == 0) {
                ObjectAnimator translationUp = ObjectAnimator.ofFloat(mianTopCheckVp, "Y", -mianTopCheckVp.getHeight());
                translationUp.setDuration(350);
                translationUp.start();
            }
        } else {
            if (mianTopCheckVp.getY() != 0) {
                ObjectAnimator translationUp = ObjectAnimator.ofFloat(mianTopCheckVp, "Y", 0);
                translationUp.setDuration(350);
                translationUp.start();
                //subKeepFrg.showData(bikeVO);
            }
        }


    }

    private void getUnFinishOrder(String bikeCode) {
        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            //startActivity(LoginAct.class, null);
            getHandler().sendEmptyMessageDelayed(5001, 10000);
        } else {
            RequestParams detailParams = new RequestParams();
            detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
            //detailParams.addBodyParameter("bikeCode", bikeCode);
            MFRunner.requestPost(1901, MFUrl.getUnFinishOrder, detailParams, this);
            //getHandler().sendEmptyMessageDelayed(5001, 30000);
        }
    }


    @Override
    public void onLocation(AMapLocation aMapLocation) {
        if (!isAdded()) {
            return;
        }
        if (aMapLocation != null) {
            if (locationChangedListener != null) {
                locationChangedListener.onLocationChanged(aMapLocation);
            }

            if (TextUtils.isEmpty(IMapUtils.getBikeIconVO().getIcon_android()) && isHasGetBikeIcons < 10) {
                getBikeIcons(aMapLocation.getAdCode());
            }

            MFApp.getInstance().removeLocationListener(locaTag);
            final LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            if (locaMarker == null) {
                locaMarker = new MarkerOptions();
            }
            locaMarker.position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.drawable.icon_loca))).anchor(0.5f, 0.5f);


            //clearMarks();//zhangyu2016
            // final Marker marker = aMap.addMarker(locaMarker);


            aMap.animateCamera(CameraUpdateFactory.zoomTo(19), 500, new AMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    CameraUpdate up = CameraUpdateFactory.newLatLngZoom(latLng, 19);
                    aMap.animateCamera(up, 1000, null);
                    getHandler().removeMessages(6666);

                    if (orderDetailVO != null && !TextUtils.isEmpty(orderDetailVO.getGpsX())) {
                        BikeVO bikeVO = new BikeVO();
                        bikeVO.setGpsXy(new GpsXy(Double.valueOf(orderDetailVO.getGpsX()), Double.valueOf(orderDetailVO.getGpsY())));
                        showRoute(bikeVO);
                    }

                }

                @Override
                public void onCancel() {

                }
            });
        }
        getHandler().sendEmptyMessageDelayed(2060, 30000);
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
            case 1901:
                if (!isAdded()) {
                    return;
                }
                //是否有未完成的订单
                dismmisDialog();
                RequestResultBean<OrderDetailVO> unFinish = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<OrderDetailVO>>() {
                }.getType());

                if (unFinish != null && unFinish.getCode() == 0) {

                    if (unFinish.getData() == null || TextUtils.isEmpty(unFinish.getData().getOrderId())) {
                        getActivity().finish();
                        return;
                    } else if ("2".equals(unFinish.getData().getState())) {
                        startActivity(CurrentOrderAct.class, null);
                        getActivity().finish();
                        return;
                    } else if (!"1".equals(unFinish.getData().getState())) {
                        getActivity().finish();
                        return;
                    }
                    showOrDismmisTop(false, null);
                    subKeepFrg.showData(unFinish.getData());

                    orderDetailVO = unFinish.getData();
                    if (orderDetailVO != null && !TextUtils.isEmpty(orderDetailVO.getGpsX()) && !"null".equals(orderDetailVO.getGpsX())) {
                        titleTitle.setText("NO." + unFinish.getData().getBikeCode());

                        titleRightText.setVisibility(View.VISIBLE);
                        titleRightText.setText("取消用车");
                        titleRightText.setOnClickListener(this);

                        clearMarks();

                        LatLng LatLng = new LatLng(Double.valueOf(orderDetailVO.getGpsX()), Double.valueOf(orderDetailVO.getGpsY()), true);
                        BitmapDescriptor des = null;
                        if (!TextUtils.isEmpty(IMapUtils.getBikeIconVO().getIcon_android())) {
                            des = BitmapDescriptorFactory.fromBitmap(ImageLoader.getInstance().loadImageSync(IMapUtils.getBikeIconVO().getIcon_android()));
                        } else {
                            des = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.icon_bike_st1));
                        }
                        Marker marker = aMap.addMarker(new MarkerOptions().position(LatLng).icon(des)
                                .anchor(0.5f, 1f));
                        marker.setObject(new Object());
                    }
                    //getHandler().removeMessages(5001);
                }
                break;
            case 3022:
                if (!isAdded()) {
                    return;
                }
                RequestResultBean<BikeIconVO> bikeIconBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<BikeIconVO>>() {
                }.getType());
                if (bikeIconBean != null) {
                    isHasGetBikeIcons = 15;
                    if (bikeIconBean.getCode() == 0) {
                        final BikeIconVO tempIcon = bikeIconBean.getData();
                        if (tempIcon != null) {
                            if (!TextUtils.isEmpty(tempIcon.getIcon_android()) && tempIcon.getIcon_android().contains("oss")) {
                                int tempDpi = ScreenUtil.getDPI(mContext);
                                String tempIconStr = tempIcon.getIcon_android();
                                if (tempDpi >= 640) {
                                    //xxxh
                                    tempIcon.setIcon_android(tempIconStr.replace("oss", "img") + "?x-oss-process=image/resize,w_128");
                                    tempIcon.setIcon_max(tempIconStr.replace("oss", "img") + "?x-oss-process=image/resize,w_156");
                                } else if (tempDpi >= 480) {
                                    //xxh
                                    tempIcon.setIcon_android(tempIconStr.replace("oss", "img") + "?x-oss-process=image/resize,w_96");
                                    tempIcon.setIcon_max(tempIconStr.replace("oss", "img") + "?x-oss-process=image/resize,w_116");
                                } else {
                                    //xh
                                    tempIcon.setIcon_android(tempIconStr.replace("oss", "img") + "?x-oss-process=image/resize,w_64");
                                    tempIcon.setIcon_max(tempIconStr.replace("oss", "img") + "?x-oss-process=image/resize,w_77");
                                }
                                tempIconStr = null;
                            } else {
                                tempIcon.setIcon_max(tempIcon.getIcon_android());
                            }

                            ImageLoader.getInstance().loadImage(tempIcon.getIcon_max(), null);
                            ImageLoader.getInstance().loadImage(tempIcon.getIcon_android(), new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                    IMapUtils.setBikeIconVO(tempIcon);
                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }
                    }
                }
                if (isHasGetBikeIcons < 10) {
                    getBaseHandler().sendEmptyMessage(5004);
                }
                break;
        }


    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        switch (i) {
            case 3022:
                if (isHasGetBikeIcons < 10) {
                    getBaseHandler().removeMessages(5004);
                    getBaseHandler().sendEmptyMessageDelayed(5004, 5000);
                }
                break;
        }
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }


    HelpBottomDialog helpBottomDialog;

    private void toHelp() {

        if (helpBottomDialog == null) {
            helpBottomDialog = new HelpBottomDialog(mContext);
//            helpBottomDialog.setState(2);
        }
//        helpBottomDialog.setState(2);
        helpBottomDialog.setHelpDialogClickListener(new HelpBottomDialog.HelpDialogClickListener() {
            @Override
            public void onLayout0(View v, Dialog d) {
                if (orderDetailVO == null) {
                    d.dismiss();
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_reserve_feelbackopenlock");
                Intent intent = new Intent(mContext, TripFeedbackDetailAct.class);
                Bundle bundle = new Bundle();
                bundle.putString("pagetype", MFConstansValue.FEELBACK_MAIN_BREAK);
                bundle.putString("bikeCode", orderDetailVO.getBikeCode() + "");
                bundle.putString("orderId", orderDetailVO.getOrderId() + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                d.dismiss();
            }

            @Override
            public void onLayout1(View v, Dialog d) {
                if (orderDetailVO == null) {
                    d.dismiss();
                    return;
                }


                MobclickAgent.onEvent(mContext, "click_main_feelback_stop");
                Intent intent = new Intent(mContext, TripFeedbackDetailAct.class);
                Bundle bundle = new Bundle();
                bundle.putString("pagetype", MFConstansValue.FEELBACK_MAIN_STOPCAR);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                d.dismiss();
            }


            @Override
            public void onCancel(View v, Dialog d) {
                d.dismiss();
                startActivity(CostomerCenterAct.class, null);
            }
        });
        helpBottomDialog.show();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        locationChangedListener = null;
    }


    private void clearMarks() {
        List<Marker> ms = aMap.getMapScreenMarkers();
        for (Marker m : ms) {
            if (m != null && m.getObject() != null) {
                m.remove();
            } else if (m != null && m.isVisible()) {
            }
        }
    }

    private void showRoute(BikeVO bikeVO) {
        if (locaMarker != null && bikeVO != null) {
            LatLonPoint startPoint = new LatLonPoint(locaMarker.getPosition().latitude, locaMarker.getPosition().longitude);
            LatLonPoint endPoint = new LatLonPoint(bikeVO.getGpsXy().getLat(), bikeVO.getGpsXy().getLon());
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
            if (mRouteSearch == null) {
                mRouteSearch = new RouteSearch(mContext);
                mRouteSearch.setRouteSearchListener(this);
            }
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (walkRouteOverlay != null) {
            walkRouteOverlay.removeFromMap();
        }

        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {

                    final WalkPath walkPath = result.getPaths()
                            .get(0);
                    walkRouteOverlay = new WalkRouteOverlay(
                            mContext, aMap, walkPath,
                            result.getStartPos(),
                            result.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    //mBottomLayout.setVisibility(View.VISIBLE);
                    aMap.setOnCameraChangeListener(null);
                } else if (result != null && result.getPaths() == null) {
                    MFUtil.showToast(mContext, "对不起，没有搜索到相关数据！");
                }
            } else {
                MFUtil.showToast(mContext, "对不起，没有搜索到相关数据！");
            }
        } else {
            MFUtil.showToast(mContext, errorCode + "");
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
//            if (data != null && data.getBooleanExtra("CreateResult", false)) {
            getActivity().finish();
//            }
        }
    }

    private void toNavi() {
        if (orderDetailVO != null && !TextUtils.isEmpty(orderDetailVO.getGpsX())) {
            MobclickAgent.onEvent(mContext, "click_currentorder_navigation");
            //returnBike();
            boolean hasBaidu = MFUtil.isAvilible(mContext, "com.baidu.BaiduMap");
            boolean hasGaode = MFUtil.isAvilible(mContext, "com.autonavi.minimap");


            Tip tip = new Tip();
            tip.setDistrict(orderDetailVO.getLocation());
            tip.setPostion(new LatLonPoint(Double.valueOf(orderDetailVO.getGpsX()), Double.valueOf(orderDetailVO.getGpsY())));

            if (hasBaidu && hasGaode) {
                NavigateDialog navigateDialog = new NavigateDialog(mContext);
                navigateDialog.setTip(tip);
                navigateDialog.setDialogItemClickListener(new DialogItemClickListener() {
                    @Override
                    public void onItemClick(View v, Dialog d, String type) {

                        if (!TextUtils.isEmpty(type) && "baidu".equals(type)) {
                            toNavigate(0, ((NavigateDialog) d).getTip());
                        } else if (!TextUtils.isEmpty(type) && "gaode".equals(type)) {
                            toNavigate(1, ((NavigateDialog) d).getTip());
                        }
                        d.dismiss();
                    }
                });
                navigateDialog.show();
            } else if (hasBaidu && !hasGaode) {
                toNavigate(0, tip);
            } else if (!hasBaidu && hasGaode) {
                toNavigate(1, tip);
            } else {
                NavigateDialog navigateDialog = new NavigateDialog(mContext);
                navigateDialog.setTip(tip);
                navigateDialog.setDialogItemClickListener(new DialogItemClickListener() {
                    @Override
                    public void onItemClick(View v, Dialog d, String type) {

                        if (!TextUtils.isEmpty(type) && "baidu".equals(type)) {
                            MFUtil.showToast(mContext, "您未安装百度地图，请安装后重试");
                        } else if (!TextUtils.isEmpty(type) && "gaode".equals(type)) {
                            MFUtil.showToast(mContext, "您未安装高德地图，请安装后重试");
                        }
                        d.dismiss();
                    }
                });
                navigateDialog.show();
            }
        }
    }

    private void toNavigate(int type, Tip tip) {
        MobclickAgent.onEvent(mContext, "click_poi_navigations");
        if (tip == null) {
            return;
        }
        switch (type) {
            case 0:
                double[] latlon = MFUtil.gaoDeToBaidu(tip.getPoint().getLongitude(), tip.getPoint().getLatitude());
                StringBuffer sb = new StringBuffer();
                sb.append("intent://map/direction?").append("destination=").append("latlng:").append(latlon[1]).append(",").append(latlon[0]);
                sb.append("|name:我的目的地").append("&mode=walking").append("&region=").append(tip.getDistrict()).append("&src=蜜蜂出行#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                Intent intent = null;
                try {
                    intent = Intent.getIntent(sb.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                StringBuffer sbs = new StringBuffer();
                sbs.append("androidamap://route?sourceApplication=蜜蜂出行&sname=我的位置&dlat=").append(tip.getPoint().getLatitude()).append("&dlon=").append(tip.getPoint().getLongitude());
                sbs.append("&dname=").append(tip.getDistrict()).append("&dev=1&t=4");
                Intent intentgao = null;
                try {
                    intentgao = Intent.getIntent(sbs.toString());
                    startActivity(intentgao);
                } catch (URISyntaxException e) {

                    e.printStackTrace();
                }

                break;
        }
    }

    private ArrayList<Polygon> polygonList;

    private void addArea(boolean showContext) {
        Log.e("addArea -start", System.currentTimeMillis() + "");
        RequestResultListBean<ArrayList<AreaPoint>> poings = GsonTransformUtil.getObjectListFromJson(SharedPreferenceTool.getPrefString(mContext, MFConstansValue.SP_KEY_AREAPOINT, ""), new TypeToken<RequestResultListBean<ArrayList<AreaPoint>>>() {
        }.getType());
        if (poings == null || 0 != poings.getCode() || poings.getData() == null || poings.getData().size() == 0) {
            Log.e("addArea -empt", System.currentTimeMillis() + "");
            return;
        }

        if (polygonList == null || polygonList.size() == 0 || !IMapUtils.isHasPolygon()) {
            IMapUtils.reSetPolygonList();

            if (polygonList == null) {
                polygonList = new ArrayList<Polygon>();
            }

            for (Polygon s : polygonList) {
                if (s != null) {
                    s.remove();
                }
            }
            polygonList.clear();

            ABC:
            for (ArrayList<AreaPoint> a : poings.getData()) {
                PolygonOptions polygonOptions = new PolygonOptions();

                if (a == null || a.size() < 3) {
                    continue ABC;
                }
//                clearPolygon();
                for (AreaPoint p : a) {
                    polygonOptions.add(new LatLng(p.getLatitude(), p.getLongitude()));
                }
                if (showContext) {
                    // 声明 多边形参数对象
                    // 添加 多边形的每个顶点（顺序添加）6
                    polygonOptions.strokeWidth(15) // 多边形的边框
                            .strokeColor(Color.argb(80, 87, 128, 233)) // 边框颜色
                            .fillColor(Color.argb(50, 87, 128, 233));   // 多边形的填充色
                    Polygon tp = aMap.addPolygon(polygonOptions);
                    polygonList.add(tp);
                    IMapUtils.addPolygon(tp, false);
                } else {
                    // 声明 多边形参数对象
                    // 添加 多边形的每个顶点（顺序添加）
                    polygonOptions.strokeWidth(15) // 多边形的边框
                            .strokeColor(Color.argb(80, 87, 128, 233)) // 边框颜色
                            .fillColor(Color.argb(0, 87, 128, 233));   // 多边形的填充色
                    Polygon tp = aMap.addPolygon(polygonOptions);
                    polygonList.add(tp);
                    IMapUtils.addPolygon(tp, false);

                }
            }
        } else {
            if (showContext) {
                for (Polygon p : polygonList) {
                    if (p != null) {
                        p.setFillColor(Color.argb(50, 87, 128, 233));
                    }
                }
            } else {
                for (Polygon p : polygonList) {
                    if (p != null) {
                        p.setFillColor(Color.argb(0, 87, 128, 233));
                    }
                }
            }
        }
        Log.e("addArea -end", System.currentTimeMillis() + "");
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (cameraPosition.zoom > 14f) {
            addArea(false);
        } else {
            addArea(true);
        }

    }

    private void clearPolygon() {
        List<Marker> ms = aMap.getMapScreenMarkers();
        for (Marker m : ms) {
            if (m != null && m.getObject() != null) {
            } else if (m != null) {
                m.remove();
            }
        }
    }

    private void getBikeIcons(String cityCode) {
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("cityCode", cityCode);
        MFRunner.requestPost(3022, MFUrl.GETBIKEICONS, detailParams, this);
    }


    public A2B getA2B() {
        return a2B;
    }

    public void setA2B(A2B a2B) {
        this.a2B = a2B;
    }
}