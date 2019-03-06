package com.example.myrxjavatest01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ThreadTranslateActivity extends AppCompatActivity {
    private static final String TAG = "ThreadTranslateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Observable<Integer> observable=Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {


                e.onNext(1);
                Log.e(TAG, "subscribe: "+ Thread.currentThread().getName() );
            }
        });

        Consumer<Integer> consumer=new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

                Log.e(TAG, "accept: "+Thread.currentThread().getName() );
                Log.e(TAG, "accept: "+integer );

            }
        };


        //Rxjava切换线程

        observable.subscribeOn(Schedulers.newThread())//上游 被观察者发送事件的线程
                .observeOn(AndroidSchedulers.mainThread())//下游 观察者接收事件的线程
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                        Log.e(TAG, "accept: "+Thread.currentThread().getName() );
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: "+Thread.currentThread().getName() );


                    }
                })
                .subscribe(consumer);

    }
}
