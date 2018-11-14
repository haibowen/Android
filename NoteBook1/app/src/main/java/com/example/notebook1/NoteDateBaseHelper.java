package com.example.notebook1;

/**
 * Created by 刘明 on 2017/5/25.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

            //定义数据库：将日志的数据保存在数据库中，使用sqlite来创建数据库，数据库中有三个属性，"_id"、"content"、"date"这三个属性

public class NoteDateBaseHelper extends SQLiteOpenHelper {


    public NoteDateBaseHelper(Context context) {
        super(context, "note", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table note(id   integer   primary key autoincrement,content, date)");
                  //创建一个名为note的表其中的信息：id，类型，主键，自增长；  content，  date


    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }


}