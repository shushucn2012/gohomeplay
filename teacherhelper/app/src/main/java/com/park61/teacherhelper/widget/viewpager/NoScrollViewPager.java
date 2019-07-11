package com.park61.teacherhelper.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by chenlie on 2018/5/7.
 * 禁止滑动
 */

public class NoScrollViewPager extends ViewPager {

    private boolean isScroll = false;
    private float beforeX;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScroll && super.onTouchEvent(ev);
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(isScroll){
//            return super.dispatchTouchEvent(ev);
//        }else  {
//            switch (ev.getAction()) {
//                case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
//                    beforeX = ev.getX();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float motionValue = ev.getX() - beforeX;
//                    if (motionValue < 0) {//禁止左滑
//                        return true;
//                    }
//                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
//
//                    break;
//                default:
//                    break;
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//
//    }
}
