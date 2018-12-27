package com.example.administrator.mycompanytest;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private String data[]={"LCD分辨率","CPU信息","声音测试"};

    private SoundPool soundPool;
   private AudioManager mAudioManager;

   private Context mcontext;

  private   MediaPlayer mMediaPlayer = new MediaPlayer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext=this;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

       // soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC,5);
       // soundPool.load(this,R.raw.test,1);

       // am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);



        listView=findViewById(R.id.lv);

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent=new Intent(MainActivity.this,Main3Activity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(MainActivity.this,Main2Activity.class);
                        startActivity(intent1);
                        break;
                    case 2:

                        /**
                        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                        float volumnRatio = volumnCurrent / audioMaxVolumn;

                        soundPool.play(1,volumnRatio, volumnRatio, 0, 2, 1);

                         **/


                        try {
                            setAudio();

                            if (mMediaPlayer == null) {
                                mMediaPlayer = new MediaPlayer();
                            }
                            mMediaPlayer.reset();
                            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                            mMediaPlayer = MediaPlayer.create(mcontext, R.raw.uncsound);//xuxiaojing qualsound->uncsound
                            // Uri uri =
                            // RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                            // mMediaPlayer = MediaPlayer.create(mContext, uri);
                            mMediaPlayer.setLooping(true);
                            mMediaPlayer.start();

                        }catch (Exception e){
                            e.printStackTrace();
                        }



                        break;

                }
            }
        });

    }

    public void setAudio() {

        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        //AudioSystem.setForceUse(AudioSystem.FOR_MEDIA,
               // AudioSystem.FORCE_SPEAKER);
        // AudioSystem.setDeviceConnectionState(AudioSystem.DEVICE_OUT_WIRED_HEADSET,
        // AudioSystem.DEVICE_STATE_AVAILABLE,
        // "");
        // AudioSystem.setForceUse(AudioSystem.FOR_MEDIA,
        // AudioSystem.FORCE_WIRED_ACCESSORY);
        float ratio = 1.0f;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int) (ratio * mAudioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
        mAudioManager
                .setStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        (int) (ratio * mAudioManager
                                .getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)),
                        0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING,
                (int) (ratio * mAudioManager
                        .getStreamMaxVolume(AudioManager.STREAM_RING)), 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,
                (int) (ratio * mAudioManager
                        .getStreamMaxVolume(AudioManager.STREAM_SYSTEM)), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMediaPlayer.stop();
    }
}
