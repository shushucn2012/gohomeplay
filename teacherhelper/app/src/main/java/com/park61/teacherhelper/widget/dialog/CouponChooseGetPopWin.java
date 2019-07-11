package com.park61.teacherhelper.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.module.course.adapter.CouponChooseGetListAdapter;
import com.park61.teacherhelper.module.home.bean.CourseBean;
import com.park61.teacherhelper.module.pay.bean.CouponBean;

import java.util.List;

public class CouponChooseGetPopWin extends PopupWindow {
    private View toolView, area_bottom_finish;
    private ListView lv_teachers;
    private CouponChooseGetListAdapter mCouponChooseGetListAdapter;

    public CouponChooseGetPopWin(Context context, List<CouponBean> _list) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_coupon_choose_get_layout, null);
        lv_teachers = (ListView) toolView.findViewById(R.id.lv_teachers);
        area_bottom_finish = toolView.findViewById(R.id.area_bottom_finish);
        mCouponChooseGetListAdapter = new CouponChooseGetListAdapter(context, _list);
        lv_teachers.setAdapter(mCouponChooseGetListAdapter);
        // 设置按钮监听
//		lv_teachers.setOnItemClickListener(itemClickListener);
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);

        //int height = DevAttr.dip2px(context, 400);//RelativeLayout.LayoutParams.WRAP_CONTENT;
       /* if (_list.size() < 4) {
            height = _list.size() * DevAttr.dip2px(context, 100);
        } else {
            height = (int) (DevAttr.getScreenHeight(context) * 0.6);
        }*/
        //lv_teachers.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        //ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        //this.setBackgroundDrawable(context.getDrawable(R.drawable.planchoose_list_bg));
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置弹出窗体显示时的动画，从底部向上弹出
        //this.setAnimationStyle(R.style.take_photo_anim);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.toolView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = toolView.findViewById(R.id.menuview_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        area_bottom_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setAnimationStyle(R.style.AnimBottom);
    }

    public ListView getLvTeachers() {
        return lv_teachers;
    }

    public CouponChooseGetListAdapter getmCouponChooseGetListAdapter() {
        return mCouponChooseGetListAdapter;
    }

}
