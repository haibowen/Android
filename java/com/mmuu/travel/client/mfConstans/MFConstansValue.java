package com.mmuu.travel.client.mfConstans;

/**
 * 项目中其他固定值
 * JiaGuangWei on 2016/12/15 16:15
 */
public class MFConstansValue {
    /**
     * 客户端加密私钥
     */
    public static final String RSA_PRIVATE_KEY =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALviW0YwnDzP2HdhMrlL+H9LmTySQxSHZhTxVhHrePHBiEkHhxf4PuyQtppEhD8fejLbfUuvTroHwoESbvW4Lp5E4sSPOExMumZmvVVNYtAxzCSbzDsgYpGziP/lCX485i1g/0/uiKsElprBlZkTD6q3dY7cs+gQvbVmDDEm1SAdAgMBAAECgYEAuEWgya2hfs9HeoadLjmBpq/pwguduQO3LjEo/nhZbZsn5KB6chrq9DBWt+UdY5+mxTyaneF5sGEt9lOy+lLDfIsXbZeyxeWU3X1fjrwI2azokIQYrXdZcyWZ8NsonG9FzHrlroq5gGignspMIGbFgWBqlHK3oSkJBzVw+5/ZEUECQQD6OUEXW9zwLhlRQsvXWRrgSJLNcZOJhWCAWHpJBUllnRpuzvfWDERjBurERG22vaIHSF7f50ndk+xm2daUqgUxAkEAwDix8lBJhhvvZoH8/6fsti1jjDIYj2ywasneTh6lRlkKuQ0uu6jXDEuSlcobbhNyvtdZ/ArRN2VqAZDQDRn+rQJBAISX4k4md6UuSGwvISU0KnG/A0uqa6vr6X8ZY3NeQc5+uYsOUXYzeemfuLfYKuszAbEqQFwVi6bGw/acaIDOt4ECQEdE/h1UzV3u/51nNtHnhimpvI4fiOGsr+B4Rnd6f1cM7p/cFma33DwPzSLCk9cFWPcPS6raq+W5MUuxUKJ67+UCQQC75GFbye3Kggju0kskZAwXkMFEtWrG9YFD+1pseSH8fa9T6rGiUVvzppBBk3gYChBtA6ymldu89EdY4/Y7a2BM";
    /**
     * 返回错误固定码
     */
    public static final int BACK_SUCCESS = 0;//成功
    public static final int BACK_BUSINESS = 1000;//业务类描述
    public static final int BACK_LOGOUT = 1001;//请重新登录系统
    public static final int BACK_SYSTEMERROR = 1002;//系统异常联系客服
    /**
     * TAXI返回错误固定码
     */
    public static final int TAXI_BACK_SUCCESS = 0;//成功
    public static final int TAXI_BACK_LOGOUT = 1100;//请重新登录系统
    public static final int TAXI_BACK_SYSTEMERROR = 1000;//系统异常联系客服

    /**
     * oss相关信息
     */
    public static final String OSS_KEY = "LTAInUlEMJlMLHFx";
    public static final String OSS_SECRET = "wqJzn6jy1lZCCAjN5i7B7G0OD6IlUe";
    public static final String OSS_ENDPOINT = "http://oss-cn-qingdao.aliyuncs.com";
    public static final String OSS_BUCKETNAME = "mfcxbj";
    public static final String OSS_BASEURL = "http://" + OSS_BUCKETNAME + ".img-cn-qingdao.aliyuncs.com/";
    /**
     * 腾讯分享
     * <p>
     * wx23ad7f4b8b8a0f26
     */
    public static final String APP_TENCENTID = "1105953632";
    /**
     * 微信appid
     * <p>
     * wx23ad7f4b8b8a0f26
     */
    public static final String APP_ID = "wx23ad7f4b8b8a0f26";
    /**
     * 微博分享appkey
     * <p>
     */
    public static final String APP_WEIBOKEY = "991958993";
    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * 保存当前版本的share_key
     */
    public static final String SHARE_KEY_VERIONCODE = "share_version";

    /**
     * 保存是否展示过订单中引导
     */
    public static final String SP_KEY_ISGUIDE = "sp_key_isguide";

