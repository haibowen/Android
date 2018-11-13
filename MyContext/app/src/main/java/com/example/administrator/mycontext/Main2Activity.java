package com.example.administrator.mycontext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Main2Activity extends AppCompatActivity {
    public  static  final  String TAG="123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /**
         *    Person person= (Person) getIntent().getSerializableExtra("data");
         *         Log.d(TAG, "onCreate: "+person.getName());
         *         Log.d(TAG, "onCreate: "+person.getAge());
         */

        Animal animal=getIntent().getParcelableExtra("bobo");
        Log.d(TAG, "onCreate: "+animal.getName());
        Log.d(TAG, "onCreate: "+animal.getAge());
    }
}
