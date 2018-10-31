package com.mmuu.travel.client.mfConstans;

import com.mmuu.travel.client.base.MFApp;

/**
 * 固定url信息
 */
public class MFUrl {
    /**
     * 租赁协议 服务协议
     */
    public static String USERAGREEMENT = MFApp.urlWebRoot + "activity/h5/userAgreement/userAgreement.html";
    /**
     * 押金退款说明
     */
    public static String DEPOSITREFUND = MFApp.urlWebRoot + "activity/h5/depositRefund.html";
    /**
     * 出行quan说明
     */
    public static String CASHCOUPON = MFApp.urlWebRoot + "activity/h5/cashCoupon.html";
    /**
     * 如何获取出行券说明
     */
    public static String GETCASHCOUPON = MFApp.urlWebRoot + "activity/h5/allProblem/authenticationProblem/cashCoupon.html";
    /**
     * 信用积分说明
     */
    public static String CREDITSCORE = MFApp.urlWebRoot + "activity/h5/allProblem/beeCredit/creditScore.html";
    /**
     * 信用规则说明
     */
    public static String CREDITRULES = MFApp.urlWebRoot + "activity/h5/creditRules.html";

    /**
     * 使用说明
     */
    public static String INSTRUCTIONS = MFApp.urlWebRoot + "activity/h5/instructions.html";
    /**
     * 解锁帮助
     */
    public static String UNLOCKHELP = MFApp.urlWebRoot + "activity/h5/unlockHelp.html";
    /**
     * 计费规则
     */
    public static String HOWCHARGE = MFApp.urlWebRoot + "activity/h5/allProblem/fareAndDeposit/howCharge.html";
    /**
     * 用户指南
     */
    public static String INDEX = MFApp.urlWebRoot + "activity/h5/index.html";
    /**
     * 抽奖记录
     */
    public static String DRAWRECORD = MFApp.urlWebRoot + "activity/choujiang/drawrecord.html";
    /**
     * 活动列表
     */
    public static String ACTIVITIESLIST = MFApp.webRoot + "activity-list/index.html";
    /**
     * //信用积分
     */
    public static String CREDITPOINT = MFApp.webRoot + "credit-point/index.html";
    /**
     * 邀请好友
     */
    public static String INTEGRALPRIZE = MFApp.webRoot + "integral-prize/index.html";

    /**
     * 获取验证码Url
     */

    public static final String getCheckCodeURL = MFApp.urlRoot + "login/getCheckCode";
    /**
     * 获取语音验证码
     */

    public static final String BROADSMS = MFApp.urlRoot + "login/broadSms";

    /**
     * 登录
     */
    public static final String LOGIN_URL = MFApp.urlRoot + "login/login";
    /**
     * 退出
     */
    public static final String LOGOUT_URL = MFApp.urlRoot + "login/logout";
    /**
     * 获取app配置信息
     */
    public static final String GETSERVICEINFO_URL = MFApp.urlRoot + "help/getServiceInfo";
    /**
     * 获取更新信息
     */
    public static final String GETVERSION_URL = MFApp.urlRoot + "help/checkVersion";
    /**
     * 分享行程次数累加
     */
    public static final String ITINERARYSHARINGADDINTEGRAL_URL = MFApp.urlActivityRoot + "activity/sharingAddIntegral";
    /**
     * 活动webview分享回调
     */
    public static final String SHAREASKACTIVITY = MFApp.urlActivityRoot + "activityWeb/shareAskActivity";

    /**
     * 支付押金
     */
    public static final String BUILDDEPOSIT_URL = MFApp.urlRoot + "pay/buildDeposit";
    /**
     * 我的钱包
     */
    public static final String MY_WALLET_URL = MFApp.urlRoot + "users/myPurse";
    /**
     * 获取押金配置
     */
    public static final String GETDEPOSITCONFIG = MFApp.urlRoot + "help/getDepositConfig";
    /**
     * 意见建议
     */
    public static final String SUGGESTION = MFApp.urlRoot + "users/suggestion";
    /**
     * 我的行程
     */
    public static final String STROKELIST_URL = MFApp.urlRoot + "orders/strokeList";


    /**
     * 个人中心
     */
    public static final String PERSONAL_CENTER_URL = MFApp.urlRoot + "users/personalCenter";

    /**
     * 上传头像接口
     */
    public static final String UPLOADAVATAR_URL = MFApp.urlRoot + "users/upLoadAvatar";

    /**
     * 出行券
     */
    public static final String MY_VOUCHER_URL = MFApp.urlRoot + "users/getUserCouponList";

    /**
     * 钱包明细
     */
    public static final String MY_WALLET_DETAIL_URL = MFApp.urlRoot + "users/costDetails";
    /**
     * 信用明细
     */
    public static final String MYCREDITRECORD = MFApp.urlRoot + "users/myCreditRecord";

