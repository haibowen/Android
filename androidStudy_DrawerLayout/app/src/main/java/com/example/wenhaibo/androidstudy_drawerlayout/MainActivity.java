package com.example.wenhaibo.androidstudy_drawerlayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButton;
    private List<News> mNews = new ArrayList<>(  );
    private NewsAdapter newsAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String>data=new ArrayList<>(  );
    private List<String>data1=new ArrayList<>(  );

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //控件绑定id
        toolbar=findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        drawerLayout=findViewById( R.id.drawerlayout );
        floatingActionButton=findViewById( R.id.fab_1 );

        navigationView=findViewById( R.id.nav );
        navigationView.setItemIconTintList(null);
        recyclerView=findViewById( R.id.recycler );
        swipeRefreshLayout=findViewById( R.id.swipe_refersh );

        // 刷新的方法
        swipeRefreshLayout.setColorSchemeColors( R.color.colorPrimary );
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refershNews();

            }
        } );


        //网络请求数据
        HttpUtil.sendOKHttpRequest( "http://api.jisuapi.com/news/get?channel=头条&start=0&num=40&appkey=39c530448f7f431f", new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseData=response.body().string();
                Log.e( "haibo66666666666", responseData );
                DealtheResult(responseData);
            }
        } );


        //recycler的布局适配
        GridLayoutManager layoutManager=new GridLayoutManager( this,2 );
        recyclerView.setLayoutManager(layoutManager  );

        //隐藏自带布局
        ActionBar actionBar=getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator(  R.drawable.abc);
        }

        //侧滑的菜单栏的点击事件
        navigationView.setCheckedItem( R.id.nav );
        navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){

                    //逻辑
                }

                return true;
            }
        } );

            //fab的点击事件
        floatingActionButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make( v,"我被点击了" ,Snackbar.LENGTH_SHORT)
//                        .setAction( "Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText( MainActivity.this,
//                                        "exit",Toast.LENGTH_SHORT ).show();
//
//                            }
//                        } ).show();
                Intent intent =new Intent( MainActivity.this,Main2Activity.class );
                startActivity( intent );

            }
        } );

    }

    //刷新数据的方法
    private void refershNews() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep( 2000 );

                }catch (InterruptedException e){
                    e.printStackTrace();


                }
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {

//                        HttpUtil.sendOKHttpRequest( "http://api.jisuapi.com/news/get?channel=头条&start=0&num=10&appkey=39c530448f7f431f", new okhttp3.Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//
//                                String responseData=response.body().string();
//                                Log.e( "haibo66666666666", responseData );
//                                DealtheResult(responseData);
//                            }
//                        } );



                        newsAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing( false );

                    }
                } );
            }
        } ).start();
    }

    //数据的解析
    private void DealtheResult(String  responseData) {
        try {
            JSONObject jsonObject = new JSONObject( responseData );
            JSONObject jsonObject1 = jsonObject.getJSONObject( "result" );
            JSONArray jsonArray = jsonObject1.getJSONArray( "list" );
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.get( i);
                String title = jsonObject2.getString( "title" );
                String pic=jsonObject2.getString( "pic" );
                data.add( title );
                data1.add( pic );
                Log.e( "haibo777777777777", String.valueOf( data ) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult(data);
    }

    //数据的显示
    private void showResult(List<String> data) {
        Log.e( "9999999", String.valueOf( data) );
        News[] news=new News[data.size()];
        for (int j=0;j<data.size();j++){
            news[j]=new News(data.get(j),data1.get(j));

            mNews.add( news[j] );

        }

        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                newsAdapter =new NewsAdapter( mNews );

                recyclerView.setAdapter( newsAdapter );


            }
        } );



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                drawerLayout.openDrawer( GravityCompat.START );
                break;
                default:
                    break;

        }
        return true;
    }
}
