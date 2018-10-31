package com.mmuu.travel.client.ui.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
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
import com.mmuu.travel.client.bean.ADBean;
import com.mmuu.travel.client.bean.PersonalCenterVo;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.RequestResultListBean;
import com.mmuu.travel.client.bean.UpdataBean;
import com.mmuu.travel.client.bean.bike.AreaPoint;
import com.mmuu.travel.client.bean.bike.BikeDetailVO;
import com.mmuu.travel.client.bean.bike.BikeIconVO;
import com.mmuu.travel.client.bean.bike.BikeTag;
import com.mmuu.travel.client.bean.bike.BikeVO;
import com.mmuu.travel.client.bean.bike.GpsXy;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.bean.mfinterface.BookingBikeListener;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.bean.mfinterface.LocationListener;
import com.mmuu.travel.client.bean.mfinterface.NetChangeListener;
import com.mmuu.travel.client.bean.order.OrderDetailVO;
import com.mmuu.travel.client.bean.user.CityInfoVO;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.overlay.WalkRouteOverlay;
import com.mmuu.travel.client.tools.ApkDownLoad;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.IMapUtils;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PermissionHelper;
import com.mmuu.travel.client.tools.ScreenUtil;
import com.mmuu.travel.client.tools.SensorHandle;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.tools.SystemUtils;
import com.mmuu.travel.client.ui.order.CurrentOrderAct;
import com.mmuu.travel.client.ui.other.ScanAct;
import com.mmuu.travel.client.ui.other.WebAty;
import com.mmuu.travel.client.ui.other.adapter.CheckBookPageAdp;
import com.mmuu.travel.client.ui.user.LoginAct;
import com.mmuu.travel.client.ui.user.POISearchAct;
import com.mmuu.travel.client.ui.user.TripFeedbackDetailAct;
import com.mmuu.travel.client.ui.user.UserCenterAct;
import com.mmuu.travel.client.widget.MyViewPage;
import com.mmuu.travel.client.widget.dialog.ADDialog;
import com.mmuu.travel.client.widget.dialog.CostomerCenterDialog;
import com.mmuu.travel.client.widget.dialog.UpdateDialog;
import com.mmuu.travel.client.widget.dialog.UseCarHelpDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XIXIHAHA on 2016/12/13.
 */

public class MainFrg extends MFBaseFragment implements View.OnClickListener, LocationListener, NetChangeListener, PublicRequestInterface, AMap.OnCameraChangeListener, AMap.OnMapClickListener, RouteSearch.OnRouteSearchListener, BookingBikeListener, LocationSource {


    @BindView(R.id.main_top_menu)
    ImageView topMenu;
    @BindView(R.id.main_top_act)
    ImageView topAct;
    @BindView(R.id.frg_main_maplayout)
    FrameLayout mMapLayout;
    @BindView(R.id.main_bottom_loca)
    LinearLayout mBottomLoca;
    @BindView(R.id.main_bottom_ref)
    LinearLayout mBottomRef;
    @BindView(R.id.main_bottom_ref_iv)
    ImageView mBottomRefIV;
    @BindView(R.id.main_bottom_help)
    LinearLayout mBottomHelp;
    @BindView(R.id.main_bottom_search)
    LinearLayout mBottomSearch;
    @BindView(R.id.main_bottom_scan)
    ImageView mBottomScan;
    @BindView(R.id.main_bottom_scan_progress_circle)
    ProgressBar mBottomScanBar;

    @BindView(R.id.frg_mian_use)
    RelativeLayout frgMianUse;
    @BindView(R.id.frg_mian_use_text)
    TextView frgMianUseText;
    @BindView(R.id.frg_mian_use_ad)
    ImageView frgMianUseAD;
    @BindView(R.id.ll_bottomlay)
    LinearLayout llBottomlay;
    Unbinder unbinder;
    @BindView(R.id.rl_scan)
    RelativeLayout rlScan;


    private MyViewPage mianTopCheckVp;
    @BindView(R.id.main_top_check_layout)
    RelativeLayout mainTopCheckLayout;

    private MapView mapView;
    private AMap aMap;
    //地图中心标志
    private Marker screenMarker = null;
    private MarkerOptions locaMarker = null;

    private SubBookingFrg subBookingFrg;
//    private SubKeepFrg subKeepFrg;

    private RouteSearch mRouteSearch;
    private WalkRouteOverlay walkRouteOverlay;

    private static final int TAG_GETVERSION_URL = 1695701;
    private static final int TAG_GETADBYTYPE = 1695703;
    private static final int TAG_GETADBYTYPE5 = 1695704;
    private static final int Tag_PERSONAL_CENTER_URL = 1695702;

    private OnLocationChangedListener locationChangedListener;

    private SensorHandle sensorHandle;

    private String locaTag = "MainFrg";
    private ADBean adBean;
    private boolean isforce = false;
    private boolean isHasPartnerInfo = false;
    private int isHasGetBikeIcons = 0;
    /**
     * needReLoca解决刚刚进入主页就跳转导致定位无返回问题
     */
    private boolean needReLoca = false;

    /**
     * 默认的bike图标
     */
    private BitmapDescriptor generalBikeBD = null;

    /**
     * 当前选中的bike图标
     */
    private BitmapDescriptor currentGeneralBikeBD = null;

    /**
     * 定位成功，但由于高德不执行动画导致不刷新数据
     */
    private boolean locaS = false;

    private ArrayList<Polygon> polygonList;

    private A2B act2Main;
    private boolean canRefList = true;
    private CostomerCenterDialog costomerCenterDialog;

    private int topADImangeWidth, topADImangeHeight;

