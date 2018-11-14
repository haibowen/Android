package com.example.wenhaibo.vister;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;
import com.example.wenhaibo.vister.weatherutil.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class PlayerJokeActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    //String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_joke);
        button= (Button) findViewById(R.id.btshow);
        textView=(TextView)findViewById(R.id.tvshow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpRequest("http://v.juhe.cn/joke/content/list.php?key=95afc54cc0bf09f40cf2e1e3880e42a3&page=2&pagesize=10&sort=asc&time=1418745237", new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData=response.body().string();
                        //textView.setText(responseData);
                         parseJSONWithJSONObject(responseData);

                        showResponse(responseData);
                        //parseJSONWithGSON(responseData);


                    }



                   private void parseJSONWithJSONObject(String jsonData) {
                        try{
                            JSONArray jsonArray=new JSONArray(jsonData);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                String  content=jsonObject.getString("content");
                                String hashld =jsonObject.getString("hashld");
                                String unixtime=jsonObject.getString("unixtime");
                                String updatetime =jsonObject.getString("updatetime");





                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                   /**
                    private void parseJSONWithGSON(String jsonData){
                        Gson gson=new Gson();
                        List<App> appList=gson.fromJson(jsonData, new TypeToken<List<App>>() {}.getType());
                        for (App app:appList){
                            textView.setText(app.getContent());
                        }

                    }
                    **/


                    private void showResponse(final String responseData) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(responseData);
                                //textView.setText(jsonData);
                            }
                        });
                    }
                });

            }
        });
    }
}
