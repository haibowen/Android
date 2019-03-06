/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.event;

import android.os.IBinder;

public class ServiceEvent extends DataEvent<IBinder> {

    public static final String SERVICE_CONNECTED = "com.telink.bluetooth.light.EVENT_SERVICE_CONNECTED";
    public static final String SERVICE_DISCONNECTED = "com.telink.bluetooth.light.EVENT_SERVICE_DISCONNECTED";

    public ServiceEvent(Object sender, String type, IBinder args) {
        super(sender, type, args);
    }

    public static ServiceEvent newInstance(Object sender, String type, IBinder args) {
        return new ServiceEvent(sender, type, args);
    }
}