    /**
     * 将userVO保存到sp
     */
    public static final String SP_KEY_USERVO = "sp_key_uservo";
    /**
     * 将personresultbean保存到sp
     */
    public static final String SP_KEY_PERSONRESULTBEAN = "sp_key_personresultbean";
    /**
     * 将电话号码保存到sp
     */
    public static final String SP_KEY_CALLNUMBER = "sp_key_callnumber";
    /**
     * 将电话号码结束服务时间
     */
    public static final String SP_KEY_SERVICEENDTIME = "sp_key_serviceendtime";
    /**
     * 将电话号码开始服务时间
     */
    public static final String SP_KEY_SERVICESTARTTIME = "sp_key_servicestarttime";

    /**
     * 将是否有充反活动
     */
    public static final String SP_KEY_HASRECHARGEACTIVITY = "hasRechargeActivity";

    /**
     * 将围栏保存到sp
     */
    public static final String SP_KEY_AREAPOINT = "sp_key_areapoint";
    /**
     * 将反围栏保存到sp
     */
    public static final String SP_KEY_AREAPOINT_NOPARKING = "sp_key_areapoint_noparking";
    /**
     * 保存日期
     */
    public static final String SP_CANCELORDER_DATE = "sp_cancelorder_date";


    /**
     * 行程上报入口
     */
    public static final String FEELBACK_TRIPBACK = "tripback";
    /**
     * 首页故障上报入口
     */
    public static final String FEELBACK_MAIN_BREAK = "main_break";
    /**
     * 首页违停上报入口
     */
    public static final String FEELBACK_MAIN_STOPCAR = "main_stopcar";
    /**
     * 首页续航不准上报入口
     */
    public static final String FEELBACK_MAIN_XUHANGBUZHUN = "main_xuhangbuzhun";
    /**
     * 首页开不了锁入口
     */
    public static final String FEELBACK_MAIN_NOTLOCK = "main_notlock";


    /**
     * 预定界面开不了锁
     */
    public static final String FEELBACK_BOOK_NOTLOCK = "book_notlock";

    /**
     * 预定界面违章上报
     */
    public static final String FEELBACK_BOOK_BREAK = "book_break";
    /**
     * 预定界面找不到车
     */
    public static final String FEELBACK_BOOK_NOTFINDCAR = "book_notfindcar";


    /**
     * 订单中故障上报入口
     */
    public static final String FEELBACK_CURRENTORDER_BREAK = "currentorder_break";

    /**
     * 订单中违停上报入口
     */
    public static final String FEELBACK_CURRENTORDER_NOTENDORDER = "currentorder_notendorder";
    /**
     * 订单中无法启动车辆
     */
    public static final String FEELBACK_CURRENTORDER_COULDNOTSTART = "currentorder_couldnotstart";
    /**
     * web首页开不了锁入口
     */
    public static final String FEELBACK_WEB_NOTLOCK = "web_notlock";
    /**
     * web找不到车
     */
    public static final String FEELBACK_WEB_NOTFINDCAR = "web_notfindcar";
    /**
     * web无法结束订单
     */
    public static final String FEELBACK_WEB_NOTENDORDER = "web_notendorder";
    /**
     * web无法启动车辆
     */
    public static final String FEELBACK_WEB_COULDNOTSTART = "web_couldnotstart";


    /**
     * web预约后找不到车
     */
    public static final String WEB_FOUNDCAR = "foundCar.html";
    /**
     * web开不了锁
     */
    public static final String WEB_NOTOPENLOCK = "notOpenLock.html";
    /**
     * web无法启动车辆
     */
    public static final String WEB_COULDNOTSTART = "couldNotStart.html";
    /**
     * web车辆故障
     */
    public static final String WEB_BREAK = "fault.html";
    /**
     * web举报违规
     */
    public static final String WEB_ILLEGAL = "illegal.html";
    /**
     * web无法结束订单
     */
    public static final String WEB_CREDITRULES = "cannotEnd.html";
    /**
     * web拨打电话
     */
    public static final String WEB_TEL = "tel:";
}
