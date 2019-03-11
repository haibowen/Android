package com.example.mygreendaotest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDao = getUserDao();
//
//        User user=new User();
//        user.setId(1);
//        user.setName("haibwoen");
//        user.setAge(24);
//        userDao.insert(user);
//
//        User user1=new User();
//        user1.setAge(22);
//        user1.setName("wenhiabo");
//        user1.setId(2);
//        userDao.insert(user1);



        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                query();

            }
        },2000);
    }

    private UserDao getUserDao() {
        //获取UserDao

        MyApp myApp = (MyApp) getApplication();

        DaoSession daoSession = myApp.getDaoSession();
        return daoSession.getUserDao();
    }

    //插入
    private void insertData(User user) {


        userDao.insert(user);
    }

    //删除
    public void deleteDao(User user) {
        userDao.delete(user);


    }

    //更新
    public void update(User user) {
        userDao.delete(user);


    }

    //查询所有记录
    public List query() {


        Log.e("222222", "query: "+userDao.loadAll().get(1).getName());
        return userDao.loadAll();
    }

    //根据id查询
    public User query1() {

        return userDao.loadByRowId(1);

    }

    //查询年龄大于10的用户
    public List query2() {


        QueryBuilder builder = userDao.queryBuilder();
        return builder.where(UserDao.Properties.Age.gt(10)).build().list();

    }
}
