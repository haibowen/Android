package activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mycompanytest02.R;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private EditText editTextInputName,editTextPassword;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextInputName=findViewById(R.id.input_name);
        editTextPassword=findViewById(R.id.input_password_singup);



        button=findViewById(R.id.bt_next);
        checkBox=findViewById(R.id.checkbox_auto);

        if (checkBox.isChecked()) {
            String account = editTextInputName.getText().toString();
            String password = editTextPassword.getText().toString();
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);

            if (account.equals(sp.getString("name", "")) && password.equals(sp.getString("password", ""))) {
                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextInputName.getText().toString().equals("")||editTextPassword.getText().toString().equals("")){

                    Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();

                }else {

                    SharedPreferences.Editor editor=  getSharedPreferences("data",MODE_PRIVATE).edit();

                    editor.putString("name",editTextInputName.getText().toString());
                    editor.putString("password",editTextPassword.getText().toString());
                    editor.commit();
                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                    if (checkBox.isChecked()){
                        String account=editTextInputName.getText().toString();
                        String password=editTextPassword.getText().toString();
                        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);

                        if (account.equals(sp.getString("name",""))&&password.equals(sp.getString("password",""))){
                            Intent intent=new Intent(LoginActivity.this,Main2Activity.class);
                            startActivity(intent);
                            //NoteActivity noteActivity=new NoteActivity();
                            //Log.e("7777", "onClick: "+noteActivity.getData() );
                        }




                    }
                }






            }
        });









    }
}
