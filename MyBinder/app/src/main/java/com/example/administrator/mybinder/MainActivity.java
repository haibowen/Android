package com.example.administrator.mybinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindViews({R.id.bt_first,R.id.bt_second,R.id.bt_third,R.id.bt_fourth})
    public List<Button> buttonList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.bt_first)
    public  void  change(){
        buttonList.get(0).setText("wen");
        buttonList.get(1).setText("hai");
        buttonList.get(2).setText("bo");
        buttonList.get(3).setText("liu");

    }
    @OnClick({R.id.bt_second,R.id.bt_third,R.id.bt_fourth})
    public  void  test(View view){
        switch (view.getId()){
            case R.id.bt_second:
                buttonList.get(1).setText("jiang");
                break;

            case R.id.bt_third:
                buttonList.get(2).setText("dong");
                break;

            case R.id.bt_fourth:
                buttonList.get(3).setText("feng");

                break;
        }

    }
}
