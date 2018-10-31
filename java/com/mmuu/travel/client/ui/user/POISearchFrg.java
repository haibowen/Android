package com.mmuu.travel.client.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.MapUtils;
import com.mmuu.travel.client.ui.main.SetCommonlyAddressAct;
import com.mmuu.travel.client.ui.user.adapter.InputTipsAdapter;
import com.mmuu.travel.client.widget.dialog.NavigateDialog;
import com.umeng.analytics.MobclickAgent;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HuangYuan on 2016/12/22.
 */

public class POISearchFrg extends MFBaseFragment implements View.OnClickListener, TextWatcher, Inputtips.InputtipsListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.address_one_address)
    TextView addressOneAddress;
    @BindView(R.id.address_two_address)
    TextView addressTwoAddress;
    @BindView(R.id.poi_search_history)
    ListView poiSearchHistory;
    @BindView(R.id.poi_search_key)
    EditText poiSearchKey;
    @BindView(R.id.delete_edit_content)
    ImageView deleteEditContent;
    @BindView(R.id.set_address)
    TextView setAddress;
    @BindView(R.id.address_one_rl)
    RelativeLayout addressOneRl;
    @BindView(R.id.address_two_rl)
    RelativeLayout addressTwoRl;
    private Activity context;
    private List<Tip> mCurrentTipList;
    private InputTipsAdapter mIntipAdapter;

    private Tip commenlyAddressOne;
    private Tip commenlyAddressTwo;


    /**
     * 是否是去导航页
     */
    private boolean toNavi = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_poisearch, null);
        }
        ButterKnife.bind(this, fragContentView);
        initView();
        initData();
        return fragContentView;

    }

    private void initData() {
        mCurrentTipList = MapUtils.getPoiHistory(context);
        mIntipAdapter = new InputTipsAdapter(
                context,
                mCurrentTipList, 0);
        poiSearchHistory.setAdapter(mIntipAdapter);
        commenlyAddressOne = MapUtils.getCommonlyAddress(context, 0);
        commenlyAddressTwo = MapUtils.getCommonlyAddress(context, 1);

        if (commenlyAddressOne != null) {
            addressOneAddress.setText(commenlyAddressOne.getName());
        }
        if (commenlyAddressTwo != null) {
            addressTwoAddress.setText(commenlyAddressTwo.getName());
        }
        if (getActivity().getIntent().getExtras() != null) {
            toNavi = true;
            mIntipAdapter.setA2B(new A2B() {
                @Override
                public int callB(int type, Object o) {
                    Tip mtip = (Tip) o;
                    if (mtip != null) {
                        toNavigate(mtip);
                    }
                    return 0;
                }
            });
        }
    }

    private void initView() {

        back.setOnClickListener(this);
        setAddress.setOnClickListener(this);
        deleteEditContent.setOnClickListener(this);
        poiSearchKey.addTextChangedListener(this);

        addressOneRl.setOnClickListener(this);
        addressTwoRl.setOnClickListener(this);

        deleteEditContent.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.back:
                context.finish();
                break;
            case R.id.set_address:
//                MFUtil.showToast(context,"设置地址");
                startActivityForResult(CommonlyUsedAddressAct.class, null, 10000);
                //startActivity(CommonlyUsedAddressAct.class, null);
                break;
            case R.id.delete_edit_content:
                poiSearchKey.setText("");
                break;
            case R.id.address_one_rl:
                if (commenlyAddressOne == null) {
                    mIntipAdapter.setStartActRequestCode(10001);
                    bundle.putInt("requestCode", 10001);
                    startActivityForResult(SetCommonlyAddressAct.class, bundle, 10001);

                } else {

                    if (toNavi) {
                        toNavigate(commenlyAddressOne);
                    } else {
                        // MFUtil.showToast(context, commenlyAddressOne.toString());
                        Intent adr0 = new Intent();
                        adr0.putExtra("ADR", commenlyAddressOne);
                        getActivity().setResult(Activity.RESULT_OK, adr0);
                        getActivity().finish();
                    }


                }
                break;
            case R.id.address_two_rl:
                if (commenlyAddressTwo == null) {
                    mIntipAdapter.setStartActRequestCode(10002);
                    bundle.putInt("requestCode", 10002);
                    startActivityForResult(SetCommonlyAddressAct.class, bundle, 10002);
                } else {
                    if (toNavi) {
                        toNavigate(commenlyAddressTwo);
                    } else {
                        //MFUtil.showToast(context, commenlyAddressTwo.toString());
                        Intent adr1 = new Intent();
                        adr1.putExtra("ADR", commenlyAddressTwo);
//                    Bundle navBundle = new Bundle();
//                    navBundle.putParcelable("ADR", commenlyAddressTwo);
//                    startActivity(NavigateAct.class, navBundle);
                        getActivity().setResult(Activity.RESULT_OK, adr1);
                        getActivity().finish();
                    }
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null && s.length() > 0) {
            deleteEditContent.setVisibility(View.VISIBLE);
            InputtipsQuery inputquery;
            //TODO 修改定位地点
            if (MFApp.getInstance().getmLocation() != null && !TextUtils.isEmpty(MFApp.getInstance().getmLocation().getCity())) {
                inputquery = new InputtipsQuery(s.toString(), MFApp.getInstance().getmLocation().getCity());
            } else {
                inputquery = new InputtipsQuery(s.toString(), "北京");
            }

            Inputtips inputTips = new Inputtips(context, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            deleteEditContent.setVisibility(View.GONE);
//            if (mIntipAdapter != null && mCurrentTipList != null) {
//                mCurrentTipList.clear();
//                mIntipAdapter.notifyDataSetChanged();
//            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetInputtips(List<Tip> list, int code) {
        if (code == 1000)//正确返回
        {
            mCurrentTipList = list;
            mIntipAdapter = new InputTipsAdapter(
                    context,
                    mCurrentTipList, 1);
            if (toNavi) {
                mIntipAdapter.setA2B(new A2B() {
                    @Override
                    public int callB(int type, Object o) {
                        Tip mtip = (Tip) o;
                        if (mtip != null) {
                            toNavigate(mtip);
                        }
                        return 0;
                    }
                });
            }
            poiSearchHistory.setAdapter(mIntipAdapter);
        } else {
            Toast.makeText(context, "错误码：" + code, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commenlyAddressOne = MapUtils.getCommonlyAddress(context, 0);
        commenlyAddressTwo = MapUtils.getCommonlyAddress(context, 1);
        if (commenlyAddressOne != null) {
            addressOneAddress.setText(commenlyAddressOne.getName());
        }
        if (commenlyAddressTwo != null) {
            addressTwoAddress.setText(commenlyAddressTwo.getName());
        }
    }

    /**
     * 导航
     */

    private void toNavigate(Tip tip) {
        MobclickAgent.onEvent(mContext, "click_poi_navigations");

        NavigateDialog navigateDialog = new NavigateDialog(mContext);
        navigateDialog.setTip(tip);

        double[] latlon = MFUtil.gaoDeToBaidu(tip.getPoint().getLongitude(), tip.getPoint().getLatitude());
        StringBuffer sb = new StringBuffer();
        sb.append("intent://map/direction?").append("destination=").append("latlng:").append(latlon[1]).append(",").append(latlon[0]);
        sb.append("|name:我的目的地").append("&mode=riding").append("&region=").append(tip.getDistrict()).append("&src=蜜蜂出行#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        Intent intent = null;
        try {
            intent = Intent.getIntent(sb.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        getActivity().finish();


        return;

//        navigateDialog.setDialogItemClickListener(new DialogItemClickListener() {
//            @Override
//            public void onItemClick(View v, Dialog d, String type) {
//                d.dismiss();
//                Tip mtip = ((NavigateDialog) d).getTip();
//                if (mtip == null) {
//                    return;
//                }
//
//                if (!TextUtils.isEmpty(type) && "baidu".equals(type)) {
//                    double[] latlon = MFUtil.gaoDeToBaidu(mtip.getPoint().getLongitude(), mtip.getPoint().getLatitude());
//                    StringBuffer sb = new StringBuffer();
//                    sb.append("intent://map/direction?").append("destination=").append("latlng:").append(latlon[1]).append(",").append(latlon[0]);
//                    sb.append("|name:我的目的地").append("&mode=riding").append("&region=").append(mtip.getDistrict()).append("&src=蜜蜂出行#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
//                    Intent intent = null;
//                    try {
//                        intent = Intent.getIntent(sb.toString());
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    startActivity(intent);
//
//                } else if (!TextUtils.isEmpty(type) && "gaode".equals(type)) {
//
//                    StringBuffer sb = new StringBuffer();
//                    sb.append("androidamap://route?sourceApplication=蜜蜂出行&sname=我的位置&dlat=").append(mtip.getPoint().getLatitude()).append("&dlon=").append(mtip.getPoint().getLongitude());
//                    sb.append("&dname=").append(mtip.getDistrict()).append("&dev=0&t=3");
//                    Intent intent = null;
//                    try {
//                        intent = Intent.getIntent(sb.toString());
//                        context.startActivity(intent);
//                    } catch (URISyntaxException e) {
//
//                        e.printStackTrace();
//                    }
//
//                } else if (!TextUtils.isEmpty(type) && "google".equals(type)) {
//
//                }
//                getActivity().finish();
//            }
//        });
//        navigateDialog.show();
    }


}
