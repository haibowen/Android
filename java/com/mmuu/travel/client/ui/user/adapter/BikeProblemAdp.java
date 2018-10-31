package com.mmuu.travel.client.ui.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.FeelBackBean;
import com.mmuu.travel.client.bean.MyRunsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 问题反馈其中adp
 * Created by HuangYuan on 2016/12/20.
 */

public class BikeProblemAdp extends BaseAdapter {

    private Context context;
    private List<FeelBackBean> datas;
    private LayoutInflater inflater;
    private Map<String, Object> problems = new HashMap<>();
    private OnCheckedProblemOption onCheckedProblemOption;

    public BikeProblemAdp(Context context, List<FeelBackBean> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public void setOnCheckedProblemOption(OnCheckedProblemOption onCheckedProblemOption) {
        this.onCheckedProblemOption = onCheckedProblemOption;
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
        FeelBackBean desc = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_bike_problem_feedback, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setText(desc.getCheckName());
        holder.checkBox.setTag(desc);

        return convertView;
    }


    class ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.bike_problem_checkbox)
        CheckBox checkBox;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            FeelBackBean desc = (FeelBackBean) buttonView.getTag();
            if (isChecked) {
                problems.put(desc.getId() + "", "");
            } else {
                problems.remove(desc.getId() + "");
            }

            if (onCheckedProblemOption != null) {
                onCheckedProblemOption.checkCount(problems);
            }
        }
    }

    public interface OnCheckedProblemOption {
        void checkCount(Map<String, Object> checkedMap);
    }

    public void replaceData(List<FeelBackBean> mPostionlistItems) {
        if (datas == null) {
            datas = new ArrayList<FeelBackBean>();
        }
        datas.clear();
        if (mPostionlistItems != null) {
            datas.addAll(mPostionlistItems);
        }
    }

}
