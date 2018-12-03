package com.example.administrator.myzhihuproject;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<News> mlists;

    public  MyAdapter(List<News> lists){

        mlists=lists;
    }

    static class  ViewHolder extends  RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             cardView= (CardView) itemView;
             imageView=itemView.findViewById(R.id.item_image);
             textView=itemView.findViewById(R.id.item_textview);


        }
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       if (context==null){
           context=viewGroup.getContext();

       }
       View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_item,viewGroup,false);

       return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {

        News news=mlists.get(i);
        viewHolder.textView.setText(news.getTitle());
        Glide.with(context).load(news.getImageid()).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return mlists.size();
    }
}
