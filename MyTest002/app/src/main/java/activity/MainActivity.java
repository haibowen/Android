package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mytest002.R;

import java.io.PrintWriter;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button buttonNormal,buttonDialog;
    private Handler handler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        Log.e(TAG, "onCreate: " );

        HoldUP();
    }

    private void HoldUP() {
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(MainActivity.this, ListViewActivity.class);
                startActivity(intent);
            }
        },5000);
    }

    private void initView() {
        buttonNormal=findViewById(R.id.bt_NormalActivity);
        buttonDialog=findViewById(R.id.bt_DialogActivity);
        buttonDialog.setOnClickListener(this);
        buttonNormal.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        getMenuInflater().inflate(R.menu.main,menu);
        return true;



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){


            case R.id.first01:

                Intent intent=new Intent("com.example.mytest002.ACTION_START");
                intent.addCategory("com.example.mytest002.MYTEST");
                startActivity(intent);

                break;
            case R.id.first02:
                Intent intent1=new Intent("android.net.haibowen");
                sendBroadcast(intent1);
                Toast.makeText(MainActivity.this,"我确实发送了",Toast.LENGTH_SHORT).show();
                break;




        }




        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_NormalActivity:

                //Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                //startActivity(intent);
                Main2Activity.actionStart(MainActivity.this,"1","1");

                break;

            case R.id.bt_DialogActivity:
                Intent intent1=new Intent(MainActivity.this,DialogActivity.class);
                startActivity(intent1);

                break;
        }

    }

    //延时操作




    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: " );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: " );
    }
}
