package activity;

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
import android.widget.Switch;
import android.widget.TextView;

import com.example.administrator.mycompanytest02.R;

import java.util.ArrayList;
import java.util.List;

import adapter.SportAdapter;
import bean.Sport;

public class CourseActivity extends AppCompatActivity {
    private ListView listView;
    //private String []data={"折返训练","反应时间训练"};

    private SportAdapter sportAdapter;
    private List<Sport> sportList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        initSportList();//数据源初始化

        listView=findViewById(R.id.listview_course);
        sportAdapter=new SportAdapter(this,R.layout.itemsport,sportList);
        listView.setAdapter(sportAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               switch (view.getId()){

                   case 0:

                       Intent intent=new Intent(CourseActivity.this,ReentryTestActivity.class);
                       startActivity(intent);
                       break;

               }
            }
        });
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(CourseActivity.this,android.R.layout.simple_list_item_1,data){
//
//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                TextView textView= (TextView) super.getView(position, convertView, parent);
//                textView.setGravity(Gravity.CENTER);
//
//
//
//                return textView;
//            }
//        };
//
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//
//                    case 0:
//                        Intent intent=new Intent(CourseActivity.this,ReentryTestActivity.class);
//                        startActivity(intent);
//
//
//                        break;
//
//                    case 1:
//                        break;
//
//                }
//            }
//        });



    }
  public void   initSportList(){

        for (int i=0;i<3;i++){

            Sport sport=new Sport("主标题折返训练","副标题折返训练",R.drawable.fab_bg_normal);
            sportList.add(sport);
        }


    }
}
