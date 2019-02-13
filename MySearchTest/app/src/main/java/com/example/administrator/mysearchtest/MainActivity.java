package com.example.administrator.mysearchtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileFilter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileFilter ff = new FileFilter(){
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] fileDir = flist.listFiles(ff);
        for (int i = 0; i < fileDir.length; i++) {
            String str = fileDir[i].getName();
            mResult += str;
            mResult += "\n";
        }
    }
}
