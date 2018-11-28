package com.example.administrator.myviewpager;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    public ViewPager viewPager;
    @BindViews({R.id.image_view0,R.id.image_view1,R.id.image_view2})
    public List<ImageView> imageViewList;

    @BindViews({R.id.fist_linerlayout, R.id.second_linlearlayout, R.id.third_linearlayout})
    public List<LinearLayout> linearLayoutList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.fist_linerlayout, R.id.second_linlearlayout, R.id.third_linearlayout})
    public void change(View view) {
        switch (view.getId()) {
            case R.id.fist_linerlayout:
                viewPager.setCurrentItem(0);


               //imageViewList.get(0).setImageDrawable(getResources().getDrawable(R.drawable.find1));
                updatabottomSelect(true, false, false);
                break;

            case R.id.second_linlearlayout:
                viewPager.setCurrentItem(1);
                //imageViewList.get(1).setBackgroundColor(mcolor);
                updatabottomSelect(false, true, false);
                break;
            case R.id.third_linearlayout:
                viewPager.setCurrentItem(2);
               // imageViewList.get(2).setBackgroundColor(mcolor);
                updatabottomSelect(false, false, true);
                break;

        }
    }

    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        ButterKnife.bind(this);

        //初始化数据
        initFragmnetList();

        //构造适配器
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager, fragmentList);

        //初始化ViewPager
        viewPager.addOnPageChangeListener(new ViewPageOnChangelistener());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);

        updatabottomSelect(true, false, false);


    }


    public void initFragmnetList() {
        Fragment findfragment = new FindFragment();
        Fragment filefragment = new FileFragment();
        Fragment myfilefragment = new MyFragment();
        fragmentList.add(findfragment);
        fragmentList.add(filefragment);
        fragmentList.add(myfilefragment);


    }

    private void updatabottomSelect(boolean b, boolean b1, boolean b2) {

        linearLayoutList.get(0).setSelected(b);


        linearLayoutList.get(1).setSelected(b1);

        linearLayoutList.get(2).setSelected(b2);



    }

    class ViewPageOnChangelistener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int i, float v, int i1) {


            /**
            ArgbEvaluator evaluator = new ArgbEvaluator(); // ARGB求值器
            int evaluate = 0x00FFFFFF; // 初始默认颜色（透明白）
            if (i == 0) {
                evaluate = (Integer) evaluator.evaluate(i1, 0XFFFF8080, 0XFFFFBC00); // 根据positionOffset和第0页~第1页的颜色转换范围取颜色值
            }else if(i == 1){
                evaluate = (Integer) evaluator.evaluate(i1, 0XFFFFBC00, 0XFF199AFE); // 根据positionOffset和第1页~第2页的颜色转换范围取颜色值
            } else if(i == 2){
                evaluate = (Integer) evaluator.evaluate(i1, 0XFF199AFE, 0XFF00AB96); // 根据positionOffset和第2页~第3页的颜色转换范围取颜色值
            }
            ((View)viewPager.getParent()).setBackgroundColor(evaluate); // 为ViewPager的父容器设置背景色


**/

        }

        @Override
        public void onPageSelected(int i) {
            boolean[] state = new boolean[3];
            state[i]=true;
            updatabottomSelect(state[0], state[1], state[2]);


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

}
