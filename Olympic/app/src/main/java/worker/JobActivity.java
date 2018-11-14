package worker;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.wenhaibo.olympic.R;

public class JobActivity extends AppCompatActivity implements View.OnClickListener {
    private Button BT_notice1,BT_notice2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        findViewById(R.id.BT_notice1).setOnClickListener(this);
        findViewById(R.id.BT_notice2).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BT_notice1:{
                Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);
                startActivity(alarms);

                break;

            }
            case R.id.BT_notice2:{
                Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);
                startActivity(alarms);
            }
                Intent intent = new Intent();
                ComponentName cn = null;
                if(Integer.parseInt (Build.VERSION.SDK ) >=8){
                    cn = new ComponentName("com.android.calendar","com.android.calendar.LaunchActivity");
                }
                else{
                    cn = new ComponentName("com.google.android.calendar","com.android.calendar.LaunchActivity");
                }
                intent.setComponent(cn);
                startActivity(intent);
                break;
            }

        }

    }

