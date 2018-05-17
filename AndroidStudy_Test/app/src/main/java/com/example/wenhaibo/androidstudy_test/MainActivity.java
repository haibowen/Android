package com.example.wenhaibo.androidstudy_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private EditText editText;
    private EditText editText1;

    private CheckBox checkBox;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        preferences= PreferenceManager.getDefaultSharedPreferences( this );

        //绑定id
        editText = findViewById( R.id.et_zhanghao );
        editText1 = findViewById( R.id.et_password );
        button = findViewById( R.id.bt_login );
        checkBox=findViewById( R.id.re_pass );

        boolean isremeber=preferences.getBoolean( "rember_password",false );
        if (isremeber){

             String zhanghao =preferences.getString( "zhanghao" ,"");
             String password=preferences.getString( "password","" );
             editText.setText( zhanghao );
             editText1.setText( password );
             checkBox.setChecked( true );
        }

        button.setOnClickListener( this );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:

                //获取输入值
                String zhanghao = editText.getText().toString();
                String password = editText1.getText().toString();

                if ("123456".equals( zhanghao ) && "123456".equals( password )) {

                    editor =preferences.edit();
                    if(checkBox.isChecked()){

                        editor.putBoolean( "remember_password",true );
                        editor.putString( "account",zhanghao);
                        editor.putString( "password",password );

                    }else {
                        editor.clear();

                    }
                    editor.apply();

                    Intent intent = new Intent( MainActivity.this, Main2Activity.class );

                    //添加数据
                    intent.putExtra( "zhanghao", zhanghao );
                    intent.putExtra( "password", password );
                    startActivity( intent );

                }
                break;
            default:
                break;

        }

    }
}
