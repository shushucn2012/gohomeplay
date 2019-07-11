package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.SubCourseAudioActivity;
import com.park61.teacherhelper.module.course.SubCourseVideoActivity;
import com.park61.teacherhelper.module.my.InterestingClassActApplyDetailsActivity;
import com.park61.teacherhelper.module.my.InterestingClassActApplyListActivity;
import com.park61.teacherhelper.module.my.bean.InterestingCourseBean;
import com.park61.teacherhelper.module.workplan.bean.KnowledgeBean;

import java.util.List;

/**
 * 兴趣课活动课程列表
 * Created by super on 20180815
 */

public class InterestingCrouseListAdapter extends BaseAdapter {

    public static class FROM_PAGE {
        public static final String ACTDETAILS_PAGE = "ActDetails";
        public static final String APPLYLIST_PAGE = "ApplyList";
    }

    private List<InterestingCourseBean> datas;
    private Context mContext;
    private String from;//在哪里显示，不同的效果，ActDetails-活动详情；ApplyList-报名列表

    public InterestingCrouseListAdapter(Context context, List<InterestingCourseBean> list, String from) {
        mContext = context;
        datas = list;
        this.from = from;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public InterestingCourseBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_interestingclass_courses, parent, false);
            holder = new ViewHolder();
            holder.img_course = (ImageView) convertView.findViewById(R.id.img_course);
            holder.img_renqi = (ImageView) convertView.findViewById(R.id.img_renqi);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_old_price = (TextView) convertView.findViewById(R.id.tv_old_price);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InterestingCourseBean interestingCourseBean = datas.get(position);

        ImageManager.getInstance().displayImg(holder.img_course, interestingCourseBean.getDetailUrl());
        holder.tv_title.setText(interestingCourseBean.getName());
        if (FROM_PAGE.ACTDETAILS_PAGE.equals(from)) {
            holder.tv_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(interestingCourseBean.getSalePrice()));
            holder.tv_old_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(interestingCourseBean.getOriginalPrice()));
            ViewInitTool.lineText(holder.tv_old_price);
            holder.img_renqi.setVisibility(View.VISIBLE);
        } else if (FROM_PAGE.APPLYLIST_PAGE.equals(from)) {
            holder.tv_price.setVisibility(View.GONE);
            holder.tv_old_price.setText(interestingCourseBean.getBuyNum() + "人购买");
            holder.img_renqi.setVisibility(View.GONE);
            convertView.setOnClickListener(v -> {
                Intent it = new Intent(mContext, InterestingClassActApplyDetailsActivity.class);
                it.putExtra("interestClassActivityId", interestingCourseBean.getActId());
                it.putExtra("interestClassId", interestingCourseBean.getId());
                it.putExtra("classActName", interestingCourseBean.getName());
                mContext.startActivity(it);
            });
        }
        if (position == datas.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView img_course, img_renqi;
        private TextView tv_title, tv_price, tv_old_price;
        private View bottom_line;
    }

}
