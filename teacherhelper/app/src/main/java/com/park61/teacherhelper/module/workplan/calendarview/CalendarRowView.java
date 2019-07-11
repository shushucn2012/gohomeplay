// Copyright 2012 Square, Inc.
package com.park61.teacherhelper.module.workplan.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.DevAttr;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * TableRow that draws a divider between each cell. To be used with {@link CalendarGridView}.
 */
public class CalendarRowView extends ViewGroup implements View.OnClickListener {
    private boolean isHeaderRow;
    private MonthView.Listener listener;
    private int cellSize;

    public CalendarRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        child.setOnClickListener(this);
        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        cellSize = totalWidth / 7;
        int cellWidthSpec = makeMeasureSpec(cellSize, EXACTLY);
        Log.out("cellSize===================" + cellSize);
        Log.out("cellWidthSpec===================" + cellWidthSpec);
        int cellHeightSpec = isHeaderRow ? makeMeasureSpec(DevAttr.dip2px(getContext(), 50), EXACTLY) : makeMeasureSpec(DevAttr.dip2px(getContext(), 60), EXACTLY);
        int rowHeight = 0;
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            child.measure(cellWidthSpec, cellHeightSpec);
            // The row height is the height of the tallest cell.
            if (child.getMeasuredHeight() > rowHeight) {
                rowHeight = child.getMeasuredHeight();
            }
        }
        final int widthWithPadding = totalWidth + getPaddingLeft() + getPaddingRight();
        final int heightWithPadding = rowHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthWithPadding, heightWithPadding);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int cellHeight = bottom - top;
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            child.layout(c * cellSize, 0, (c + 1) * cellSize, cellHeight);
        }
    }

    public void setIsHeaderRow(boolean isHeaderRow) {
        this.isHeaderRow = isHeaderRow;
    }

    @Override
    public void onClick(View v) {
        // Header rows don't have a click listener
        if (listener != null) {
            listener.handleClick((MonthCellDescriptor) v.getTag());
        }
    }

    public void setListener(MonthView.Listener listener) {
        this.listener = listener;
    }

}
