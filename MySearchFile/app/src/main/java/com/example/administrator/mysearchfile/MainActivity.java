package com.example.administrator.mysearchfile;

import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String>mylist=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchFile();

        //Log.e("wenhaibo", "onCreate: "+mylist.get(0));





    }
    public void searchFile(){
        //判断手机是否有sd卡

        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){

            File path= new File(Environment.getExternalStorageDirectory() + "/All/");//获得SD卡路径
            //File path= new File("/storage");
            Log.e("222", "searchFile: "+path );

            final  File [] files=path.listFiles();//获取文件
            Log.e("222", "searchFile: "+files );
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getFileName(files);


                }
            }).start();

        }else {
            Toast.makeText(MainActivity.this,"没有发现sd卡",Toast.LENGTH_SHORT).show();

        }

    }
    private void getFileName(File [] files){
        //判空
        if (files!=null){
            for(File file:files){
                if (file.isDirectory()){
                    getFileName(file.listFiles());//递归查询

                }else {
                  String filename= file.getName();

                  if (filename.endsWith("txt")){
                      mylist.add(filename);

                  }

                }

            }
        }else {
            Looper.prepare();

            Toast.makeText(this,"没有发现文件",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }


    }



}
