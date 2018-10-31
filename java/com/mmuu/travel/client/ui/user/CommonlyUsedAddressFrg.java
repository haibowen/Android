package com.mmuu.travel.client.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.tools.MapUtils;
import com.mmuu.travel.client.ui.main.SetCommonlyAddressAct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 常用地址frg
 * Created by HuangYuan on 2016/12/22.
 */

public class CommonlyUsedAddressFrg extends MFBaseFragment implements View.OnClickListener {


    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.address_one_address)
    TextView addressOneAddress;
    @BindView(R.id.address_two_address)
    TextView addressTwoAddress;
    @BindView(R.id.address_one_rl)
    RelativeLayout addressOneRl;
    @BindView(R.id.address_two_rl)
    RelativeLayout addressTwoRl;
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    private Activity context;
    private Tip commenlyAddressOne;
    private Tip commenlyAddressTwo;
    private final int START_ACTIVITY_CODE_10001 = 10001;
    private final int START_ACTIVITY_CODE_10002 = 10002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_commonly_used_address, null);
        }
        ButterKnife.bind(this, fragContentView);

        initView();
        initData();

        return fragContentView;
    }

    private void initData() {
        commenlyAddressOne = MapUtils.getCommonlyAddress(context, 0);
        commenlyAddressTwo = MapUtils.getCommonlyAddress(context, 1);

        if (commenlyAddressOne != null) {
            addressOneAddress.setText(commenlyAddressOne.getName());
        }
        if (commenlyAddressTwo != null) {
            addressTwoAddress.setText(commenlyAddressTwo.getName());
        }
    }

    private void initView() {
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setOnClickListener(this);

        titleTitle.setText(R.string.common_address);

        addressOneRl.setOnClickListener(this);
        addressTwoRl.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.title_left_image:
                context.finish();
                break;
            case R.id.address_one_rl:
                bundle.putInt("requestCode", START_ACTIVITY_CODE_10001);
                startActivityForResult(SetCommonlyAddressAct.class, bundle, START_ACTIVITY_CODE_10001);
//                startActivity(SetCommonlyAddressAct.class,null);
                break;
            case R.id.address_two_rl:
                bundle.putInt("requestCode", START_ACTIVITY_CODE_10002);
                startActivityForResult(SetCommonlyAddressAct.class, bundle, START_ACTIVITY_CODE_10002);
//                startActivity(SetCommonlyAddressAct.class,null);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            context.finish();
        }
    }
}
