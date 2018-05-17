package com.example.wenhaibo.androidstudy03;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
public class Main2Activity extends AppCompatActivity {
    private ListView listView;
    private String []data={"apple","spring","coolpera","hotapple","waterfool","banable","strawbarrey","pice","pear","holean"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        ArrayAdapter<String>adapter=new ArrayAdapter<String>( Main2Activity.this,android.R.layout.simple_list_item_1,data );
        listView=findViewById( R.id.list_simple );
        listView.setAdapter( adapter );

    }
}
