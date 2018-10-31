package com.mmuu.travel.client.bean;

import java.util.List;

/**
 * 描述
 * JiaGuangWei on 2017/1/20 17:14
 */
public class LocationBean extends MFBaseResBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * time : 2017-02-08 18:30:26
         * lon : 116.48153657654238
         * lat : 39.99040454821466
         * speed : 26
         */

        private String time;
        private double lon;
        private double lat;
        private float speed;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }
    }
}
