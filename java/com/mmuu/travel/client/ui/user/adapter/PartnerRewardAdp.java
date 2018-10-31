package com.mmuu.travel.client.ui.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.user.PartnerBoonBean;
import com.mmuu.travel.client.bean.user.PartnerRewardBean;
import com.mmuu.travel.client.tools.TimeDateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 福利券记录adp
 * Created by HuangYuan on 2016/12/21.
 */

public class PartnerRewardAdp extends BaseAdapter {

    private Context context;
    private List<PartnerRewardBean.DataBean> datas;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");

    public PartnerRewardAdp(Context context, List<PartnerRewardBean.DataBean> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public void replaceData(List<PartnerRewardBean.DataBean> mPostionlistItems) {
        if (datas == null) {
            datas = new ArrayList<PartnerRewardBean.DataBean>();
        }
        datas.clear();
        if (mPostionlistItems != null) {
            datas.addAll(mPostionlistItems);
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
        PartnerRewardBean.DataBean bean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_partner_hongli, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_partner_hongli_time.setText(TimeDateUtil.longToDate(bean.getTradeTime(), "yyyy-MM-dd  HH:mm:ss"));
        holder.item_partner_hongli_name.setText(bean.getTypeName());
        //10 +  -------  12-
        switch (bean.getType()) {
            case 10:
                holder.item_partner_hongli_detail.setText("+" + bean.getMoney());
                break;
            case 12:
                holder.item_partner_hongli_detail.setText("-" + bean.getMoney());
                break;
        }
        return convertView;
    }

    class ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_partner_hongli_name)
        TextView item_partner_hongli_name;
        @BindView(R.id.item_partner_hongli_time)
        TextView item_partner_hongli_time;
        @BindView(R.id.item_partner_hongli_detail)
        TextView item_partner_hongli_detail;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
