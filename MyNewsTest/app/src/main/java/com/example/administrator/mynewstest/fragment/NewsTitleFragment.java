package com.example.administrator.mynewstest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.mynewstest.R;
import com.example.administrator.mynewstest.activity.NewsContentActivity;
import com.example.administrator.mynewstest.bean.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment  extends Fragment {


    private boolean isTwopane;

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<News>  list;
        class ViewHolder extends  RecyclerView.ViewHolder{
            TextView newsTitleText;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                newsTitleText =itemView.findViewById(R.id.news_title1);

            }
        }

        public  NewsAdapter(List<News> newsList){

            list=newsList;


        }

        @NonNull
        @Override
        public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
           View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_item,viewGroup,false);
           final  ViewHolder holder=new ViewHolder(view);
           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   News news=list.get(holder.getAdapterPosition());
                   if (isTwopane){
                       NewsContentFragment newsContentFragment= (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragmenta);
                       newsContentFragment.refersh(news.getTitle(),news.getContent());

                   }else {

                       NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());


                   }
               }
           });



            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder viewHolder, int i) {


            News news=list.get(i);
            viewHolder.newsTitleText.setText(news.getTitle());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.news_title_frag,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.news_titlt_recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        NewsAdapter adapter=new NewsAdapter(getNews());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<News> getNews() {
        List<News> list=new ArrayList<News>();
        for (int i=0;i<=50;i++){
            News news=new News();
            news.setTitle("This is news title"+i);
            news.setContent(getRandomLengthContent("This is content"+i+" ."));
            list.add(news);

        }
        return list;
    }

    private String getRandomLengthContent(String s) {

        Random random=new Random();
        int length=random.nextInt(20)+1;
        StringBuilder builder =new StringBuilder();
        for (int i=0;i<length;i++){
            builder.append(s);

        }

return builder.toString();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content)!=null){
            isTwopane=true;

        }else {
            isTwopane=false;
        }
    }
}
