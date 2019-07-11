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
import com.park61.teacherhelper.module.course.bean.BigCate;

import java.util.List;

/**
 * 成长首页孩子列表
 *
 * @author super
 */
public class SearchTypePopWin extends PopupWindow {
    private View toolView;
    private ListView lv_big_cate, lv_small_cate;
    private Button btn_confirm, btn_reset;
    private OnBigCateSelectLsner mOnSelectLsner;

    private Context mContext;
    private List<BigCate> bigCateList;
    private List<BigCate.ListContentTagBean> smallCateList;
    private BigCateAdapter adapter;
    private SmallCateAdapter sadapter;
    private int selectedBigPos;

    public SearchTypePopWin(Context context, List<BigCate> bList, int selecteddPos, OnBigCateSelectLsner lsner) {
        super(context);
        mContext = context;
        this.bigCateList = bList;
        this.mOnSelectLsner = lsner;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_typefilter_layout, null);
        // 初始化视图
        lv_big_cate = (ListView) toolView.findViewById(R.id.lv_big_cate);
        lv_small_cate = (ListView) toolView.findViewById(R.id.lv_small_cate);
        btn_confirm = (Button) toolView.findViewById(R.id.btn_confirm);
        btn_reset = (Button) toolView.findViewById(R.id.btn_reset);

        for (int i = 0; i < bigCateList.size(); i++) {
            if (i == selecteddPos) {
                bigCateList.get(i).setSelected(true);
            } else {
                bigCateList.get(i).setSelected(false);
            }
        }

        // 初始化数据
        adapter = new BigCateAdapter(mContext, bigCateList);
        lv_big_cate.setAdapter(adapter);
        // 初始化监听
        lv_big_cate.setOnItemClickListener(new OnItemClickListener() {

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
                selectedBigPos = position;
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSmallCateList(bigCateList.get(selectedBigPos).getListContentTag(), 0);
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.getScreenWidth(mContext));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.5));
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

    public void initSmallCateList(List<BigCate.ListContentTagBean> sList, int selecteddPos) {
        this.smallCateList = sList;

        for (int i = 0; i < smallCateList.size(); i++) {
            if (i == selecteddPos) {
                smallCateList.get(i).setSelected(true);
            } else {
                smallCateList.get(i).setSelected(false);
            }
        }

        sadapter = new SmallCateAdapter(mContext, smallCateList);
        lv_small_cate.setAdapter(sadapter);

        lv_small_cate.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && smallCateList.get(0).getId() == 0) {//点击全部的时候
                    for (int i = 0; i < smallCateList.size(); i++) {
                        if (i == 0) {
                            smallCateList.get(i).setSelected(true);
                        } else {
                            smallCateList.get(i).setSelected(false);
                        }
                    }
                    sadapter.notifyDataSetChanged();
                } else if (smallCateList.get(0).getId() == 0) {
                    smallCateList.get(0).setSelected(false);
                    if (smallCateList.get(position).isSelected()) {
                        smallCateList.get(position).setSelected(false);
                        if (isAllUnSelected(smallCateList)) {//如果所有item都没有选择
                            smallCateList.get(0).setSelected(true);//全部被选择
                        }
                    } else {
                        smallCateList.get(position).setSelected(true);
                    }
                    sadapter.notifyDataSetChanged();
                }
            }
        });
    }

    private boolean isAllUnSelected(List<BigCate.ListContentTagBean> smallCateList) {
        for (int i = 0; i < smallCateList.size(); i++) {
            if (smallCateList.get(i).isSelected())
                return false;
        }
        return true;
    }

    public Button getBtnConfirm() {
        return btn_confirm;
    }

    public long getSelectedBigCate() {
        for (int i = 0; i < bigCateList.size(); i++) {
            if (bigCateList.get(i).isSelected()) {
                return bigCateList.get(i).getId();
            }
        }
        return -1;
    }

    public String getSelectedTags() {
        String s = "";
        for (int i = 0; i < smallCateList.size(); i++) {
            if (smallCateList.get(i).isSelected()) {
                s += smallCateList.get(i).getId() + ",";
            }
        }
        if (!TextUtils.isEmpty(s)) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

}
