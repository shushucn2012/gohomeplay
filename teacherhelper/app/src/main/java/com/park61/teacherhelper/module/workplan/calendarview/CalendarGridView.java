// Copyright 2012 Square, Inc.
package com.park61.teacherhelper.module.workplan.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import com.park61.teacherhelper.R;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * ViewGroup that draws a grid of calendar cells.  All children must be {@link CalendarRowView}s.
 * The first row is assumed to be a header and no divider is drawn above it.
 */
public class CalendarGridView extends ViewGroup {
    /**
     * The grid lines don't exactly line up on certain devices (Nexus 7, Nexus 5). Fudging the
     * co-ordinates by half a point seems to fix this without breaking other devices.
     */
    private static final float FLOAT_FUDGE = 0.5f;

    private final Paint dividerPaint = new Paint();
    private int oldWidthMeasureSize;
    private int oldNumRows;

    public CalendarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dividerPaint.setColor(getResources().getColor(R.color.calendar_divider));
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() == 0) {
            ((CalendarRowView) child).setIsHeaderRow(true);
        }
        super.addView(child, index, params);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        final ViewGroup row = (ViewGroup) getChildAt(1);
        int top = row.getTop();
        int bottom = getBottom();
        // Left side border.
        final int left = row.getChildAt(0).getLeft() + getLeft();
        //canvas.drawLine(left + FLOAT_FUDGE, top, left + FLOAT_FUDGE, bottom, dividerPaint);

        // Each cell's right-side border.
        for (int c = 0; c < 7; c++) {
            //float x = left + row.getChildAt(c).getRight() - FLOAT_FUDGE;
            //canvas.drawLine(x, top, x, bottom, dividerPaint);
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean retVal = super.drawChild(canvas, child, drawingTime);
        // Draw a bottom border.
        final int bottom = child.getBottom() - 1;
        //canvas.drawLine(child.getLeft(), bottom, child.getRight() - 2, bottom, dividerPaint);
        return retVal;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        if (oldWidthMeasureSize == widthMeasureSize) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
            return;
        }
        oldWidthMeasureSize = widthMeasureSize;
        int cellSize = widthMeasureSize / 7;
        // Remove any extra pixels since /7 is unlikely to give whole nums.
        widthMeasureSize = cellSize * 7;
        int totalHeight = 0;
        final int rowWidthSpec = makeMeasureSpec(widthMeasureSize, EXACTLY);
        final int rowHeightSpec = makeMeasureSpec(cellSize, EXACTLY);
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            if (child.getVisibility() == View.VISIBLE) {
                if (c == 0) { // It's the header: height should be wrap_content.
                    measureChild(child, rowWidthSpec, makeMeasureSpec(cellSize, AT_MOST));
                } else {
                    measureChild(child, rowWidthSpec, rowHeightSpec);
                }
                totalHeight += child.getMeasuredHeight();
            }
        }
        final int measuredWidth = widthMeasureSize + 2; // Fudge factor to make the borders show up.
        setMeasuredDimension(measuredWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        top = 0;
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            final int rowHeight = child.getMeasuredHeight();
            child.layout(left, top, right, top + rowHeight);
            top += rowHeight;
        }
    }

    public void setNumRows(int numRows) {
        if (oldNumRows != numRows) {
            // If the number of rows changes, make sure we do a re-measure next time around.
            oldWidthMeasureSize = 0;
        }
        oldNumRows = numRows;
    }
}
