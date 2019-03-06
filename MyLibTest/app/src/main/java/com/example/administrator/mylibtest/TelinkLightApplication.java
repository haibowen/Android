package com.example.administrator.mylibtest;

import com.telink.TelinkApplication;
//注释一
/**
 * 注释二
 *
 *
 */
/*
注释三


 */

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

    /**
     *
     *
     */

    /*

     */


    enum Size {SMALL,MEDIU,BIG,LARGE};
    Size s=Size.MEDIU;
    //wenhaibo add 20190219
    public void getPath(){

        int [] a={1,2,3,45,5};
        int [] b=new int[5];



    }




    //end
}