    @Override
    protected void baseHandMessage(Message msg) {
        super.baseHandMessage(msg);
        switch (msg.what) {
            case 5001:
                getHandler().removeMessages(5001);
                MFStaticConstans.needRefOrder = false;
                getUnFinishOrder("");
                break;
            case 5002:
                getHandler().removeMessages(5002);
                getHandler().removeMessages(5001);
                MFStaticConstans.needRefOrder2Sce = false;
                getUnFinishOrder("");
                break;
            case 5003:
                //城市合伙人（采蜜人）
                break;
            case 5004:
                //车辆图标
                if (msg.getData() != null) {
                    isHasGetBikeIcons++;
                    getBikeIcons(msg.getData().getString("cityCode"));
                }
                break;

            case 6667:
                if (locaS) {
                    getHandler().removeMessages(6666);
                    getHandler().removeMessages(6665);
                    getHandler().removeMessages(6667);
                    startJumpAnimation(true);
                }
                locaS = false;
                break;
            case 6666://跳动中心标志
                locaS = false;
                getHandler().removeMessages(6666);
                getHandler().removeMessages(6665);
                getHandler().removeMessages(6667);
                startJumpAnimation(false);
                break;
            case 6665://跳动中心标志
                locaS = false;
                getHandler().removeMessages(6666);
                getHandler().removeMessages(6665);
                getHandler().removeMessages(6667);
                startJumpAnimation(true);
                break;

            case 6668:
                mianTopCheckVp.setVisibility(View.VISIBLE);
                break;
            case 6669:
                mBottomScanBar.setVisibility(View.GONE);
                mBottomScan.setTag(null);
                mBottomScan.setImageResource(R.drawable.main_scan_sel);
//                mainTopCurrentLayout.setTag(null);
//                mainTopCurrentLayout.setVisibility(View.GONE);
                break;
            case 7001:
                PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                        PersonalCenterVo.class);
                if (personalCenterVo != null) {
//                    initTopLayout(personalCenterVo.getIsOldUser());//2.3.0取消用老用户的判断（如何使用小蜜蜂）
                }
                break;
        }
    }


    public void RequestUpdata() {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("versionNum", SystemUtils.getVersionCode(mContext) + "");
        params.addBodyParameter("version", SystemUtils.getVersionName(mContext));
        params.addBodyParameter("clientType", "0");
        PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                PersonalCenterVo.class);
        if (personalCenterVo == null || TextUtils.isEmpty(personalCenterVo.getLoginCityCode())) {
            if (MFApp.getInstance().getmLocation() != null) {
                params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode() + "");
            }
        } else {
            params.addBodyParameter("cityCode", personalCenterVo.getLoginCityCode() + "");
        }
        MFRunner.requestPost(TAG_GETVERSION_URL, MFUrl.GETVERSION_URL, params, this);
    }

    @Override
    public View
    onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            //140
            if (!MFUtil.checkNetwork(mContext)) {
                showNoNetworkDialog(true);
            }

            fragContentView = inflater.inflate(R.layout.frg_main, null);
            unbinder = ButterKnife.bind(this, fragContentView);
            //frgMainMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg_main_map);
            initView();
            initMapView(savedInstanceState);
            showData();
            MFApp.getInstance().addLocationListener(locaTag, this);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCityGpsList();
                    MFApp.getInstance().startLocation();
                }
            }, 650);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RequestUpdata();
                    getADData();
                }
            }, 2500);
            MFStaticConstans.needRefOrder = true;
        }
        return fragContentView;
    }

    private void getADData() {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "2");

        PersonalCenterVo tempVoAD = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                PersonalCenterVo.class);
        if (tempVoAD != null && !TextUtils.isEmpty(tempVoAD.getLoginCityCode())) {
            params.addBodyParameter("cityName", tempVoAD.getCityName());
            params.addBodyParameter("cityCode", tempVoAD.getLoginCityCode());
        } else if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
            params.addBodyParameter("cityName", MFApp.getInstance().getmLocation().getCity());
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
        }
        MFRunner.requestPost(TAG_GETADBYTYPE, MFUrl.GETADBYTYPE, params, this);
    }

    private void initView() {
        frgMianUse.setVisibility(View.VISIBLE);
        frgMianUseAD.setVisibility(View.VISIBLE);
        topMenu.setOnClickListener(this);
        topAct.setOnClickListener(this);
        mBottomHelp.setOnClickListener(this);
        mBottomLoca.setOnClickListener(this);
        mBottomRef.setOnClickListener(this);
        mBottomSearch.setOnClickListener(this);
        mBottomScan.setOnClickListener(this);
        frgMianUse.setOnClickListener(this);
//        frgMianUseAD.setOnClickListener(this);
        initViewpage();
        initTopLayout(0);
        frgMianUse.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (frgMianUse.getLayoutParams() != null) {
                    if (frgMianUse.getHeight() < 1 || topADImangeWidth > 10) {
                        return;
                    }
                    topADImangeHeight = frgMianUse.getHeight() - ScreenUtil.dip2px(mContext, 8);
                    topADImangeWidth = frgMianUse.getWidth() - ScreenUtil.dip2px(mContext, 8);
                    frgMianUseAD.setVisibility(View.GONE);
//                    if (topADImangeHeight > 10 && topADImangeHeight < 150) {
//                        double s = 150 * 1.0 / topADImangeHeight;
//                        topADImangeHeight = 150;
//                        topADImangeWidth = (int) (topADImangeWidth * s);
//                    }

                    RequestParams paramsAD5 = new RequestParams();
                    paramsAD5.addBodyParameter("type", "5");
                    PersonalCenterVo tempVoAD = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
                            PersonalCenterVo.class);
                    if (tempVoAD != null && !TextUtils.isEmpty(tempVoAD.getLoginCityCode())) {
                        paramsAD5.addBodyParameter("cityName", tempVoAD.getCityName());
                        paramsAD5.addBodyParameter("cityCode", tempVoAD.getLoginCityCode());
                    } else if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
                        paramsAD5.addBodyParameter("cityName", MFApp.getInstance().getmLocation().getCity());
                        paramsAD5.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
                    }
                    MFRunner.requestPost(TAG_GETADBYTYPE5, MFUrl.GETADBYTYPE, paramsAD5, MainFrg.this);
                }
            }
        });

    }

    private void initViewpage() {
        mianTopCheckVp = (MyViewPage) fragContentView.findViewById(R.id.mian_top_check_vp);
        mianTopCheckVp.setScrollble(false);
        CheckBookPageAdp CheckBookPageAdp = new CheckBookPageAdp(getChildFragmentManager());
        ArrayList<MFBaseFragment> subList = new ArrayList<MFBaseFragment>();
        subBookingFrg = new SubBookingFrg();
        //subKeepFrg = new SubKeepFrg();
        subBookingFrg.setBookingBikeListener(this);
        subBookingFrg.setA2B(new A2B() {
            @Override
            public int callB(int t, Object o) {
                showOrDismmisTop(true, null);
                if (walkRouteOverlay != null) {
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.setCancelShow(true);
                }
                List<Marker> lists = aMap.getMapScreenMarkers();
                if (lists != null) {
                    for (Marker m : lists) {
                        if (m.getObject() != null) {
                            m.setIcon(getGeneralBikeBD());
                        }
                    }
                }
                return 0;
            }
        });
        subList.add(subBookingFrg);
        //subList.add(subKeepFrg);
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
        mMapLayout.removeAllViews();
        if (MFApp.getInstance().getmLocation() != null) {
            aOptions.camera(new CameraPosition(new LatLng(MFApp.getInstance().getmLocation().getLatitude() + 0.00005, MFApp.getInstance().getmLocation().getLongitude() + 0.00005), 16f, 0, 0));
        }

        mapView = new MapView(mContext, aOptions);
        mapView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mMapLayout.addView(mapView, mParams);
        aMap = mapView.getMap();

        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        UiSettings mUiSettings = aMap.getUiSettings();

        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setZoomInByScreenCenter(true);
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
        aMap.setOnMapClickListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                                        @Override
                                        public void onMapLoaded() {
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

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                                          @Override
                                          public boolean onMarkerClick(Marker marker) {
                                              if (MFUtil.checkNetwork(mContext)) {
                                                  if (walkRouteOverlay != null) {
                                                      walkRouteOverlay.removeFromMap();
                                                      walkRouteOverlay.setCancelShow(false);
                                                  }
                                                  if (marker.getObject() != null) {
                                                      BikeVO bikeVO = (BikeVO) marker.getObject();
                                                      //bikeVO.getId();
                                                      if (bikeVO != null && bikeVO.getGpsXy() != null && MFApp.getInstance().getmLocation() != null) {
                                                          float[] results = new float[1];
                                                          Location.distanceBetween(bikeVO.getGpsXy().getLat(), bikeVO.getGpsXy().getLon(), MFApp.getInstance().getmLocation().getLatitude(), MFApp.getInstance().getmLocation().getLongitude(), results);

                                                          if (results[0] > 100000) {
                                                              MFUtil.showToast(mContext, "只能预约您当前所在城市的小蜜蜂");
                                                          } else {
                                                              mBottomRef.setTag(new BikeTag(bikeVO, marker));
                                                              getBikeDetail(bikeVO);
                                                          }
                                                      }
                                                  }
                                              } else {
                                                  showNoNetworkDialog(true);
                                              }

                                              return true;
                                          }
                                      }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        MFApp.getInstance().addNetChangeListener(locaTag, this);
        if (isforce) {
            return;
        }
        loadUserImage();
        if (mapView != null) {
            mapView.onResume();
        }
        if (sensorHandle != null) {
            sensorHandle.onResume();
        }

        if (needReLoca && (screenMarker == null || !screenMarker.isVisible())) {
            needReLoca = false;
            //重新定位刷新数据
            locaAct();
        }

        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
            initTopLayout(0);
            getHandler().removeMessages(5001);
            mBottomScanBar.setVisibility(View.GONE);
            mBottomScan.setTag(null);
            mBottomScan.setImageResource(R.drawable.main_scan_sel);
//            mainTopCurrentLayout.setVisibility(View.GONE);
        } else {
            getUserCenter();
            if (MFStaticConstans.needRefOrder) {
                getHandler().sendEmptyMessageDelayed(5001, 300);
            }
            if (MFStaticConstans.needRefOrder2Sce) {
                getHandler().sendEmptyMessageDelayed(5002, 30000);
            }
        }
    }

    private long tempTime = 0;

    private void getUserCenter() {
        if (!MFUtil.checkNetwork(mContext)) {
            return;
        }
        if (System.currentTimeMillis() - tempTime < 10000) {
            tempTime = System.currentTimeMillis();
            return;
        }
        tempTime = System.currentTimeMillis();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
            params.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
        }
        MFRunner.requestPost(Tag_PERSONAL_CENTER_URL, MFUrl.PERSONAL_CENTER_URL, params, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MFApp.getInstance().removeNetChangeListener(locaTag);
        MFApp.getInstance().removeLocationListener(locaTag);
        getHandler().removeMessages(5001);
        needReLoca = true;
        if (mapView != null) {
            mapView.onPause();
        }
        if (sensorHandle != null) {
            sensorHandle.onPause();
        }
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
        getHandler().removeMessages(6669);
        if (mapView != null) {
            mapView.onDestroy();
        }
        unbinder.unbind();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.main_top_menu:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    if (act2Main != null) {
                        act2Main.callB(1, null);
                    }
                }
                break;
            case R.id.main_top_act:
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class
                            , null);
                } else {
                    Bundle actBundle = new Bundle();
                    String url = MFUtil.getValueUrl(MFUrl.ACTIVITIESLIST, "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                            , "mobile=" + MFStaticConstans.getUserBean().getUser().getMobile(), "token=" + MFStaticConstans.getUserBean().getToken()
                            , "version=" + MFApp.versionName);
                    actBundle.putString("url", url);
                    startActivity(WebAty.class, actBundle);
                }
                break;
            case R.id.main_bottom_help:
                MobclickAgent.onEvent(mContext, "click_main_feelback");
                toHelp();
                break;
            case R.id.main_bottom_loca:
                locaAct();
                break;
            case R.id.main_bottom_ref:
                showOrDismmisTop(true, null);
                MobclickAgent.onEvent(mContext, "click_main_refresh");
                getHandler().removeMessages(6665);
                getHandler().removeMessages(6666);
                getHandler().sendEmptyMessageDelayed(6665, 500);
                break;
            case R.id.main_bottom_scan:
