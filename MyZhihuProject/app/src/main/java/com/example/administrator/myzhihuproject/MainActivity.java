package com.example.administrator.myzhihuproject;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private List<String> mtitle=new ArrayList<>();
    private List<String> mimage=new ArrayList<>();
    private List<String>mdata=new LinkedList<>();
    private List<News> mydata=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //侧滑
        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.nav_view);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        }
        //侧滑的导航菜单的点击事件
        navigationView.setCheckedItem(R.id.nav_first);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();

                return true;
            }
        });

        //数据获取
        GetInternetData();


        //RecyclerView
        recyclerView=findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        //




    }

//顶部菜单
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.toolbar,menu);
        return  true;

    }
//菜单的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.first:
                Toast.makeText(this,"onclick",Toast.LENGTH_SHORT).show();

                break;
            case R.id.second:

                break;
            case R.id.third:

                break;
                default:
                    break;

        }
        return true;
    }
    //请求网络数据
    public  void  GetInternetData(){

        MyHttpUtil.SendRequestWithOkHttp("https://news-at.zhihu.com/api/4/news/latest",
                new okhttp3.Callback()  {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String ResponseData=response.body().string();

                DealWithResponseData(ResponseData);

            }
        });
    }
    //解析数据
    public  void DealWithResponseData(String ResponseData){
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(ResponseData);
            Log.e("2222", "DealWithResponseData: "+jsonObject );



            JSONArray jsonArray=jsonObject.getJSONArray("stories");
            Log.e("1234", "DealWithResponseData: "+jsonArray );

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1= (JSONObject) jsonArray.get(i);
                String title=jsonObject1.getString("title");

                String image=jsonObject1.getString("image");


                mtitle.add(title);
                mimage.add(image);
                GetDataSource(mtitle);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    //适配器的数据源
    public  void GetDataSource(List<String> mimage){


        News [] news=new News[mimage.size()];
        for (int i=0;i<mimage.size();i++){


            news[i]=new News(mtitle.get(i),mimage.get(i));







            mydata.add(news[i]);

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyAdapter myAdapter=new MyAdapter(mydata);
                recyclerView.setAdapter(myAdapter);
            }
        });

    }
}
