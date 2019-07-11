package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.module.my.InterestingClassActApplyListActivity;
import com.park61.teacherhelper.module.my.InterestingClassActDetailsActivity;
import com.park61.teacherhelper.module.my.bean.InterestingClassActBean;
import com.park61.teacherhelper.widget.pw.SharePopWin;

import java.util.List;

/**
 * 兴趣班列表adapter
 * Created by shubei on 2018/8/15
 */

public class InterestingClassActsAdapter extends BaseAdapter {

    private Context mContext;
    private List<InterestingClassActBean> datas;

    public InterestingClassActsAdapter(Context context, List<InterestingClassActBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_interestingclass_acts, null);
            holder = new ViewHolder();
            holder.img_act = (ImageView) convertView.findViewById(R.id.img_act);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.cardview_status = (CardView) convertView.findViewById(R.id.cardview_status);
            holder.btn_apply_details = (Button) convertView.findViewById(R.id.btn_apply_details);
            holder.btn_share = (Button) convertView.findViewById(R.id.btn_share);
            holder.area_img_cover = convertView.findViewById(R.id.area_img_cover);
            holder.area_content = convertView.findViewById(R.id.area_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InterestingClassActBean interestingClassActBean = datas.get(position);
        ImageManager.getInstance().displayImg(holder.img_act, interestingClassActBean.getDetailUrl());
        if (interestingClassActBean.getStatus() == 1) {
            holder.tv_status.setText("进行中");
            holder.cardview_status.setCardBackgroundColor(mContext.getResources().getColor(R.color.com_orange));
            holder.area_img_cover.setVisibility(View.GONE);
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.g333333));
            holder.area_content.setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
        } else {
            holder.tv_status.setText("已结束");
            holder.cardview_status.setCardBackgroundColor(mContext.getResources().getColor(R.color.gbbbbbb));
            holder.area_img_cover.setVisibility(View.VISIBLE);
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.g999999));
            holder.area_content.setBackgroundColor(mContext.getResources().getColor(R.color.gfbfbfb));
        }
        holder.tv_title.setText(interestingClassActBean.getTitle());

        convertView.setOnClickListener(v -> {
            Intent it = new Intent(mContext, InterestingClassActDetailsActivity.class);
            it.putExtra("id", interestingClassActBean.getId());
            mContext.startActivity(it);
        });
        holder.btn_apply_details.setOnClickListener(v -> {
            Intent it = new Intent(mContext, InterestingClassActApplyListActivity.class);
            it.putExtra("id", interestingClassActBean.getId());
            mContext.startActivity(it);
        });
        holder.btn_share.setOnClickListener(v -> {
            Intent it = new Intent(mContext, SharePopWin.class);
            it.putExtra("shareUrl", "http://m.61park.cn/#/hobby/detail/" + interestingClassActBean.getId());
            it.putExtra("picUrl", interestingClassActBean.getDetailUrl());
            it.putExtra("title", interestingClassActBean.getTitle());
            it.putExtra("description", interestingClassActBean.getSummary());
            mContext.startActivity(it);
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_act;
        TextView tv_title, tv_status;
        Button btn_apply_details, btn_share;
        CardView cardview_status;
        View area_img_cover, area_content;
    }
}
