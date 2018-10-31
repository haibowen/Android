package com.mmuu.travel.client.ui.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmuu.travel.client.bean.MyRunsItemBean;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.MyRunsBean;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.main.RunDetailAct;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的行程adp
 */

public class MyrunsAdp extends BaseAdapter {
    private ArrayList<MyRunsItemBean> myRunsItemBeans;
    private Context context;
    private LayoutInflater inflater;

    public MyrunsAdp(Context context, ArrayList<MyRunsItemBean> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.myRunsItemBeans = data;
    }

    public void setData(ArrayList<MyRunsItemBean> data) {
        if (this.myRunsItemBeans == null) {
            this.myRunsItemBeans = new ArrayList<MyRunsItemBean>();
        }
        this.myRunsItemBeans.clear();
        if (this.myRunsItemBeans != null) {
            this.myRunsItemBeans.addAll(data);
        }
    }


    @Override
    public int getCount() {
        return myRunsItemBeans == null ? 0 : myRunsItemBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return myRunsItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        MyRunsItemBean dataBean = myRunsItemBeans.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_trips, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvDate.setText(dataBean.getDateStr());
        viewHolder.llViewgroup.removeAllViews();
        for (MyRunsBean.DataBean bean : dataBean.getData()) {
            View view = inflater.inflate(R.layout.item_my_trips_item, null);
            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
            TextView tv_meleage = (TextView) view.findViewById(R.id.tv_meleage);
            TextView tv_usecounttime = (TextView) view.findViewById(R.id.tv_usecounttime);
            TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
            tv_time.setText(TimeDateUtil.longToDate(bean.getPlaceOrderTime(), "HH:mm"));
            if (!TextUtils.isEmpty(bean.getSumMileage())) {
                tv_meleage.setText("" + MFUtil.formatDoubleValue((Double.parseDouble(
                        bean.getSumMileage())) / 1000.0, "0.0") + "公里");
            } else {
                tv_meleage.setText("" + "0.0公里");
            }
            if (!TextUtils.isEmpty(bean.getSumTime())) {
                tv_usecounttime.setText("" + TimeDateUtil.HMTransform
                        (Integer.parseInt(MFUtil.formatDoubleValue
                                (Double.parseDouble(bean.getSumTime()), "0"))));
            } else {
                tv_usecounttime.setText("" + 0 + "分");
            }
            tv_money.setText(MFUtil.formatDoubleValue((Double.parseDouble(bean.getActualAmount())), "0.0") + "元");
            view.setTag(bean);
            view.setOnClickListener(onClickListener);
            viewHolder.llViewgroup.addView(view);
        }
        return convertView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyRunsBean.DataBean dataBean = (MyRunsBean.DataBean) v.getTag();
            if (dataBean == null) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("orderId", dataBean.getId() + "");
            context.startActivity(new Intent(context, RunDetailAct.class).putExtras(bundle));
        }
    };

    static class ViewHolder {
        private MyRunsBean.DataBean dataBean;

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.ll_viewgroup)
        LinearLayout llViewgroup;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }

    public void replaceData(ArrayList<MyRunsItemBean> mPostionlistItems) {
        if (myRunsItemBeans == null) {
            myRunsItemBeans = new ArrayList<MyRunsItemBean>();
        }
        myRunsItemBeans.clear();
        if (mPostionlistItems != null) {
            myRunsItemBeans.addAll(mPostionlistItems);
        }
    }

}
