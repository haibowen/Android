package com.example.wenhaibo.androidstudy_drawerlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHOlder> {

private Context context;
private List<News> mNews;


    static  class  ViewHOlder extends  RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView textView;

        public ViewHOlder(View itemView) {
            super( itemView );

            cardView = (CardView) itemView;
            imageView=itemView.findViewById( R.id.image_item );
            textView=itemView.findViewById( R.id.text_item);

        }
    }

    public NewsAdapter(List<News> mNews) {

        this.mNews = mNews;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(context==null){

           context=parent.getContext();

       }
       View view= LayoutInflater.from( context ).inflate( R.layout.recycler_item,parent,false );


        return new ViewHOlder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHOlder holder, int position) {
        News news = mNews.get( position );
        holder.textView.setText( news.getTitle() );

        Glide.with( context ).load( news.getImageid() ).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }
}
