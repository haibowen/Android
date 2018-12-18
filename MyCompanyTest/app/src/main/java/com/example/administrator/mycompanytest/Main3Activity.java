package com.example.administrator.mycompanytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main3Activity extends AppCompatActivity {
    private ListView listView;
    //private String []data={LcdInfo.getDisplayAbsolutewidth(this),LcdInfo.getDisplayAbsoluteheight(this),LcdInfo.getDisplayWidth(this),LcdInfo.getDisplayHeight(this),LcdInfo.getDisplayXdpi(this),LcdInfo.getDisplayYdpi(this),LcdInfo.getDisplayDpi(this),LcdInfo.getDisPlaydensity(this)};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        listView=findViewById(R.id.list_Lcd);
      String []data={LcdInfo.getDisplayAbsolutewidth(this),LcdInfo.getDisplayAbsoluteheight(this),LcdInfo.getDisplayWidth(this),LcdInfo.getDisplayHeight(this),LcdInfo.getDisplayXdpi(this),LcdInfo.getDisplayYdpi(this),LcdInfo.getDisplayDpi(this),LcdInfo.getDisPlaydensity(this)};


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Main3Activity.this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);


    }
}
