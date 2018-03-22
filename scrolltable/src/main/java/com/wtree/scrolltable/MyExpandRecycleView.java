package com.wtree.scrolltable;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by WTree on 2018/2/28.
 *
 * 把滑动交给父类的ScrollView
 */

public class MyExpandRecycleView extends RecyclerView {

    public MyExpandRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }





    RecyclerView view;

    /**
     * 为了传递触摸事件
     * @param view
     */
    public void setView(RecyclerView view){
        this.view=view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(view!=null){
            view.onTouchEvent(resetEvent(e));
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if(view!=null){
            view.dispatchTouchEvent(resetEvent(e));
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean isNext=super.onInterceptTouchEvent(e);
        if(view!=null){
            view.onInterceptTouchEvent(resetEvent(e));
        }
        return  isNext;

    }

    /**
     * ScrollView 滑动了，会超出一屏幕
     * 需要 重置x ,
     * @param e
     * @return
     */

    private MotionEvent resetEvent(MotionEvent e){

        e.setLocation(e.getRawX(),e.getY());
        return e;
    }
}
