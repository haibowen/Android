package com.example.administrator.mynewstest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.administrator.mynewstest.R;


public class NewsContentFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.news_content_frag,container,false);
        return view;


    }

  public  void   refersh(String newsTitle,String newsContent){
        View view1=view.findViewById(R.id.vis_linear);
        view1.setVisibility(View.VISIBLE);
      TextView textView=view.findViewById(R.id.news_title);
      TextView textView1=view.findViewById(R.id.news_content);
      textView.setText(newsTitle);
      textView1.setText(newsContent);



    }
}
