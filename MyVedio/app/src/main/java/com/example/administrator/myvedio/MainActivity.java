package com.example.administrator.myvedio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.security.spec.ECField;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button,button1,button2;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_play);
        button1=findViewById(R.id.bt_pause);
        button2=findViewById(R.id.bt_stop);

        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        //运行时权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            initVideoPath();
        }
    }

    private void initVideoPath() {
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"move.mp4");
            videoView.setVideoPath(file.getPath());
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case  1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initVideoPath();

                }else {
                    Toast.makeText(this,"拒绝权限将无法使用",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

                default:
                    break;


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_play:
                if (!videoView.isPlaying())
                 videoView.start();

                break;
            case R.id.bt_pause:

                if (videoView.isPlaying())
                    videoView.pause();
                break;

            case R.id.bt_stop:
                if (videoView.isPlaying())
                    videoView.resume();


                break;

                default:
                    break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView!=null){
            videoView.suspend();
        }
    }
}
