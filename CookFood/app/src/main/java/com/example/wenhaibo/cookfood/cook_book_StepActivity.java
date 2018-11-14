package com.example.wenhaibo.cookfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class cook_book_StepActivity extends Activity {
    private ListView lv_step_listview;
    private cook_book_Person step_img;
    //步骤 图片链接
    private ArrayList list_step;
    private ArrayList list_image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cookbook_activity_step);
        lv_step_listview = (ListView)findViewById(R.id.lv_step_listview);
        //接收对象
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        step_img = (cook_book_Person) bd.getSerializable("list_s_i");

       /* list_step = step_img.getList_step();
        list_image_url = step_img.getList_image_url();*/

        cook_book_MyAdapter cookbookMyAdapter = new cook_book_MyAdapter(cook_book_StepActivity.this, step_img);
        lv_step_listview.setAdapter(cookbookMyAdapter);
    }
}
