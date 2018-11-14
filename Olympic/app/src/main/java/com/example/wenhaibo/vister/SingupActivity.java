package com.example.wenhaibo.vister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenhaibo.olympic.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SingupActivity extends AppCompatActivity {
    private TextView link_login;
    EditText editText,editText2;
    Button button;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        Bmob.initialize(this,"7ebef8a8a90886d141fede64ebb493ca");
        editText=(EditText) findViewById(R.id.input_name);
         //editText1=(EditText)findViewById(R.id.input_phone);
        editText2=(EditText)findViewById(R.id.input_password_singup);
        button=(Button)findViewById(R.id.btn_signup_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=new User();
                String a=editText.getText().toString();
                //String b=editText1.getText().toString();
                String b=editText2.getText().toString();
                user.setUsername(a);
                user.setPassword(b);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e==null){
                            Toast.makeText(SingupActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(SingupActivity.this,RegisterActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SingupActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });



        link_login=(TextView) findViewById(R.id.link_login);

        /*link_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent =new Intent(SingupActivity.this,RegisterActivity.class);
                startActivity(intent);
                return false;
            }
        });
        */
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SingupActivity.this,RegisterActivity.class);
                startActivity(intent);
              finish();

            }
        });
    }
}
