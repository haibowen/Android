/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Manufacture {

    private static final Manufacture DEFAULT_MANUFACTURE = new Builder().build();
    private static Manufacture definitionManufacture;

    private final Map<String, UUID> uuidMap = new HashMap<>();

    private String name;
    private String version;
    private String info;
    private String factoryName;
    private String factoryPassword;
    private byte[] factoryLtk;
    private int vendorId;
    private int otaDelay;

    private Manufacture(String name, String version, String info, String defaultMeshName, String defaultPassword, byte[] defaultLongTermKey, int vendorId, int otaDelay, UUID serviceUUID, UUID pairUUID, UUID commandUUID, UUID notifyUUID, UUID otaUUID) {
        this.name = name;
        this.version = version;
        this.info = info;
        this.factoryName = defaultMeshName;
        this.factoryPassword = defaultPassword;
        this.factoryLtk = Arrays.copyOf(defaultLongTermKey, 16);
        this.vendorId = vendorId;
        this.otaDelay = otaDelay;

        this.putUUID(UUIDType.SERVICE.getKey(), serviceUUID);
        this.putUUID(UUIDType.PAIR.getKey(), pairUUID);
        this.putUUID(UUIDType.COMMAND.getKey(), commandUUID);
        this.putUUID(UUIDType.OTA.getKey(), otaUUID);
        this.putUUID(UUIDType.NOTIFY.getKey(), notifyUUID);
    }

    public static Manufacture getDefaultManufacture() {
        return DEFAULT_MANUFACTURE;
    }

    public static Manufacture getDefinitionManufacture() {
        synchronized (Manufacture.class) {
            return definitionManufacture;
        }
    }

    public static void setManufacture(Manufacture manufacture) {
        synchronized (Manufacture.class) {
            definitionManufacture = manufacture;
        }
    }

    public static Manufacture getDefault() {
        synchronized (Manufacture.class) {
            if (definitionManufacture == null)
                return DEFAULT_MANUFACTURE;
        }

        return definitionManufacture;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getInfo() {
        return info;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public String getFactoryPassword() {
        return factoryPassword;
    }

    public byte[] getFactoryLtk() {
        return factoryLtk;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getOtaDelay() {
        return otaDelay;
    }

    public UUID getUUID(UUIDType uuidType) {
        return this.getUUID(uuidType.getKey());
    }

    public UUID getUUID(String key) {
        UUID result = null;

        synchronized (this.uuidMap) {
            if (this.uuidMap.containsKey(key))
                result = this.uuidMap.get(key);
        }

        return result;
    }

    public void putUUID(String key, UUID value) {
        synchronized (this.uuidMap) {
            if (!this.uuidMap.containsKey(key))
                this.uuidMap.put(key, value);
        }
    }

    public enum UUIDType {

        SERVICE("SERVICE_UUID"), PAIR("PAIR_UUID"), COMMAND("COMMAND_UUID"), OTA("OTA_UUID"), NOTIFY("NOTIFY_UUID");

        private final String key;

        UUIDType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static final class Builder {

        private String name = "telink";
        private String version = "1.0";
        private String info = "TELINK SEMICONDUCTOR (Shanghai) CO, LTD is a fabless IC design company";

        private String factoryName = "telink_mesh1";
        private String factoryPassword = "123";
        private byte[] factoryLtk = new byte[]{
                (byte) 0xC0, (byte) 0xC1, (byte) 0xC2, (byte) 0xC3, (byte) 0xC4,
                (byte) 0xC5, (byte) 0xC6, (byte) 0xC7, (byte) 0xD8, (byte) 0xD9,
                (byte) 0xDA, (byte) 0xDB, (byte) 0xDC, (byte) 0xDD, (byte) 0xDE,
                (byte) 0xDF};

        private int vendorId = 0x1102;
        private UUID serviceUUID = UuidInformation.TELINK_SERVICE.getValue();
        private UUID pairUUID = UuidInformation.TELINK_CHARACTERISTIC_PAIR.getValue();
        private UUID commandUUID = UuidInformation.TELINK_CHARACTERISTIC_COMMAND.getValue();
        private UUID notifyUUID = UuidInformation.TELINK_CHARACTERISTIC_NOTIFY.getValue();
        private UUID otaUUID = UuidInformation.TELINK_CHARACTERISTIC_OTA.getValue();
        private int otaDelay = 12;

        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setInfo(String info) {
            this.info = info;
            return this;
        }

        public Builder setFactoryName(String factoryName) {
            this.factoryName = factoryName;
            return this;
        }

        public Builder setFactoryPassword(String factoryPassword) {
            this.factoryPassword = factoryPassword;
            return this;
        }

        public Builder setFactoryLtk(byte[] factoryLtk) {
            this.factoryLtk = factoryLtk;
            return this;
        }

        public Builder setVendorId(int vendorId) {
            this.vendorId = vendorId;
            return this;
        }

        public Builder setOtaDelay(int otaDelay) {
            this.otaDelay = otaDelay;
            return this;
        }

        public Builder setServiceUUID(UUID serviceUUID) {
            this.serviceUUID = serviceUUID;
            return this;
        }

        public Builder setPairUUID(UUID pairUUID) {
            this.pairUUID = pairUUID;
            return this;
        }

        public Builder setCommandUUID(UUID commandUUID) {
            this.commandUUID = commandUUID;
            return this;
        }

        public Builder setNotifyUUID(UUID notifyUUID) {
            this.notifyUUID = notifyUUID;
            return this;
        }

        public Builder setOtaUUID(UUID otaUUID) {
            this.otaUUID = otaUUID;
            return this;
        }

        public Manufacture build() {
            return new Manufacture(this.name, this.version, this.info, this.factoryName, this.factoryPassword, this.factoryLtk, this.vendorId, this.otaDelay, this.serviceUUID, this.pairUUID, this.commandUUID, this.notifyUUID, this.otaUUID);
        }
    }
}
