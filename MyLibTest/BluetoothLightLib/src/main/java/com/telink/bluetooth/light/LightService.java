/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.telink.bluetooth.light;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.telink.bluetooth.Command;

public abstract class LightService extends Service implements
        LightAdapter.Callback {

    public static final String ACTION_LE_SCAN = "com.telink.bluetooth.light.ACTION_LE_SCAN";
    public static final String ACTION_SCAN_COMPLETED = "com.telink.bluetooth.light.ACTION_SCAN_COMPLETED";
    public static final String ACTION_LE_SCAN_TIMEOUT = "com.telink.bluetooth.light.ACTION_LE_SCAN_TIMEOUT";
    public static final String ACTION_NOTIFICATION = "com.telink.bluetooth.light.ACTION_NOTIFICATION";
    public static final String ACTION_STATUS_CHANGED = "com.telink.bluetooth.light.ACTION_STATUS_CHANGED";
    public static final String ACTION_UPDATE_MESH_COMPLETED = "com.telink.bluetooth.light.ACTION_UPDATE_MESH_COMPLETED";
    public static final String ACTION_OFFLINE = "com.telink.bluetooth.light.ACTION_OFFLINE";
    public static final String ACTION_ERROR = "com.telink.bluetooth.light.ACTION_ERROR";

    public static final String EXTRA_MODE = "com.telink.bluetooth.light.EXTRA_MODE";
    public static final String EXTRA_DEVICE = "com.telink.bluetooth.light.EXTRA_DEVICE";
    public static final String EXTRA_NOTIFY = "com.telink.bluetooth.light.EXTRA_NOTIFY";
    public static final String EXTRA_ERROR_CODE = "com.telink.bluetooth.light.EXTRA_ERROR_CODE";

    protected LightAdapter mAdapter;
    protected IBinder mBinder;

    public LightAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.mAdapter != null) {
            this.mAdapter.stop();
        }
    }

    public void startScan(Parameters params) {
        System.out.println("-------------startScan");
        if (this.mAdapter == null)
            return;
        this.mAdapter.startScan(params, this);
    }

    public void autoConnect(Parameters params) {

        if (this.mAdapter == null)
            return;

        this.mAdapter.autoConnect(params, this);
    }

    public void updateMesh(Parameters params) {

        if (this.mAdapter == null)
            return;

        this.mAdapter.updateMesh(params, this);
    }

    public void idleMode(boolean disconnect) {

        if (this.mAdapter == null)
            return;

        this.mAdapter.idleMode(disconnect);
    }

    public boolean sendCommand(byte opcode, int address, byte[] params) {
        return this.mAdapter != null && this.mAdapter.sendCommand(opcode, address, params);
    }

    public boolean sendCommand(byte opcode, int address, byte[] params, Object tag) {
        return this.mAdapter != null && this.mAdapter.sendCommand(opcode, address, params, tag);
    }


    public void autoRefreshNotify(boolean enable, Parameters params) {

        if (this.mAdapter == null)
            return;

        if (enable) {
            this.mAdapter.enableAutoRefreshNotify(params);
        } else {
            this.mAdapter.disableAutoRefreshNotify();
        }
    }

    public void enableNotification() {
        if (this.mAdapter == null)
            return;
        this.mAdapter.enableNotification();
    }

    public void disableNotification() {
        if (this.mAdapter == null)
            return;
        this.mAdapter.disableNotification();
    }

    public int getMode() {
        return this.mAdapter != null ? this.mAdapter.getMode() : -1;
    }

    public boolean isLogin() {
        return this.mAdapter != null && this.mAdapter.isLogin();
    }

    public boolean connect(String mac, int timeoutSeconds) {
        return this.mAdapter != null && this.mAdapter.connect(mac, timeoutSeconds);
    }

    public void startOta(Parameters parameters) {
        if (this.mAdapter == null)
            return;
        this.mAdapter.startOta(parameters, this);
    }

    public boolean startOta(byte[] firmware) {
        return this.mAdapter != null && this.mAdapter.startOta(firmware);
    }

    public boolean getFirmwareVersion() {
        return this.mAdapter != null && this.mAdapter.getFirmwareVersion();
    }

    public void delete(Parameters parameters) {
        if (this.mAdapter == null)
            return;
        this.mAdapter.delete(parameters, this);
    }

    public boolean delete() {
        return this.mAdapter != null && this.mAdapter.delete();
    }

    @Override
    public boolean onLeScan(LightPeripheral light, int mode, byte[] scanRecord) {

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.macAddress = light.getMacAddress();
        deviceInfo.deviceName = light.getDeviceName();
        deviceInfo.meshName = light.getMeshNameStr();
        deviceInfo.meshAddress = light.getMeshAddress();
        deviceInfo.meshUUID = light.getMeshUUID();
        deviceInfo.productUUID = light.getProductUUID();
        deviceInfo.status = light.getStatus();

        Intent intent = new Intent();
        intent.setAction(ACTION_LE_SCAN);
        intent.putExtra(EXTRA_MODE, mode);
        intent.putExtra(EXTRA_DEVICE, deviceInfo);

        LocalBroadcastManager.getInstance(LightService.this)
                .sendBroadcast(intent);

        return true;
    }

    @Override
    public void onStatusChanged(LightController controller, int mode, int oldStatus,
                                int newStatus) {

        LightPeripheral light = controller.getCurrentLight();

        Intent intent = new Intent();

        if (newStatus == LightAdapter.STATUS_MESH_SCAN_TIMEOUT) {
            intent.setAction(ACTION_LE_SCAN_TIMEOUT);
        } else if (newStatus == LightAdapter.STATUS_MESH_SCAN_COMPLETED) {
            intent.setAction(ACTION_SCAN_COMPLETED);
        } else if (newStatus == LightAdapter.STATUS_MESH_OFFLINE) {
            intent.setAction(ACTION_OFFLINE);
        } else if (newStatus == LightAdapter.STATUS_UPDATE_ALL_MESH_COMPLETED) {
            intent.setAction(ACTION_UPDATE_MESH_COMPLETED);
        } else if (newStatus == LightAdapter.STATUS_OTA_PROGRESS) {
            OtaDeviceInfo deviceInfo = new OtaDeviceInfo();
            deviceInfo.firmwareRevision = light.getFirmwareRevision();
            deviceInfo.macAddress = light.getMacAddress();
            deviceInfo.progress = controller.getOtaProgress();
            deviceInfo.status = newStatus;
            intent.setAction(ACTION_STATUS_CHANGED);
            intent.putExtra(EXTRA_MODE, mode);
            intent.putExtra(EXTRA_DEVICE, deviceInfo);
        } else {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.macAddress = light.getMacAddress();
            deviceInfo.deviceName = light.getDeviceName();
            deviceInfo.meshName = light.getMeshNameStr();
            deviceInfo.meshAddress = light.getMeshAddress();
            deviceInfo.meshUUID = light.getMeshUUID();
            deviceInfo.productUUID = light.getProductUUID();
            deviceInfo.status = newStatus;
            deviceInfo.firmwareRevision = light.getFirmwareRevision();
            intent.setAction(ACTION_STATUS_CHANGED);
            intent.putExtra(EXTRA_MODE, mode);
            intent.putExtra(EXTRA_DEVICE, deviceInfo);
        }

        LocalBroadcastManager.getInstance(LightService.this)
                .sendBroadcast(intent);
    }

    @Override
    public void onNotify(LightPeripheral light, int mode, int opcode, int src,
                         byte[] params) {

        Intent intent = new Intent();
        intent.setAction(ACTION_NOTIFICATION);
        intent.putExtra(EXTRA_MODE, mode);

        NotificationInfo notifyInfo = new NotificationInfo();
        notifyInfo.src = src;
        notifyInfo.opcode = opcode;
        notifyInfo.params = params;

        if (light != null) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.macAddress = light.getMacAddress();
            deviceInfo.deviceName = light.getDeviceName();
            deviceInfo.meshName = light.getMeshNameStr();
            deviceInfo.meshAddress = light.getMeshAddress();
            deviceInfo.meshUUID = light.getMeshUUID();
            deviceInfo.productUUID = light.getProductUUID();
            notifyInfo.deviceInfo = deviceInfo;
        }

        intent.putExtra(EXTRA_NOTIFY, notifyInfo);

        LocalBroadcastManager.getInstance(LightService.this)
                .sendBroadcast(intent);
    }

    @Override
    public void onCommandResponse(LightPeripheral light, int mode, Command command, boolean success) {

    }

    @Override
    public void onError(int errorCode) {

        Intent intent = new Intent();
        intent.setAction(ACTION_ERROR);
        intent.putExtra(EXTRA_ERROR_CODE, errorCode);

        LocalBroadcastManager.getInstance(LightService.this)
                .sendBroadcast(intent);
    }
}
