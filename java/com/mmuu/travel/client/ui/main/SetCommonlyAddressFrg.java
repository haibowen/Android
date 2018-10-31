package com.mmuu.travel.client.ui.main;

import android.app.Activity;
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
import android.widget.Toast;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.tools.MapUtils;
import com.mmuu.travel.client.ui.user.adapter.InputTipsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HuangYuan on 2016/12/22.
 */

public class SetCommonlyAddressFrg extends MFBaseFragment implements View.OnClickListener, TextWatcher, Inputtips.InputtipsListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.poi_search_key)
    EditText poiSearchKey;
    @BindView(R.id.delete_edit_content)
    ImageView deleteEditContent;
    @BindView(R.id.poi_search_result)
    ListView poiSearchResult;
    private Activity context;
    private List<Tip> mCurrentTipList;
    private InputTipsAdapter mIntipAdapter;
    private int requestCode = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (context.getIntent() != null && context.getIntent().getExtras() != null) {
            requestCode = context.getIntent().getExtras().getInt("requestCode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_set_commonly_address, null);
        }
        ButterKnife.bind(this, fragContentView);
        InitView();
        initData();
        return fragContentView;


    }

    private void initData() {
        mCurrentTipList = MapUtils.getPoiHistory(context);
        mIntipAdapter = new InputTipsAdapter(
                context,
                mCurrentTipList, 0);
        mIntipAdapter.setStartActRequestCode(requestCode);
        poiSearchResult.setAdapter(mIntipAdapter);
    }

    private void InitView() {
        deleteEditContent.setOnClickListener(this);
        poiSearchKey.addTextChangedListener(this);
        back.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_edit_content:
                poiSearchKey.setText("");
                break;
            case R.id.back:
                context.finish();
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

            //TODO 修改定位地点
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
            poiSearchResult.setAdapter(mIntipAdapter);
            mIntipAdapter.setType(1);
            mIntipAdapter.setStartActRequestCode(requestCode);
        } else {
            Toast.makeText(context, "错误码：" + code, Toast.LENGTH_SHORT).show();
        }
    }
}
