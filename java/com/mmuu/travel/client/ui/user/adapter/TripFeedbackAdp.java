package com.mmuu.travel.client.ui.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.MyRunsBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.user.TripFeedbackDetailAct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HuangYuan on 2016/12/19.
 */

public class TripFeedbackAdp extends BaseAdapter {
    private Context context;
    private List<MyRunsBean.DataBean> datas;
    private LayoutInflater inflater;

    public TripFeedbackAdp(Context context, List<MyRunsBean.DataBean> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public void replaceData(List<MyRunsBean.DataBean> dataBeanList) {
        if (this.datas == null) {
            this.datas = new ArrayList<MyRunsBean.DataBean>();
        }
        this.datas.clear();
        if (dataBeanList != null) {
            this.datas.addAll(dataBeanList);
        }
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyRunsBean.DataBean dataBean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_trip_feedback, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(dataBean);
        holder.date.setText(TimeDateUtil.longToDate(dataBean.getPlaceOrderTime(), "yyyy-MM-dd  HH:mm") + "");
        holder.money.setText("花费：" + MFUtil.formatDoubleValue(Double.parseDouble(dataBean.getActualAmount()), "0.0") + "元");
        if (!TextUtils.isEmpty(dataBean.getSumMileage())) {
            holder.path.setText("里程：" + MFUtil.formatDoubleValue((Double.parseDouble(dataBean.getSumMileage())) / 1000.0, "0.0") + "公里");
        } else {
            holder.path.setText("里程：" + "0.0公里");
        }
        if (!TextUtils.isEmpty(dataBean.getSumTime())) {
            holder.time.setText("用时：" + TimeDateUtil.HMTransform
                    (Integer.parseInt(MFUtil.formatDoubleValue
                            (Double.parseDouble(dataBean.getSumTime()), "0"))));
        } else {
            holder.time.setText("用时：" + 0 + "分");
        }
        return convertView;
    }

    class ViewHolder implements View.OnClickListener {
        @BindView(R.id.trip_feedback_date)
        TextView date;
        @BindView(R.id.trip_feedback_money)
        TextView money;
        @BindView(R.id.trip_feedback_path)
        TextView path;
        @BindView(R.id.trip_feedback_time)
        TextView time;
        MyRunsBean.DataBean testMyTrip;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        private void setData(MyRunsBean.DataBean dataBean) {
            testMyTrip = dataBean;
        }

        @Override
        public void onClick(View v) {
            if (testMyTrip == null) {
                return;
            }
            Intent intent = new Intent(context, TripFeedbackDetailAct.class);
            Bundle bundle = new Bundle();
            bundle.putString("pagetype", MFConstansValue.FEELBACK_TRIPBACK);
            bundle.putString("bikeCode", testMyTrip.getBikeCode() + "");
            bundle.putString("orderId", testMyTrip.getId() + "");
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }


}
