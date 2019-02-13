package com.example.administrator.mycompanytest02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonAssessment, buttonCourse, buttonStudent, buttonSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        buttonAssessment = findViewById(R.id.assessment);
        buttonCourse = findViewById(R.id.course);
        buttonStudent = findViewById(R.id.student);
        buttonSettings = findViewById(R.id.settings);


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

                break;

            case R.id.student:

                Intent intent=new Intent(Main2Activity.this,NoteActivity.class);
                startActivity(intent);


                break;

            case R.id.settings:

                break;

            default:

                break;

        }

    }
}
