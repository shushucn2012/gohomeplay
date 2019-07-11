package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.module.workplan.WorkDetailActivity;
import com.park61.teacherhelper.module.workplan.bean.CalendarTask;

import java.util.List;

/**
 * Created by chenlie on 2018/4/24.
 * 日历界面当月的任务
 */

public class CalendarAllTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CalendarTask> datas;
    private OperateListener mOperateListener;
    private Context mContext;

    public CalendarAllTaskAdapter(List<CalendarTask> list, Context context) {
        datas = list;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View v;
        if (viewType == 0) {
            //日期类
            v = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_date, parent, false);
            holder = new DateHolder(v);
        } else if (viewType == 2) {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_empty, parent, false);
            holder = new EmptyHolder(v);
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_all_task, parent, false);
            holder = new TaskHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CalendarTask t = datas.get(position);
        if (holder instanceof DateHolder) {
            //显示日期
            //((DateHolder)holder).dateTv.setText(t.getLeftTopTime());
            ((DateHolder) holder).dateTv.setText("");
        } else if (holder instanceof TaskHolder) {
            //显示任务,如果任务是已完成，不显示三个操作按钮
            TaskHolder th = (TaskHolder) holder;
            if (t.getIsKeyView() == 1) {//显示关键
                th.keyView.setVisibility(View.VISIBLE);
            } else {//隐藏关键
                th.keyView.setVisibility(View.GONE);
            }
            th.taskType.setText(t.getLevel1Name());
            if (t.getTemplateTaskType() == 0) {//工作任务
                th.taskType.setBackgroundResource(R.drawable.circle_shape_gfa7f94);
            } else {//学习任务
                th.taskType.setBackgroundResource(R.drawable.circle_shape_gfcce09);
            }
            th.taskName.setText(t.getName());
            th.task_class.setText(t.getTeachClassName());
            /*th.img_leftmsg.setOnClickListener(v -> {
                if (mOperateListener != null) {
                    mOperateListener.leaveMsg(t.getId(), position);
                }
            });*/
            th.task_status.setText(t.getTaskStatusDescription() + "    " + t.getTaskExecutorsDescription());
            holder.itemView.setOnClickListener(v -> {
                Intent it = new Intent(mContext, WorkDetailActivity.class);
                it.putExtra("taskCalendarId", t.getId());
                it.putExtra("isDetailFrom", "1");
                it.putExtra("taskType", t.getTemplateTaskType());
                it.putExtra("isOwnTask", t.getIsOwnTask());
                mContext.startActivity(it);
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        TextView taskType;
        TextView taskName;
        TextView task_class;
        TextView task_status;
        ImageView keyView;
        ImageView img_leftmsg;

        public TaskHolder(View v) {
            super(v);
            taskType = (TextView) v.findViewById(R.id.task_type_name);
            taskName = (TextView) v.findViewById(R.id.task_name);
            task_class = (TextView) v.findViewById(R.id.task_class);
            task_status = (TextView) v.findViewById(R.id.task_status);
            keyView = (ImageView) v.findViewById(R.id.keyview);
            img_leftmsg = (ImageView) v.findViewById(R.id.img_leftmsg);
        }
    }

    class DateHolder extends RecyclerView.ViewHolder {

        TextView dateTv;

        public DateHolder(View itemView) {
            super(itemView);
            dateTv = (TextView) itemView.findViewById(R.id.task_date);
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OperateListener {
        void leaveMsg(int taskId, int position);
    }

    public void setOperateListener(OperateListener listener) {
        mOperateListener = listener;
    }
}
