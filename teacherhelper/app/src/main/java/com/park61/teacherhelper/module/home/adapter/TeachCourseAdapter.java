package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;

import java.util.List;

/**
 * Created by chenlie on 2018/3/21.
 *
 */

public class TeachCourseAdapter extends RecyclerView.Adapter<TeachCourseAdapter.CourseHolder> {

    private Context mContext;
    private List<TeacherCourseBean> datas;

    public TeachCourseAdapter(Context context, List<TeacherCourseBean> list){
        mContext = context;
        datas = list;
    }

    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.teach_course_item, parent, false);
        return new CourseHolder(v);
    }

    @Override
    public void onBindViewHolder(CourseHolder holder, int position) {
        TeacherCourseBean b = datas.get(position);

        ImageManager.getInstance().displayImg(holder.coverImg, b.getListCover());
        if(b.getType() == 1){
            //系列课
            holder.updateTime.setText("系列课 已更新至"+b.getCourseTotal()+" | 共"+b.getCourseNum()+"课时");
            holder.leftIcon.setVisibility(View.VISIBLE);
        }else{
            holder.updateTime.setText(b.getUpdateTime());
            holder.leftIcon.setVisibility(View.GONE);
        }

        if(b.getForm() == 0){
            holder.typeImg.setImageResource(R.mipmap.icon_listener);
        }else{
            holder.typeImg.setImageResource(R.mipmap.icon_watch);
        }

        holder.title.setText(b.getTitle());
        holder.buyNum.setText(b.getLearningNum()+"人学习");

        if(b.getSalePrice()==0 || b.getChargeType() == 0){
            holder.money.setText("免费");
        }else{
            holder.money.setText("￥"+b.getSalePrice());
        }

        if(position == datas.size() -1){
            holder.bl.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            //点击课程，跳转课程详情页，课时列表
            Intent it = new Intent(mContext, ExpertCourseListActivity.class);
            it.putExtra("courseId", b.getId());
            it.putExtra("fromTeachHouse", true);
            mContext.startActivity(it);
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class CourseHolder extends RecyclerView.ViewHolder{

        View bl;
        ImageView leftIcon, coverImg, typeImg;
        TextView title, buyNum, updateTime, money;

        public CourseHolder(View v) {
            super(v);
            leftIcon = (ImageView) v.findViewById(R.id.course_left_icon);
            coverImg = (ImageView) v.findViewById(R.id.cover_img);
            typeImg = (ImageView) v.findViewById(R.id.course_type_img);

            title = (TextView) v.findViewById(R.id.course_title);
            updateTime = (TextView) v.findViewById(R.id.course_time);
            buyNum = (TextView) v.findViewById(R.id.course_buy_num);
            money = (TextView) v.findViewById(R.id.course_price);
            bl = v.findViewById(R.id.bottom_line);
        }
    }
}
