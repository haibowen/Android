package com.example.wenhaibo.androidstudy_drawerlayout;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static  void  sendOKHttpRequest(String adress,
                                           okhttp3.Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url( adress )
                .build();
        okHttpClient.newCall( request ).enqueue( callback );
    }
}
