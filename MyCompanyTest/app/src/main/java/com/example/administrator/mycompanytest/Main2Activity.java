package com.example.administrator.mycompanytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main2Activity extends AppCompatActivity {
    private ListView listView;
    private String [] data={"厂家:高通","型号:MSM8909","内核数:"+CpuUtils.getNumCpuCores(),"主频:"+(float)CpuUtils.getCpuMaxFreq()/1000000+"GHZ"};

    //private String [] data={"厂家:高通","型号:MSM8953","内核数:"+CpuUtils.getNumCpuCores(),"主频:"+(float)CpuUtils.getCpuMaxFreq()/1000000+"GHZ"};

    /**
     *
     * "CPU当前频率:"+CpuUtils. getCpuCurFreq() ,"CPU架构:"+ProcCpuInfo.getArchitecture(),"CPU调频策略:"+CpuUtils.getCpuGovernor(),"CPU最大频率:"+CpuUtils.getCpuMaxFreq(),"CPU最小频率"+CpuUtils.getCpuMinFreq(),"CPU频率档位:"+CpuUtils.getCpuAvailableFrequencies(),"CPU支持的调频策略:"+CpuUtils.getCpuAvailableGovernorsSimple()
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       listView=findViewById(R.id.list_cpu);

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

        Log.e("wenhaibo", "onCreate: "+LcdInfo.getDisplayMetrics(this) );


    }
}
