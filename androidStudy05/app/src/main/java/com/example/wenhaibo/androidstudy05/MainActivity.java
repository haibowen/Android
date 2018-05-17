package com.example.wenhaibo.androidstudy05;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // 绑定id

        button = findViewById( R.id.bt_fragment );

        //注册点击事件

        button.setOnClickListener( this );

        //加载fragment 事件

        replaceFragment( new rightfragment() );

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bt_fragment:
                replaceFragment( new anofragment() );
                break;
            default:
                break;

        }

    }


    private void replaceFragment(Fragment fragment) {

        //获取 fragmentManager  实例
        FragmentManager fragmentManager = getFragmentManager();

        //开启一个事务

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //替换方法


        fragmentTransaction.replace( R.id.framelayout_another, fragment );

        //放进返回栈

        fragmentTransaction.addToBackStack( null );

        //提交方法

        fragmentTransaction.commit();


    }

}
