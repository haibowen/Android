package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseActivity;

/**首页左上角 地点搜索
 * Created by HuangYuan on 2016/12/22.
 */

public class POISearchAct extends MFBaseActivity{

    private POISearchFrg poiSearchFrg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mf_base_act);
        poiSearchFrg = new POISearchFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, poiSearchFrg);
        transaction.commitAllowingStateLoss();
    }

}