//                reSetMapScreen();
                if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    startActivity(LoginAct.class, null);
                } else {
                    if (!checkPermission(PermissionHelper.ACCESS_FINE_LOCATION_MODEL)) {
                        MFUtil.showToast(mContext, "检测到没有定位权限，请设置后重试");
                        return;
                    }
                    if (!IMapUtils.isHasPolygon()) {
                        getCityGpsList();
                    }
                    MobclickAgent.onEvent(mContext, "click_main_scan");

                    if (!MFUtil.checkNetwork(mContext)) {
                        showNoNetworkDialog(true);
                        return;
                    }

                    if (view.getTag() != null) {
                        toOrderScreen(view);
                    } else {
                        Bundle scanBundle = new Bundle();
                        scanBundle.putBoolean("needAnim", true);
                        startActivityForResult(ScanAct.class, scanBundle, 1001);
                    }
                }
                break;
            case R.id.title_left_image:
                if (!TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
                    SharedPreferenceTool.setPrefBoolean(mContext, "main_left_image_mark", false);
//                    leftImageMark.setVisibility(View.GONE);
                }
                MobclickAgent.onEvent(mContext, "click_main_percenalcenter");
                startActivity(UserCenterAct.class, null);
                break;
            case R.id.main_bottom_search:
                if (reSetMapScreen()) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MobclickAgent.onEvent(mContext, "click_main_search");
                            startActivityForResult(POISearchAct.class, null, 4004);
                        }
                    }, 500);
                } else {
                    MobclickAgent.onEvent(mContext, "click_main_search");
                    startActivityForResult(POISearchAct.class, null, 4004);
                }
                break;
//            case R.id.main_top_current_layout:
//                MobclickAgent.onEvent(mContext, "click_main_recovery");
//                if (!MFUtil.checkNetwork(mContext)) {
//                    showNoNetworkDialog(true);
//                    return;
//                }
//                toOrderScreen(view);
//                break;
            case R.id.frg_mian_use_ad:
                ADBean tempAD = (ADBean) frgMianUseAD.getTag();
                if (tempAD != null && !TextUtils.isEmpty(tempAD.getAdUrl())) {
                    StringBuffer sbUrl = new StringBuffer();
                    if (MFStaticConstans.getUserBean().getUser() != null) {
//                        String tempCityCode = null;
//                        PersonalCenterVo tempVoAD = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
//                                PersonalCenterVo.class);
//                        if (tempVoAD != null && !TextUtils.isEmpty(tempVoAD.getLoginCityCode())) {
//                            tempCityCode = tempVoAD.getLoginCityCode();
//                        } else if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
//                            tempCityCode = MFApp.getInstance().getmLocation().getAdCode();
//                        }

                        String url = MFUtil.getValueUrl(tempAD.getAdUrl(), "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                                , "mobile=" + MFStaticConstans.getUserBean().getUser().getMobile()
                                , "token=" + MFStaticConstans.getUserBean().getToken());
                        sbUrl.append(url);
                    } else {
                        sbUrl.append(tempAD.getAdUrl());
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("url", sbUrl.toString());
                    bundle.putString("title", tempAD.getTitle());
                    startActivity(WebAty.class, bundle);
                    sendStatistics("3004", "click", MFUtil.getValueForURL("cityCode", tempAD.getAdUrl()), tempAD.getId() + "");
                    sbUrl = null;
                }
                break;
            case R.id.frg_mian_use:
                String tap = (String) view.getTag();
                if (!TextUtils.isEmpty(tap)) {
                    switch (tap) {
                        case "-2":
                            ADBean tempAD2 = (ADBean) frgMianUseAD.getTag();
                            if (tempAD2 != null && !TextUtils.isEmpty(tempAD2.getAdUrl())) {
                                StringBuffer sbUrl = new StringBuffer();
                                if (MFStaticConstans.getUserBean().getUser() != null) {
//                                    String tempCityCode = null;
//                                    PersonalCenterVo tempVoAD = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
//                                            PersonalCenterVo.class);
//                                    if (tempVoAD != null && !TextUtils.isEmpty(tempVoAD.getLoginCityCode())) {
//                                        tempCityCode = tempVoAD.getLoginCityCode();
//                                    } else if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCityCode())) {
//                                        tempCityCode = MFApp.getInstance().getmLocation().getAdCode();
//                                    }

                                    String url = MFUtil.getValueUrl(tempAD2.getAdUrl(), "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                                            , "mobile=" + MFStaticConstans.getUserBean().getUser().getMobile()
                                            , "token=" + MFStaticConstans.getUserBean().getToken());
                                    sbUrl.append(url);
                                } else {
                                    sbUrl.append(tempAD2.getAdUrl());
                                }
                                Bundle bundle = new Bundle();
                                bundle.putString("url", sbUrl.toString());
                                bundle.putString("title", tempAD2.getTitle());
                                startActivity(WebAty.class, bundle);
                                sendStatistics("3004", "click", MFUtil.getValueForURL("cityCode", tempAD2.getAdUrl()), tempAD2.getId() + "");
                                sbUrl = null;
                            }
                            break;
                        case "0":
                            UseCarHelpDialog useCarHelpDialog = new UseCarHelpDialog(mContext);
                            useCarHelpDialog.show();
                            break;
                        case "2":
//                            StringBuffer sb = new StringBuffer(MFUrl.PARTNERACT);
//                            sb.append("?");
//                            if (MFStaticConstans.getUserBean().getUser() != null) {
//                                sb.append("userId=").append(MFStaticConstans.getUserBean().getUser().getId());
//                            }
//                            if (!TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
//                                sb.append("&token=").append(MFStaticConstans.getUserBean().getToken());
//                            }
//                            if (MFApp.getInstance().getmLocation() != null) {
//                                sb.append("&cityCode=").append(MFApp.getInstance().getmLocation().getAdCode());
//                            }
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("url", sb.toString());
//                            bundle.putString("title", "");
//                            startActivity(WebAty.class, bundle);
//                            sb = null;
                            break;
                        default:
                            break;
                    }
                } else {
                    UseCarHelpDialog useCarHelpDialog = new UseCarHelpDialog(mContext);
                    useCarHelpDialog.show();
                }
                break;
        }
    }

    @Override
    public void onLocation(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (!IMapUtils.isHasPolygon()) {
                getCityGpsList();
            }
            getCityGpsList(1);
            if (!isHasPartnerInfo) {
                getBikeIcons(aMapLocation.getAdCode());
            }
            if (locationChangedListener != null) {
                locationChangedListener.onLocationChanged(aMapLocation);
            }
//            MFUtil.showToast(mContext, aMapLocation.getCity());
            MFApp.getInstance().removeLocationListener(locaTag);
            final LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            if (locaMarker == null) {
                locaMarker = new MarkerOptions();
                addMarkerInScreenCenter();
//                addArea();
            }
            locaMarker.position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.drawable.icon_loca))).anchor(0.5f, 0.5f);

            //aMap.clear();
            //addArea();2017
            clearMarks();//zhangyu2016

            // final Marker marker = aMap.setMyLocationStyle(locaMarker);
