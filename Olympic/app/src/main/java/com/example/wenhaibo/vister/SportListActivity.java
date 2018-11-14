package com.example.wenhaibo.vister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;

import list.Mylist10Activity;
import list.Mylist11Activity;
import list.Mylist12Activity;
import list.Mylist13Activity;
import list.Mylist14Activity;
import list.Mylist15Activity;
import list.Mylist1Activity;
import list.Mylist2Activity;
import list.Mylist3Activity;
import list.Mylist4Activity;
import list.Mylist5Activity;
import list.Mylist6Activity;
import list.Mylist7Activity;
import list.Mylist8Activity;
import list.Mylist9Activity;

public class SportListActivity extends AppCompatActivity  {
    private ListView LV;
    private MyAdapter MyAdapter;
    private ImageView IV;
    private TextView TV;


    private String tet[]={"短道速滑","速度滑冰","花样滑冰","冰球","冰壶","俯式冰橇","自由式滑雪","单板滑雪","无舵雪橇","有舵雪橇","高山滑雪","跳台滑雪","越野滑雪","北欧两项","冬季两项",};
    private int[] icon={R.drawable.u,R.drawable.u1,R.drawable.u2,R.drawable.u3,R.drawable.u4,R.drawable.u5,R.drawable.u6,R.drawable.u7,R.drawable.u8,R.drawable.u9,R.drawable.u10,R.drawable.u11,R.drawable.u12,R.drawable.u13,R.drawable.u14};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportlist);
        LV=(ListView)findViewById(R.id.LV);
        LV.setAdapter(new MyAdapter());


        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent0=new Intent(SportListActivity.this, Mylist1Activity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1=new Intent(SportListActivity.this, Mylist2Activity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2=new Intent(SportListActivity.this, Mylist3Activity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3=new Intent(SportListActivity.this, Mylist4Activity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4=new Intent(SportListActivity.this, Mylist5Activity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5=new Intent(SportListActivity.this, Mylist6Activity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6=new Intent(SportListActivity.this, Mylist7Activity.class);
                        startActivity(intent6);
                        break;
                    case 7:
                        Intent intent7=new Intent(SportListActivity.this, Mylist8Activity.class);
                        startActivity(intent7);
                        break;
                    case 8:
                        Intent intent8=new Intent(SportListActivity.this, Mylist9Activity.class);
                        startActivity(intent8);
                        break;
                    case 9:
                        Intent intent9=new Intent(SportListActivity.this, Mylist10Activity.class);
                        startActivity(intent9);
                        break;
                    case 10:
                        Intent intent10=new Intent(SportListActivity.this, Mylist11Activity.class);
                        startActivity(intent10);
                        break;
                    case 11:
                        Intent intent11=new Intent(SportListActivity.this, Mylist12Activity.class);
                        startActivity(intent11);
                        break;
                    case 12:
                        Intent intent12=new Intent(SportListActivity.this, Mylist13Activity.class);
                        startActivity(intent12);
                        break;
                    case 13:
                        Intent intent13=new Intent(SportListActivity.this, Mylist14Activity.class);
                        startActivity(intent13);
                        break;
                    case 14:
                        Intent intent14=new Intent(SportListActivity.this, Mylist15Activity.class);
                        startActivity(intent14);
                        break;



                }







                // 全屏显示的方法
               /* final Dialog dialog = new Dialog(SportListActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                ImageView imgView = new ImageView(SportListActivity.this);
                imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                InputStream is = getResources().openRawResource(icon[position]);
                Drawable drawable = BitmapDrawable.createFromStream(is, null);
                imgView.setImageDrawable(drawable);
                dialog.setContentView(imgView);
                dialog.show();
                // 点击图片消失
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });*/

            }
        });




    }

  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main,menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Back:{
                Intent intent=new Intent(SportListActivity.this,FunctionActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.refresh:{
                Intent intent=new Intent(SportListActivity.this,SportListActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    public class MyAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(SportListActivity.this,R.layout.items,null);
          ImageView  IV=(ImageView) view.findViewById(R.id.IV);
           IV.setBackgroundResource(icon[position]);
            //TV.setBackgroundResource(Integer.parseInt(tet[position]));
            TV=(TextView)view.findViewById(R.id.TV);
            TV.setText(tet[position]);

            return view;
        }
    }


}