    /**
     * 用户认证
     */
    public static String AUTHENTICATE_URL = MFApp.urlRoot + "users/authenticate";
    /**
     * 获取单车列表
     */
    public static String getBikeList = MFApp.urlRoot + "bikes/getBikeList";
    /**
     * 获取车辆详情
     */
    public static String getBikeDetail = MFApp.urlRoot + "bikes/getBikeDetail";

    /**
     * 扫码订单或预订前检测
     */
    public static String getMessageBeforeOrder = MFApp.urlRoot + "orders/getMessageBeforeOrder";

    /**
     * 扫码下单或车辆预约
     */
    public static String reserveOrOrderBike = MFApp.urlRoot + "orders/reserveOrOrderBike";

    /**
     * 获取未完成订单//订单恢复/轮询
     */
    public static String getUnFinishOrder = MFApp.urlRoot + "orders/recoveryOrderDetail";

    /**
     * 更改订单状态
     */
    public static String updateOrderStatus = MFApp.urlRoot + "orders/updateOrderStatus";

    /**
     * 修改查看订单状态接口
     */
    public static String updateView = MFApp.urlRoot + "orders/updateView";

    /**
     * 上报 0：未开始订单1：预约订单2：开锁后订单3：完成订单反馈
     */
    public static String REPORTING = MFApp.urlRoot + "bikes/reporting";

    /**
     * 订单状态开锁
     */
    public static String unlockBikeInOrder = MFApp.urlRoot + "orders/unlockBikeInOrder";
    /**
     * 轮询获取车辆真实解锁状态//开锁/关锁/临停/小轮询
     */
    public static String roundRobinUnlockBike = MFApp.urlRoot + "orders/roundRobinUnlockBike";

    /**
     * 支付完成后调用
     */
    public static String modifyPayResultByPayType = MFApp.urlRoot + "pay/modifyPayResultByPayType";

    /**
     * 账户充值
     */
    public static String buildRecharge = MFApp.urlRoot + "pay/buildRecharge";

    /**
     * 设置常用地址
     */
    public static String setCommonAddress = MFApp.urlRoot + "users/setCommonAddress";

    /**
     * 获取常用地址
     */
    public static String getCommonAddress = MFApp.urlRoot + "users/getCommonAddress";
    /**
     * 获取故障原因
     */
    public static String GETTRIPPROBLEMTERMS = MFApp.urlRoot + "orders/getTripProblemTerms";
    /**
     * 退还押金
     */
    public static String APPLYDEPOSITBACK = MFApp.urlRoot + "users/applyDepositBack";
    /**
     * 行程详情
     */
    public static String STROKEDETAIL = MFApp.urlRoot + "orders/strokeDetail";

    /**
     * 地图范围
     * fencetype=1--》禁停
     * fencetype=0--》运营区域
     */
    public static String getCityGpsList = MFApp.urlRoot + "orders/getCityGpsList";

    /**
     * 鸣笛寻车
     */
    public static String findEbikeWhistle = MFApp.urlRoot + "bikes/findEbikeWhistle";
    /**
     * 还车
     */
    public static String returnCar = MFApp.urlRoot + "orders/returnCar";

    /**
     * 车辆闪灯
     */
    public static String ctrLight = MFApp.urlRoot + "bikes/ctrLight";

    /**
     * 取消预约
     */
    public static String cancelReserveOrder = MFApp.urlRoot + "orders/cancelReserveOrder";
    /**
     * 上传devicetoken
     */
    public static String addphonedeviceinfo = MFApp.urlRoot + "login/addPhoneDeviceInfo";
    /**
     * 广告
     */
    public static String GETADBYTYPE = MFApp.urlRoot + "help/getAdByType";
    /**
     * 抽奖次数
     */
    public static String SHOWINTEGRALDRAWCOUNT = MFApp.urlActivityRoot + "activityWeb/showIntegralDrawCount";

    /**
     * 下单前积分过低提示
     */
    public static String SCOREINFINENOTICE = MFApp.urlRoot + "orders/scoreInFineNotice";
//    /**
//     * 所有分享接口
//     */
//    public static String ACTIVITYSHARING = MFApp.urlRoot + "users/activitySharing";
    /**
     * 活動
     */
    public static String FINDACTIVITYINFO = MFApp.urlActivityRoot + "activityApp/findActivityInfo";
    /**
     * 接口：充值前检验地区
     */
    public static String CHECKCITYBEFORERECHARGE = MFApp.urlRoot + "pay/checkCityBeforeRecharge";

    /**
     * 临停开锁
     */
    public static String UNLOCKBIKEINORDER = MFApp.urlRoot + "orders/unlockBikeInOrder";

    /**
     * (update)1.5.0校验是否服务区域内外
     */
    public static String PROMPTRETURNCAR = MFApp.urlRoot + "bikes/promptReturnCar";

    /**
     * (update)1.5.2根据订单获取车辆gps信息
     */
    public static String GETBIKEGPS = MFApp.urlRoot + "orders/getBikeGps";

