package com.example.administrator.mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.Statement;

public class MyDatabase extends SQLiteOpenHelper {
    private static  final  String CREATE_BOOK="create table Book ("
            +"id integer primary key autoincrement,"
            +"author text,"
            +"price real,"
            +"pages integer,"
            +"name text)";

    private static  final  String CREATE_FILE="create table File("
            +"id integer primary key autoincrement,"
            +"name  text,"
            +"type text)";


    private Context mcontext;
    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_FILE);
        Toast.makeText(mcontext,"数据库创建成功",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists  File");
        onCreate(db);


    }
}
