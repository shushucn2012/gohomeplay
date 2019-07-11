package com.park61.teacherhelper.widget.pw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.park61.teacherhelper.R;

/**
 * 行事历首页日历提示图
 */
public class MySubsWorkplanTipPopWin extends PopupWindow {

    private Context mContext;
    private int touchCount = 0;//点击次数

    public MySubsWorkplanTipPopWin(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View toolView = inflater.inflate(R.layout.pw_calandarmain_tip_layout, null);
        View root_view = toolView.findViewById(R.id.root_view);
        root_view.setBackgroundResource(R.mipmap.tip_cover_sub_sucess);
        // 设置按钮监听
        //root_view.setOnClickListener(rightMenuClickedLsner);
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        toolView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (touchCount == 0) {
                        root_view.setBackgroundResource(R.mipmap.tip_cover_start_workplan);
                    } else if (touchCount == 1) {
                        root_view.setBackgroundResource(R.mipmap.tip_cover_lookcatalog);
                    } else {
                        dismiss();
                    }
                    touchCount++;
                }
                return true;
            }
        });
    }

}
