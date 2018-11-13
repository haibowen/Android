package com.example.administrator.mycontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button, button1,button2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.bt_show);
        button1=findViewById(R.id.bt_intent);
        button2=findViewById(R.id.bt_intent2);


        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case  R.id.bt_show:
                ToastTest.show();
                break;
            case  R.id.bt_intent:
                Person person=new Person();
                person.setAge(16);
                person.setName("wenhaibo");
                Intent intent=new Intent(MyApplication.getContext(),Main2Activity.class);
                intent.putExtra("data",person);
                startActivity(intent);
                break;
            case R.id.bt_intent2:
                Animal animal=new Animal();
                animal.setAge(22);
                animal.setName("luanwuchunqiu");
                Intent intent1=new Intent(MainActivity.this,Main2Activity.class);
                intent1.putExtra("bobo",animal);
                startActivity(intent1);
                break;

        }
    

    }


}







