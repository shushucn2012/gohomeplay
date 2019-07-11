package com.park61.teacherhelper.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.UIUtil;

/**
 * Created by nieyu on 2017/11/7.
 */

public class CirButton  extends TextView {

    private Paint mPaint;

    private int radiu;

    public CirButton(Context context) {
        super(context);
    }

    public CirButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.cir_button);

        int resourceId = typedArray.getResourceId(R.styleable.cir_button_cirColor, R.color.alivc_blue);

        int color = context.getResources().getColor(resourceId);

        mPaint.setColor(color);
        int integer = typedArray.getInteger(R.styleable.cir_button_cirRadiu, 10);
        radiu = UIUtil.dp(context, integer);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(rectF, radiu, radiu, mPaint);
        super.onDraw(canvas);
    }

    public void setColor(String color){
        mPaint.setColor(Color.parseColor("#"+color));
        invalidate();
    }
}
