package com.example.administrator.mysdcardtest;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class MainActivity extends AppCompatActivity   {

    private String mResult = new String();
    private String[] mFileList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestpermission();







    }

    @AfterPermissionGranted(1)
    public void requestpermission(){
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {


            search();
            //...
        } else {
            //...

            EasyPermissions.requestPermissions(this, "应用需要照相机和sdcard权限",
                    1, perms);

        }

    }


    public void search(){

        File flist = new File("/mnt/sdcard");

        FileFilter ff = new FileFilter(){
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] fileDir = flist.listFiles(ff);
        for (int i = 0; i < fileDir.length; i++) {
            String str = fileDir[i].getName();
            Log.e("wenhaibo", "search: "+str );
            mResult += str;
            mResult += "\n";
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}
