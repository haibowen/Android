package com.example.mydbtest;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import db.PlayerDB;

public class MainActivity extends AppCompatActivity {

    private Button button;

    private SQLiteDatabase sqLiteDatabase;
    private PlayerDB playerDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_create_db);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerDB=new PlayerDB(MainActivity.this,"player",null,1);
                sqLiteDatabase=playerDB.getReadableDatabase();
                Toast.makeText(MainActivity.this,"数据库创建成功",Toast.LENGTH_SHORT).show();


            }
        });


    }
}
