package com.park61.teacherhelper.widget.pw;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
 * 查询年龄
 *
 * @author super
 */
public class SearchAgePopWin extends PopupWindow {
    private View toolView;
    private ListView lv_small_cate;
    private Button btn_commit, btn_reset;
    private OnBigCateSelectLsner mOnSelectLsner;

    private Context mContext;
    private List<AgeCate> ageCateList;
    private TypeCateAdapter sadapter;

    public SearchAgePopWin(Context context, List<AgeCate> bList, OnBigCateSelectLsner lsner) {
        super(context);
        mContext = context;
        this.ageCateList = bList;
        this.mOnSelectLsner = lsner;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_agefilter_layout, null);
        // 初始化视图
        lv_small_cate = (ListView) toolView.findViewById(R.id.lv_small_cate);
        btn_commit = (Button) toolView.findViewById(R.id.btn_commit);
        btn_reset = (Button) toolView.findViewById(R.id.btn_reset);

        for (int i = 0; i < ageCateList.size(); i++) {
            ageCateList.get(i).setSelected(true);
        }

        sadapter = new TypeCateAdapter(mContext, ageCateList);
        lv_small_cate.setAdapter(sadapter);

        lv_small_cate.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ageCateList.get(position).isSelected()) {
                    ageCateList.get(position).setSelected(false);
                } else {
                    ageCateList.get(position).setSelected(true);
                }
                sadapter.notifyDataSetChanged();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectLsner.onSelect(0);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < ageCateList.size(); i++) {
                    ageCateList.get(i).setSelected(false);
                }
                sadapter.notifyDataSetChanged();
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.getScreenWidth(mContext));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.4));
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

    public String getSelectedTags() {
        String s = "";
        for (int i = 0; i < ageCateList.size(); i++) {
            if (ageCateList.get(i).isSelected()) {
                s += ageCateList.get(i).getValue() + ",";
            }
        }
        if (!TextUtils.isEmpty(s)) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

}
