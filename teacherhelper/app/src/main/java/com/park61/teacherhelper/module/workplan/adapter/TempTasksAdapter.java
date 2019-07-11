package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Space;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.bean.TempTaskBean;

import java.util.List;

/**
 * Created by shubei on 2018/4/28.
 */

public class TempTasksAdapter extends BaseAdapter {

    private Context mContext;
    private List<TempTaskBean> mlist;

    public TempTasksAdapter(Context context, List<TempTaskBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public TempTaskBean getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.temptasks_item, null);
            holder = new ViewHolder();
            holder.line_bottom = convertView.findViewById(R.id.line_bottom);
            holder.tv_task_type = (TextView) convertView.findViewById(R.id.tv_task_type);
            holder.tv_task_name = (TextView) convertView.findViewById(R.id.tv_task_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TempTaskBean item = mlist.get(position);
        String cateName = "";
        if (!TextUtils.isEmpty(item.getCategoryName())) {
            if (item.getCategoryName().length() <= 2) {
                cateName = item.getCategoryName();
            } else if (item.getCategoryName().length() == 3) {
                cateName = item.getCategoryName().substring(0, 2) + "\n" + item.getCategoryName().substring(2, 3);
            } else if (item.getCategoryName().length() > 3) {
                cateName = item.getCategoryName().substring(0, 2) + "\n" + item.getCategoryName().substring(2, 4);
            }
        }
        holder.tv_task_type.setText(cateName);
        holder.tv_task_name.setText(item.getTitle());
        if (position == mlist.size() - 1) {
            holder.line_bottom.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        View line_bottom;
        TextView tv_task_type, tv_task_name;
    }
}
