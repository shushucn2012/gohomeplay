package com.park61.teacherhelper.module.my.adapter;

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
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.course.SubCourseAudioActivity;
import com.park61.teacherhelper.module.course.SubCourseVideoActivity;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.module.my.bean.RecentlyItemBean;

import java.util.List;

/**
 * Created by chenlie on 2018/3/21.
 */

public class RecentlyCourseListAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecentlyItemBean> datas;

    public RecentlyCourseListAdapter(Context context, List<RecentlyItemBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.teach_course_item, null);
            holder = new ViewHolder();
            holder.leftIcon = (ImageView) convertView.findViewById(R.id.course_left_icon);
            holder.coverImg = (ImageView) convertView.findViewById(R.id.cover_img);
            holder.typeImg = (ImageView) convertView.findViewById(R.id.course_type_img);
            holder.title = (TextView) convertView.findViewById(R.id.course_title);
            holder.updateTime = (TextView) convertView.findViewById(R.id.course_time);
            holder.buyNum = (TextView) convertView.findViewById(R.id.course_buy_num);
            holder.money = (TextView) convertView.findViewById(R.id.course_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecentlyItemBean b = datas.get(position);
        ImageManager.getInstance().displayImg(holder.coverImg, b.getCoverImg());
        holder.updateTime.setText(b.getStudyTime());
        holder.leftIcon.setVisibility(View.GONE);

        if (b.getType() == 3) {//3音频
            holder.typeImg.setImageResource(R.mipmap.icon_listener);
        } else {
            holder.typeImg.setImageResource(R.mipmap.icon_watch);
        }

        holder.title.setText(b.getTitle());
        holder.buyNum.setText(b.getStudyNum() + "人学习");

        /*if (b.getSalePrice() == 0 || b.getChargeType() == 0) {
            holder.money.setText("免费");
        } else {
            holder.money.setText("￥" + b.getSalePrice());
        }*/

        convertView.setOnClickListener(v -> {
            if (b.getType() == 3) {//3音频
                Intent it = new Intent(mContext, SubCourseAudioActivity.class);
                it.putExtra("subCourseId", b.getTrainerCourseId());
                mContext.startActivity(it);
            } else {
                Intent it = new Intent(mContext, SubCourseVideoActivity.class);
                it.putExtra("subCourseId", b.getTrainerCourseId());
                mContext.startActivity(it);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView leftIcon, coverImg, typeImg;
        TextView title, buyNum, updateTime, money;
    }
}
