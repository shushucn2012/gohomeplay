package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.module.home.bean.TeachCourseItemListBean;
import com.park61.teacherhelper.module.home.bean.TeachCourseSourceListBean;

import java.util.List;

public class CourseResourceListAdapter extends BaseAdapter {

    private List<TeachCourseSourceListBean> mList;
    private Context mContext;

    public CourseResourceListAdapter(Context _context, List<TeachCourseSourceListBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TeachCourseSourceListBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.coursesection_list_item, null);
            holder = new ViewHolder();
            holder.img_teacher = (ImageView) convertView.findViewById(R.id.img_teacher);
            holder.tv_teacher_name = (TextView) convertView.findViewById(R.id.tv_teacher_name);
            holder.tv_course_time = (TextView) convertView.findViewById(R.id.tv_course_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TeachCourseSourceListBean item = mList.get(position);
        holder.tv_teacher_name.setText((position + 1) + "、" + item.getSourceName());
        if (item.isSelected()) {
            holder.tv_teacher_name.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        } else {
            holder.tv_teacher_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }
        holder.tv_course_time.setText(item.getUserViewNum() + "次浏览");
        TeachCourseSourceListBean teachCourseSourceListBean = mList.get(position);
        switch (teachCourseSourceListBean.getSourceType()) {
            case 1://视频
                holder.img_teacher.setImageResource(R.mipmap.icon_course_section);
                break;
            case 2://图片
                holder.img_teacher.setImageResource(R.mipmap.icon_res_word);
                break;
            case 3://音频
                holder.img_teacher.setImageResource(R.mipmap.icon_course_section);
                break;
            case 4://pdf
                holder.img_teacher.setImageResource(R.mipmap.icon_res_word);
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_teacher;
        TextView tv_teacher_name;
        TextView tv_course_time;
    }

}
