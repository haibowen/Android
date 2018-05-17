package com.example.wenhaibo.androidstudy05;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class rightfragment extends Fragment {

    public static  final  String TAG="rightfragment";


    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        Log.d( TAG, "onAttach: " );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Log.d( TAG, "onCreate: " );

    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //加载布局

        View view=inflater.inflate( R.layout.right_fragment,container,false );

        Log.d( TAG, "onCreateView: " );

        return view;
        
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        Log.d( TAG, "onActivityResult: " );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d( TAG, "onStart: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d( TAG, "onResume: " );
        
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d( TAG, "onPause: " );
        
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d( TAG, "onStop: " );
        
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d( TAG, "onDestroy: " );
        
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d( TAG, "onDestroyView: " );
        

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d( TAG, "onDetach: " );
    }
}
