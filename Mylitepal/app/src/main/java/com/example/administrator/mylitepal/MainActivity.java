package com.example.administrator.mylitepal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button,button1,button2,button3,button4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.bt_db);
        button1=findViewById(R.id.bt_add);
        button2=findViewById(R.id.bt_update);
        button3=findViewById(R.id.bt_delete);
        button4=findViewById(R.id.bt_query);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LitePal.getDatabase();

                Toast.makeText(MainActivity.this,"database created sucessful",Toast.LENGTH_SHORT).show();

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                People people=new People();
                people.setName("wenhaibo");
                people.setAge(22);
                people.setHobby("visit");
                people.setSex("man");
                people.save();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                People people=new People();
                people.setName("jiangdongfengyouqi");
                people.setSex("man");
                people.setHobby("artist");
                people.setAge(20);
                people.save();
                people.setAge(22);
                people.save();
              //  people.updateAll("name= ? and sex = ?","haibowen","woman");

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(People.class,"name =?","wenhaibo");

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<People> people=DataSupport.findAll(People.class);
                for (People people1:people){

                }
            }
        });
    }
}
