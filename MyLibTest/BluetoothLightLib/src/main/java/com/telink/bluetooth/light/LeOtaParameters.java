/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

public final class LeOtaParameters extends Parameters {

    public static LeOtaParameters create() {
        return new LeOtaParameters();
    }

    public LeOtaParameters setMeshName(String value) {
        this.set(PARAM_MESH_NAME, value);
        return this;
    }

    public LeOtaParameters setPassword(String value) {
        this.set(PARAM_MESH_PASSWORD, value);
        return this;
    }

    public LeOtaParameters setLeScanTimeoutSeconds(int value) {
        this.set(PARAM_SCAN_TIMEOUT_SECONDS, value);
        return this;
    }

    public LeOtaParameters setDeviceInfo(OtaDeviceInfo value) {
        this.set(PARAM_DEVICE_LIST, value);
        return this;
    }
}
