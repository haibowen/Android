package com.example.wenhaibo.wechattest1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Button.AuterRecoderButton;

public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas =new ArrayList<Recorder>();
    private AuterRecoderButton auterRecoderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.LV);
        auterRecoderButton=(AuterRecoderButton)findViewById(R.id.AB);
        auterRecoderButton.setAudioFinshRecoderListener(new AuterRecoderButton.AudioFinshRecoderListener() {
            @Override
            public void onFinsh(float seconds, String filepath) {
                Recorder recorder=new Recorder(seconds,filepath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                listView.setSelection(mDatas.size()-1);
            }
        });

        mAdapter=new RecorderAdapter(this,mDatas);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放动画


                //播放音频
                MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                    }
                });

            }
        });
    }





    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }


}


class Recorder
{
    float time;
    String filePath;

    public Recorder(float time, String filePath) {
        this.time = time;
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
