package com.example.administrator.myviewpagertest;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    private MyAdapter myAdapter;


    private View view;
    private View view1;
    private View view2;
    private ArrayList<View> pageview;

    private ImageView[] tips=new ImageView[3];
    private ImageView imageView;

    private ViewGroup viewGroup;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        viewPager=findViewById(R.id.viewpager);

        view=getLayoutInflater().inflate(R.layout.view1,null);
        view1=getLayoutInflater().inflate(R.layout.view2,null);
        view2=getLayoutInflater().inflate(R.layout.view3,null);
        pageview=new ArrayList<View>();
        pageview.add(view);
        pageview.add(view1);
        pageview.add(view2);



        myAdapter=new MyAdapter(pageview);

        viewGroup=findViewById(R.id.linearlayout);
        tips=new ImageView[pageview.size()];

        for (int i=0;i<pageview.size();i++){

            imageView=new ImageView(MainActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(12,12));
            imageView.setPadding(12,0,12,0);
            tips[i]=imageView;

            //morenxuanzhong
            if (i==0){
                tips[i].setBackgroundResource(R.mipmap.cir_fouce);
            }else {

                tips[i].setBackgroundResource(R.mipmap.cir_unfouce);

            }
            viewGroup.addView(tips[i]);


        }

        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

        tips[i].setBackgroundResource(R.mipmap.cir_fouce);
        for (int j=0;j<pageview.size();j++){
            if (i!=j){

                tips[j].setBackgroundResource(R.mipmap.cir_unfouce);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
