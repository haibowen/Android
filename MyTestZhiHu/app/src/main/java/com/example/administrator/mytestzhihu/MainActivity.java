package com.example.administrator.mytestzhihu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.OkHttp;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_request);

       button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


                String url="https://news-at.zhihu.com/api/4/news/latest";
                OkHttp.OkhttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {


                        Log.e("2222", "onResponse: "+response );
                    }
                });

            }

}
