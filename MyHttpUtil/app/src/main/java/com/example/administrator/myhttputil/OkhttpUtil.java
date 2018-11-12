package com.example.administrator.myhttputil;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkhttpUtil {

    public  static  void sendOkhttpRequest(String addres,okhttp3.Callback callback){

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(addres)
                .build();
        client.newCall(request).enqueue(callback);


    }



}
