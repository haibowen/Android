package com.example.mygreendaotest;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoMaster;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;

import org.greenrobot.greendao.database.Database;

public class MyApp extends Application {


    private DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        inite();


    }

    private void inite() {
        DaoMaster.DevOpenHelper devOpenHelper=new DaoMaster.DevOpenHelper(this,"test.db");

        SQLiteDatabase db=devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(db);

        daoSession=daoMaster.newSession();
    }

    public DaoSession getDaoSession(){

        return daoSession;
    }
}
