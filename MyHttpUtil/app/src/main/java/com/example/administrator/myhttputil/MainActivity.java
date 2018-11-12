package com.example.administrator.myhttputil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //普通的网络请求

        String adress="https://www.baidu.com";
        HttpUtilCallBack.sendHttpRequest(adress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        //okhttp的网络请求

        OkhttpUtil.sendOkhttpRequest(adress, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                
                String respionsedata=response.body().string();
            }
        });




    }
}
