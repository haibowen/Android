package com.example.administrator.mydownloadtest;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static   Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            ExitPackage(MainActivity.this,"com.xianzhitech.ptt.zrt.tysz");
        Log.e("haibo", "soundMap.get(index)" );

    }

    public boolean ExitPackage(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        if(packageNames.contains(packageName)){
            //Toast.makeText(MainActivity.this, "天翼数字对讲已经存在", Toast.LENGTH_SHORT).show();
            showShortToast(MainActivity.this,"天翼数字对讲已经存在");
        }else {
            //Toast.makeText(MainActivity.this, "天翼数字对讲已经被卸载", Toast.LENGTH_SHORT).show();
            showShortToast(MainActivity.this,"天翼数字对讲已经被卸载");
        }

          return false;
    }

    public static void showShortToast(Context context,String content){
        if(mToast==null){
            mToast=Toast.makeText(context,content,Toast.LENGTH_SHORT);
        }else {
            // mToast.setText(null);
       mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


}
