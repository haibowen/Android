package com.example.administrator.myfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private     Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_left_fragment);
        button.setOnClickListener(this);
        replaceFragment(new RightFragment());


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_left_fragment:
                replaceFragment(new AnotherFragment());
                break;
                default:
                    break;


        }

    }

    public  void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.right_fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
