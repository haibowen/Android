package player;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;
import com.example.wenhaibo.vister.HeartRate;
import com.example.wenhaibo.vister.PlayerJokeActivity;

import CookFood.cook_book_MainActivity;
import newnote.NoteActivity;

public class PlayerActivity extends AppCompatActivity {
    private GridView GV;
    private TextView TV;
    private ImageView IV;
    private Mydapter mydapter;
    private int icon[] = {R.drawable.a1, R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6,R.drawable.a7,R.drawable.a8,R.drawable.a9};
    private String word[] = {"健康检测", "时间提醒", "状态调整", "做点美食", "赛程预报", "训练心得", "知己知彼", "周边景点", "轻松一刻"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        GV = (GridView) findViewById(R.id.GV);
        GV.setAdapter(new Mydapter());
        GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :{
                        Intent intent =new Intent(PlayerActivity.this, HeartRate.class);
                        startActivity(intent);

                        break;

                    }
                    case 1:{
                        Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);
                        startActivity(alarms);


                        break;

                    }
                    case 2:{
                        Intent intent2 = new Intent("android.intent.action.MUSIC_PLAYER");
                        startActivity(intent2);
                        finish();
                        /*Intent mIntent = new Intent();
                        ComponentName comp = new ComponentName("com.android.music","com.android.music.MusicBrowserActivity");
                        mIntent.setComponent(comp);
                        mIntent.setAction(android.content.Intent.ACTION_VIEW);
                        startIntent(intent);*/


                        break;

                    }
                    case 3:{
                        Intent intent =new Intent(PlayerActivity.this, cook_book_MainActivity.class);
                        startActivity(intent);
                        finish();



                        break;

                    }
                    case 4:{



                        break;

                    }
                    case 5:{
                        Intent intent5=new Intent(PlayerActivity.this, NoteActivity.class);
                        startActivity(intent5);
                        break;

                    }
                    case 6:{
                        break;

                    }
                    case 7:{
                        Intent intent7=new Intent(Intent.ACTION_VIEW);
                        intent7.setData(Uri.parse("https://lvyou.baidu.com/zhangjiakou/?from=zhixin"));
                        startActivity(intent7);
                        break;

                    }
                    case 8:{
                        Intent intent=new Intent(PlayerActivity.this, PlayerJokeActivity.class);
                        startActivity(intent);
                        break;

                    }
                }
            }
        });
    }
    public class Mydapter extends BaseAdapter{

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
            View view =View.inflate(PlayerActivity.this,R.layout.item,null);
            IV=(ImageView)view.findViewById(R.id.IV);
            TV=(TextView)view.findViewById(R.id.TV);
            IV.setImageResource(icon[position]);
            TV.setText(word[position]);

            return view;
        }
    }
}


