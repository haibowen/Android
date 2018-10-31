package com.mmuu.travel.client.ui.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.user.CreditDetailBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 信用明细
 */

public class CreditDetailAdp extends BaseAdapter {

    private Context context;
    private List<CreditDetailBean.DataBean> datas;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");

    public CreditDetailAdp(Context context, List<CreditDetailBean.DataBean> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public void replaceData(List<CreditDetailBean.DataBean> mPostionlistItems) {
        if (datas == null) {
            datas = new ArrayList<CreditDetailBean.DataBean>();
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
        CreditDetailBean.DataBean bean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_credit_detail, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.creditDetailCost.setText(bean.getType());
        holder.creditDetailDate.setText(sdf.format(new Date(bean.getCreateTime())));
        if (bean.getUnit() == 0) {
            holder.creditDetail.setText("+" + bean.getValue() + "分");
        } else if (bean.getUnit() == 1) {
            holder.creditDetail.setText("-" + bean.getValue() + "分");
        } else {
            holder.creditDetail.setText(bean.getValue());
        }
        return convertView;
    }

    static class ViewHolder implements View.OnClickListener {
        @BindView(R.id.line)
        View line;
        @BindView(R.id.credit_detail_cost)
        TextView creditDetailCost;
        @BindView(R.id.credit_detail_date)
        TextView creditDetailDate;
        @BindView(R.id.credit_detail)
        TextView creditDetail;
        @BindView(R.id.rl_top)
        RelativeLayout rlTop;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
