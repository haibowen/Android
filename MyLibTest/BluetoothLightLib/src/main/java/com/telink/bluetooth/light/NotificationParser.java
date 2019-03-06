/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

import android.util.SparseArray;

public abstract class NotificationParser<E> {

    private static final SparseArray<NotificationParser> PARSER_ARRAY = new SparseArray<>();

    public static void register(NotificationParser parser) {
        synchronized (NotificationParser.class) {
            PARSER_ARRAY.put(parser.opcode() & 0xFF, parser);
        }
    }

    public static NotificationParser get(int opcode) {
        synchronized (NotificationParser.class) {
            return PARSER_ARRAY.get(opcode & 0xFF);
        }
    }

    public static NotificationParser get(Opcode opcode) {
        return get(opcode.getValue());
    }

    abstract public byte opcode();

    abstract public E parse(NotificationInfo notifyInfo);
}
