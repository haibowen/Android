package com.example.jiannote;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    private Context context;
    private List<Map<String, Object>> dataList;


    public RecyclerAdapter(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context==null){
            context=viewGroup.getContext();

        }
        View view= LayoutInflater.from(context).inflate(R.layout.newitem,viewGroup,false);
        final  ViewHolder holder=new ViewHolder(view);
        //点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion=holder.getAdapterPosition();


            }
        });

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewHolder, int i) {


      //  viewHolder.textView.setText( );
       // Log.e("wenhaibo", "onBindViewHolder: "+news.getTitle() );
       // Glide.with(context).load(news.getImageid()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView textView,textView1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.tv_date);
            textView1=itemView.findViewById(R.id.tv_content);
            //imageView=itemView.findViewById(R.id.item_image);


        }
    }
}
