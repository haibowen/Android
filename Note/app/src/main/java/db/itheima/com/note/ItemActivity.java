package db.itheima.com.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etContent_1 ,etTittle_1;
    private Button btnSave_1;
    private Bean b_1 ,b_2 ,b_3;
    private Dao d_1;
    private  int d;
    private static final String TAG = "ItemActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        d_1=new Dao(getBaseContext());
        etTittle_1=(EditText)findViewById(R.id.etTittle_1);
        etContent_1=(EditText)findViewById(R.id.etContent_1);
        btnSave_1=(Button)findViewById(R.id.btnSave_1);
        btnSave_1.setOnClickListener(this);

        Intent intent_1=getIntent();
        String str=intent_1.getStringExtra("Search");
        if (str!= null){
            b_3=d_1.find(str);
            etTittle_1.setText(b_3.getName());
           etContent_1.setText(b_3.getContent());
            d=b_3.getId();

        }
        Intent intent=getIntent();
        String temp=intent.getStringExtra("item");

        if (temp!=null){
            int result = Integer.valueOf(temp);
            b_1=d_1.find(result);
            etContent_1.setText(b_1.getContent());
            etTittle_1.setText(b_1.getName());
            d=b_1.getId();
            Log.d("ItemActivity","onCreate execute");
            Log.e(TAG, "onCreate: ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave_1:
                String content=etContent_1.getText().toString();
                String name=etTittle_1.getText().toString();
                b_2=new Bean();
                Intent intent =getIntent();
                String temp=intent.getStringExtra("item");
                if (temp!=null){
                    int result =Integer.valueOf(temp);
                    b_2.setId(result);
                    b_2.setName(name);
                    b_2.setContent(content);
                    d_1.update(b_2);
                    Intent i2=new Intent(ItemActivity.this,MainActivity.class);
                    startActivity(i2);
                    finish();
                }
                else {
                    b_2.setId(d);
                    b_2.setName(name);
                    b_2.setContent(content);
                    d_1.update(b_2);
                    Intent i3=new Intent(ItemActivity.this,MainActivity.class);
                    startActivity(i3);
                    finish();

                }
                break;
        }

    }
}
