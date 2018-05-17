package com.example.wenhaibo.androidstudy03;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.bean.Fruit;
import java.util.ArrayList;
import java.util.List;

import general.adapter.FruitAdapter;

public class Main3Activity extends AppCompatActivity {

    //声明控件
    private ListView listView;
    private List<Fruit> fruitList=new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main3 );

        //初始化水果的数据

        initFruit();

        //加载布局

        FruitAdapter fruitAdapter=new FruitAdapter( Main3Activity.this,R.layout.layout_item,fruitList );


        //绑定id

        listView=findViewById( R.id.list_hard );

        //设置适配器

        listView.setAdapter( fruitAdapter );

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long id) {
                Fruit fruit=fruitList.get(postion);
                Toast.makeText( Main3Activity.this,fruit.getName(),Toast.LENGTH_SHORT).show();

            }
        } );





    }

    private void initFruit() {
        for (int i=0;i<2;i++){
                Fruit fruit=new Fruit( "apple",R.drawable.apple );
                fruitList.add( fruit );
                Fruit fruit1=new Fruit( "apple",R.drawable.apple );
                fruitList.add( fruit1 );
                Fruit fruit2=new Fruit( "apple",R.drawable.apple );
                fruitList.add( fruit2 );
                Fruit fruit3=new Fruit( "apple",R.drawable.apple );
                fruitList.add( fruit3 );
                Fruit fruit4=new Fruit( "apple",R.drawable.apple );
                fruitList.add( fruit4 );
                Fruit fruit5=new Fruit( "apple",R.drawable.apple );
                fruitList.add( fruit5 );

        }
    }
}
