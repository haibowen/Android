package com.mmuu.travel.client.ui.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.user.VoucherBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 出行券adp
 */

public class VoucherAdp extends BaseAdapter {

    private Context context;
    private List<VoucherBean.DataBean> datas;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //        使用状态  0有效 ；1其他无效
    private String flag = "";

    public VoucherAdp(Context context, List<VoucherBean.DataBean> datas, String flag) {
        this.context = context;
        this.datas = datas;
        this.flag = flag;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<VoucherBean.DataBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
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
        VoucherBean.DataBean voucherVO = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_voucher, null);
            holder = new ViewHolder(convertView);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                holder.vLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMoney.setText(voucherVO.getValue() + "元");
        try {
            holder.tvDate.setText(
                    "有效期：" + sdf.format(new Date(voucherVO.getValidityStartDate())) + " 至 " +
                            sdf.format(new Date(voucherVO.getValidityEndDate())));
        } catch (Exception e) {
        }
        switch (flag) {
            case "enable":
                holder.tvDate.setTextColor(ContextCompat.getColor(context, R.color.c333333_hint_font1));
                holder.tvMoney.setTextColor(ContextCompat.getColor(context, R.color.c333333_hint_font1));
                holder.tvMoney.setBackgroundColor(Color.parseColor("#ffdb35"));
                break;
            case "beused":
            case "invalid":
                holder.tvDate.setTextColor(ContextCompat.getColor(context, R.color.c999999_hint_font3));
                holder.tvMoney.setTextColor(ContextCompat.getColor(context, R.color.c999999_hint_font3));
                holder.tvMoney.setBackgroundColor(Color.parseColor("#dddddd"));
                break;

        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.tv_date)
        TextView tvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
