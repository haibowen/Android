package db.itheima.com.note;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {
 private EditText etTittle,etContent;
    private Button btnSave;
    private Dao d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        d=new Dao(getBaseContext());
        btnSave= (Button)findViewById(R.id.btnSave);
        etTittle=(EditText)findViewById(R.id.etTittle);
        etContent=(EditText)findViewById(R.id.etContent);


        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String content = etContent.getText().toString();
        String tittle= etTittle.getText().toString();
        switch(v.getId()){
            case R.id.btnSave:
                d.add(tittle,content);
                finish();
                break;

        }
    }
}
