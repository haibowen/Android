package com.example.wenhaibo.androidstudy03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LinearTitle extends LinearLayout implements View.OnClickListener {

    //声明控件

    private Button button;
    private  Button button1;

    public LinearTitle(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );

        //绑定前台布局

        LayoutInflater.from( context ).inflate( R.layout.title_layout,this );

        //绑定控件id

        button=findViewById( R.id.bt_back );
        button1=findViewById( R.id.bt_next );

        //点击事件

        button.setOnClickListener( this );
        button1.setOnClickListener( this );


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.bt_back:
                //Intent intent =new Intent( getContext(),MainActivity.class );
                //getContext().startActivity( intent );
                ((Activity) getContext() ).finish();

                break;
            case  R.id.bt_next:
                Intent intent1=new Intent( getContext(),Main3Activity.class );
                getContext().startActivity( intent1 );
                break;

                default:
                    break;


        }
    }
}
