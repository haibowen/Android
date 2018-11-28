package com.example.administrator.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {
    private int mcolor=Color.GREEN;
    private Paint mpaint=new Paint(Paint.ANTI_ALIAS_FLAG);


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        mcolor=a.getColor(R.styleable.CircleView_cicle_color,Color.RED);
        a.recycle();
        init();
    }
    private void  init(){
        mpaint.setColor(mcolor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final  int paddingleft=getPaddingLeft();
        final  int paddingright=getPaddingRight();
        final  int paddingtop=getPaddingTop();
        final int paddinbottom=getPaddingBottom();
        int width=getWidth()-paddingleft-paddingright;
        int height=getHeight()-paddingtop-paddinbottom;
        int radius=Math.min(width,height)/2;
        canvas.drawCircle(paddingleft+width/2,paddingtop+height/2,radius,mpaint);
    }
}
