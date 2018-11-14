package com.example.wenhaibo.vister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenhaibo.olympic.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_login;
    private TextInputLayout inputLayout;
    private TextInputLayout inputLayout1;
    private EditText input_email;
    private EditText input_password;
    private TextView link_singup;




    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_login=(Button)findViewById(R.id.btn_login);
        inputLayout=(TextInputLayout)findViewById(R.id.TY_Email);
        inputLayout1=(TextInputLayout)findViewById(R.id.TY_Password);
        input_email=(EditText)findViewById(R.id.input_email);
        input_password=(EditText)findViewById(R.id.input_password);
        link_singup=(TextView) findViewById(R.id.link_singup);
        // inputLayout.setError("chu");
        // input_email.setError("chu");



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(input_email.getText().length()>20){
                    input_email.setError("请输入正确的格式");

                }else if(input_password.getText().length()<6&&input_password.getText().length()>0){
                    input_password.setError("密码因不低于6位");
                }else {
                    User user1=new User();
                    String a=input_email.getText().toString();
                    String b=input_password.getText().toString();
                    user1.setUsername(a);
                    user1.setPassword(b);
                    user1.login(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e==null){
                                Toast.makeText(RegisterActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity .class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Log.e("haibo", "执行了");

                }

            }
        });
        /*link_singup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(RegisterActivity.this,SingupActivity.class);
                startActivity(intent);
                return false;
            }
        });
        */

        link_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,SingupActivity.class);
                startActivity(intent);


            }
        });

    }

    //@Override
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);// 退出程序
        }
        return super.onKeyDown(keyCode, event);

    }
    */

}
