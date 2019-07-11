package com.park61.teacherhelper.widget.listview;

/**
 * Created by hlking123 on 2016/1/13.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private ScrollBottomListener scrollBottomListener;
    private ScrollDoneListener scrollDoneListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(x, y, oldx, oldy);
        }

        if (scrollBottomListener != null) {
            if (y + getHeight() >= computeVerticalScrollRange()) {
                //ScrollView滑动到底部了
                scrollBottomListener.scrollBottom();
            }
        }

        if (scrollDoneListener != null) {
            if (y >= getContext().getResources().getDisplayMetrics().heightPixels * 1.2) {
                //ScrollView滑动一定距离了
                scrollDoneListener.scrollDone(true);
            } else {
                scrollDoneListener.scrollDone(false);
            }
        }
    }

    public void setScrollBottomListener(ScrollBottomListener scrollBottomListener) {
        this.scrollBottomListener = scrollBottomListener;
    }

    public void setScrollDoneListener(ScrollDoneListener scrollBottomListener) {
        this.scrollDoneListener = scrollBottomListener;
    }

    /**
     * 滚动的回调接口
     *
     * @author xiaanming
     */
    public interface ScrollViewListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * 、
         */
        void onScrollChanged(int x, int y, int oldx, int oldy);

    }

    public interface ScrollBottomListener {
        public void scrollBottom();
    }

    public interface ScrollDoneListener {
        public void scrollDone(boolean isDone);
    }
}