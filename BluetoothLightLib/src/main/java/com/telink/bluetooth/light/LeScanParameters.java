/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

public final class LeScanParameters extends Parameters {

    public static LeScanParameters create() {
        return new LeScanParameters();
    }

    public LeScanParameters setMeshName(String value) {
        this.set(Parameters.PARAM_MESH_NAME, value);
        return this;
    }

    public LeScanParameters setTimeoutSeconds(int value) {
        this.set(Parameters.PARAM_SCAN_TIMEOUT_SECONDS, value);
        return this;
    }

    public LeScanParameters setOutOfMeshName(String value) {
        this.set(PARAM_OUT_OF_MESH, value);
        return this;
    }

    public LeScanParameters setScanMode(boolean singleScan) {
        this.set(Parameters.PARAM_SCAN_TYPE_SINGLE, singleScan);
        return this;
    }
}
