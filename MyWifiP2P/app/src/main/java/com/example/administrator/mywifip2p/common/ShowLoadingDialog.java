package com.example.administrator.mywifip2p.common;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import android.support.annotation.StringRes;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.mywifip2p.R;

public class ShowLoadingDialog extends Dialog {

private ImageView imageView;
private TextView textView;
private Animation animation;




    public ShowLoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_layout);

        imageView=findViewById(R.id.image);
        textView=findViewById(R.id.tv_hint);
        animation= AnimationUtils.loadAnimation(context,R.anim.loading_dialog);


    }

   public  void  show(String hinTx,boolean cancelable,boolean canceledOnTouchOutside){

        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);

        textView.setText(hinTx);
        imageView.startAnimation(animation);

        show();



   }

    public void show(@StringRes int hintTextRes, boolean cancelable, boolean canceledOnTouchOutside) {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
       textView.setText(hintTextRes);
        imageView.startAnimation(animation);
        show();
    }

    @Override
    public void cancel() {
        super.cancel();

        animation.cancel();
        imageView.clearAnimation();
    }

    @Override
    public void dismiss() {
        super.dismiss();

        animation.cancel();
        imageView.clearAnimation();
    }
}
