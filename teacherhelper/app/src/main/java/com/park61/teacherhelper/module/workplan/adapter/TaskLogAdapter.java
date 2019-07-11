package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.workplan.bean.TaskLogBean;

import java.util.List;

/**
 * Created by shubei on 2018/4/28.
 */

public class TaskLogAdapter extends BaseAdapter {

    private Context mContext;
    private List<TaskLogBean> mlist;

    public TaskLogAdapter(Context context, List<TaskLogBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.worklog_item, null);
            holder = new ViewHolder();
            holder.check_task = (ImageView) convertView.findViewById(R.id.check_task);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            holder.line_bottom = convertView.findViewById(R.id.line_bottom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskLogBean item = mlist.get(position);
        holder.tv_name.setText(item.getUserName());
        holder.tv_time.setText(item.getCreateDateStr());
        holder.tv_info.setText(item.getIntro());
        /*if (item.getType() == 0) {//0日志完成,1留言,2日志重启,3任务共享,4任务调整
            holder.check_task.setImageResource(R.mipmap.task_gou);
        } else if (item.getType() == 2) {//0日志完成,1留言,2日志重启,3任务共享,4任务调整
            holder.check_task.setImageResource(R.mipmap.task_state_restart);
        } else {
            holder.check_task.setImageResource(R.mipmap.icon_trans);
        }*/
        ImageManager.getInstance().displayImg(holder.check_task, item.getPictureUrl());
        if (position == mlist.size() - 1) {
            holder.line_bottom.setVisibility(View.GONE);
        } else {
            holder.line_bottom.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_time, tv_info;
        ImageView check_task;
        View line_bottom;
    }
}