//            titleTitle.setTag(null);
            locaS = true;
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng), 500, new AMap.CancelableCallback() {
                @Override
                public void onFinish() {
//                    MFUtil.showToast(mContext, "onFinish");
                    locaS = false;
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getHandler().removeMessages(6666);
                            CameraUpdate up = CameraUpdateFactory.zoomTo(19);
                            aMap.animateCamera(up, 500, new AMap.CancelableCallback() {
                                @Override
                                public void onFinish() {
//                                    if (titleTitle.getTag() == null) {
//                                        getHandler().removeMessages(6665);
//                                        getHandler().removeMessages(6666);
//                                        getHandler().sendEmptyMessageDelayed(6665, 500);
//                                    }
                                }

                                @Override
                                public void onCancel() {
                                    getHandler().removeMessages(6665);
                                    getHandler().removeMessages(6666);
                                    getHandler().sendEmptyMessageDelayed(6665, 500);
                                }
                            });
                        }
                    }, 250);
                }

                @Override
                public void onCancel() {
//                    MFUtil.showToast(mContext, "onCancel");
                    locaS = false;
                }
            });
            getHandler().sendEmptyMessageDelayed(6667, 650);
            if (!TextUtils.isEmpty(aMapLocation.getAdCode())) {
                if (isHasGetBikeIcons < 10) {
                    Message message = new Message();
                    message.what = 5004;
                    Bundle bundle = new Bundle();
                    bundle.putString("cityCode", aMapLocation.getAdCode());
                    message.setData(bundle);
                    getHandler().sendMessageDelayed(message, 350);
                }

                if (!isHasPartnerInfo) {
                    Message message = new Message();
                    message.what = 5003;
                    Bundle bundle = new Bundle();
                    bundle.putString("cityCode", aMapLocation.getAdCode());
                    message.setData(bundle);
                    getHandler().sendMessageDelayed(message, 680);
                }
            }


        }
    }

    /**
     * 定位按钮
     */
    private void locaAct() {
        if (!checkPermission(PermissionHelper.ACCESS_FINE_LOCATION_MODEL)) {
            return;
        }
        showOrDismmisTop(true, null);
        aMap.setOnCameraChangeListener(this);
        MFApp.getInstance().addLocationListener(locaTag, this);
        MFApp.getInstance().startLocation();
    }


    private void toOrderScreen(View view) {
        OrderDetailVO tempVo = (OrderDetailVO) view.getTag();
        if (tempVo != null) {
            if ("1".equals(tempVo.getState())) {
                startActivityForResult(BookIngAct.class, null, 3001);
            } else if ("2".equals(tempVo.getState())) {
                startActivity(CurrentOrderAct.class, null);
            }
        }
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

//    private void clearPolygon() {
//
//        List<Marker> ms = aMap.getMapScreenMarkers();
//        for (Marker m : ms) {
//            if (m != null && m.getObject() != null) {
//            } else if (m != null) {
//                m.remove();
//            }
//        }
//    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        //up();
    }

    @Override
    public void onCameraChangeFinish(CameraPosition camera) {
//        MFUtil.showToast(mContext, "onCameraChangeFinish");
        getHandler().removeMessages(6666);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getHandler().sendEmptyMessageDelayed(6666, 500);
            }
        }, 500);

        if (camera.zoom > 14f) {
            addArea(false);
        } else {
            addArea(true);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4004 && resultCode == Activity.RESULT_OK) {


            if (data != null) {
                Tip tip = data.getParcelableExtra("ADR");
                if (tip != null) {
                    LatLng finalLatLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                    final CameraUpdate up = CameraUpdateFactory.newLatLngZoom(finalLatLng, 19);
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aMap.animateCamera(up, 1000, new AMap.CancelableCallback() {
                                @Override
                                public void onFinish() {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                    }, 250);
                }

            }
        } else if (requestCode == 1001) {
            getHandler().sendEmptyMessageDelayed(5001, 5000);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getHandler().sendEmptyMessageDelayed(5001, 5000);
                }
            }, 10000);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getHandler().sendEmptyMessageDelayed(5001, 5000);
                }
            }, 20000);
        } else if (requestCode == 3001 && resultCode == Activity.RESULT_OK) {
            MFStaticConstans.hasToOrder = true;
            mBottomScanBar.setVisibility(View.GONE);
            mBottomScan.setTag(null);
            mBottomScan.setImageResource(R.drawable.main_scan_sel);
//            mainTopCurrentLayout.setTag(null);
//            mainTopCurrentLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        screenMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_loca_center)));
        screenMarker.setToTop();
        screenMarker.setZIndex(9999);
//        screenMarker.setObject(new String("123"));

        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

    }


    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation(boolean update) {

        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            if (!update && latLng != null) {
                if (!needRefBikes(latLng)) {
                    return;
                }
            }
//            titleTitle.setTag(screenMarker.getPosition());
            Point point = aMap.getProjection().toScreenLocation(latLng);
            if (point == null) {
                return;
            }

            if (mBottomRefIV != null) {
                android.view.animation.Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.ref_rotate);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
//                mBottomRefIV.startAnimation(operatingAnim);
            }

            point.y -= ScreenUtil.dip2px(mContext, 40);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(400);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    loadBikes(latLng);

                }
            });
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e("ama", "screenMarker is null");
        }
    }


    private void loadBikes(LatLng latLng) {

        RequestParams loadParams = new RequestParams();
        loadParams.addBodyParameter("longitude", latLng.longitude + "");
        loadParams.addBodyParameter("latitude", latLng.latitude + "");
        if (MFApp.getInstance().getmLocation() != null) {
            loadParams.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
        }
        loadParams.addBodyParameter("radius", 1000 + "");
        MFRunner.requestPost(2001, MFUrl.getBikeList, loadParams, this);
    }

    private void showData() {
//        titleLeftImage.setVisibility(View.VISIBLE);
//        titleLeftImage.setImageResource(R.drawable.main_userimage_sel);
//        titleLeftImage.setScaleType(ImageView.ScaleType.FIT_START);
//        titleLeftImage.setPadding(ScreenUtil.dip2px(mContext, 10),
//                ScreenUtil.dip2px(mContext, 12),
//                ScreenUtil.dip2px(mContext, 20),
//                ScreenUtil.dip2px(mContext, 12)
//        );
//        titleRightImage.setVisibility(View.VISIBLE);
//        titleRightImage.setImageResource(R.drawable.main_search_sel);
//        titleRightImage.setOnClickListener(this);
    }

    private boolean showOrDismmisTop(boolean dismmis, BikeDetailVO bikeVO) {
        boolean hasAnim = false;
        BikeVO temp = null;
        canRefList = dismmis;
        if (bikeVO != null && !TextUtils.isEmpty(bikeVO.getGpsX())) {
            temp = new BikeVO();
            temp.setCode(bikeVO.getBikeCode());
            temp.setGpsXy(new GpsXy(Double.valueOf(bikeVO.getGpsX()), Double.valueOf(bikeVO.getGpsY())));
        }
        if (dismmis) {
            if (mianTopCheckVp.getY() == 0) {
                ObjectAnimator translationUp = ObjectAnimator.ofFloat(mianTopCheckVp, "Y", -mianTopCheckVp.getHeight());
                translationUp.setDuration(350);
                translationUp.start();
//                titleTitle.setText(getResources().getText(R.string.app_name));
                hasAnim = true;
            }
            if (screenMarker != null && screenMarker.isVisible()) {
                CameraUpdate up = CameraUpdateFactory.changeLatLng(screenMarker.getPosition());
                aMap.animateCamera(up, 1000, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        screenMarker.remove();
                        addMarkerInScreenCenter();
                        aMap.setOnCameraChangeListener(MainFrg.this);
                    }

                    @Override
                    public void onCancel() {
                        screenMarker.remove();
                        addMarkerInScreenCenter();
                        aMap.setOnCameraChangeListener(MainFrg.this);
                    }
                });
                hasAnim = true;
            }
        } else {
            ObjectAnimator translationUp = ObjectAnimator.ofFloat(mianTopCheckVp, "Y", 0);
            translationUp.setDuration(350);
            translationUp.start();
//            titleTitle.setText("NO." + bikeVO.getBikeCode());
            subBookingFrg.showData(bikeVO);
            hasAnim = true;
        }

        if (walkRouteOverlay != null) {
            walkRouteOverlay.removeFromMap();
        }

        if (temp != null) {
            if (subBookingFrg != null) {
                subBookingFrg.showDis(-1, -1);
            }
            showRoute(temp);
        } else {

        }
        return hasAnim;
    }

    private void showRoute(final BikeVO bikeVO) {

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (locaMarker != null && bikeVO != null) {
                    LatLonPoint startPoint = new LatLonPoint(locaMarker.getPosition().latitude, locaMarker.getPosition().longitude);
                    LatLonPoint endPoint = new LatLonPoint(bikeVO.getGpsXy().getLat(), bikeVO.getGpsXy().getLon());
                    RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
                    RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
                    if (mRouteSearch == null) {
                        mRouteSearch = new RouteSearch(mContext);
                        mRouteSearch.setRouteSearchListener(MainFrg.this);
                    }
                    mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
                }
            }
        }, 200);


    }


    private boolean needRefBikes(LatLng latLng) {
//        Object o = titleTitle.getTag();
//        if (latLng != null && screenMarker != null && titleTitle.getTag() != null) {
//            LatLng oldLatLng = (LatLng) titleTitle.getTag();
//            float[] results = new float[1];
//            Location.distanceBetween(latLng.latitude, latLng.longitude, oldLatLng.latitude, oldLatLng.longitude, results);
//            if (results[0] <= 100.0f) {
//                return false;
//            }
//        } else if (latLng == null) {
//            return false;
//        }
        return true;
    }


    private void toHelp() {
        if (costomerCenterDialog == null) {
            costomerCenterDialog = new CostomerCenterDialog(mContext, this);
        }
        costomerCenterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ObjectAnimator animatorrlllMore = ObjectAnimator.ofFloat(llBottomlay,
                        "TranslationY", 0);
                animatorrlllMore.setDuration(400);
                animatorrlllMore.start();
                ScaleAnimation scaleAnimationrlScan = new ScaleAnimation(0, 1f, 0, 1f,
                        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimationrlScan.setDuration(400);
                scaleAnimationrlScan.setFillAfter(true);
                rlScan.startAnimation(scaleAnimationrlScan);
            }
        });
        costomerCenterDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ObjectAnimator animatorrlllMore = ObjectAnimator.ofFloat(llBottomlay,
                        "TranslationY", ScreenUtil.getScreenHeigth(mContext));
                animatorrlllMore.setDuration(400);
                animatorrlllMore.start();
                ScaleAnimation scaleAnimationrlScan = new ScaleAnimation(1, 0f, 1, 0f,
                        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimationrlScan.setDuration(400);
                scaleAnimationrlScan.setFillAfter(true);
                rlScan.startAnimation(scaleAnimationrlScan);
            }
        });
        costomerCenterDialog.setCostomerCenterDlgClickListener(new CostomerCenterDialog.CostomerCenterDlgClickListener() {
            @Override
            public void onLeft(View v, Dialog d) {
                String token = MFStaticConstans.getUserBean().getToken();
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class, null);
                    d.dismiss();
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_main_feelback_openlock");
                Intent intent = new Intent(mContext, TripFeedbackDetailAct.class);
                Bundle bundle = new Bundle();
                bundle.putString("pagetype", MFConstansValue.FEELBACK_MAIN_BREAK);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                d.dismiss();
            }

            @Override
            public void onRight(View v, Dialog d) {
                String token = MFStaticConstans.getUserBean().getToken();
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginAct.class, null);
                    d.dismiss();
                    return;
                } else {
                    MobclickAgent.onEvent(mContext, "click_main_feelback_stop");
                    Intent intent = new Intent(mContext, TripFeedbackDetailAct.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("pagetype", MFConstansValue.FEELBACK_MAIN_STOPCAR);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    d.dismiss();
                }
            }
        });
        costomerCenterDialog.show();
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {
        switch (i) {
            case 3002:
                dialogShow();
                break;
        }
    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        switch (i) {
            case TAG_GETADBYTYPE:
                if (!isAdded()) {
                    return;
                }
                String getAdDataInfoString = responseInfo.result.toString();
                RequestResultBean<ADBean> adBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(getAdDataInfoString, new TypeToken<RequestResultBean<ADBean>>() {
                }.getType());
                if (adBeanRequestResultBean == null || adBeanRequestResultBean.getData() == null || MFConstansValue.BACK_SUCCESS != adBeanRequestResultBean.getCode()) {
                    return;
                }
                adBean = adBeanRequestResultBean.getData();
                long lastShowTime = 0;
                if (!TextUtils.isEmpty(SharedPreferenceTool.getPrefString(mContext, "adtype2", ""))) {
                    String[] adInfo = SharedPreferenceTool.getPrefString(mContext, "adtype2", "").split("，");
                    if (adInfo != null && adInfo.length != 2) {
                        return;
                    }
                    Long adId = Long.parseLong(adInfo[0]);
                    if (adBean.getId() == adId) {
                        lastShowTime = Long.parseLong(adInfo[1]);
                    } else {
                        SharedPreferenceTool.setPrefString(mContext, "adtype2", "");
                    }
                }
                if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && (new Date().getTime() - lastShowTime) >
                        adBean.getActiveFrequency() * 1000 * 60) {
                    String imgrul = null;
                    if (!TextUtils.isEmpty(adBean.getPhotoUrl()) && adBean.getPhotoUrl().contains("oss")) {
                        imgrul = adBean.getPhotoUrl().replace("oss", "img") + "@500w.jpg";
                    } else {
                        imgrul = adBean.getPhotoUrl();
                    }

                    ImageLoader.getInstance().loadImage(imgrul, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            SharedPreferenceTool.setPrefString(mContext, "adtype2", adBean.getId() + "，" + new Date().getTime());
                            sendStatistics("3001", "show", MFUtil.getValueForURL("cityCode", adBean.getAdUrl()), adBean.getId() + "");
                            final ADDialog adDialog = new ADDialog(mContext);
                            adDialog.setImageUrl(s);
                            adDialog.setAdBean(adBean);
                            adDialog.setBaseFragment(MainFrg.this);
                            adDialog.setCanceledOnTouchOutside(false);
                            getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adDialog.show();
                                }
                            }, 3000);

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }
                break;
            case TAG_GETADBYTYPE5:
                if (!isAdded()) {
                    return;
                }
                RequestResultBean<ADBean> adBeanRequest = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<ADBean>>() {
                }.getType());
                if (adBeanRequest == null || adBeanRequest.getData() == null || MFConstansValue.BACK_SUCCESS != adBeanRequest.getCode()) {
                    return;
                }
                if (adBeanRequest.getCode() == 0 && adBeanRequest.getData() != null) {
                    ADBean tempAD = adBeanRequest.getData();
                    if (tempAD != null) {
                        frgMianUseAD.setTag(tempAD);
                        initTopLayout(-2);
                    }
                }
                break;
            case 1901:
                //是否有未完成的订单
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                RequestResultBean<OrderDetailVO> unFinish = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<OrderDetailVO>>() {
                }.getType());

                if (unFinish != null) {
                    getHandler().removeMessages(6669);
                    switch (unFinish.getCode()) {
                        case 0:
                            String code = null;
                            List<NameValuePair> list = requestParams.getBodyParams();
                            for (NameValuePair nvp : list) {
                                if ("bikeCode".equals(nvp.getName())) {
                                    code = nvp.getValue();
                                    break;
                                }
                            }

                            if (unFinish.getData() == null || TextUtils.isEmpty(unFinish.getData().getOrderId())) {
                                MFStaticConstans.hasToOrder = true;
                                mBottomScanBar.setVisibility(View.GONE);
                                mBottomScan.setTag(null);
                                mBottomScan.setImageResource(R.drawable.main_scan_sel);
//                                mainTopCurrentLayout.setVisibility(View.GONE);
                                getHandler().removeMessages(5001);
//                                mainTopCurrentLayout.setTag(null);
                            } else if ("2".equals(unFinish.getData().getState())) {
                                mBottomScanBar.setVisibility(View.VISIBLE);
                                mBottomScan.setTag(unFinish.getData());
                                mBottomScan.setImageResource(R.drawable.main_scan_ording_sel);

//                                mainTopCurrentLayout.setVisibility(View.VISIBLE);
//                                mainTopCurrentLayout.setTag(unFinish.getData());
                                if (!MFStaticConstans.hasToOrder) {
                                    MFStaticConstans.hasToOrder = true;
                                    toOrderScreen(mBottomScan);
                                }
                            } else if ("1".equals(unFinish.getData().getState())) {
                                mBottomScanBar.setVisibility(View.VISIBLE);
                                mBottomScan.setTag(unFinish.getData());
                                mBottomScan.setImageResource(R.drawable.main_scan_book_sel);
//                                mainTopCurrentLayout.setVisibility(View.VISIBLE);
//                                mainTopCurrentLayout.setTag(unFinish.getData());
                                if (!MFStaticConstans.hasToOrder) {
                                    MFStaticConstans.hasToOrder = true;
                                    toOrderScreen(mBottomScan);
                                }

                                long timess = 0;
                                if (!TextUtils.isEmpty(unFinish.getData().getServerDate()) && !TextUtils.isEmpty(unFinish.getData().getPlaceOrderTime())) {
                                    timess = 5 + 10 * 60 - (Long.valueOf(unFinish.getData().getServerDate()) - Long.valueOf(unFinish.getData().getPlaceOrderTime())) / 1000;
                                }

                                if (timess > 0) {
                                    // getHandler().removeMessages(5001);
                                    getHandler().sendEmptyMessageDelayed(6669, timess * 1000);
                                }


                            } else {
                                MFStaticConstans.hasToOrder = true;
                                mBottomScanBar.setVisibility(View.GONE);
                                mBottomScan.setTag(null);
                                mBottomScan.setImageResource(R.drawable.main_scan_sel);
//                                mainTopCurrentLayout.setTag(null);
//                                mainTopCurrentLayout.setVisibility(View.GONE);
                                if (("3".equals(unFinish.getData().getState()) || "0".equals(unFinish.getData().getState())) && "0".equals(unFinish.getData().getViewType())) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("OrderDetailVO", unFinish.getData());
                                    startActivity(RunEndAct.class, bundle);
                                    getHandler().removeMessages(5001);
                                } else {
                                    //开锁
                                    // unlockBikeInOrder(unFinish.getData(), code);
                                    getHandler().removeMessages(5001);
                                }
                            }
                            break;
                        case 1001:
                            MFStaticConstans.hasToOrder = true;
                            mBottomScanBar.setVisibility(View.GONE);
                            mBottomScan.setTag(null);
                            mBottomScan.setImageResource(R.drawable.main_scan_sel);
//                            mainTopCurrentLayout.setTag(null);
//                            mainTopCurrentLayout.setVisibility(View.GONE);
                            break;
                        default:
                            MFStaticConstans.hasToOrder = true;
                            break;
                    }
                } else {
                    //showBookFailureDialog("数据异常");
                }

                break;
            case 2001:
                if (!isAdded()) {
                    return;
                }
                mBottomRefIV.clearAnimation();

                if (!canRefList) {
                    canRefList = true;//为防止被锁死，要立即设置为true.
                    return;
                }

                //单车列表
                RequestResultListBean<BikeVO> listBean = GsonTransformUtil.getObjectListFromJson(responseInfo.result.toString(), new TypeToken<RequestResultListBean<BikeVO>>() {
                }.getType());
                if (listBean != null) {
                    if (0 == listBean.getCode()) {
                        List<BikeVO> bikeVOs = listBean.getData();
                        if (bikeVOs != null && bikeVOs.size() > 0) {
                            addBikesMark(bikeVOs, 1);
                        } else {
                            addBikesMark(bikeVOs, 1);
                        }
                    }
                }

                break;
            case TAG_GETVERSION_URL:
                if (!isAdded()) {
                    return;
                }
                RequestResultBean<UpdataBean> updataBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<UpdataBean>>() {
                }.getType());
                if (updataBeanRequestResultBean != null && 0 == updataBeanRequestResultBean.getCode()) {
                    final UpdataBean updataBean = updataBeanRequestResultBean.getData();
                    if (updataBean != null) {
                        String isupdataCode = SharedPreferenceTool.getPrefString(mContext, "isupdata", "");
                        if (SystemUtils.getVersionCode(mContext) < updataBean.getVersionCode() && !isupdataCode.equals(updataBean.getVersionCode() + "") && updataBean.getForces() == 0) {
                            UpdateDialog updateDialog = new UpdateDialog(mContext, updataBean.getVersions() + "更新包已准备好", updataBean.getDescription() + "", "残忍拒绝", "极速升级");
                            updateDialog.setTag(updataBean);
                            updateDialog.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                    SharedPreferenceTool.setPrefString(mContext, "isupdata", updataBean.getVersionCode() + "");
                                    d.dismiss();
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {
                                    if (ApkDownLoad.isDownloadManagerAvailable(mContext)) {
                                        if (checkPermission(PermissionHelper.WRITE_EXTERNAL_STORAGE_MODEL)) {
                                            if (new ApkDownLoad(mContext, ((UpdataBean) ((UpdateDialog) d).getTag()).getUrl(), ((UpdataBean) ((UpdateDialog) d).getTag()).getVersionCode() + "", "蜜蜂出行", "版本升级")
                                                    .execute()) {
                                                MFUtil.showToast(mContext, "正在更新，请稍后...");
                                            }
                                        }
                                    } else {
                                        ApkDownLoad.toWEB(mContext, ((UpdataBean) ((UpdateDialog) d).getTag()).getUrl());
                                    }
                                    d.dismiss();
                                }
                            });
                            updateDialog.show();
                        } else if (SystemUtils.getVersionCode(mContext) < updataBean.getVersionCode() && updataBean.getForces() == 1) {
                            UpdateDialog updateDialog = new UpdateDialog(mContext, updataBean.getVersions() + "更新包已准备好", updataBean.getDescription() + "", "", "极速升级");
                            updateDialog.setTag(updataBean);
                            updateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return keyCode == KeyEvent.KEYCODE_BACK
                                            && event.getAction() == KeyEvent.ACTION_DOWN;
                                }
                            });
                            updateDialog.setDialogClickListener(new DialogClickListener() {
                                @Override
                                public void onLeftClick(View v, Dialog d) {
                                }

                                @Override
                                public void onRightClick(View v, Dialog d) {

                                    if (ApkDownLoad.isDownloadManagerAvailable(mContext)) {
                                        if (checkPermission(PermissionHelper.WRITE_EXTERNAL_STORAGE_MODEL)) {
                                            new ApkDownLoad(mContext, ((UpdataBean) ((UpdateDialog) d).getTag()).getUrl(), ((UpdataBean) ((UpdateDialog) d).getTag()).getVersionCode() + "", "蜜蜂出行", "版本升级")
                                                    .execute();
                                            d.dismiss();
                                            dialogShow("正在更新，请稍等...", true);
                                            isforce = true;
                                        }
                                    } else {
                                        ApkDownLoad.toWEB(mContext, ((UpdataBean) ((UpdateDialog) d).getTag()).getUrl());
                                    }

                                }
                            });
                            updateDialog.show();
                        }
                        break;
                    }
                }
                break;
            case 3001:
                if (!isAdded()) {
                    return;
                }

                RequestResultListBean<ArrayList<AreaPoint>> poings = GsonTransformUtil.getObjectListFromJson(responseInfo.result.toString(), new TypeToken<RequestResultListBean<ArrayList<AreaPoint>>>() {
                }.getType());
                if (poings != null && 0 == poings.getCode() && poings.getData() != null && poings.getData().
                        size() > 0) {
                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_KEY_AREAPOINT, responseInfo.result.toString());
                } else if (poings != null && 0 != poings.getCode()) {
                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_KEY_AREAPOINT, "");
                }
                break;
            case 3011:
                if (!isAdded()) {
                    return;
                }

                RequestResultListBean<ArrayList<AreaPoint>> noParkingPoints = GsonTransformUtil.getObjectListFromJson(responseInfo.result.toString(), new TypeToken<RequestResultListBean<ArrayList<AreaPoint>>>() {
                }.getType());
                if (noParkingPoints != null && 0 == noParkingPoints.getCode() && noParkingPoints.getData() != null && noParkingPoints.getData().
                        size() > 0) {
                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_KEY_AREAPOINT_NOPARKING, responseInfo.result.toString());
                } else if (noParkingPoints != null) {
                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_KEY_AREAPOINT_NOPARKING, "");
                }
                break;
            case 3002:
                if (!isAdded()) {
                    return;
                }

                dismmisDialog();

                RequestResultBean<BikeDetailVO> detail = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<BikeDetailVO>>() {
                }.getType());
                if (detail != null && detail.getCode() == 0) {
                    BikeDetailVO bikeVO = detail.getData();
                    if (bikeVO != null) {
                        BikeTag bt = (BikeTag) mBottomRef.getTag();
                        if (bt != null && bikeVO.getBikeCode().equals(bt.getBikeVO().getCode())) {
                            List<Marker> lists = aMap.getMapScreenMarkers();
                            if (lists != null) {
                                for (Marker m : lists) {
                                    if (m.getObject() != null) {
                                        if (bt.getBikeMarker().equals(m)) {
                                            showOrDismmisTop(false, bikeVO);
                                            bt.getBikeMarker().setIcon(getCurrentGeneralBikeBD());
                                        } else if (locaMarker.equals(m) || screenMarker.equals(m)) {

                                        } else {
                                            m.setIcon(getGeneralBikeBD());
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    MFUtil.showToast(mContext, "数据错误");
                }
                break;
            case 3021:
                if (!isAdded()) {
                    return;
                }

                RequestResultBean<CityInfoVO> cityInfoBean = GsonTransformUtil.getObjectFromJson(responseInfo.result.toString(), new TypeToken<RequestResultBean<CityInfoVO>>() {
                }.getType());
                boolean tempIsHasPartner = false;
                if (cityInfoBean != null) {
                    SharedPreferenceTool.setPrefInt(mContext, MFConstansValue.SP_KEY_HASRECHARGEACTIVITY, cityInfoBean.getData().getHasRechargeActivity());
                    isHasPartnerInfo = true;
                    if (cityInfoBean.getCode() == 0 && cityInfoBean.getData() != null) {
                        if (!TextUtils.isEmpty(cityInfoBean.getData().getPartnerState()) && "1".equals(cityInfoBean.getData().getPartnerState())) {
                            tempIsHasPartner = true;
                            initTopLayout(2);
                        } else {

                        }
                    }
                }
                if (!tempIsHasPartner) {
                    getBaseHandler().sendEmptyMessage(7001);
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
            case Tag_PERSONAL_CENTER_URL:
                if (!isAdded()) {
                    return;
                }

                String personString = responseInfo.result.toString();
                RequestResultBean<PersonalCenterVo> personResultBean = GsonTransformUtil.getObjectFromJson(personString, new TypeToken<RequestResultBean<PersonalCenterVo>>() {
                }.getType());
                if (personResultBean != null && MFConstansValue.BACK_SUCCESS == personResultBean.getCode()) {
                    PersonalCenterVo personalCenterVo = personResultBean.getData();
                    SharedPreferenceTool.setPrefString(mContext, MFConstansValue.SP_KEY_PERSONRESULTBEAN, GsonTransformUtil.toJson(personalCenterVo));
                    getBaseHandler().sendEmptyMessageDelayed(7001, 5000);
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
            case 2001:
                mBottomRefIV.clearAnimation();
                break;
            case 3002:
                dismmisDialog();
                break;
            case 3021:
                getBaseHandler().sendEmptyMessage(7001);
                break;
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


    @Override
    public void onMapClick(LatLng latLng) {
//        getAdressByLatLon();
        showOrDismmisTop(true, null);
        if (walkRouteOverlay != null) {
            walkRouteOverlay.removeFromMap();
        }

        List<Marker> lists = aMap.getMapScreenMarkers();
        if (lists != null) {
            for (Marker m : lists) {
                if (m.getObject() != null) {
                    m.setIcon(getGeneralBikeBD());
                }
            }
        }
    }


    private boolean reSetMapScreen() {
        boolean hasAnim = showOrDismmisTop(true, null);
        if (walkRouteOverlay != null) {
            walkRouteOverlay.removeFromMap();
        }

        if (aMap != null) {
            List<Marker> lists = aMap.getMapScreenMarkers();
            if (lists != null) {
                for (Marker m : lists) {
                    if (m.getObject() != null) {
                        m.setIcon(getGeneralBikeBD());
                    }
                }
            }
        }
        return hasAnim;
    }

    private void addBikesMark(List<BikeVO> bikeVOs, int type) {
        //aMap.clear();
        // addArea();2017
        clearMarks();
        //  aMap.addMarker(locaMarker);
        if (bikeVOs == null) {
            return;
        }
        for (BikeVO bike : bikeVOs) {
            if (bike == null) {
                continue;
            }

            LatLng LatLng = new LatLng(bike.getGpsXy().getLat(), bike.getGpsXy().getLon(), true);
            Marker marker = aMap.addMarker(new MarkerOptions().position(LatLng).icon(getGeneralBikeBD())
                    .anchor(0.5f, 1f));


            if (marker != null) {
                marker.setObject(bike);
                marker.setZIndex(5000);

                Animation animation = new com.amap.api.maps.model.animation.ScaleAnimation(0, 1, 0, 1);
                animation.setInterpolator(new AnticipateOvershootInterpolator());
                //整个移动所需要的时间
                animation.setDuration(380);
                //设置动画z
                marker.setAnimation(animation);
                //开始动画
                marker.startAnimation();
            }
        }

        //addMarkerInScreenCenter();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        //aMap.clear();// 清理地图上的所有覆盖物

        if (walkRouteOverlay != null) {
            walkRouteOverlay.removeFromMap();
        }
        if (walkRouteOverlay != null && walkRouteOverlay.isCancelShow()) {
            return;
        }

        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {

                    if (screenMarker != null && screenMarker.isVisible()) {
                        screenMarker.remove();
                        screenMarker = aMap.addMarker(new MarkerOptions()
                                .anchor(0.5f, 0.5f).position(screenMarker.getPosition())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_loca_center)));
                        screenMarker.setZIndex(9999);
//                        screenMarker.setObject(new String("123"));
                        //screenMarker.setToTop();
                    }

                    final WalkPath walkPath = result.getPaths()
                            .get(0);
                    walkRouteOverlay = new WalkRouteOverlay(
                            mContext, aMap, walkPath,
                            result.getStartPos(),
                            result.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    walkRouteOverlay.setCancelShow(false);
                    //mBottomLayout.setVisibility(View.VISIBLE);
                    if (subBookingFrg != null) {
                        subBookingFrg.showDis(walkPath.getDistance(), walkPath.getDuration());
                    }
                    aMap.setOnCameraChangeListener(null);
                } else if (result != null && result.getPaths() == null) {
                    showWalkRouteFaild();
                    MFUtil.showToast(mContext, "路径规划失败");
                }
            } else {
                showWalkRouteFaild();
                MFUtil.showToast(mContext, "路径规划失败");
            }
        } else {
            showWalkRouteFaild();
            MFUtil.showToast(mContext, "路径规划失败");
        }

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onBookingBike(int state, BikeDetailVO bikeVO) {

        switch (state) {
            case 0:
                break;
            case 1:
                Bundle bundle = new Bundle();
                bundle.putString("BikeCode", bikeVO.getBikeCode());
                startActivityForResult(BookIngAct.class, bundle, 3001);
                // mianTopCheckVp.setCurrentItem(1);
                break;
        }
    }


    private void showWalkRouteFaild() {
        if (subBookingFrg != null) {
            subBookingFrg.showDis(-2, -2);
        }

//        List<Marker> lists = aMap.getMapScreenMarkers();
//        if (lists != null) {
//            for (Marker m : lists) {
//                if (m.getObject() != null) {
//                    m.setIcon(getGeneralBikeBD());
//                }
//            }
//        }
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showOrDismmisTop(true, null);
//            }
//        }, 350);
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
            getHandler().sendEmptyMessageDelayed(5001, 20000);
        }
    }


    private void getCityGpsList() {
        RequestParams detailParams = new RequestParams();
        if (MFApp.getInstance().getmLocation() != null) {
            detailParams.addBodyParameter("cityName", MFApp.getInstance().getmLocation().getCity());
            detailParams.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
            detailParams.addBodyParameter("fenceType", "0");
            MFRunner.requestPost(3001, MFUrl.getCityGpsList, detailParams, this);
        }
    }

    private void getCityGpsList(int fenceType) {
        RequestParams detailParams = new RequestParams();
        if (MFApp.getInstance().getmLocation() != null) {
            detailParams.addBodyParameter("cityName", MFApp.getInstance().getmLocation().getCity());
            detailParams.addBodyParameter("cityCode", MFApp.getInstance().getmLocation().getAdCode());
            detailParams.addBodyParameter("fenceType", fenceType + "");
            switch (fenceType) {
                case 0:
                    MFRunner.requestPost(3001, MFUrl.getCityGpsList, detailParams, this);
                    break;
                case 1:
                    MFRunner.requestPost(3011, MFUrl.getCityGpsList, detailParams, this);
                    break;
                default:
                    MFRunner.requestPost(3001, MFUrl.getCityGpsList, detailParams, this);
                    break;
            }
        }
    }


    private void addArea(boolean showContext) {
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
                    //                    // 添加 多边形的每个顶点（顺序添加）6
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
    }

    private void getBikeDetail(BikeVO b) {
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        detailParams.addBodyParameter("bikeCode", b.getCode());
        //detailParams.addBodyParameter("token", MFStaticConstans.getUserBean().getToken());
        MFRunner.requestPost(3002, MFUrl.getBikeDetail, detailParams, this);
    }


    private void loadUserImage() {
//        if (TextUtils.isEmpty(MFStaticConstans.getUserBean().getToken())) {
//            titleLeftImage.setImageResource(R.drawable.user_default_img_n1);
//            initTopLayout(0);
//        } else {
//            PersonalCenterVo personalCenterVo = (PersonalCenterVo) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, ""),
//                    PersonalCenterVo.class);
//            if (personalCenterVo == null || TextUtils.isEmpty(personalCenterVo.getHeadPic())) {
//                if (!TextUtils.isEmpty(MFStaticConstans.getUserBean().getUser().getHeadPic())) {
//                    String imgUrl = MFStaticConstans.getUserBean().getUser().getHeadPic() + "?x-oss-process=image/circle,r_1000/format,png";
//                    ImageLoader.getInstance().displayImage(imgUrl, titleLeftImage, MFOptions.Option_UserPhoto);
//                } else {
//                    titleLeftImage.setImageResource(R.drawable.user_photo_selector);
//                }
//            } else {
//                String imgUrl = personalCenterVo.getHeadPic() + "?x-oss-process=image/circle,r_1000/format,png";
//                ImageLoader.getInstance().displayImage(imgUrl, titleLeftImage, MFOptions.Option_UserPhoto);
//            }
//
//            if (SharedPreferenceTool.getPrefBoolean(mContext, "main_left_image_mark", true)) {
//                leftImageMark.setVisibility(View.VISIBLE);
//            } else {
//                leftImageMark.setVisibility(View.GONE);
//            }
//        }

    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        locationChangedListener = null;
    }

    @Override
    public void onNetChange(int type) {
        switch (type) {
            case 0:
                MFUtil.showToast(mContext, getResources().getString(R.string.nonet));
                break;
            case 1:
            case 2:
                getHandler().sendEmptyMessageDelayed(5001, 1000);
                break;

        }
    }

    /**
     * 獲取一般bike圖標
     *
     * @return
     */
    private BitmapDescriptor getGeneralBikeBD() {
        if (generalBikeBD == null) {
            if (!TextUtils.isEmpty(IMapUtils.getBikeIconVO().getIcon_android())) {
                generalBikeBD = BitmapDescriptorFactory.fromBitmap(ImageLoader.getInstance().loadImageSync(IMapUtils.getBikeIconVO().getIcon_android()));
            } else {
                generalBikeBD = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.icon_bike_st1));
            }
        }
        return generalBikeBD;
    }


    /**
     * 獲取当前bike圖標
     *
     * @return
     */
    private BitmapDescriptor getCurrentGeneralBikeBD() {
        if (currentGeneralBikeBD == null) {
            if (!TextUtils.isEmpty(IMapUtils.getBikeIconVO().getIcon_max())) {
                currentGeneralBikeBD = BitmapDescriptorFactory.fromBitmap(ImageLoader.getInstance().loadImageSync(IMapUtils.getBikeIconVO().getIcon_max()));
            } else {
                currentGeneralBikeBD = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.icon_main_current_bike));
            }
        }
        return currentGeneralBikeBD;
    }

    private void getAdressByLatLon() {
        GeocodeSearch geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                String s = regeocodeResult.getRegeocodeAddress().getCity();
                String s1 = regeocodeResult.getRegeocodeAddress().getCityCode();
                String s2 = regeocodeResult.getRegeocodeAddress().getDistrict();
                String s3 = regeocodeResult.getRegeocodeAddress().getAdCode();
                System.err.print(s);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                System.err.print(geocodeResult.toString());
            }
        });//和上面一样
        // 第一个参数表示一个Latlng(经纬度)，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        LatLonPoint latLonPoint = new LatLonPoint(34.150625, 113.478709);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private void getBikeIcons(String cityCode) {
        RequestParams detailParams = new RequestParams();
        detailParams.addBodyParameter("cityCode", cityCode);
        MFRunner.requestPost(3022, MFUrl.GETBIKEICONS, detailParams, this);
    }


    private void initTopLayout(int state) {
        if (!isAdded() || frgMianUse == null) {
            return;
        }

        String tag = (String) frgMianUse.getTag();
        if (state == 2) {
            return;
        }
        if (!TextUtils.isEmpty(tag) && ("2".equals(tag) || "-2".equals(tag))) {
            return;
        }

        switch (state) {
            case -2:
                ADBean tempAD = (ADBean) frgMianUseAD.getTag();
                if (tempAD != null) {
                    if (TextUtils.isEmpty(tempAD.getPhotoUrl())) {
//                        userimage.setImageResource(R.drawable.user_photo_selector);
                    } else {
                        sendStatistics("3004", "show", MFUtil.getValueForURL("cityCode", tempAD.getAdUrl()), tempAD.getId() + "");
                        final StringBuffer imSB = new StringBuffer();
                        if (!TextUtils.isEmpty(tempAD.getPhotoUrl()) && tempAD.getPhotoUrl().contains("oss")) {
                            tempAD.setPhotoUrl(tempAD.getPhotoUrl().replace("oss", "img"));
                        }
                        imSB.append(tempAD.getPhotoUrl()).append("?x-oss-process=image/resize,m_lfit,");
                        imSB.append("w_").append(topADImangeWidth == 0 ? 500 : topADImangeWidth);
                        imSB.append(",h_").append(topADImangeHeight == 0 ? 200 : topADImangeHeight);
//                        .append(",g_center");
                        ImageLoader.getInstance().loadImage(imSB.toString(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                frgMianUse.setVisibility(View.VISIBLE);
                                frgMianUseAD.setVisibility(View.VISIBLE);
                                frgMianUseAD.setOnClickListener(MainFrg.this);
                                ImageLoader.getInstance().displayImage(imSB.toString(), frgMianUseAD);
                                frgMianUse.setTag("-2");
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                    }
                }
                break;
            case 0:
                frgMianUse.setVisibility(View.VISIBLE);
                frgMianUseText.setText("如何使用小蜜蜂");
                frgMianUse.setTag("0");
                frgMianUseAD.setOnClickListener(null);
                frgMianUseAD.setImageResource(0);
                frgMianUseAD.setVisibility(View.GONE);
                break;
            case 1:
                frgMianUse.setVisibility(View.GONE);
                frgMianUseAD.setOnClickListener(null);
                frgMianUseAD.setImageResource(0);
                frgMianUseText.setText(null);
                frgMianUse.setTag(null);
                frgMianUseAD.setVisibility(View.GONE);
                break;
            case 2:
                frgMianUse.setVisibility(View.VISIBLE);
                frgMianUseAD.setOnClickListener(null);
                frgMianUseAD.setImageResource(0);
                frgMianUseText.setText("采蜜人征集活动，前去围观……");
                frgMianUse.setTag("2");
                frgMianUseAD.setVisibility(View.GONE);
                break;
            default:
                frgMianUse.setVisibility(View.GONE);
                frgMianUseAD.setOnClickListener(null);
                frgMianUseAD.setImageResource(0);
                frgMianUseText.setText(null);
                frgMianUse.setTag(null);
                frgMianUseAD.setVisibility(View.GONE);
                break;
        }
    }

    public A2B getAct2Main() {
        return act2Main;
    }

    public void setAct2Main(A2B act2Main) {
        this.act2Main = act2Main;
    }
}