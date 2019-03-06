/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.event;

import com.telink.bluetooth.light.DeviceInfo;

public class DeviceEvent extends DataEvent<DeviceInfo> {

    public static final String STATUS_CHANGED = "com.telink.bluetooth.light.EVENT_STATUS_CHANGED";
    public static final String CURRENT_CONNECT_CHANGED = "com.telink.bluetooth.light.EVENT_CURRENT_CONNECT_CHANGED";

    public DeviceEvent(Object sender, String type, DeviceInfo args) {
        super(sender, type, args);
    }

    public static DeviceEvent newInstance(Object sender, String type, DeviceInfo args) {
        return new DeviceEvent(sender, type, args);
    }
}