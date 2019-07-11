package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.module.workplan.CalendarPlanMainActivity;
import com.park61.teacherhelper.module.workplan.bean.TaskDoneInfoBean;
import com.park61.teacherhelper.widget.listview.MyRItem;
import com.park61.teacherhelper.widget.progress.RoundProgressBar;

import java.util.List;

/**
 * Created by shubei on 2018/4/28.
 */

public class TTaskDoneInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<TaskDoneInfoBean> mlist;
    private OnNoticeClickedListener mOnNoticeClickedListener;

    public TTaskDoneInfoAdapter(Context context, List<TaskDoneInfoBean> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ttask_doneinfos_item, null);
            holder = new ViewHolder();
            holder.space_top = (Space) convertView.findViewById(R.id.space_top);
            holder.roundProgressBar1 = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar1);
            holder.roundProgressBar2 = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar2);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_tempname = (TextView) convertView.findViewById(R.id.tv_tempname);
            holder.tv_delay_num = (TextView) convertView.findViewById(R.id.tv_delay_num);
            holder.tv_delay_desc = (TextView) convertView.findViewById(R.id.tv_delay_desc);
            holder.btn_notice = (Button) convertView.findViewById(R.id.btn_notice);
            holder.area_title = convertView.findViewById(R.id.area_title);
            holder.img_right_arrow = (ImageView) convertView.findViewById(R.id.img_right_arrow);
            holder.img_right_setting = (ImageView) convertView.findViewById(R.id.img_right_setting);
            holder.recyclerViewItem = (MyRItem) convertView.findViewById(R.id.scroll_item);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskDoneInfoBean taskDoneInfoBean = mlist.get(position);
        holder.tv_name.setText(taskDoneInfoBean.getTeachGroupName() + taskDoneInfoBean.getTeachClassName());
        holder.tv_tempname.setText(taskDoneInfoBean.getTaskCalendarTemplateName());
        if (GlobalParam.isTempManager) {//园中校显示
            holder.btn_notice.setVisibility(View.VISIBLE);
            holder.img_right_arrow.setVisibility(View.VISIBLE);
            holder.img_right_setting.setVisibility(View.GONE);
        } else {//个人统计页面不显示督办按钮
            holder.btn_notice.setVisibility(View.GONE);
            holder.img_right_arrow.setVisibility(View.GONE);
            if (GlobalParam.isGroupManager)//普通幼儿园用户不能进入,避免混乱
                holder.img_right_setting.setVisibility(View.VISIBLE);
            else
                holder.img_right_setting.setVisibility(View.GONE);
        }

        holder.roundProgressBar1.setProgress((int) (taskDoneInfoBean.getTaskCompletionRate() * 100));
        if (taskDoneInfoBean.getTaskOverdueNum() > 0) {//有逾期
            holder.roundProgressBar2.setProgress(100);
            if (taskDoneInfoBean.getIsInformDirector() == 0) {//并且没有通知过
                holder.btn_notice.setBackgroundResource(R.drawable.rec_deepred_stroke_deepred_solid_corner30);
                holder.btn_notice.setTextColor(mContext.getResources().getColor(R.color.gffffff));
                holder.btn_notice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnNoticeClickedListener.onClicked(position);
                        //点击之后置灰
                        holder.btn_notice.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
                        holder.btn_notice.setTextColor(mContext.getResources().getColor(R.color.g666666));
                        holder.btn_notice.setOnClickListener(null);
                    }
                });
            } else {
                holder.btn_notice.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
                holder.btn_notice.setTextColor(mContext.getResources().getColor(R.color.g666666));
                holder.btn_notice.setOnClickListener(null);
            }
        } else {
            holder.roundProgressBar2.setProgress(0);
            holder.btn_notice.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
            holder.btn_notice.setTextColor(mContext.getResources().getColor(R.color.g666666));
            holder.btn_notice.setOnClickListener(null);
        }
        holder.tv_delay_num.setText(taskDoneInfoBean.getTaskOverdueNum() + "");
        //holder.tv_delay_desc.setText("逾期工作" + taskDoneInfoBean.getTaskOverdueNum() + "件");
        /*if (position != 0) {
            holder.space_top.setVisibility(View.GONE);
        }*/
        holder.area_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNoticeClickedListener != null)
                    mOnNoticeClickedListener.onTitle(position);
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNoticeClickedListener != null)
                    mOnNoticeClickedListener.deleteTask(taskDoneInfoBean.getId(), position);
            }
        });
        if (!GlobalParam.isTempManager) {//非园中校用户点击去日历总览
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!GlobalParam.isGroupManager)//普通幼儿园用户不能进入,避免混乱
                        return;
                    Intent it = new Intent(mContext, CalendarPlanMainActivity.class);
                    it.putExtra("taskCalendarClassId", mlist.get(position).getId());
                    GlobalParam.taskCalendarClassId = mlist.get(position).getId();
                    mContext.startActivity(it);
                }
            });
        } else {//园中校用户点击去日历总览
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalParam.teachClassId = mlist.get(position).getTeachClassId();
                    Intent it = new Intent(mContext, CalendarPlanMainActivity.class);
                    it.putExtra("taskCalendarClassId", mlist.get(position).getId());
                    GlobalParam.taskCalendarClassId = mlist.get(position).getId();
                    mContext.startActivity(it);
                }
            });
        }
        //恢复状态
        holder.recyclerViewItem.reset();
        return convertView;
    }

    class ViewHolder {
        Space space_top, space_bottom;
        RoundProgressBar roundProgressBar1, roundProgressBar2;
        TextView tv_name, tv_tempname, tv_delay_num, tv_delay_desc;
        Button btn_notice;
        View area_title;
        ImageView img_right_setting, img_right_arrow;
        MyRItem recyclerViewItem;
        TextView tv_delete;
    }

    public interface OnNoticeClickedListener {
        void onClicked(int position);

        void onTitle(int position);

        void deleteTask(int id, int position);
    }

    public void setOnNoticeClickedListener(OnNoticeClickedListener listener) {
        this.mOnNoticeClickedListener = listener;
    }
}
