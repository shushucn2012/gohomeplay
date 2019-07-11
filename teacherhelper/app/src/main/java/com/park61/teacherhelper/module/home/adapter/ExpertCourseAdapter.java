package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.ExpertCourseBean;

import java.util.List;

public class ExpertCourseAdapter extends BaseAdapter {

    private List<ExpertCourseBean> mList;
    private Context mContext;

    public ExpertCourseAdapter(Context context, List<ExpertCourseBean> list) {
        mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ExpertCourseBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expert_course_item, null);
            holder = new ViewHolder();
            holder.root_hotgame = convertView.findViewById(R.id.root_hotgame);
            holder.img_teacher = (RoundedImageView) convertView.findViewById(R.id.img_teacher);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_lable = (TextView) convertView.findViewById(R.id.tv_lable);
            holder.tv_tip = (TextView) convertView.findViewById(R.id.tv_tip);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            holder.tv_popularity = (TextView) convertView.findViewById(R.id.tv_popularity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExpertCourseBean teacher = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_teacher, teacher.getHeadPictureUrl());
        holder.tv_title.setText(teacher.getTitle());
        holder.tv_lable.setText(teacher.getTrainerName() + " | " + teacher.getSubhead());
        holder.tv_tip.setText(teacher.getDigest());
        holder.tv_price.setText(FU.zeroToMF(teacher.getSalePrice()));
        holder.tv_popularity.setText(teacher.getLearningNum() + "");
        if (position == mList.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        RoundedImageView img_teacher;
        TextView tv_title, tv_tip, tv_lable, tv_price, tv_popularity;
        View root_hotgame, bottom_line;
    }

}
