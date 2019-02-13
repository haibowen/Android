package com.example.administrator.mycontext;

import android.content.*;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;

import java.io.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button, button1,button2;

    private MyserviceTest.MyTask myTask;

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myTask= (MyserviceTest.MyTask) service;
            //myTask 调用服务里的方法，实现活动对service的控制
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

   public void save(){

       String data="this is data";
       FileOutputStream fileOutputStream=null;
       BufferedWriter bufferedWriter=null;
       try {
           fileOutputStream=openFileOutput("data", Context.MODE_PRIVATE);
           bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
           bufferedWriter.write(data);
       }catch (IOException e){
           e.printStackTrace();
       }finally {
           try {
               if (bufferedWriter!=null){
                   bufferedWriter.close();
               }
           }catch (IOException e){
               e.printStackTrace();
           }
       }
   }
   public  String read(){
       FileInputStream fileInputStream=null;
       BufferedReader bufferedReader=null;
       StringBuilder  stringBuilder=new StringBuilder();
       try {
           fileInputStream=openFileInput("data");
           bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream));
           String line="";
           while ((line=bufferedReader.readLine())!=null){
               stringBuilder.append(line);
           }

       }catch (IOException e){
           e.printStackTrace();
       }finally {
           if (bufferedReader!=null){
               try{
                   bufferedReader.close();
               }catch (IOException e){
                   e.printStackTrace();
               }
           }
           return  bufferedReader.toString();
       }
   }


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







