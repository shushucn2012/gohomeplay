package com.park61.teacherhelper.widget.pw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.module.course.adapter.BigCateAdapter;
import com.park61.teacherhelper.module.course.adapter.SmallCateAdapter;
import com.park61.teacherhelper.module.course.adapter.TypeCateAdapter;
import com.park61.teacherhelper.module.course.bean.AgeCate;
import com.park61.teacherhelper.module.course.bean.BigCate;

import java.util.List;

/**
 * 成长首页孩子列表
 *
 * @author super
 */
public class SearchOrderPopWin extends PopupWindow {
    private View toolView;
    private ListView lv_small_cate;
    private OnBigCateSelectLsner mOnSelectLsner;

    private Context mContext;
    private List<AgeCate> bigCateList;
    private TypeCateAdapter adapter;

    public SearchOrderPopWin(Context context, List<AgeCate> bList, int selecteddPos, OnBigCateSelectLsner lsner) {
        super(context);
        mContext = context;
        this.bigCateList = bList;
        this.mOnSelectLsner = lsner;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_orderfilter_layout, null);
        // 初始化视图
        lv_small_cate = (ListView) toolView.findViewById(R.id.lv_small_cate);

        for (int i = 0; i < bigCateList.size(); i++) {
            if (i == selecteddPos) {
                bigCateList.get(i).setSelected(true);
            } else {
                bigCateList.get(i).setSelected(false);
            }
        }

        // 初始化数据
        adapter = new TypeCateAdapter(mContext, bigCateList);
        lv_small_cate.setAdapter(adapter);

        lv_small_cate.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < bigCateList.size(); i++) {
                    if (i == position) {
                        bigCateList.get(i).setSelected(true);
                    } else {
                        bigCateList.get(i).setSelected(false);
                    }
                }
                adapter.notifyDataSetChanged();
                mOnSelectLsner.onSelect(position);
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.getScreenWidth(mContext));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.3));
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
        this.setOutsideTouchable(true);
    }

    public interface OnBigCateSelectLsner {
        void onSelect(int pos);
    }

}
