package com.park61.teacherhelper.module.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.bean.StudyRecordBean;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;

import java.util.List;

public class StudyRecordListAdapter extends BaseAdapter {

    private List<StudyRecordBean> mList;
    private Context mContext;
    private OnOperateClickedLsner mOnOperateClickedLsner;

    public StudyRecordListAdapter(Context _context, List<StudyRecordBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public StudyRecordBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.study_recordlist_item, null);
            holder = new ViewHolder();
            holder.img_course = (ImageView) convertView.findViewById(R.id.img_course);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
            holder.tv_read_num = (TextView) convertView.findViewById(R.id.tv_read_num);
            holder.tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            holder.tv_cancel_collect = (TextView) convertView.findViewById(R.id.tv_cancel_collect);
            holder.root_hotgame = convertView.findViewById(R.id.root_hotgame);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StudyRecordBean courseBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_course, courseBean.getCoverUrl());
        holder.tv_title.setText(courseBean.getCourseName());
        holder.tv_teacher.setText(courseBean.getCourseAuthor());
        holder.tv_read_num.setText(courseBean.getViewNum() + "");
        /*holder.root_hotgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", courseBean.getCourseId());
                mContext.startActivity(it);
            }
        });*/
        return convertView;
    }

    class ViewHolder {
        ImageView img_course;
        TextView tv_title;
        TextView tv_teacher;
        TextView tv_read_num;
        TextView tv_top;
        TextView tv_cancel_collect;
        View root_hotgame;
    }

    public interface OnOperateClickedLsner {
        void onCancel(int pos);

        void onTop(int pos);
    }

    public void setOnOperateClickedLsner(OnOperateClickedLsner lsner) {
        this.mOnOperateClickedLsner = lsner;
    }

}
