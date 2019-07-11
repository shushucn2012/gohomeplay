package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.WorkDetailActivity;
import com.park61.teacherhelper.module.workplan.WorkResponseActivity;
import com.park61.teacherhelper.module.workplan.bean.TaskCalendarListBean;
import com.park61.teacherhelper.module.workplan.bean.WorkMonthBean;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import java.util.List;

public class OutTimeWorkRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater factory;
    private List<WorkMonthBean> mlist;
    private OnItemOperateClickListener listener;

    public OutTimeWorkRvAdapter(Context context, List<WorkMonthBean> list) {
        this.mContext = context;
        this.mlist = list;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.worklist_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        WorkMonthBean item = mlist.get(position);
        myViewHolder.tv_month_date.setText(item.getShowStartDate());
        myViewHolder.lv_work_of_month.setAdapter(new WorkOfMonthListAdapter(item.getTaskCalendarList()));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_month_date;
        private ListViewForScrollView lv_work_of_month;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_month_date = (TextView) itemView.findViewById(R.id.tv_month_date);
            lv_work_of_month = (ListViewForScrollView) itemView.findViewById(R.id.lv_work_of_month);
            lv_work_of_month.setFocusable(false);
        }
    }


    class WorkOfMonthListAdapter extends BaseAdapter {

        private List<TaskCalendarListBean> wlist;

        public WorkOfMonthListAdapter(List<TaskCalendarListBean> list) {
            this.wlist = list;
        }

        @Override
        public int getCount() {
            return wlist.size();
        }

        @Override
        public Object getItem(int position) {
            return wlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WViewHolder holder = null;
            if (convertView == null) {
                convertView = factory.inflate(R.layout.lv_workofmonth_item, null);
                holder = new WViewHolder();
                holder.task_type_name = (TextView) convertView.findViewById(R.id.task_type_name);
                holder.task_name = (TextView) convertView.findViewById(R.id.task_name);
                holder.task_status = (TextView) convertView.findViewById(R.id.task_status);
                holder.task_class = (TextView) convertView.findViewById(R.id.task_class);
                holder.img_key = (ImageView) convertView.findViewById(R.id.img_key);
                holder.img_leftmsg = (ImageView) convertView.findViewById(R.id.img_leftmsg);
                convertView.setTag(holder);
            } else {
                holder = (WViewHolder) convertView.getTag();
            }
            TaskCalendarListBean taskCalendarListBean = wlist.get(position);
            holder.task_type_name.setText(taskCalendarListBean.getLevel1Name());
            holder.task_name.setText(taskCalendarListBean.getName());
            holder.task_class.setText(taskCalendarListBean.getTeachClassName());
            if (taskCalendarListBean.getTemplateTaskType() == 0) {//工作任务
                holder.task_type_name.setBackgroundResource(R.drawable.circle_shape_gfa7f94);
            } else {//学习任务
                holder.task_type_name.setBackgroundResource(R.drawable.circle_shape_gfcce09);
            }
            if (taskCalendarListBean.getTaskCalendarLog() != null) {
                holder.task_status.setText(taskCalendarListBean.getTaskCalendarLog().getShowUpdateDate() + " | "
                        + taskCalendarListBean.getTaskCalendarLog().getIntro() + "-"
                        + taskCalendarListBean.getTaskCalendarLog().getUserName());
            } else {
                holder.task_status.setText(" ");
            }
            if (taskCalendarListBean.getIsKeyView() == 0) {//是否显示关键目标0不显示1显示
                holder.img_key.setVisibility(View.GONE);
            } else {
                holder.img_key.setVisibility(View.VISIBLE);
            }
            holder.img_leftmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onLeftMsgClicked(taskCalendarListBean.getId());
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, WorkDetailActivity.class);
                    it.putExtra("taskCalendarId", taskCalendarListBean.getId());
                    it.putExtra("taskType", taskCalendarListBean.getTemplateTaskType());
                    it.putExtra("isOwnTask", taskCalendarListBean.getIsOwnTask());
                    mContext.startActivity(it);
                }
            });
            return convertView;
        }

        class WViewHolder {
            ImageView img_key, img_leftmsg;
            TextView task_type_name, task_name, task_status, task_class;
        }
    }

    public interface OnItemOperateClickListener {
        void onLeftMsgClicked(int id);//点击留言
    }

    public void setOnItemOperateClickListener(OnItemOperateClickListener listener) {
        this.listener = listener;
    }

}
