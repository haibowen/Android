package com.example.administrator.mydatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button,button1,button2,button3,button4;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.bt_db);
        button1=findViewById(R.id.bt_add);
        button2=findViewById(R.id.bt_update);
        button3=findViewById(R.id.bt_delete);
        button4=findViewById(R.id.bt_query);


        myDatabase=new MyDatabase(this,"BookStore.db",null,3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.getWritableDatabase();

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =myDatabase.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("name","shujuku");
                values.put("type","haibo");
                db.insert("File",null,values);
                db.execSQL("insert into File (name ,type) values(?,?)",new String[]{ "luangwuchunqiu","6666"});
                /**
                 *
                 *
                 */
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db= myDatabase.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put("type","lianbo");
                db.update("File",contentValues,"name= ?",new String[]{"shujuku"});


            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=myDatabase.getWritableDatabase();
                db.delete("File","id >?",new String[]{"8"});
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=myDatabase.getWritableDatabase();
                Cursor cursor=db.query("File",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String name= cursor.getString(cursor.getColumnIndex("name"));
                        String type=cursor.getString(cursor.getColumnIndex("type"));
                        String id=  cursor.getString(cursor.getColumnIndex("id"));

                        Log.e("wenhaibo", name );
                        Log.e("wenhaibo", type );
                        Log.e("wenhaibo", id );
                    }while (cursor.moveToNext());
                    
                }
                cursor.close();


            }
        });


    }
}
