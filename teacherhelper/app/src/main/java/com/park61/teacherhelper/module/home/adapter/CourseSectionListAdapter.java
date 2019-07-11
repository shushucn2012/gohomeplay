package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.bean.ContentItemListBean;
import com.park61.teacherhelper.module.course.bean.ContentSourseBean;


import java.util.List;

import static com.park61.teacherhelper.R.id.img_teacher;
import static com.park61.teacherhelper.R.id.tv_teacher_name;

public class CourseSectionListAdapter extends BaseAdapter {
int pos=999;
    private List<ContentItemListBean> mList;
    static int position;
    private Context mContext;
    static ViewHolder holder ;
    public CourseSectionListAdapter(Context _context, List<ContentItemListBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ContentItemListBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        this.position=position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.coursesection_list_item, null);
            holder = new ViewHolder();
            holder.img_teacher = (ImageView) convertView.findViewById(img_teacher);
            holder.tv_teacher_name = (TextView) convertView.findViewById(tv_teacher_name);
            holder.tv_course_time = (TextView) convertView.findViewById(R.id.tv_course_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContentItemListBean item = mList.get(position);

        holder.tv_teacher_name.setText((position + 1) + "、" + item.getName());
        if(position==pos){
            holder.tv_teacher_name.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }else {
            holder.tv_teacher_name.setTextColor(mContext.getResources().getColor(R.color.bdp_dark_gray));
        }
//        if (item.isSelected()) {
//            holder.tv_teacher_name.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
//        } else {
//            holder.tv_teacher_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
//        }
        if (!CommonMethod.isListEmpty(mList.get(position).getContentSourceList())) {
            ContentSourseBean teachCourseSourceListBean = mList.get(position).getContentSourceList().get(0);
            switch (teachCourseSourceListBean.getSourceType()) {
                case 1://视频
                    holder.img_teacher.setImageResource(R.mipmap.icon_course_section);
                    holder.tv_course_time.setText(item.getContentSourceList().get(0).getSourceTime());
                    break;
                case 2://图片
                    holder.img_teacher.setImageResource(R.mipmap.icon_res_word);
                    holder.tv_course_time.setText("");//非视频资源不显示时长
                    break;
                case 3://音频
                    holder.img_teacher.setImageResource(R.mipmap.icon_course_section);
                    holder.tv_course_time.setText("");//非视频资源不显示时长
                    break;
                case 4://pdf
                    holder.img_teacher.setImageResource(R.mipmap.icon_res_word);
                    holder.tv_course_time.setText("");//非视频资源不显示时长
                    break;
            }
        }
        return convertView;
    }
    class ViewHolder {
        ImageView img_teacher;
        TextView tv_teacher_name;
        TextView tv_course_time;
    }
public void setClickPosition(int position){
    pos=position;
}

}
