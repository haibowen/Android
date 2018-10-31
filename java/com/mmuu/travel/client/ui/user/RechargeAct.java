//package com.mmuu.travel.client.ui.user;
//
//import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
//
//import com.mmuu.travel.client.R;
//import com.mmuu.travel.client.base.MFBaseActivity;
//
///**
// * 充值
// * Created by HuangYuan on 2016/12/13.
// */
//
//public class RechargeAct extends MFBaseActivity {
//
//    private RechargeFrg rechargeFrg;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.mf_base_act);
//
//        rechargeFrg = new RechargeFrg();
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        transaction.replace(R.id.base_contextlayout, rechargeFrg);
//        transaction.commit();
//    }
//}
