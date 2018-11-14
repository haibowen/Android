package map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.example.wenhaibo.olympic.R;


public class IndexActivity extends CheckPermissionsActivity implements INaviInfoCallback {

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {//有终点
                //LatLng epoint = new LatLng(39.935039, 116.492446);
               // Poi epoi = new Poi("乐视大厦", epoint, "");
               // AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(epoi), IndexActivity.this);
            } else if (position == 1) {//HUD导航高德地图(无终点)
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null), IndexActivity.this);
            }

        }
    };
    private String[] examples = new String[]

            {"总有几分钟，其中的每一秒，你都愿意拿一年去换取。总有几颗泪，其中的每一次抽泣，你都愿意拿满手的承诺去代替。总有几段场景，其中的每幅画面，你都愿意拿全部的力量去铭记。总有几段话，其中的每个字眼，你都愿意拿所有的夜晚去复习。亲爱的，如果一切可以重来，我想和你，永远在一起。不是迷路的人太多，而是这世上路太多！你有你的路，我有我的路，至于适当的路，正确的路请交给导航", "                  点我开始您的导航！"
            };
    AmapTTSController amapTTSController;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
        // SpeechUtils.getInstance(this).speakText();//系统自带的语音播报
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();

    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples));
        setTitle("导航SDK " + AMapNavi.getVersion());
        listView.setOnItemClickListener(mItemClickListener);
    }

    /**
     * 返回键处理事件
     */
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);// 退出程序
        }
        return super.onKeyDown(keyCode, event);

    }
*/

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {
        amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onStopSpeaking() {
        amapTTSController.stopSpeaking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        amapTTSController.destroy();
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }


}
