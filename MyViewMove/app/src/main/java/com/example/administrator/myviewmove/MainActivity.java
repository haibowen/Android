package com.example.administrator.myviewmove;

import android.animation.ObjectAnimator;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_move)
    public Button button;

    @OnClick(R.id.bt_move)
    public void change() {
        Toast.makeText(MainActivity.this, "wobeidinjile", Toast.LENGTH_SHORT).show();
        Log.d("wenhaibo", "change: " + "2222222222222");

        ObjectAnimator.ofFloat(button, "translationX",
                0, 100).setDuration(100).start();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        /**
         public boolean onTouchEvent(MotionEvent event){

         int x= (int) event.getRawX();
         int y= (int) event.getRawY();
         switch ( event.getAction()){
         case MotionEvent.ACTION_DOWN:

         //
         break;
         case MotionEvent.ACTION_MOVE:
         int deltax=x-mlastx;
         int deltay=y-mlasty;

         int translationX= ViewDragHelper.getTranslationX(this)+deltax;
         int translationY= ViewDragHelper.getTranslationY(this)+deltay;
         ViewDragHelper.setTranslationX(this,translationX);
         ViewDragHelper.setTranslationY(this,translationY);
         break;
         case MotionEvent.ACTION_UP:
         break;
         default:
         break;




         }
         mlastx=x;
         mlasty=y;
         return true;


         }
         **/
    }
}
