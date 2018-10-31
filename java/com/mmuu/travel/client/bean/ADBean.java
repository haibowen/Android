package com.mmuu.travel.client.bean;

import java.io.Serializable;

/**
 * 描述
 * JiaGuangWei on 2017/3/7 15:22
 */
public class ADBean implements Serializable {
    /**
     * activeFrequency : 1//  分钟
     * adUrl : http://www.baidu.com
     * content : 测试content
     * endTime : 1490889600000
     * id : 1
     * onLine : 1
     * photoUrl : http://image.so.com/v?ie=utf-8&src=hao_360so&q=美女&correct=美女&fromurl=http%3A%2F%2Fwww.miui.com%2Fthread-1227814-1-1.html&gsrc=1#ie=utf-8&src=hao_360so&q=%E7%BE%8E%E5%A5%B3&correct=%E7%BE%8E%E5%A5%B3&fromurl=http%3A%2F%2Fwww.miui.com%2Fthread-1227814-1-1.html&gsrc=1&lightboxindex=5&id=ef3c285844b6ebd02e7f3908e2f68685&multiple=0&itemindex=0&dataindex=31
     * remark : 测试remark
     * startTime : 1488816000000
     * title : 测试tile
     * type : 1
     */

    private int activeFrequency;
    private String adUrl;
    private int id;
    private String photoUrl;
    private String title;
    private int type;

    public int getActiveFrequency() {
        return activeFrequency;
    }

    public void setActiveFrequency(int activeFrequency) {
        this.activeFrequency = activeFrequency;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
