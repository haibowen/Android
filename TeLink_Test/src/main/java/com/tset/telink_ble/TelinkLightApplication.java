package com.tset.telink_ble;

import com.telink.TelinkApplication;

public final class TelinkLightApplication extends TelinkApplication {

    private Mesh mesh;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void doInit() {
        super.doInit();
        //AES.Security = true;
        if (FileSystem.exists("telink.meshs")) {
            this.mesh = (Mesh) FileSystem.readAsObject("telink.meshs");
        }

        this.startLightService(TelinkLightService.class);
    }

    @Override
    public void doDestroy() {
        super.doDestroy();
    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public boolean isEmptyMesh() {
        return this.mesh == null;
    }
}
