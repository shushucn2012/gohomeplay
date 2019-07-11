package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.module.workplan.bean.CourseSeriesBean;
import com.park61.teacherhelper.module.workplan.bean.KnowledgeBean;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作任务选择课程列表adapter
 * Created by shubei on 2018/8/5.
 */

public class WorkTaskChooseFileAdapter extends BaseAdapter {

    private Context mContext;
    private List<CourseSeriesBean> sList;
    private OnCheckedListener mOnCheckedListener;

    public WorkTaskChooseFileAdapter(Context context, List<CourseSeriesBean> list) {
        this.mContext = context;
        this.sList = list;
    }

    @Override
    public int getCount() {
        return sList.size();
    }

    @Override
    public CourseSeriesBean getItem(int position) {
        return sList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.worktask_choose_file_list_item, null);
            holder = new ViewHolder();
            holder.tv_sname = (TextView) convertView.findViewById(R.id.tv_sname);
            holder.lv_course = (ListViewForScrollView) convertView.findViewById(R.id.lv_course);
            holder.lv_course.setFocusable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CourseSeriesBean item = sList.get(position);
        holder.tv_sname.setText(item.getTitle());
        if (CommonMethod.isListEmpty(item.getTrainerCourseList())) {
            holder.lv_course.setAdapter(new CourseListAdapter(new ArrayList<KnowledgeBean>()));
        } else {
            holder.lv_course.setAdapter(new CourseListAdapter(item.getTrainerCourseList()));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_sname;
        ListViewForScrollView lv_course;
    }

    class CourseListAdapter extends BaseAdapter {

        private List<KnowledgeBean> klist;

        public CourseListAdapter(List<KnowledgeBean> list) {
            this.klist = list;
        }

        @Override
        public int getCount() {
            return klist.size();
        }

        @Override
        public Object getItem(int position) {
            return klist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.scourse_list_item, null);
                holder = new WViewHolder();
                holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
                holder.tv_no = (TextView) convertView.findViewById(R.id.tv_no);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (WViewHolder) convertView.getTag();
            }
            KnowledgeBean knowledgeBean = klist.get(position);
            holder.tv_no.setText(position + 1 + "");
            holder.tv_title.setText(knowledgeBean.getTitle());
            if (knowledgeBean.isChecked()) {
                holder.img_check.setImageResource(R.mipmap.download_check);
            } else {
                holder.img_check.setImageResource(R.mipmap.download_uncheck);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (knowledgeBean.isChecked()) {
                        knowledgeBean.setChecked(false);
                    } else {
                        knowledgeBean.setChecked(true);
                    }
                    notifyDataSetChanged();
                    if (mOnCheckedListener != null)
                        mOnCheckedListener.onChecked();
                }
            });
            return convertView;
        }

        class WViewHolder {
            ImageView img_check;
            TextView tv_no, tv_title;
        }
    }

    public interface OnCheckedListener {
        void onChecked();
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.mOnCheckedListener = listener;
    }
}
