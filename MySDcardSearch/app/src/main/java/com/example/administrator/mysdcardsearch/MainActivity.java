package com.example.administrator.mysdcardsearch;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;
import yogesh.firzen.filelister.FileListerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String mResult = new String();
    private String[] mFileList = null;


    List<File> list = new ArrayList<File>();

    public static final String SDCard = Environment
             .getExternalStorageDirectory().getAbsolutePath();

    public static String currDir = SDCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file=Environment.getExternalStorageDirectory();
        File file1=Environment.getDataDirectory();
        File file2= new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        Log.e("0000000", "onCreate: "+file );
        Log.e("0000000", "onCreate: "+file1 );
        Log.e("0000000", "onCreate: "+file2);

        getAllFiles();

        /**
        FileListerDialog fileListerDialog = FileListerDialog.createFileListerDialog(MainActivity.this);
        fileListerDialog.setFileFilter(FileListerDialog.FILE_FILTER.ALL_FILES);
        fileListerDialog.show();

**/


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.
                    requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);



        }else {

            search();

        }


    }

    public void search(){





                File path= Environment.getExternalStorageDirectory();
                  Environment.getExternalStorageState();
                 // Environment.getExternalStoragePublicDirectory(String.valueOf(path));
        Environment.getExternalStorageDirectory();
                Log.e("333333", "onCreate: "+path );
        Log.e("333333", "onCreate: "+Environment.getExternalStorageDirectory() );
        Log.e("333333", "onCreate: "+  Environment.getExternalStoragePublicDirectory(String.valueOf(path)) );


                File flist = new File(String.valueOf(Environment.getRootDirectory()));
                //Environment.DIRECTORY_ALARMS;

                mFileList = flist.list();

                for(String str:mFileList){
                    mResult+=str;
                    mResult+="\n";




                }


                Log.e("333333", "onCreate: "+mResult );






    }


    public void getAllFiles() {
        list.clear();
        File file = new File(currDir);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    list.add(file2);
                }
                Log.e("55555", "getAllFiles: "+list );
            }
        }
    }

    //申请权限：


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){

                    //处理逻辑
                    //search();

                  boolean  is=  isExternalStorageAvailable();
                    Log.e("44444", "onRequestPermissionsResult: "+is );
                 String size=   getInternalMemorySize(MainActivity.this);
                    Log.e("44444", "onRequestPermissionsResult: "+size );


                }else {

                    Toast.makeText(this, "拒绝权限导致功能不可用", Toast.LENGTH_SHORT).show();

                }
                break;

        }

    }


    /**
     * 判断sd卡是否可用
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }



    /**
     * 获取手机内部存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        return Formatter.formatFileSize(context, size);
    }



    /**
     * 获取手机内部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return Formatter.formatFileSize(context, availableBlocksLong
                * blockSizeLong);
    }

    /**
     * 获取手机外部存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getExternalMemorySize(Context context) {
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return Formatter
                .formatFileSize(context, blockCountLong * blockSizeLong);
    }

    /**
     * 获取手机外部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableExternalMemorySize(Context context) {
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return Formatter.formatFileSize(context, availableBlocksLong
                * blockSizeLong);
    }


}
