package com.mmuu.travel.client.ui.user.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.MyRunsBean;
import com.mmuu.travel.client.bean.user.CostDetailBean;
import com.mmuu.travel.client.bean.user.CostDetailVO;
import com.mmuu.travel.client.tools.MFUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 费用明细
 * Created by HuangYuan on 2016/12/21.
 */

public class CostDetailAdp extends BaseAdapter {

    private Context context;
    private List<CostDetailBean.DataBean> datas;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");

    public CostDetailAdp(Context context, List<CostDetailBean.DataBean> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public void replaceData(List<CostDetailBean.DataBean> mPostionlistItems) {
        if (datas == null) {
            datas = new ArrayList<CostDetailBean.DataBean>();
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
        CostDetailBean.DataBean bean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cost_detail, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cost.setText(bean.getTypeName());
        holder.payMethod.setVisibility(View.GONE);
        switch (bean.getType()) {
            case 2:
                holder.money.setText("+" + MFUtil.formatDoubleValue(bean.getMoney(), "0.0") + "元");
                if (bean.getGrantAmount() > 0) {
                    holder.payMethod.setVisibility(View.VISIBLE);
                    holder.payMethod.setText("充值" + MFUtil.formatDoubleValue(
                            (bean.getMoney() - bean.getGrantAmount()), "0.#") + "元 + 赠送" + MFUtil.formatDoubleValue(bean.getGrantAmount(), "0.#") + "元");
                }
                break;
            case 0:
            case 5:
            case 7:
                holder.money.setText("+" + MFUtil.formatDoubleValue(bean.getMoney(), "0.0") + "元");
                break;
            case 1:
            case 3:
            case 4:
            case 6:
            case 8:
                holder.money.setText("-" + MFUtil.formatDoubleValue(bean.getMoney(), "0.0") + "元");
                break;
            default:
                holder.money.setText(MFUtil.formatDoubleValue(bean.getMoney(), "0.0") + "元");
                break;

        }
        holder.date.setText(sdf.format(new Date(bean.getTradeTime())));

//        switch (bean.getChannel()) {
//            case 0:
//                holder.payMethod.setText("支付宝");
//                break;
//            case 1:
//                holder.payMethod.setText("微信");
//                break;
//            case 2:
//                holder.payMethod.setText("余额");
//                break;
//        }
        return convertView;
    }

    class ViewHolder implements View.OnClickListener {

        @BindView(R.id.cost_detail_cost)
        TextView cost;
        @BindView(R.id.cost_detail_date)
        TextView date;
        @BindView(R.id.cost_detail_money)
        TextView money;
        @BindView(R.id.cost_detail_paymethod)
        TextView payMethod;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
