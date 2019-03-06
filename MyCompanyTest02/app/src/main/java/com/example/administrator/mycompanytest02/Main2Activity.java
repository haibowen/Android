package com.example.administrator.mycompanytest02;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonAssessment, buttonCourse, buttonStudent, buttonSettings;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ZXingLibrary.initDisplayOpinion(this);

        buttonAssessment = findViewById(R.id.assessment);
        buttonCourse = findViewById(R.id.course);
        buttonStudent = findViewById(R.id.student);
        buttonSettings = findViewById(R.id.settings);

        toolbar=findViewById(R.id.toolbar);
        //toolbar设置
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_fullscreen_black_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }



        buttonAssessment.setOnClickListener(this);
        buttonCourse.setOnClickListener(this);
        buttonStudent.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.assessment:

                break;

            case R.id.course:
                Intent intent2=new Intent(Main2Activity.this,CourseActivity.class);
                startActivity(intent2);


                break;

            case R.id.student:

                Intent intent=new Intent(Main2Activity.this,NoteActivity.class);
                startActivity(intent);


                break;

            case R.id.settings:
                Intent intent1=new Intent(Main2Activity.this,SettingActivity.class);
                startActivity(intent1);


                break;

            default:

                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.scanning, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.scanning:
                Intent intent = new Intent(Main2Activity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);





                //Toast.makeText(Main2Activity.this,"点解了",Toast.LENGTH_SHORT).show();



                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(Main2Activity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
