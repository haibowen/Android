package com.example.wenhaibo.vister;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;

import map.IndexActivity;
import newnote.NoteActivity;


public class FunctionActivity extends Activity {
    private GridView GV;
    private MyAdapter MyAdapter;
    private ImageView IV;
    private TextView TV;
    private int[] icon = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i};
    private String[] Tex = {"酒店预订", "周边美食", "定位导航", "一键求医", "项目简介", "纪念品购买", "心情笔记", "天气预告","机票预订"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        GV = (GridView) findViewById(R.id.GV);
        GV.setLayoutAnimation(getAnimationController());
        GV.setAdapter(new MyAdapter());
        GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://hotel.qunar.com/"));
                        startActivity(intent);
                        break;
                        /*Intent intent=new Intent(FunctionActivity.this,HotelActivity.class);
                        startActivity(intent);
                        break;*/
                    }
                    case 1: {
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("http://www.meituan.com/?mtt=1.index%2Fchangecity.0.0.j21duhea"));
                        startActivity(intent1);
                        break;
                       /* Intent intent1=new Intent(FunctionActivity.this,FoodActivity.class);
                        startActivity(intent1);
                        break;*/
                    }
                    case 2: {
                        Intent intent2 = new Intent(FunctionActivity.this, IndexActivity.class);
                        startActivity(intent2);
                        break;
                    }
                    case 3: {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FunctionActivity.this);
                        dialog.setTitle(" HELP ME！！！");
                        dialog.setMessage("紧急电话拨打,请确认你是否需要！！！");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent3 = new Intent(Intent.ACTION_CALL);
                                intent3.setData(Uri.parse("tel:10086"));
                                if (ActivityCompat.checkSelfPermission(FunctionActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;

                                }
                                startActivity(intent3);


                            }

                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                            }
                        });

                    dialog.show();
                    break;
                    }

                    case 4:{
                        Intent intent4=new Intent(FunctionActivity.this,SportListActivity.class);
                        startActivity(intent4);
                        break;

                    }
                    case 5:{
                        Intent intent5=new Intent(FunctionActivity.this,ScanActivity.class);
                        startActivity(intent5);
                        break;
                    }
                    case 6:{
                        Intent intent6=new Intent(FunctionActivity.this, NoteActivity.class);
                        startActivity(intent6);
                        break;

                    }
                    case 7:{
                        Intent intent7=new Intent(FunctionActivity.this, Mainweather.class);
                        startActivity(intent7);
                        break;

                    }
                    case 8:{
                        Intent intent8 = new Intent(Intent.ACTION_VIEW);
                        intent8.setData(Uri.parse("https://flight.qunar.com"));
                        startActivity(intent8);
                        break;

                    }
                }

            }
        });
    }




        protected LayoutAnimationController getAnimationController() {
            int duration=300;
            AnimationSet set = new AnimationSet(true);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(duration);
            set.addAnimation(animation);

            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            animation.setDuration(duration);
            set.addAnimation(animation);

            LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
            controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
            return controller;
        }






    public class  MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return icon.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


           /* View view =View.inflate(FunctionActivity.this,R.layout.items1,null);

            IV=(ImageView)view.findViewById(R.id.IV);
            IV.setImageResource(icon[position]);
            return view;*/

            //得到布局接口
            LayoutInflater from = LayoutInflater.from(FunctionActivity.this);
            //得到另一个布局文件的view
            View view = from.inflate(R.layout.items1, null);
            //得到布局的组件对象
            ImageView IV = (ImageView) view.findViewById(R.id.IV);
            TextView TV = (TextView) view.findViewById(R.id.TV);
            //图文混排
            IV.setImageResource(icon[position]);
            TV.setText(Tex[position]);
            //返回 view
            return view;
        }
    }
}
