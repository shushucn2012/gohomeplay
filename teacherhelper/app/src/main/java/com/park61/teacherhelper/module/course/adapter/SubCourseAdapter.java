package com.park61.teacherhelper.module.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.bean.SubCourseListBean;

import java.util.List;

/**
 * Created by chenlie on 2018/3/23.
 *
 * 课时列表
 */

public class SubCourseAdapter extends BaseAdapter {

    private Context mContext;
    private List<SubCourseListBean> datas;
    private int type;

    public SubCourseAdapter(Context context, List<SubCourseListBean> list){
        mContext = context;
        datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if(v == null){
            holder = new ViewHolder();
            v = LayoutInflater.from(mContext).inflate(R.layout.teach_course_item, parent, false);
            holder.leftIcon = (ImageView) v.findViewById(R.id.course_left_icon);
            holder.coverImg = (RoundedImageView) v.findViewById(R.id.cover_img);
            holder.typeImg = (ImageView) v.findViewById(R.id.course_type_img);

            holder.title = (TextView) v.findViewById(R.id.course_title);
            holder.updateTime = (TextView) v.findViewById(R.id.course_time);
            holder.test_view = (TextView) v.findViewById(R.id.test_view);
            holder.buyNum = (TextView) v.findViewById(R.id.course_buy_num);
            holder.money = (TextView) v.findViewById(R.id.course_price);
            holder.bottomL = v.findViewById(R.id.bottom_line);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        SubCourseListBean b = datas.get(position);
        ImageManager.getInstance().displayImg(holder.coverImg, b.getCoverImg());
        holder.updateTime.setText(b.getStringUpdateDate());
        if(b.getIsProbation() == 1){
            holder.test_view.setVisibility(View.VISIBLE);
        }else{
            holder.test_view.setVisibility(View.GONE);
        }

        if(type == 0){
            holder.typeImg.setImageResource(R.mipmap.icon_listener);
        }else{
            holder.typeImg.setImageResource(R.mipmap.icon_watch);
        }

        holder.title.setText(b.getTitle());
        holder.buyNum.setText(b.getLearningNum()+"人学习");

        holder.leftIcon.setVisibility(View.GONE);
        holder.money.setVisibility(View.GONE);

        if(position == datas.size()-1){
            holder.bottomL.setVisibility(View.GONE);
        }else{
            holder.bottomL.setVisibility(View.VISIBLE);
        }
        return v;
    }

    class ViewHolder{
        View bottomL;
        RoundedImageView coverImg;
        ImageView leftIcon, typeImg;
        TextView title, buyNum, updateTime, money, test_view;
    }

    public void setType(int t){
        type = t;
    }
}
