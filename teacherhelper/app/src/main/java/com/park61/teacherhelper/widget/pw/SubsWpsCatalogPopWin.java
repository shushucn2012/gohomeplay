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
import com.park61.teacherhelper.module.course.adapter.CouponChooseGetListAdapter;
import com.park61.teacherhelper.module.pay.bean.CouponBean;
import com.park61.teacherhelper.module.workplan.bean.SubsTaskBean;

import java.util.List;

public class SubsWpsCatalogPopWin extends PopupWindow {
    private View toolView;
    private ListView lv_items;
    private SubsWpsCatalogListAdapter mSubsWpsCatalogListAdapter;
    private Context mContext;
    private List<SubsTaskBean> mList;

    public SubsWpsCatalogPopWin(Context context, List<SubsTaskBean> _list) {
        super(context);
        mList = _list;
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_subs_wps_catalog, null);
        lv_items = (ListView) toolView.findViewById(R.id.lv_items);
        mSubsWpsCatalogListAdapter = new SubsWpsCatalogListAdapter();
        lv_items.setAdapter(mSubsWpsCatalogListAdapter);
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
        return lv_items;
    }

    public SubsWpsCatalogListAdapter getListAdapter() {
        return mSubsWpsCatalogListAdapter;
    }

    private class SubsWpsCatalogListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_subs_wps_catalog, null);
                holder = new ViewHolder();
                holder.tv_no = convertView.findViewById(R.id.tv_no);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.tv_status = convertView.findViewById(R.id.tv_status);
                holder.line_bottom = convertView.findViewById(R.id.line_bottom);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SubsTaskBean item = mList.get(position);
            int index = position + 1;
            holder.tv_no.setText(index < 10 ? "0" + index : "" + index);
            holder.tv_name.setText(item.getTemplateDetailName());
            holder.tv_status.setText(item.getStatus() == 0 ? "未完成" : "完成");
            return convertView;
        }

        class ViewHolder {
            TextView tv_no, tv_name, tv_status;
            View line_bottom;
        }
    }

}
