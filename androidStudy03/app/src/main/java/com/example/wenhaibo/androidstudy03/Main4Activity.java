package com.example.wenhaibo.androidstudy03;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.bean.Fruit;
import java.util.ArrayList;
import java.util.List;

import general.adapter.ReclerAdapter;

public class Main4Activity extends AppCompatActivity {
    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main4 );

        initfruit();

        RecyclerView recyclerView = findViewById( R.id.recler_view );

        //创建布局实例
        StaggeredGridLayoutManager staggeredGridLayoutManager =new StaggeredGridLayoutManager( 3,StaggeredGridLayoutManager.VERTICAL);

        //加载布局

        recyclerView.setLayoutManager( staggeredGridLayoutManager );

//        //新建布局管理实例
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
//
//        //设置显示的方向  水平
//
//        linearLayoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
//
//        //recyview 加载布局
//        recyclerView.setLayoutManager( linearLayoutManager );

        ReclerAdapter adapter = new ReclerAdapter( fruitList );

        recyclerView.setAdapter( adapter );


    }

    private void initfruit() {

        for (int i = 0; i < 2; i++) {
            Fruit fruit = new Fruit( "apple", R.drawable.apple );
            fruitList.add( fruit );
            Fruit fruit1 = new Fruit( "apple", R.drawable.apple );
            fruitList.add( fruit1 );
            Fruit fruit2 = new Fruit( "apple", R.drawable.apple );
            fruitList.add( fruit2 );
            Fruit fruit3 = new Fruit( "apple", R.drawable.apple );
            fruitList.add( fruit3 );
            Fruit fruit4 = new Fruit( "apple", R.drawable.apple );
            fruitList.add( fruit4 );
            Fruit fruit5 = new Fruit( "apple", R.drawable.apple );
            fruitList.add( fruit5 );

        }
    }
}

