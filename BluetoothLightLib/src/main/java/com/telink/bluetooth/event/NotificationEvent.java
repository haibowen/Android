/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.event;

import android.app.Application;

import com.telink.bluetooth.light.NotificationInfo;
import com.telink.bluetooth.light.NotificationParser;
import com.telink.bluetooth.light.Opcode;

import java.util.HashMap;
import java.util.Map;

public class NotificationEvent extends DataEvent<NotificationInfo> {

    public static final String ONLINE_STATUS = "com.telink.bluetooth.light.EVENT_ONLINE_STATUS";
    public static final String GET_GROUP = "com.telink.bluetooth.light.EVENT_GET_GROUP";
    public static final String GET_ALARM = "com.telink.bluetooth.light.EVENT_GET_ALARM";
    public static final String GET_SCENE = "com.telink.bluetooth.light.EVENT_GET_SCENE";
    public static final String GET_TIME = "com.telink.bluetooth.light.EVENT_GET_TIME";
    public static final String GET_USER_ALL_DATA  = "com.telink.bluetooth.light.EVENT_USER_ALL_DATA ";

    private static final Map<Byte, String> EVENT_MAPPING = new HashMap<>();

    static {
        register(Opcode.BLE_GATT_OP_CTRL_DC, ONLINE_STATUS);
        register(Opcode.BLE_GATT_OP_CTRL_D4, GET_GROUP);
        register(Opcode.BLE_GATT_OP_CTRL_E7, GET_ALARM);
        register(Opcode.BLE_GATT_OP_CTRL_E9, GET_TIME);
        register(Opcode.BLE_GATT_OP_CTRL_C1, GET_SCENE);
        register(Opcode.BLE_GATT_OP_CTRL_EA, GET_USER_ALL_DATA);
    }

    protected int opcode;
    protected int src;

    public NotificationEvent(Object sender, String type, NotificationInfo args) {
        super(sender, type, args);

        this.opcode = args.opcode;
        this.src = args.src;
    }

    public static boolean register(byte opcode, String eventType) {
        opcode = (byte) (opcode & 0xFF);
        synchronized (NotificationEvent.class) {
            if (EVENT_MAPPING.containsKey(opcode))
                return false;
            EVENT_MAPPING.put(opcode, eventType);
            return true;
        }
    }

    public static boolean register(Opcode opcode, String eventType) {
        return register(opcode.getValue(), eventType);
    }

    public static String getEventType(byte opcode) {
        opcode = (byte) (opcode & 0xFF);
        synchronized (NotificationEvent.class) {
            if (EVENT_MAPPING.containsKey(opcode))
                return EVENT_MAPPING.get(opcode);
        }
        return null;
    }

    public static String getEventType(Opcode opcode) {
        return getEventType(opcode.getValue());
    }

    public static NotificationEvent newInstance(Application sender, String type, NotificationInfo args) {
        return new NotificationEvent(sender, type, args);
    }

    public Object parse() {
        NotificationParser parser = NotificationParser.get(this.opcode);
        return parser == null ? null : parser.parse(this.args);
    }
}
