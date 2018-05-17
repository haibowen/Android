package com.example.wenhaibo.androidstudy05;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class leftfragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        //加载布局

        View view = inflater.inflate( R.layout.left_fragment, container, false );

        return view;

    }
}
