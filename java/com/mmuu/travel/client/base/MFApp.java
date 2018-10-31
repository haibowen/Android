package com.mmuu.travel.client.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.HttpUtils;
import com.mmuu.travel.client.bean.mfinterface.LocationListener;
import com.mmuu.travel.client.bean.mfinterface.NetChangeListener;
import com.mmuu.travel.client.broadcast.NetChangeBroadcast;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFOptions;
import com.mmuu.travel.client.tools.CrashUtil;
import com.mmuu.travel.client.tools.DevMountInfo;
import com.mmuu.travel.client.tools.LGUtil;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.SystemUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Application类
 */
public class MFApp extends Application {
    /**
     * 本地文件缓存的根目录e
     */
    private static MFApp instance;
    private HttpUtils httpUtils;
    private List<SoftReference<Activity>> mListDel = new ArrayList<SoftReference<Activity>>();
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private static HashMap<String, LocationListener> locaMap;

    private HashMap<String, NetChangeListener> netChangeMap;

    public static int versionCode = 0;
    public static String versionName = "";
    public static String urlRoot = "";
    public static String urlWebRoot = "";
    public static String h5UrlWebRoot = "";
    public static String webRoot = "";

    /**
     * 活动项目
     */
    public static String urlActivityRoot = "";
    public static String mifengOSSDirectory = "";
    public static final String IS_LONIG = "islogin";
    private NetChangeBroadcast netChangeBroadcast;

    // 运行sample前需要配置以下字段为有效的值
    private static OSS oss;



    public static MFApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initSys();
        // initLocation();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static OSS getOss() {
        return oss;
    }

    private void initSys() {
       
        
        initYouMeng();
     
    }

   
}