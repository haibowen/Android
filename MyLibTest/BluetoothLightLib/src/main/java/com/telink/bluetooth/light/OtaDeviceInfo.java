/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

import android.os.Parcel;

public class OtaDeviceInfo extends DeviceInfo {

    public static final Creator<OtaDeviceInfo> CREATOR = new Creator<OtaDeviceInfo>() {
        @Override
        public OtaDeviceInfo createFromParcel(Parcel in) {
            return new OtaDeviceInfo(in);
        }

        @Override
        public OtaDeviceInfo[] newArray(int size) {
            return new OtaDeviceInfo[size];
        }
    };

    public byte[] firmware;
    public int progress;

    public OtaDeviceInfo() {
    }

    public OtaDeviceInfo(Parcel in) {
        super(in);
        this.progress = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.progress);
    }
}
