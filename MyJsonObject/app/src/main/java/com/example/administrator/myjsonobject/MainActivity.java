package com.example.administrator.myjsonobject;

import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.myjsonobject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private TextView textView;
    private static  final String TAG="wenhaibo";

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
                           // .url("http://www.baidu.com")
                            .url("http://10.0.2.2/bobo.json")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    //ShowResponse(responseData);
                    Log.d("wenhaibo", "run: "+responseData);
                    ParseJsonWithJsonObject(responseData);
                    ParseJsonWithGSON(responseData);


                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        }).start();
    }

    private void ParseJsonWithGSON(String responseData) {
        Gson gson=new Gson();
        List<App> list=gson.fromJson(responseData,new TypeToken<List<App>>(){}.getType());
        for (App app:list){
            Log.d(TAG, "ParseJsonWithGSON: "+app.getId());
            Log.d(TAG, "ParseJsonWithGSON: "+app.getName());
            Log.d(TAG, "ParseJsonWithGSON: "+app.getVersion());

        }




    }

    private void ParseJsonWithJsonObject(String responseData) {
        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray(responseData);
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String version = jsonObject.getString("version");
                String name = jsonObject.getString("name");
                Log.d("wenhaibo", "ParseJsonWithJsonObject: "+id);
                Log.d("wenhaibo", "ParseJsonWithJsonObject: "+version);
                Log.d("wenhaibo", "ParseJsonWithJsonObject: "+name);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }





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
