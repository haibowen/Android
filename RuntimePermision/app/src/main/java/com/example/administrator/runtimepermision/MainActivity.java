package com.example.administrator.runtimepermision;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button,button1;
    private TextView textView;
    private  static  final  int UPDATA=1;

    private Handler handler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){

                case UPDATA:
                    textView.setText("I am android");
                    break;
                default:
                    break;

            }

        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.bt_call);
        button1=findViewById(R.id.bt_send);

        textView=findViewById(R.id.text_show);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       Message message=new Message();
                       message.what=UPDATA;
                       handler.sendMessage(message);
                   }
               }).start();



            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                } else {
                    call();

                }


            }

        });


    }

    @SuppressLint("MissingPermission")
    public void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {


            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();

                } else {

                    Toast.makeText(MainActivity.this, "you don't have a permission", Toast.LENGTH_SHORT).show();

                }
                break;

            default:
                break;


        }

    }
}
