package com.example.administrator.myhttputil;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpUtilCallBack {

    public static  void  sendHttpRequest(final  String adress,final HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpsURLConnection  httpsURLConnection=null;

                try{


                    URL url=new URL(adress);
                    httpsURLConnection= (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setReadTimeout(8000);
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setConnectTimeout(8000);
                    httpsURLConnection.setDoInput(true);
                    httpsURLConnection.setDoOutput(true);
                    InputStream inputStream=httpsURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=bufferedReader.readLine())!=null){
                        response.append(line);
                    }
                   if (listener!=null){
                       listener.onFinish(response.toString());

                   }

                }catch (Exception e){
                    e.printStackTrace();
                    if (listener!=null){
                        listener.onError(e);
                    }

                }finally {
                    if (httpsURLConnection!=null){
                        httpsURLConnection.disconnect();
                    }
                }


            }
        }).start();




    }




}
