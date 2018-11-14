package activity;

import adapter.FlowerAdapterRecycler;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.myapplication.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import util.BaseActivity;
import util.Flower;
import util.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity<annoation> extends BaseActivity {
    private Button button;
    private TextView textView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    private TextView textView1, textView2;
    private FloatingActionsMenu floatingActionButton;
    private FloatingActionButton floatingActionButton1;
    private boolean fabOk = false;
    private Flower []flowers={ new Flower("牡丹", R.drawable.one),new Flower("风信子",R.drawable.two),new Flower("梅花",R.drawable.three),new Flower("海棠",R.drawable.four),new Flower("桃花",R.drawable.five),new Flower("紫荆花",R.drawable.six) };
    private List<Flower> lists=new ArrayList<>();
    private FlowerAdapterRecycler flowerAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //当前界面使用toolbar
        toolbar = findViewById(R.id.toobal);
        setSupportActionBar(toolbar);
        //eventbus 消息注册
        EventBus.getDefault().register(this);
        //activity_main 界面控件注册
        textView = findViewById(R.id.text_up);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu(floatingActionButton);
                floatingActionButton.collapse();
            }
        });
        //侧滑界面控件注册
        navigationView = findViewById(R.id.nav_hey);
        //导航控件加载头布局
        final View view = navigationView.inflateHeaderView(R.layout.native_header);
        //头布局控件绑定id
        imageView = view.findViewById(R.id.icon_image);

        textView2 = view.findViewById(R.id.text_username);
        //
        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                openMenu(floatingActionButton);
                // Toast.makeText(MainActivity.this,"5555",Toast.LENGTH_SHORT).show();
                // Snackbar.make(floatingActionButton,"data delted",Snackbar.LENGTH_SHORT).show();

/**
                Snackbar.make(floatingActionButton, "data delted", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();

**/
            }

            @Override
            public void onMenuCollapsed() {
                closeMenu(floatingActionButton);
                //Toast.makeText(MainActivity.this,"666",Toast.LENGTH_SHORT).show();


            }
        });
        initfruit();

        recyclerView=findViewById(R.id.recylerview);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        flowerAdapter =new FlowerAdapterRecycler(lists);
        recyclerView.setAdapter(flowerAdapter);

        swipeRefreshLayout=findViewById(R.id.swipfresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refrersh();

            }
        });


        //头布局控件的点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
            }
        });
        //侧滑提示图标显示注册
        drawerLayout = findViewById(R.id.drawler);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.nav);

        }

        //侧滑导航菜单点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.first:
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.second:
                        Intent intent1=new Intent(MainActivity.this,Main4Activity.class);
                        startActivity(intent1);
                        break;
                    case R.id.third:
                        Intent intent2 =new Intent(MainActivity.this,Main5Activity.class);
                        startActivity(intent2);
                        break;
                    case  R.id.fourth:
                        Intent intent3=new Intent(MainActivity.this,Main3Activity.class);
                        startActivity(intent3);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });


    }
    public void initfruit() {

        lists.clear();
        for (int i=0;i<50;i++){

            Random random=new Random();
            int index=random.nextInt(flowers.length);
            lists.add(flowers[index]);

        }
    }

    private void openMenu(View view) {
        //ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 0, 0);
        animator.setDuration(500);
        animator.start();
        textView.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.7f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        textView.startAnimation(alphaAnimation);
        fabOk = true;

    }

    private void closeMenu(View view) {
        //ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 0, 0);
        animator.setDuration(500);
        animator.start();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 0);
        alphaAnimation.setDuration(500);

        textView.startAnimation(alphaAnimation);
        textView.setVisibility(View.GONE);
        fabOk = false;
    }


    //receiver the message
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
       //textView.setText(messageEvent.getMessage());
       if (messageEvent.getMessage().equals("更新UI")){
           initfruit();
           flowerAdapter.notifyDataSetChanged();
           swipeRefreshLayout.setRefreshing(false);

       }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobarl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.backup:
                Toast.makeText(MainActivity.this, "yes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Settings:
                Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);


    }

    public void refrersh(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    EventBus.getDefault().post(new MessageEvent("更新UI"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
