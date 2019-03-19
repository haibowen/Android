package activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.administrator.mycompanytest02.R;

public class ReentryTestActivity extends AppCompatActivity {

    private Spinner spinner,spinner1,spinner2,spinner3,spinner4,spinner5,spinner6;
    private String [] data=new String[100];
    private String [] data1=new String[10];
    private String [] data2=new String[5];
    private String []data3={"红色","绿色","蓝色"};
    private String []data4={"弱光","中等","强光"};

    private String []data5={"折返"};
    private String [] data6={"4"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reentry_test);
        getData();
        getData1();
        getData2();

        spinner=findViewById(R.id.spinner_item);
        spinner1=findViewById(R.id.spinner_item1);
        spinner2=findViewById(R.id.spinner_item2);
        spinner3=findViewById(R.id.spinner_item3);
        spinner4=findViewById(R.id.spinner_item4);
        spinner5=findViewById(R.id.spinner_name);
        spinner6=findViewById(R.id.spinner_num);


        //spinner1
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(ReentryTestActivity.this,android.R.layout.simple_list_item_1,data);

        spinner.setAdapter(adapter);
        //spinner2
        ArrayAdapter<String>adapter1=new ArrayAdapter<String>(ReentryTestActivity.this,android.R.layout.simple_list_item_1,data1);
        spinner1.setAdapter(adapter1);
        //spinner3
        ArrayAdapter<String>adapter2=new ArrayAdapter<String>(ReentryTestActivity.this,android.R.layout.simple_list_item_1,data2);
        spinner2.setAdapter(adapter2);
        //spinner4
        ArrayAdapter<String>adapter3=new ArrayAdapter<String>(ReentryTestActivity.this,android.R.layout.simple_list_item_1,data3);
        spinner3.setAdapter(adapter3);
        //spinner5
        ArrayAdapter<String>adapter4=new ArrayAdapter<String>(ReentryTestActivity.this,android.R.layout.simple_list_item_1,data4);
        spinner4.setAdapter(adapter4);
        //spinner6
        ArrayAdapter<String>adapter5=new ArrayAdapter<String>(ReentryTestActivity.this,android.R.layout.simple_list_item_1,data5);
        spinner5.setAdapter(adapter5);
        //spinner7
        ArrayAdapter<String>adapter6=new ArrayAdapter<String>(ReentryTestActivity.this,android.R.layout.simple_list_item_1,data6);
        spinner6.setAdapter(adapter6);




    }
    //数据源的获取

    private void getData(){

        for (int i = 0; i <100 ; i++) {
            data[i]= String.valueOf(i+1);

        }
    }
    private void getData1(){

        for (int i = 0; i <10 ; i++) {
            data1[i]= String.valueOf(i+1);

        }
    }
    private void getData2(){

        for (int i = 0; i <5 ; i++) {
            data2[i]= String.valueOf(i+1);

        }
    }
}
