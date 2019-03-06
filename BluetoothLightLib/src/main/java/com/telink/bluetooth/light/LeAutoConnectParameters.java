/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

public final class LeAutoConnectParameters extends Parameters {

    public static LeAutoConnectParameters create() {
        return new LeAutoConnectParameters();
    }

    public LeAutoConnectParameters setMeshName(String value) {
        this.set(PARAM_MESH_NAME, value);
        return this;
    }

    public LeAutoConnectParameters setPassword(String value) {
        this.set(PARAM_MESH_PASSWORD, value);
        return this;
    }

    public LeAutoConnectParameters autoEnableNotification(boolean value) {
        this.set(PARAM_AUTO_ENABLE_NOTIFICATION, value);
        return this;
    }
}