    /**
     * 1.5.3手工还车接口
     */
    public static String manualReturnCar = MFApp.urlRoot + "orders/manualReturnCar";

    /**
     * 1.5.4还车时，上报参数信息
     */
    public static String addBikeReturn = MFApp.urlRoot + "orders/addBikeReturn";

    /**
     * 临时停车接口
     */
    public static String TEMPLOCKBIKE = MFApp.urlRoot + "orders/tempLockBike";
    /**
     * 轮询时添加日志
     */
    public static String ADDROBINLOG = MFApp.urlRoot + "orders/addRobinLog";
    /**
     * 用户结束订单原因
     */
    public static String ORDERREASON = MFApp.urlRoot + "orders/orderReason";

    /**
     * 服务端判断是否在区域内
     */
    public static String ISPOINTINRECT = MFApp.urlRoot + "orders/isPointInRect";
    /**
     * 采蜜人优惠券记录接口
     */
//    public static String COUPONLIST = MFApp.urlRoot + "partner/couponList";
    /**
     * 采蜜人 骑行奖励记录 接口
     */
//    public static String GRANTLIST = MFApp.urlRoot + "partner/grantList";


    /**
     * 1.5.8呼叫出租车
     */
//    public static String CALLTAXI = "http://59.110.32.154:8090/" + "taxi/callTaxi";
    /**
     * 1.5.8出租车获取正在进行中订单
     */
//    public static String GETRUNNINGORDEROFUSER = "http://59.110.32.154:8090/" + "taxi/getRunningOrderOfUser";
    /**
     * 1.5.8出租车取消订单
     */
//    public static String CANCELORDER = "http://59.110.32.154:8090/" + "taxi/cancelOrder";

    /**
     * 1.9采蜜人活动（web-首页入口）
     */
//    public static String PARTNERACT = MFApp.h5UrlWebRoot + "city-partner/index.html";
    /**
     * 充值界面入口
     */
    public static String TOPUPINDEX = MFApp.h5UrlWebRoot + "topUp/index.html";
    /**
     * 1.9采蜜人活动（首页入口-网页本地回调）
     */
//    public static String ADDPARTNERINFO = MFApp.urlRoot + "partner/buildRecharge";
    /**
     * 1.9采蜜人账户
     */
//    public static String PARTNERDETAIL = MFApp.urlRoot + "partner/partnerDetail";
    /**
     * 1.9采蜜人提现
     */
//    public static String WITHDRAW = MFApp.urlRoot + "partner/withdraw";
    /**
     * 1.9采蜜人退出
     */
//    public static String QUITPARTNER = MFApp.urlRoot + "partner/quitPartner";

    /**
     * 1.9采蜜人活动（首页入口）
     */
//    public static String CITYINFO = MFApp.urlRoot + "help/cityInfo";
    /**
     * 1.9采蜜人协议
     */
//    public static String CITYPARTNER = MFApp.h5UrlWebRoot + "city-partner/index.html#/agreement";

    /**
     * 1.9.1 车辆图标
     */
    public static String GETBIKEICONS = MFApp.urlRoot + "bikes/getBikeIcons";

    /**
     * 广告
     */
    public static String ADSTATISTICS = MFApp.urlActivityRoot + "activityApp/adStatistics";

    /**
     * 2.0申请退款-转账支付宝
     */
    public static String APPLYDEPOSITBACKBYACCOUNT = MFApp.urlRoot + "users/applyDepositBackByAccount";

    /**
     * 2.0.0取消1小时40分钟结束订单
     */
    public static String CANCALENDORDER = MFApp.urlRoot + "orders/cancalEndOrder";


    /**
     * 2.0.0蓝牙开锁前锁定车辆o
     */
    public static String LOCKBIKEINBLE = MFApp.urlRoot + "orders/lockBikeInBLE";
    /**
     * 2.0.0蓝牙解锁结果通知(直接扫码蓝牙解锁)
     */
    public static String NOTIFYUNLOCKBIKEINBLE = MFApp.urlRoot + "orders/notifyUnlockBikeInBLE";

    /**
     * 2.1.0操作蓝牙车辆日志
     */
    public static String BIKEBLEOPERATIONLOG = MFApp.urlRoot + "orders/bikeBleOperationLog";

    /**
     * 2.1.0订单预结束订单计算收费
     */
    public static String PREENDORDER = MFApp.urlRoot + "orders/preEndOrder";
    /**
     * websoket链接地址
     */
    public static String WEBSOKETADDRESS = "ws://47.93.119.147:8080/appserver/websocket/beefly";
//    public static String WEBSOKETADDRESS = "ws://192.168.1.13:8080/beefly-appserver-web/websocket/beefly";
//    public static String WEBSOKETADDRESS = "ws://192.168.1.64:8887/";

    /**
     * 开锁时间日志
     */
    public static String BIKEOPELOG = MFApp.urlRoot + "orders/bikeOpeLog";

}
