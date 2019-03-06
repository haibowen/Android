package com.example.administrator.mycompanytest02;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseActivity extends AppCompatActivity {
    private ListView listView;
    private String []data={"折返训练","反应时间训练"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        listView=findViewById(R.id.listview_course);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(CourseActivity.this,android.R.layout.simple_list_item_1,data){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView= (TextView) super.getView(position, convertView, parent);
                textView.setGravity(Gravity.CENTER);



                return textView;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0:
                        Intent intent=new Intent(CourseActivity.this,ReentryTestActivity.class);
                        startActivity(intent);


                        break;

                    case 1:
                        break;

                }
            }
        });

    }
}
