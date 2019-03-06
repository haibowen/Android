/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.event;

import com.telink.bluetooth.light.DeviceInfo;

public class LeScanEvent extends DataEvent<DeviceInfo> {

    public static final String LE_SCAN = "com.telink.bluetooth.light.EVENT_LE_SCAN";
    public static final String LE_SCAN_COMPLETED = "com.telink.bluetooth.light.EVENT_LE_SCAN_COMPLETED";
    public static final String LE_SCAN_TIMEOUT = "com.telink.bluetooth.light.EVENT_LE_SCAN_TIMEOUT";

    public LeScanEvent(Object sender, String type, DeviceInfo args) {
        super(sender, type, args);
    }

    public static LeScanEvent newInstance(Object sender, String type, DeviceInfo args) {
        return new LeScanEvent(sender, type, args);
    }
}
