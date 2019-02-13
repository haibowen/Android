package com.example.administrator.myfilesearch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.bt_search);
        button.setOnClickListener(this);

        //申请权限的框架

        /**
         XXPermissions.with(this)
         .permission(Permission.Group.STORAGE)
         .request(new OnPermission() {
        @Override
        public void hasPermission(List<String> granted, boolean isAll) {

        Toast.makeText(MainActivity.this,"获得权限成功",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void noPermission(List<String> denied, boolean quick) {
        Toast.makeText(MainActivity.this,"拒绝了权限",Toast.LENGTH_SHORT).show();


        }
        });
         */


        //权限申请


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else {
           onClick(button);
        }




    }

    @Override
    public void onClick(View v) {



        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir=new File(Environment.getExternalStorageDirectory().getPath());
                Log.d("1111", "onClick: "+dir);
                SearchFile(dir);
            }
        }).start();






    }
    public  static ArrayList<File> SearchFile(File dir){
        ArrayList<File> arrayList=new ArrayList<File>();

        File [] files=dir.listFiles();
        Log.e("2222", "SearchFile: "+files );
        if (files!=null){
            for (int i=0;i<files.length;i++){

                File file=files[i];
                arrayList.add(file);
                if (file.isDirectory()){
                    SearchFile(file);
                }
                Log.e("222", "SearchFile: "+arrayList );
            }
        }

        return arrayList;



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case 1:
                /**
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    onClick(button);

                } else {
                 Toast.makeText(MainActivity.this,"你拒绝了该权限",Toast.LENGTH_SHORT).show();

                 }
                 break();
                 **/

                /**
                if (grantResults.length>0){
                    for (int grantResult :grantResults){
                        if (grantResult!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(MainActivity.this,"你拒绝了该权限",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;

                 **/
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    onClick(button);

                } else {
                    Toast.makeText(MainActivity.this,"你拒绝了该权限",Toast.LENGTH_SHORT).show();

                }
                break;
                default:
                    break;
        }




    }
}
