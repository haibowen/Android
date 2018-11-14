package com.example.wenhaibo.wangkai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText1,editText2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1=(EditText)findViewById(R.id.et1);
        editText2=(EditText)findViewById(R.id.et2);
        button=(Button)findViewById(R.id.btlogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText1.getText().toString();
                String p=editText2.getText().toString();
                if(s.equals("admin")&&p.equals("123456789")){
                    Intent intent =new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,"你输入的密码和账号不匹配",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
