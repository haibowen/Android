package com.example.wenhaibo.recevier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button btnGetMoney;
    private TextView tvGetMoney;
    private Button btnLoseMoney;
    private EditText etGoalMoney;
    private ImageView imageView;
    private RadioGroup rgCCAVSuvery;
    private CheckBox cbLOL, cbGirlFriend, cbCodingMoney;
    private Button btnSwitch;
    private int money = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetMoney = (Button) findViewById(R.id.btnGetMoney);
        btnLoseMoney = (Button) findViewById(R.id.btnLoseMoney);
        tvGetMoney = (TextView) findViewById(R.id.tvGetMoney);
        etGoalMoney = (EditText) findViewById(R.id.etGoalMoney);

        btnSwitch = (Button) findViewById(R.id.btnSwitch);

        rgCCAVSuvery = (RadioGroup) findViewById(R.id.rgCCAVSuvery);
        cbLOL = (CheckBox) findViewById(R.id.cbLOL);
        cbGirlFriend = (CheckBox) findViewById(R.id.cbGirlFriend);
        cbCodingMoney = (CheckBox) findViewById(R.id.cbCodingMoney);

        btnGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strInputMoney = etGoalMoney.getText().toString().trim();
                int iMoney = Integer.parseInt(strInputMoney);
                if (iMoney == money) {
                    Toast.makeText(MainActivity.this, "你经过努力已经完成目标!", Toast.LENGTH_SHORT).show();
                } else {
                    money++;
                }
                tvGetMoney.setText("哈哈，我通过点击鼠标轻易赚了" + money + "元");
            }

        });


        btnLoseMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (money == 0) {
                    //提示用户
                    Toast.makeText(MainActivity.this, "现在已经是穷光蛋了，不要再按了!", Toast.LENGTH_SHORT).show();
                } else {
                    money--;
                    tvGetMoney.setText("哈哈，我通过点击鼠标轻易赚了" + money + "元");
                }

            }
        });


        rgCCAVSuvery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSoHappy:
                        Toast.makeText(MainActivity.this, "恭喜你状态很好，继续保持！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbNoHappy:
                        Toast.makeText(MainActivity.this, "真的吗？推荐你每天看CCAV的新闻X播！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbZeng:
                        Toast.makeText(MainActivity.this, "你是CCAV的忠实粉丝！", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        cbLOL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(MainActivity.this, "骚年，没事多看看书，别整天LOL！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cbGirlFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(MainActivity.this, "秀恩爱，你懂的！！！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cbCodingMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(MainActivity.this, "码农，活着就是为了改变世界！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到第二个Activity
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
    }
}


