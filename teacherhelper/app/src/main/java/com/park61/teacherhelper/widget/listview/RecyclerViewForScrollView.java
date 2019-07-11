package com.park61.teacherhelper.widget.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by chenlie on 2018/2/25.
 *
 */

public class RecyclerViewForScrollView extends RecyclerView {

    public RecyclerViewForScrollView(Context context) {
        super(context);
    }

    public RecyclerViewForScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewForScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
