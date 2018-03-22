package com.wtree.scrolltable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by WTree on 2018/3/1.
 */

public class LockHorizontalScrollView extends SyncHorizontalScrollView {
    public LockHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    boolean isVeritor;
    int mLastMotionX, mLastMotionY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 首先拦截down事件,记录y坐标
                mLastMotionX = x;
                mLastMotionY = y;
                isVeritor=false;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 是向下运动,< 0是向上运动
                int deltaX = x - mLastMotionX;
                int deltaY = y - mLastMotionY;
                if (Math.abs(deltaX) < Math.abs(deltaY)) {

                    isVeritor=true;
                    //传递给listview
                    return false;
                }else{
                    //锁住事件
                    if(isVeritor){
                        return false;
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isVeritor=false;
                break;

        }
        return super.onInterceptTouchEvent(e);
    }


}
