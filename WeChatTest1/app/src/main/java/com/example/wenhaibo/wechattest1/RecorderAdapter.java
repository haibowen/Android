package com.example.wenhaibo.wechattest1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wenhaibo on 2017/12/19.
 */

public class RecorderAdapter extends ArrayAdapter<Recorder> {

    private List<Recorder> mDatas;
    private Context mcontext;
    private  int mMinitemWidth;
    private  int mMaxitemWidth;
    private LayoutInflater minflater;
    public RecorderAdapter(@NonNull Context context, List<Recorder>datas) {
        super(context, -1,datas);
        mcontext=context;
        mDatas=datas;
        minflater=LayoutInflater.from(context);

        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics =new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxitemWidth=(int)(outMetrics.widthPixels*0.7f);
        mMinitemWidth=(int)(outMetrics.widthPixels*0.15f);

    }
    @SuppressLint({"WrongViewCast", "ResourceType"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            convertView=minflater.inflate(R.layout.item,parent,false);
            holder =new ViewHolder();
            holder.seconds=(TextView) convertView.findViewById(R.id.id__record_time);
            holder.length=convertView.findViewById(R.id.id__record_anim);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.seconds.setText(Math.round(getItem(position).time)+"\"");
        ViewGroup.LayoutParams lp=holder.length.getLayoutParams();
        lp.width=(int)(mMinitemWidth +(mMaxitemWidth/60f*getItem(position).time));




        return convertView;
    }

   private  class ViewHolder
   {
       TextView seconds;
       View length;
   }

}
