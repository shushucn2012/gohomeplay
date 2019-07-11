package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.WorkDetailActivity;
import com.park61.teacherhelper.module.workplan.bean.TaskInfoBean;

import java.util.List;

/**
 * 今日待办列表适配器
 *
 * @author shubei
 * @time 2018/12/19 17:06
 */
public class TodayWpListAdapter extends BaseAdapter {

    private Context mContext;
    private List<TaskInfoBean> mlist;

    public TodayWpListAdapter(Context context, List<TaskInfoBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public TaskInfoBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_today_wplist, null);
            holder = new ViewHolder();
            holder.tv_wp_task_name = convertView.findViewById(R.id.tv_wp_task_name);
            holder.img_task_state = convertView.findViewById(R.id.img_task_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskInfoBean item = mlist.get(position);
        holder.tv_wp_task_name.setText(item.getName());
        if (item.getStatus() == 1) {//已完成
            holder.img_task_state.setImageResource(R.mipmap.light_logo);
        } else {
            holder.img_task_state.setImageResource(R.mipmap.unlight_logo);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, WorkDetailActivity.class);
                it.putExtra("taskCalendarId", item.getId());
                it.putExtra("taskType", item.getTemplateTaskType());
                it.putExtra("isOwnTask", 1);
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_wp_task_name;
        ImageView img_task_state;
    }
}
