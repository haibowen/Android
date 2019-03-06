/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

import java.util.Arrays;

public final class LeUpdateParameters extends Parameters {

    public static LeUpdateParameters create() {
        return new LeUpdateParameters();
    }

    public LeUpdateParameters setOldMeshName(String value) {
        this.set(PARAM_MESH_NAME, value);
        return this;
    }

    public LeUpdateParameters setNewMeshName(String value) {
        this.set(PARAM_NEW_MESH_NAME, value);
        return this;
    }

    public LeUpdateParameters setOldPassword(String value) {
        this.set(PARAM_MESH_PASSWORD, value);
        return this;
    }

    public LeUpdateParameters setNewPassword(String value) {
        this.set(PARAM_NEW_PASSWORD, value);
        return this;
    }

    public LeUpdateParameters setLtk(byte[] value) {
        this.set(PARAM_LONG_TERM_KEY, value);
        return this;
    }

    public LeUpdateParameters setUpdateDeviceList(DeviceInfo... value) {
        this.set(PARAM_DEVICE_LIST, Arrays.asList(value));
        return this;
    }
}
