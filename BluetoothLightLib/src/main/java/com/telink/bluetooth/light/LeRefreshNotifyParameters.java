/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

public final class LeRefreshNotifyParameters extends Parameters {

    public static LeRefreshNotifyParameters create() {
        return new LeRefreshNotifyParameters();
    }

    public LeRefreshNotifyParameters setRefreshRepeatCount(int value) {
        this.set(PARAM_AUTO_REFRESH_NOTIFICATION_REPEAT, value);
        return this;
    }

    public LeRefreshNotifyParameters setRefreshInterval(int value) {
        this.set(PARAM_AUTO_REFRESH_NOTIFICATION_DELAY, value);
        return this;
    }
}
