package com.example.wenhaibo.androidstudy_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDBHelper extends SQLiteOpenHelper {
    private Context mContext;


 public static    final String CREATE_DB_BOOK = "create table book("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text)";
  public static   final String CREATE_DB_PERSON = "create table person("
            + "id integer primary key autoincrement,"
            + "name, text,"
            + "sex text)";


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, name, factory, version );
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( CREATE_DB_BOOK );
        db.execSQL( CREATE_DB_PERSON );
        Toast.makeText( mContext, "sucess", Toast.LENGTH_SHORT ).show();
        Toast.makeText( mContext,"OK PERSON",Toast.LENGTH_SHORT ).show();



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists book" );
        db.execSQL( "drop table if exists person" );
        onCreate( db );

    }
}
