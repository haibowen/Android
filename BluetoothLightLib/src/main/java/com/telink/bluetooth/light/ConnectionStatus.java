/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

public enum ConnectionStatus {
    OFF(0), ON(1), OFFLINE(2);

    private final int value;

    ConnectionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
