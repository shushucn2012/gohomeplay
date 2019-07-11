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
import com.park61.teacherhelper.module.workplan.WorkDetailActivity;
import com.park61.teacherhelper.module.workplan.WorkResponseActivity;
import com.park61.teacherhelper.module.workplan.bean.CalendarTask;
import com.park61.teacherhelper.widget.listview.MyRItem;

import java.util.List;

/**
 * Created by super on 2018/6/30.
 * 管理员日历界面当月的任务
 */

public class AdminCalendarTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CalendarTask> datas;
    private Context mContext;
    private EditListener editListener;

    public AdminCalendarTaskAdapter(List<CalendarTask> list, Context context) {
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
        if (viewType == 0) {//日期类
            v = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_date, parent, false);
            holder = new DateHolder(v);
        } else if (viewType == 2) {//空白
            v = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_empty, parent, false);
            holder = new EmptyHolder(v);
        } else {//正常
            v = LayoutInflater.from(mContext).inflate(R.layout.item_admin_calendar_task, parent, false);
            holder = new TaskHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CalendarTask t = datas.get(position);
        if (holder instanceof DateHolder) {
            ((DateHolder) holder).dateTv.setText("");//头部空白或显示日期
        } else if (holder instanceof TaskHolder) {
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
            th.taskStatus.setText(t.getTaskStatusDescription() + "    " + t.getTaskExecutorsDescription());
            th.area_content.setOnClickListener(v -> {
                Intent it = new Intent(mContext, WorkDetailActivity.class);
                it.putExtra("taskCalendarId", t.getId());
                it.putExtra("isDetailFrom", "1");
                it.putExtra("taskType", t.getTemplateTaskType());
                it.putExtra("isOwnTask", t.getIsOwnTask());
                mContext.startActivity(it);
            });
            th.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editListener != null)
                        editListener.editTask(t.getId(), position);
                }
            });
            th.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (th.tv_delete.getText().equals("删除")) {
                        th.tv_delete.setText("确认" + "\n" + "删除");
                    } else {
                        if (editListener != null)
                            editListener.deleteTask(t.getId(), position);
                    }
                }
            });
            th.recyclerViewItem.setOnResetListener(new MyRItem.OnResetListener() {
                @Override
                public void onReset() {
                    th.tv_delete.setText("删除");
                }
            });
            //恢复状态
            th.recyclerViewItem.reset();
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        TextView taskType;
        TextView taskName;
        TextView taskStatus;
        //TextView tv_edit;
        TextView tv_delete;
        TextView task_class;
        ImageView keyView, img_edit;
        MyRItem recyclerViewItem;
        View area_content;

        public TaskHolder(View v) {
            super(v);
            recyclerViewItem = (MyRItem) v.findViewById(R.id.scroll_item);
            taskType = (TextView) v.findViewById(R.id.task_type_name);
            taskName = (TextView) v.findViewById(R.id.task_name);
            taskStatus = (TextView) v.findViewById(R.id.task_status);
            keyView = (ImageView) v.findViewById(R.id.keyview);
            img_edit = (ImageView) v.findViewById(R.id.img_edit);
            //tv_edit = (TextView) v.findViewById(R.id.tv_edit);
            tv_delete = (TextView) v.findViewById(R.id.tv_delete);
            area_content = v.findViewById(R.id.area_content);
            task_class = (TextView) v.findViewById(R.id.task_class);
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

    public interface EditListener {
        void deleteTask(int taskId, int position);

        void editTask(int taskId, int position);
    }

    public void setEditListener(EditListener listener) {
        editListener = listener;
    }
}
