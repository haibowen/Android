package com.example.administrator.myhttputil;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpUtil {

    public static  String sendHttpRequest(String adress){
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
            return response.toString();

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }finally {
            if (httpsURLConnection!=null){
                httpsURLConnection.disconnect();
            }
        }



    }




}
