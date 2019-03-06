package activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mytest002.R;

import db.MySqlDatabase;

public class ListViewActivity extends AppCompatActivity {

    private IntentFilter intentFilter;
    private static final String TAG = "ListViewActivity";
    private ListView listView;
    private Button buttondb;
    private MySqlDatabase mySqlDatabase;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        buttondb=findViewById(R.id.bt_createdb);

        mySqlDatabase=new MySqlDatabase(this,"TEST.db",null,1);
        buttondb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mySqlDatabase.getWritableDatabase();
                SQLiteDatabase db=mySqlDatabase.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put("name","The Da Vinci Code");
                contentValues.put("author","haibo");
                contentValues.put("pages","222");
                db.insert("book",null,contentValues);


            }
        });


        insert();
        QueryBook();


        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.haibowen");
        registerReceiver(mybroad,intentFilter);


        //listView.invalidateViews();

    }

    private void QueryBook() {
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db =mySqlDatabase.getWritableDatabase();
                db.rawQuery("select * from book",null);
                Toast.makeText(ListViewActivity.this,"已经查询了",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "run: "+db.rawQuery("select * from book",null));


            }
        },5000);
    }

    private void insert() {
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db=mySqlDatabase.getWritableDatabase();

                db.execSQL("insert into book (name, author,pages,price) values(?,?,?,?)",new String[] {"haibo","cool","333","78.34"});
                Toast.makeText(ListViewActivity.this,"已经插入了",Toast.LENGTH_SHORT).show();


            }
        },3000);
    }

    private BroadcastReceiver mybroad=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: "+intent.getAction() );

            if (intent.getAction().equals("android.net.haibowen")){
                Toast.makeText(context,"收到海波的广播了",Toast.LENGTH_LONG).show();
            }else {

                Toast.makeText(context,"没有收到广播",Toast.LENGTH_LONG).show();
            }



        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mybroad);
    }
}
