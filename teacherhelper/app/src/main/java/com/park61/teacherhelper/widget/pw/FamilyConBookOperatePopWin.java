package com.park61.teacherhelper.widget.pw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.MainTabActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.module.home.HomeModularListActivity;
import com.park61.teacherhelper.module.my.FeebackActivity;
import com.park61.teacherhelper.module.my.MyMainActivity;
import com.park61.teacherhelper.module.workplan.WorkManageMainActivity;


/**
 * 家园联系薄操作窗口
 * @author shubei
 * @time 2018/11/26 14:26
 */
public class FamilyConBookOperatePopWin extends PopupWindow {

    private Context mContext;

    public View toolView, view_edit, view_del, area_top_arrow, area_bottom_arrow;

    public FamilyConBookOperatePopWin(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_family_operate_layout, null);
        view_edit = toolView.findViewById(R.id.view_edit);
        view_del = toolView.findViewById(R.id.view_del);
        area_top_arrow = toolView.findViewById(R.id.area_top_arrow);
        area_bottom_arrow = toolView.findViewById(R.id.area_bottom_arrow);
        // 设置按钮监听
        //view_home.setOnClickListener(rightMenuClickedLsner);
        //view_msg.setOnClickListener(rightMenuClickedLsner);
        //view_helper.setOnClickListener(rightMenuClickedLsner);
        //view_feed.setOnClickListener(rightMenuClickedLsner);
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        toolView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
    }



}
