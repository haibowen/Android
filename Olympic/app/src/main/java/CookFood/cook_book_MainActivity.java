package CookFood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import player.PlayerActivity;


public class cook_book_MainActivity extends Activity {

    public static final String DEF_CHATSET = "UTF-8";
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private static final int Change = 1;
    private TextView tv_show, tv_show_name, tv_show_material, tv_show_step;
    private EditText et_input;
    private ImageView iv_main_cook;
    private cook_book_MyJson json = new cook_book_MyJson();
    private ArrayList<String> array = new ArrayList<>();
    private ArrayList<String> array1 = new ArrayList<>();
    private ArrayList<String> arrImg = new ArrayList<>();
    //步骤 图片链接
    private ArrayList list_step;
    private ArrayList list_image_url;

    // 包装对象
    private cook_book_Person list_s_i;
    private cook_book_MyJsonSteps myJsonSteps = new cook_book_MyJsonSteps();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cookcook_activity_main);
        InitUI();
        InitEvent();

    }


    //加载布局
    private void InitUI() {
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_show_name = (TextView) findViewById(R.id.tv_show_name);
        tv_show_material = (TextView) findViewById(R.id.tv_show_material);
        tv_show_step = (TextView) findViewById(R.id.tv_show_step);
        iv_main_cook = (ImageView) findViewById(R.id.iv_main_cook);
        et_input = (EditText) findViewById(R.id.et_input);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == Change) {
                String messTitle = msg.getData().getString("title");
                String messIngredients = msg.getData().getString("ingredients");
                String messburden = msg.getData().getString("burden");
                String albums = msg.getData().getString("albums");
                Log.e("picurl", albums);
                String f = albums.replace("[\"", "").replace("\"]", "");
                Picasso.with(cook_book_MainActivity.this).load(f).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(iv_main_cook);
                tv_show_name.setText("名称: " + messTitle);
                tv_show_name.setTextColor(Color.BLUE);
                tv_show.setText("材料：" + messIngredients);
                tv_show.setTextColor(Color.RED);
                tv_show_material.setText("配料：" + messburden);
                tv_show_material.setTextColor(getResources().getColor(R.color.black));
                tv_show_step.setTextColor(getResources().getColor(R.color.darkkhaki));
                tv_show_step.setText("步骤：>>>");

            } else if (msg.what == 2) {
                list_step = msg.getData().getStringArrayList("step");
                list_image_url = msg.getData().getStringArrayList("img");
                //包装成对象
                list_s_i = new cook_book_Person(list_step, list_image_url);
            }
        }
    };


    //时间  操作
    private void InitEvent() {

    }

    //搜索点击事件
    public void click(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                getRequest1();
            }
        }.start();
    }

    //步骤点击事件
    public void click_step(View view) {
        Intent intent = new Intent();
        Bundle bd = new Bundle();
        //传对象
        bd.putSerializable("list_s_i", list_s_i);
        intent.setClass(cook_book_MainActivity.this, cook_book_StepActivity.class);
        intent.putExtras(bd);
        startActivity(intent);
    }

    //配置您申请的KEY
    public static final String APPKEY = "04afe32fdc6b821147c1f3e62692e3af";

    //1.菜谱大全
    public void getRequest1() {
        String result = null;
        String url = "http://apis.juhe.cn/cook/query.php";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("menu", et_input.getText().toString());//需要查询的菜谱名
        params.put("key", APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype", "json");//返回数据的格式,xml或json，默认json
        params.put("pn", "0");//数据返回起始下标
        params.put("rn", "2");//数据返回条数，最大30
        params.put("albums", "");//albums字段类型，1字符串，默认数组

        try {
            result = net(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                //获取所有结果
                object = object.getJSONObject("result");
                Log.i("返回结果resultjson", object + "");
                //解析材料什么的
                array = json.JsonJX(object);
                MyMes(array);

                object = (JSONObject) object.getJSONArray("data").get(0);
                //解析图片链接
                arrImg = myJsonSteps.JsonJXImgs(object);

                //解析步骤
                array1 = myJsonSteps.JsonJXSteps(object);
                MyMes1(array1, arrImg);

            } else {
                Log.i("error_code", object.get("error_code") + " " + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);

            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        Log.e("gaoyureturn", rs);
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //handler传回 材料名字
    private void MyMes(ArrayList<String> array) {
        Log.e("title", array.get(0) + "******" + array.get(1) + "buzhou***" + array.get(2) + "主图片" + array.get(3));
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("title", array.get(0));
        bundle.putString("ingredients", array.get(1));
        bundle.putString("burden", array.get(2));
        bundle.putString("albums", array.get(3));
        msg.setData(bundle);
        msg.what = Change;
        handler.sendMessage(msg);
    }

    //handler传回 步骤
    private void MyMes1(ArrayList<String> array1, ArrayList<String> arrImg) {
        Message msg1 = new Message();
        Bundle bundle1 = new Bundle();
        bundle1.putStringArrayList("step", array1);
        bundle1.putStringArrayList("img", arrImg);

        msg1.setData(bundle1);
        msg1.what = 2;
        handler.sendMessage(msg1);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //退出保存
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //确认对话框  退出
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i2 = new Intent(cook_book_MainActivity.this, PlayerActivity
                    .class);
            startActivity(i2);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }


}




