package com.mmuu.travel.client.ui.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.mfinterface.A2B;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MapUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 输入提示adapter，展示item名称和地址
 */
public class InputTipsAdapter extends BaseAdapter {
    private Context context;
    private List<Tip> datas;
    private int type;//标志是历史记录还是当前搜索  0:历史记录   1:当前搜索
    private int startActRequestCode = -1;

    private A2B a2B;

    public InputTipsAdapter(Context context, List<Tip> tipList, int type) {
        this.context = context;
        this.datas = tipList;
        this.type = type;
    }

    public void setStartActRequestCode(int startActRequestCode) {
        this.startActRequestCode = startActRequestCode;
    }


    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }


    @Override
    public Object getItem(int i) {
        if (datas != null) {
            return datas.get(i);
        }
        return null;
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        Tip tip = datas.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_inputtips, null);
            holder = new ViewHolder(view);
            view.setTag(R.id.name, holder);
        } else {
            holder = (ViewHolder) view.getTag(R.id.name);
        }
        if (datas == null) {
            return view;
        }

        holder.mName.setText(tip.getName());
        String address = tip.getAddress();
        if (address == null || address.equals("")) {
            holder.mAddress.setVisibility(View.GONE);
        } else {
            holder.mAddress.setVisibility(View.VISIBLE);
            holder.mAddress.setText(address);
        }

        if (type == 0) {
            holder.poiIcon.setImageResource(R.drawable.poi_history);
        } else {
            holder.poiIcon.setImageResource(R.drawable.poi_address);
        }
        view.setTag(R.id.address, tip);
        return view;
    }

    class ViewHolder implements View.OnClickListener {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.address)
        TextView mAddress;
        @BindView(R.id.poi_icon)
        ImageView poiIcon;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setBackgroundResource(R.drawable.mywallet_bar_selector);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Tip tip = (Tip) v.getTag(R.id.address);

            if (tip == null || tip.getPoint() == null) {
                return;
            }

            MapUtils.savePoiHistory(context, tip);
            //MFUtil.showToast(context, tip.toString() + "\n" + "保存成功");
            Intent intent = new Intent();
            intent.putExtra("ADR", tip);
            if (startActRequestCode == 10001) {
                MapUtils.saveCommonlyAddress(context, tip, 0);
                setUserCommonAdr(tip, 1);
            } else if (startActRequestCode == 10002) {
                MapUtils.saveCommonlyAddress(context, tip, 1);
                setUserCommonAdr(tip, 2);
            }

            if (a2B != null) {
                a2B.callB(1, tip);
            } else {
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                ((Activity) context).finish();
            }

        }
    }


    private void setUserCommonAdr(Tip tip, int type) {

        RequestParams adrParams = new RequestParams();
        adrParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        adrParams.addBodyParameter("address", tip.getName());
        adrParams.addBodyParameter("longitude", tip.getPoint().getLongitude() + "");
        adrParams.addBodyParameter("latitude", tip.getPoint().getLatitude() + "");
        adrParams.addBodyParameter("state", type + "");
        MFRunner.requestPost(1111, MFUrl.setCommonAddress, adrParams, new PublicRequestInterface() {
            @Override
            public void onStart(int i, RequestParams requestParams) {

            }

            @Override
            public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

            }

            @Override
            public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
                int a = 0;
            }

            @Override
            public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
                int a = 0;
            }

            @Override
            public void onCancel(int i, RequestParams requestParams) {

            }
        });

    }


    public A2B getA2B() {
        return a2B;
    }

    public void setA2B(A2B a2B) {
        this.a2B = a2B;
    }
}
