package com.example.administrator.myhttpurlconnection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private TextView textView;
    private StringBuilder response=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.te_show);
        button=findViewById(R.id.bt_request);

        button.setOnClickListener(this);

        //注册EventBus
        EventBus.getDefault().register(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_request:
                SendRequestWithHttpUrlConnection();

                break;


        }

    }

    private void SendRequestWithHttpUrlConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection=null;
                BufferedReader bufferedReader=null;
                try {
                    URL url=new URL("https://www.baidu.com");
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    /**
                    connection.setRequestMethod("POST");
                    DataOutputStream dataOutputStream=new DataOutputStream(connection.getOutputStream());
                    dataOutputStream.writeBytes(("username=admin&password=123456"));
                     **/
                    //获取输入流
                    InputStream inputStream=connection.getInputStream();

                    //读取输入流 InputStream
                    bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    response=new StringBuilder();
                    String line;
                    while ((line=bufferedReader.readLine())!=null){
                        response.append(line);

                    }
                    EventBus.getDefault().post(new Message("更新数据"));

                    showResponse(response.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (bufferedReader!=null){

                        try{
                            bufferedReader.close();
                        }catch (Exception e){
                            e.printStackTrace();

                        }

                    }
                    if (connection!=null){
                        connection.disconnect();

                    }
                }

            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void Event(Message message){

        if (("更新数据").equals(message)){
            textView.setText(response);
        }


    }
}
