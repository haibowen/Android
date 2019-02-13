package com.example.administrator.mylmada;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private Listener listener=(String name,int price)->{
        String id=name+price;
        return id;

    };
    /**
    private  Listener listener=(name,price)->{
        String id=name+price;
        return id;

    };
**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hello((name,price)->{
            String result=name+price;
            return result;

        });
    }

    public  void  testLambda(){

        new Thread(()->{

        }).start();

        /**

        Runnable runnable=new Runnable() {
            @Override
            public void run() {

            }
        };

         **/
        Runnable runnable=()->{

        };



    }

    public  void hello(Listener listener){
        String name="wenhaibo";
        int price=22;
        String result=listener.JustDoIt(name,price);
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
