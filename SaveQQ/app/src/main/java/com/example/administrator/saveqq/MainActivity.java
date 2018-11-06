package com.example.administrator.saveqq;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText,editText1;
    private Button button;
    private CheckBox checkBox;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.et_1);
        editText1=findViewById(R.id.password);
        button=findViewById(R.id.bt_login);
        checkBox=findViewById(R.id.checkb);


        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=preferences.getBoolean("remember_password",false);
        if (isRemember){
            String acount=preferences.getString("acount","");
            String password=preferences.getString("password","");
            editText.setText(acount);
            editText1.setText(password);
            checkBox.setChecked(true);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acount=editText.getText().toString();
                String password=editText1.getText().toString();
                if (acount.equals("123456")&&password.equals("123456")){
                    editor=preferences.edit();
                    if (checkBox.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("acount",acount);
                        editor.putString("password",password);

                    }else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                    //finish();
                }else {
                    Toast.makeText(MainActivity.this,"账号密码不匹配",Toast.LENGTH_SHORT);

                }
            }
        });

    }
}
