package com.example.wenhaibo.androidstudy03;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //声明控件

    private Button button;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private ImageView imageView;
    private ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //控件绑定id

        button = findViewById( R.id.bt_show );
        button1 = findViewById( R.id.bt_search );
        button2 = findViewById( R.id.bt_alert );
        button3 = findViewById( R.id.bt_Progroessbar );
        button4 = findViewById( R.id.bt_next );
        button5=findViewById( R.id.bt_next5 );
        imageView = findViewById( R.id.image_icon );
        progressBar = findViewById( R.id.prog_show );


        //按钮的点击事件

        button.setOnClickListener( this );
        button1.setOnClickListener( this );
        button2.setOnClickListener( this );
        button3.setOnClickListener( this );
        button4.setOnClickListener( this );
        button5.setOnClickListener( this );


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_show:

                //设置显示的图片

                imageView.setImageResource( R.drawable.ic_launcher_background );
                break;

            case R.id.bt_search:

                //判断progressbar是否可见

                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility( View.VISIBLE );

                } else {
                    progressBar.setVisibility( View.GONE );
                }
                break;

            case R.id.bt_alert:

                //设置对话框的显示

                AlertDialog.Builder dialog = new AlertDialog.Builder( MainActivity.this );

                //设置对话框的标题 内容 BACK按键是否可用

                dialog.setTitle( "haibowen.top" );
                dialog.setMessage( "welcome to haibowen.top" );
                dialog.setCancelable( false );

                //设置对话框上的按钮，并未其设置点击事件

                dialog.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText( MainActivity.this, "ok", Toast.LENGTH_SHORT ).show();

                    }
                } );
                dialog.setNegativeButton( "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText( MainActivity.this, "NO", Toast.LENGTH_SHORT ).show();

                    }
                } );
                dialog.show();
                break;

            case R.id.bt_Progroessbar:
                ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );
                progressDialog.setTitle( "haibo" );
                progressDialog.setMessage( "onload" );
                progressDialog.setCancelable( true );
                progressDialog.show();
                break;
            case R.id.bt_next:
                Intent intent = new Intent( MainActivity.this, Main2Activity.class );
                startActivity( intent );
                break;
            case R.id.bt_next5:
                Intent intent1=new Intent( MainActivity.this,Main4Activity.class );
                startActivity( intent1 );
                break;


            default:
                break;
        }

    }


}
