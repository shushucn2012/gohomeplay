package com.park61.teacherhelper.widget.pw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;

import java.util.List;

public class StuChoseSexPopWin extends PopupWindow {
    private Context mContext;
    private View toolView;
    private ListView lv_teachers;
    private LevelListAdapter listAdapter;
    private List<String> dataList;

    public StuChoseSexPopWin(Context context, List<String> _list, String dialogTitle) {
        super(context);
        mContext = context;
        this.dataList = _list;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_stu_chose_sex_layout, null);

        lv_teachers = (ListView) toolView.findViewById(R.id.lv_teachers);
        listAdapter = new LevelListAdapter();
        lv_teachers.setAdapter(listAdapter);
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
        this.setAnimationStyle(R.style.AnimBottom);
    }

    public ListView getListView() {
        return lv_teachers;
    }

    public class LevelListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tasklevel_item, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String item = dataList.get(position);
            holder.tv_title.setText(item);
            return convertView;
        }

        class ViewHolder {
            TextView tv_title;
        }
    }

}
