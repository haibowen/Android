package com.example.myrxjavatest01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Rxjava2();
    }

    private void Rxjava2() {
        Observable<Integer> observable=Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {


                e.onNext(1);
                e.onComplete();

            }
        });

        Observer<Integer> observer=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Log.e(TAG, "onNext: "+"222222" );

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

                Log.e(TAG, "onComplete: "+"33333333" );

            }
        };

        observable.subscribe(observer);
    }
}
