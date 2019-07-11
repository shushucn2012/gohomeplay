package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.bean.CourseCombine;

import java.util.List;

public class CourseCombineListAdapter extends BaseAdapter {

    private List<CourseCombine> mList;
    private Context mContext;
    private LayoutInflater factory;

    public CourseCombineListAdapter(Context _context, List<CourseCombine> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CourseCombine getItem(int position) {
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
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.course_combine_list_item, null);
            holder.left_area_root = convertView.findViewById(R.id.left_area_root);
            holder.img_goods_left = (ImageView) convertView.findViewById(R.id.img_goods_left);
            holder.tv_goods_name_left = (TextView) convertView.findViewById(R.id.tv_goods_name_left);
            holder.tv_teacher_left = (TextView) convertView.findViewById(R.id.tv_teacher_left);
            holder.tv_read_num_left = (TextView) convertView.findViewById(R.id.tv_read_num_left);

            holder.right_area_root = convertView.findViewById(R.id.right_area_root);
            holder.img_goods_right = (ImageView) convertView.findViewById(R.id.img_goods_right);
            holder.tv_goods_name_right = (TextView) convertView.findViewById(R.id.tv_goods_name_right);
            holder.tv_teacher_right = (TextView) convertView.findViewById(R.id.tv_teacher_right);
            holder.tv_read_num_right = (TextView) convertView.findViewById(R.id.tv_read_num_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CourseCombine comb = mList.get(position);
        holder.tv_goods_name_left.setText(comb.getCourseBeanLeft().getName());
        holder.tv_teacher_left.setText(comb.getCourseBeanLeft().getCopyright());
        holder.tv_read_num_left.setText(TextUtils.isEmpty(comb.getCourseBeanLeft().getViewNum()) ? "0" : comb.getCourseBeanLeft().getViewNum());
        ImageManager.getInstance().displayImg(holder.img_goods_left, comb.getCourseBeanLeft().getCoverUrl());
        holder.left_area_root.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", comb.getCourseBeanLeft().getId());
                mContext.startActivity(it);
            }
        });
        if (comb.getCourseBeanRight() != null) {
            holder.right_area_root.setVisibility(View.VISIBLE);

            holder.tv_goods_name_right.setText(comb.getCourseBeanRight().getName());
            holder.tv_teacher_right.setText(comb.getCourseBeanRight().getCopyright());
            holder.tv_read_num_right.setText(TextUtils.isEmpty(comb.getCourseBeanRight().getViewNum()) ? "0" : comb.getCourseBeanRight().getViewNum());
            ImageManager.getInstance().displayImg(holder.img_goods_right, comb.getCourseBeanRight().getCoverUrl());
            holder.right_area_root.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, CourseDetailsActivity.class);
                    it.putExtra("coursePlanId", comb.getCourseBeanRight().getId());
                    mContext.startActivity(it);
                }
            });
        } else {
            holder.right_area_root.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_goods_left, img_goods_right;
        TextView tv_goods_name_left, tv_goods_name_right;
        TextView tv_teacher_left, tv_teacher_right;
        TextView tv_read_num_left, tv_read_num_right;
        View left_area_root, right_area_root;
    }

}
