package com.example.myrxjavatest01;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Handler handler;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, Thread.currentThread().getName());

        Rxjava();
        Rxjava2();
        delay();


        button=findViewById(R.id.bt_show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void Rxjava2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {



                e.onNext(5);

                e.onNext(6);
                e.onComplete();
                e.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Log.e(TAG, "onNext: "+"shoudaole" );

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: "+"0000000" );

            }
        });
    }

    private void delay() {
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(MainActivity.this, ThreadTranslateActivity.class);
                startActivity(intent);


            }
        },6000);
    }

    private void Rxjava() {
        //创建被观察者对象  在复写的subscribe中进行事件的分发
        Observable<Integer> observable=Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {


                //事件的分发
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });

        //创建观察者对象 定义响应事件的行为
        Observer<Integer> observer=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe: "+"zai绑定" );

            }

            @Override
            public void onNext(Integer value) {

                Log.e(TAG, "onNext: "+"接收next事件" );

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "+"接收响应Error事件" );


            }

            @Override
            public void onComplete() {

                Log.e(TAG, "onComplete: "+"响应onComplete事件" );

            }
        };

        //被观察者和观察者通过订阅进行绑定
        observable.subscribe(observer);
    }



}
