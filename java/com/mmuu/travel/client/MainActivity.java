//package com.mmuu.travel.client;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.TextView;
//
//import com.amap.api.maps.MapView;
//import com.mmuu.travel.client.ui.user.MyWalletAct;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * 主activity
// */
//public class MainActivity extends AppCompatActivity {
//
//    @BindView(R.id.map)
//    MapView mMapView;
//    @BindView(R.id.textviews)
//    TextView textviews;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        //mMapView= (MapView) findViewById(R.id.map);
//        textviews.setText("");
//        mMapView.onCreate(savedInstanceState);
//
//        startActivity(new Intent(this, MyWalletAct.class));
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
//        mMapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
//        mMapView.onPause();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
//        mMapView.onSaveInstanceState(outState);
//    }
//
//}
