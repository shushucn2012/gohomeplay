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
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.bean.CourseBean;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;

import java.util.List;

public class MyCollectCourseListAdapter extends BaseAdapter {

    private List<CourseSectionBean> mList;
    private Context mContext;
    private OnOperateClickedLsner mOnOperateClickedLsner;

    public MyCollectCourseListAdapter(Context _context, List<CourseSectionBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CourseSectionBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mycollect_courselist_item, null);
            holder = new ViewHolder();
            holder.img_course = (ImageView) convertView.findViewById(R.id.img_course);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
            holder.area_operate = convertView.findViewById(R.id.area_operate);
            holder.tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            holder.area_to_top = convertView.findViewById(R.id.area_to_top);
            holder.tv_cancel_collect = (TextView) convertView.findViewById(R.id.tv_cancel_collect);
            holder.area_cancel_collect = convertView.findViewById(R.id.area_cancel_collect);
            holder.root_hotgame = convertView.findViewById(R.id.root_hotgame);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CourseSectionBean courseBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_course, courseBean.getCoverUrl());
        holder.tv_title.setText(courseBean.getName());
        holder.tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (courseBean.isExpan()) {
                    courseBean.setExpan(false);
                } else {
                    courseBean.setExpan(true);
                }
                for (int i = 0; i < mList.size(); i++) {
                    if (i != position) {
                        mList.get(i).setExpan(false);
                    }
                }
                notifyDataSetChanged();
            }
        });
        if (courseBean.isExpan()) {
            holder.area_operate.setVisibility(View.VISIBLE);
        } else {
            holder.area_operate.setVisibility(View.GONE);
        }
        holder.tv_teacher.setText(courseBean.getCopyright());
        holder.area_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOperateClickedLsner != null)
                    mOnOperateClickedLsner.onTop(position);
            }
        });
        holder.area_cancel_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOperateClickedLsner != null)
                    mOnOperateClickedLsner.onCancel(position);
            }
        });
        holder.root_hotgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", courseBean.getTeachCourseId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_course;
        TextView tv_title;
        TextView tv_num;
        View area_operate, area_to_top, area_cancel_collect, root_hotgame;
        TextView tv_top;
        TextView tv_cancel_collect;
        TextView tv_teacher;
    }

    public interface OnOperateClickedLsner {
        void onCancel(int pos);

        void onTop(int pos);
    }

    public void setOnOperateClickedLsner(OnOperateClickedLsner lsner) {
        this.mOnOperateClickedLsner = lsner;
    }

}
