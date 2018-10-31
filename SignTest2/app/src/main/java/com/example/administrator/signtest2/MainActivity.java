package com.example.administrator.signtest2;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.util.ArrayList;
import Bean.StepBean;

public class MainActivity extends AppCompatActivity {
    private StepsView mStepView;
    private TextView mTvSign;
    private ArrayList<StepBean> mStepBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mTvSign.setOnClickListener(v -> mStepView.startSignAnimation(2));
    }

    private void initView() {
        mStepView = findViewById(R.id.step_view);
        mTvSign = findViewById(R.id.tv_sign_click);
    }

    private void initData() {
        mStepBeans.add(new StepBean(StepBean.STEP_COMPLETED, 2));
        mStepBeans.add(new StepBean(StepBean.STEP_COMPLETED, 4));
        mStepBeans.add(new StepBean(StepBean.STEP_CURRENT, 10));
        mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 2));
        mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 4));
        mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 4));
        mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 30));
        mStepView.setStepNum(mStepBeans);
    }
}
