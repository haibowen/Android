package com.mmuu.travel.client.mfConstans;

import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.bean.user.UserBean;
import com.mmuu.travel.client.bean.user.UserVO;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.SharedPreferenceTool;

/**
 * Created by XIXIHAHA on 2016/12/24.
 */

public class MFStaticConstans {


    private static UserBean userBean;

    public synchronized static UserBean getUserBean() {

        if (userBean == null) {
            userBean = (UserBean) GsonTransformUtil.fromJson2(SharedPreferenceTool.getPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_USERVO, ""),
                    UserBean.class);
        }

        if (userBean == null) {
            userBean = new UserBean();
            userBean.setToken(null);
            userBean.setUser(new UserVO());
        }


        return userBean;
    }

    public synchronized static void setUserBean(UserBean userBean) {

        if (userBean != null && userBean.getUser() != null) {

        } else {
            userBean = new UserBean();
            userBean.setToken(null);
            userBean.setUser(new UserVO());
            SharedPreferenceTool.setPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_PERSONRESULTBEAN, "");
        }

        SharedPreferenceTool.setPrefString(MFApp.getInstance().getApplicationContext(), MFConstansValue.SP_KEY_USERVO, GsonTransformUtil.toJson(userBean));
        MFStaticConstans.userBean = userBean;
    }

    /**
     * 判断第一次进来是否直奔订单页
     */
    public static boolean hasToOrder = false;

    /**
     * 判断首页订单是否需要刷新
     */
    public static boolean needRefOrder = false;
    /**
     * 判断首页订单是否需要两分钟刷新
     */
    public static boolean needRefOrder2Sce = false;
    /**
     * 判断首页订单是否需要刷新
     */
    public static boolean needRefUserInfo = false;

}
