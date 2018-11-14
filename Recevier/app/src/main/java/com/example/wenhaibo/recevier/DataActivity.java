package com.example.wenhaibo.recevier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DataActivity extends AppCompatActivity {

    private Button btnReturnActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        btnReturnActivity = (Button) findViewById(R.id.btnReturnActivity);
        btnReturnActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strGetMsg = getIntent().getStringExtra("joke");
                if (strGetMsg.equals("joke")) {
                    Intent intentData = new Intent();
                    intentData.putExtra("ma","马航失联");
                    setResult(RESULT_OK,intentData);
                    finish();
                }else {
                    Intent intentData = new Intent();
                    setResult(RESULT_CANCELED,intentData);
                    finish();
                }
            }
        });

    }
}
