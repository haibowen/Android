package com.example.administrator.myokhttp;

import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_request);
        textView=findViewById(R.id.te_show);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_request:
                SendRequestWithOkHttp();


                break;
        }

    }

    private void SendRequestWithOkHttp() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    ShowResponse(responseData);

                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        }).start();
    }

    private void ShowResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
            }
        });


    }
}
