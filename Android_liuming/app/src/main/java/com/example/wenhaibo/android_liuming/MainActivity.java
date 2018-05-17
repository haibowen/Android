package com.example.wenhaibo.android_liuming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private EditText editText;
    private EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //绑定id
        button=findViewById( R.id.bt_login );

        //绑定id
        editText=findViewById( R.id.et_zhanghao );
        editText1=findViewById( R.id.et_password );

        //点击事件
        button.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_login:

                //获取输入的值
                String zhanghao=editText.getText().toString();
                String password=editText1.getText().toString();

                Intent intent =new Intent( MainActivity.this,Main2Activity.class );


                intent.putExtra( "zhanghao",zhanghao );
                intent.putExtra( "password",password );
                startActivity( intent );
                break;
                default:
                    break;
        }

    }
}
