package com.example.wenhaibo.recevier;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by wj on 2017/11/19.
 */
public class SecondActivity extends Activity {
    private RatingBar rtbMyScore;
    private SeekBar skbChinaScore;
    private TextView tvChinaScore;

    private ImageButton ibTimeGoing;
    private ProgressBar pbTimeWaiting;

    private DatePicker dpPicker;
    private TimePicker tpPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定UI布局文件
        setContentView(R.layout.activity_second);

        rtbMyScore = (RatingBar) findViewById(R.id.rtbMyScore);
        skbChinaScore = (SeekBar) findViewById(R.id.skbChinaScore);
        tvChinaScore = (TextView) findViewById(R.id.tvChinaScore);

        ibTimeGoing = (ImageButton) findViewById(R.id.ibTimeGoing);
        pbTimeWaiting = (ProgressBar) findViewById(R.id.pbTimeWaiting);
        dpPicker = (DatePicker) findViewById(R.id.dpPicker);
        tpPicker = (TimePicker) findViewById(R.id.tpPicker);

        rtbMyScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(SecondActivity.this,"你给你自己的评分是："+v,Toast.LENGTH_SHORT).show();

            }
        });
        skbChinaScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvChinaScore.setText("中华民族复兴进度是：" +i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ibTimeGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbTimeWaiting.setVisibility(View.VISIBLE);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                thread.start();
            }
        });

        //DatePicker初始化和绑定监听事件
        dpPicker.init(2017, 11, 20, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                Toast.makeText(SecondActivity.this,"你选取的日期为"+ year +"-" + month + "-"+ day,Toast.LENGTH_SHORT).show();
            }
        });
        tpPicker.setIs24HourView(true);
        tpPicker.setCurrentHour(20);
        tpPicker.setCurrentMinute(20);
        tpPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Toast.makeText(SecondActivity.this,"你准备的事件是:" + i + "-" + "i1",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
