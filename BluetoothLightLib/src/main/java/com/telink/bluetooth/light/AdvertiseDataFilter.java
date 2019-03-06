/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

import android.bluetooth.BluetoothDevice;

public interface AdvertiseDataFilter<E extends LightPeripheral> {

    E filter(BluetoothDevice device, int rssi,
             byte[] scanRecord);
}
