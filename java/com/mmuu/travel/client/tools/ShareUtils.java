package com.mmuu.travel.client.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.tools.AccessTokenKeeper;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.ui.user.InviteFriendAct;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ShareUtils {
    private static IWXAPI iwxapi = null;
    private static IWeiboShareAPI mWeiboShareAPI = null;
    private static Tencent tencent = null;
    private static ShareUtils shareUtils = null;
    private static Activity context;

    private ShareUtils(Activity context) {
        iwxapi = WXAPIFactory.createWXAPI(context, MFConstansValue.APP_ID, true);
        tencent = Tencent.createInstance(MFConstansValue.APP_TENCENTID, context);
        iwxapi.registerApp(MFConstansValue.APP_ID);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, MFConstansValue.APP_WEIBOKEY);
        mWeiboShareAPI.registerApp();    // 将应用注册到微博客户端
        this.context = context;

    }

    public static ShareUtils getIntance(Activity context) {
        if (getContext() == null || !getContext().getClass().equals(context.getClass())) {
            shareUtils = new ShareUtils(context);
        }
        return shareUtils;
    }

    /**
     * 分享网页
     *
     * @param url
     * @param title
     * @param description
     * @param bitmap
     * @param transaction
     * @param scene
     */
    private void shareContent(String url, String title, String description, Bitmap bitmap, String transaction, int scene) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage message = new WXMediaMessage(webpageObject);
        message.title = title;
        message.description = description;
        message.setThumbImage(bitmap);//thumbData = Util.bmpToByteArray(bitmap, true);//左侧小图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = message;
        req.scene = scene;
        iwxapi.sendReq(req);
    }

    /**
     * 分享网页到微信朋友
     *
     * @param url
     * @param title
     * @param description
     * @param bitmap
     * @param transaction
     */
    public void shareToWeiXin(String url, String title, String description, Bitmap bitmap, String transaction) {
        if (isWeixinAvilible()) {
            shareContent(url, title, description, bitmap, transaction, SendMessageToWX.Req.WXSceneSession);//分享朋友
        } else {
            MFUtil.showToast(context, "您还未安装微信手机客户端");
        }
    }

    /**
     * 分享网页到微信
     *
     * @param url
     * @param title
     * @param description
     * @param bitmap
     * @param transaction
     */
    public void shareToWeiXinCircle(String url, String title, String description, Bitmap bitmap, String transaction) {
        if (isWeixinAvilible()) {
            shareContent(url, title, description, bitmap, transaction, SendMessageToWX.Req.WXSceneTimeline);//分享朋友圈
        } else {
            MFUtil.showToast(context, "您还未安装微信手机客户端");
        }
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public boolean isWeixinAvilible() {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 分享链接
     *
     * @param url
     * @param title
     * @param description
     * @param imgurl
     * @param iUiListener
     */
    public void shareToTencent(String url, String title, String description, String imgurl, IUiListener iUiListener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgurl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "蜜蜂出行");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        tencent.shareToQQ((Activity) context, params, iUiListener);

//        Bundle bundle = new Bundle();
//        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
//        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
//        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
//        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgurl);
//        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
//        tencent.shareToQQ((Activity) context, bundle, iUiListener);
    }

    /**
     * 分享链接
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 (版本2_2以上)时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     */
    public void shareWeiboMultiMessage(String text, String title, String content, Bitmap bitmap, String url, String transaction) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        TextObject textObject = new TextObject();
//        textObject.text = text;
//        weiboMessage.textObject = textObject;
        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = content;
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        mediaObject.defaultText = "默认文案蜜蜂出行";
        weiboMessage.mediaObject = mediaObject;

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(transaction);
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        if (mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.sendRequest((Activity) context, request);
        } else {
            AuthInfo authInfo = new AuthInfo(context, MFConstansValue.APP_WEIBOKEY, MFConstansValue.REDIRECT_URL, "");
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest((Activity) context, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                }

                @Override
                public void onComplete(Bundle bundle) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(context, newToken);
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }


    /**
     * 信用积分分享到微博
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 (版本2_2以上)时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     */
    public void shareCreditPointWeiboMultiMessage(String path, String transaction) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        TextObject textObject = new TextObject();
//        textObject.text = text;
//        weiboMessage.textObject = textObject;
        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        ImageObject imageObject = new ImageObject();
        imageObject.imagePath = path;
        weiboMessage.imageObject = imageObject;

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(transaction);
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        if (mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.sendRequest((Activity) context, request);
        } else {
            AuthInfo authInfo = new AuthInfo(context, MFConstansValue.APP_WEIBOKEY, MFConstansValue.REDIRECT_URL, "");
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest((Activity) context, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                }

                @Override
                public void onComplete(Bundle bundle) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(context, newToken);
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    /**
     * 信用积分分享到QQ空间
     *
     * @param url
     * @param iUiListener
     */
    public void shareCreditPointToQQSpace(String url, IUiListener iUiListener) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        tencent.shareToQQ((Activity) context, params, iUiListener);
    }

    /**
     * 信用积分界面微信分享信用积分图片
     *
     * @param bitmap
     * @param bitmaplit
     * @param transaction
     * @param scene
     */
    private void shareCreditPointContent(Bitmap bitmap, Bitmap bitmaplit, String transaction, int scene) {
        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = wxImageObject;
//        message.setThumbImage(bitmaplit);//thumbData = Util.bmpToByteArray(bitmap, true);//左侧小图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = message;
        req.scene = scene;
        iwxapi.sendReq(req);
    }

    /**
     * //信用积分
     *
     * @param bitmap
     * @param bitmaplit
     * @param transaction
     */
    public void shareCreditPointToWeiXin(Bitmap bitmap, Bitmap bitmaplit, String transaction) {
        if (isWeixinAvilible()) {
            shareCreditPointContent(bitmap, bitmaplit, transaction, SendMessageToWX.Req.WXSceneSession);//分享朋友
        } else {
            MFUtil.showToast(context, "您还未安装微信手机客户端");
        }
    }

    /**
     * //信用积分
     *
     * @param bitmap
     * @param bitmaplit
     * @param transaction
     */
    public void shareCreditPointToWeiXinCircle(Bitmap bitmap, Bitmap bitmaplit, String transaction) {
        if (isWeixinAvilible()) {
            shareCreditPointContent(bitmap, bitmaplit, transaction, SendMessageToWX.Req.WXSceneTimeline);//分享朋友圈
        } else {
            MFUtil.showToast(context, "您还未安装微信手机客户端");
        }
    }

    private static Activity getContext() {
        return context;
    }
}
