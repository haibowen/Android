package com.example.wenhaibo.androidstudy_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private Button button1;
    private Button button2;
    private Button button3;

    private ImageView imageView;

    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private Map<String, String> map;
    private List<Map<String, String>> list;
    private String id, title, price, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main3 );

        button = findViewById( R.id.bt_JAVA );
        button1 = findViewById( R.id.bt_Python );
        button2 = findViewById( R.id.bt_PHP );
        button3=findViewById( R.id.bt_buy );

        imageView = findViewById( R.id.im_show );

        textView = findViewById( R.id.tv_title );
        textView1 = findViewById( R.id.tv_price );
        textView2 = findViewById( R.id.tv_time );
        textView3 = findViewById( R.id.tv_id );

        button.setOnClickListener( this );
        button1.setOnClickListener( this );
        button2.setOnClickListener( this );
        button3.setOnClickListener( this );


        try {
            //读取book.xml文件
            InputStream is = this.getResources().openRawResource( R.raw.book );
            //信息集合存到Bookinfos中
            List<Bookinfo> Bookinfos = BookServer.getInfosFromXML( is );
            //循环读取Bookinfos中的每一条数据
            list = new ArrayList<Map<String, String>>();
            for (Bookinfo info : Bookinfos) {
                map = new HashMap<String, String>();
                map.put( "id", info.getId() );
                map.put( "title", info.getTitle() );
                map.put( "price", info.getPrice() );
                map.put( "time", info.getTime() );

                list.add( map );
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText( this, "解析信息失败", Toast.LENGTH_SHORT ).show();
        }
        //自定义getMap()方法，显示天气信息到文本控件中，默认显示北京的天气
        getMap( 0, R.drawable.a );
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_JAVA:
                getMap( 0, R.drawable.a );
                break;
            case R.id.bt_Python:
                getMap( 1, R.drawable.b );
                break;
            case R.id.bt_PHP:
                getMap( 2, R.drawable.c );
                break;
            case  R.id.bt_buy:
                Intent intent =new Intent( Main3Activity.this,Main4Activity.class );
                intent.putExtra( "JAVA","JAVA" );
                startActivityForResult( intent,1 );

            default:
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String e = data.getStringExtra( "result" );
                Toast.makeText( Main3Activity.this, e, Toast.LENGTH_SHORT ).show();

            }

        }
    }
    private void getMap(int number, int iconNumber) {
        Map<String, String> bookMap = list.get( number );
        id = bookMap.get( "id" );
        title = bookMap.get( "title" );
        price = bookMap.get( "price" );
        time = bookMap.get( "time" );
        textView.setText( id );
        textView1.setText( "书名"+" "+title );
        textView2.setText("价格"+" "+ price );
        textView3.setText("出版时间"+" "+ time );


         imageView.setImageResource(iconNumber);
    }
}