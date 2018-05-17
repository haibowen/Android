package com.example.wenhaibo.androidstudy_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    private TextView textView;
    private ListView listView;

    private String[] data = {"JAVA", "Python", "PHP", "C#", "SQL", "Rube", "GO", "C", "C++", "phone", "pad", "pc", "mp3", "xiaomi", "huawei", "apple", "meizu", "coure"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        //绑定id
        textView = findViewById( R.id.tv_show );
        listView = findViewById( R.id.lv_show );

        final Intent intent = getIntent();

        //获得数据
        String zhanghao = intent.getStringExtra( "zhanghao" );
        String password = intent.getStringExtra( "password" );

        textView.setText( "恭喜你登录成功，你的账号和密码为：" + zhanghao + " " + password );


        ArrayAdapter<String> adapter = new ArrayAdapter<String>( Main2Activity.this, android.R.layout.simple_list_item_1, data );
        listView.setAdapter( adapter );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.bt_add:
                Intent intent = new Intent( Main2Activity.this, MainActivity.class );
                startActivity( intent );
                break;
            case R.id.bt_next:
                Intent intent1 = new Intent( Main2Activity.this, Main3Activity.class );
                startActivity( intent1 );
                break;
            default:
                break;
        }
        return true;
    }


}
