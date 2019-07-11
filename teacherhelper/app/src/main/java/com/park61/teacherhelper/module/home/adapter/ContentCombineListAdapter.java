package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.content.Intent;
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

public class ContentCombineListAdapter extends BaseAdapter {

    private List<CourseCombine> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ContentCombineListAdapter(Context _context, List<CourseCombine> _list) {
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
            convertView = factory.inflate(R.layout.content_combine_list_item, null);
            holder.area_top_space = convertView.findViewById(R.id.area_top_space);
            holder.area_bottom_space = convertView.findViewById(R.id.area_bottom_space);

            holder.left_area_root = convertView.findViewById(R.id.left_area_root);
            holder.img_goods_left = (ImageView) convertView.findViewById(R.id.img_goods_left);
            holder.img_can_play_left = (ImageView) convertView.findViewById(R.id.img_can_play_left);
            holder.img_teacher_left = (ImageView) convertView.findViewById(R.id.img_teacher_left);
            holder.tv_goods_name_left = (TextView) convertView.findViewById(R.id.tv_goods_name_left);
            holder.tv_teacher_left = (TextView) convertView.findViewById(R.id.tv_teacher_left);
            holder.tv_read_num_left = (TextView) convertView.findViewById(R.id.tv_read_num_left);
            holder.img_vip_left = convertView.findViewById(R.id.img_vip_left);

            holder.right_area_root = convertView.findViewById(R.id.right_area_root);
            holder.img_goods_right = (ImageView) convertView.findViewById(R.id.img_goods_right);
            holder.img_teacher_right = (ImageView) convertView.findViewById(R.id.img_teacher_right);
            holder.img_can_play_right = (ImageView) convertView.findViewById(R.id.img_can_play_right);
            holder.tv_goods_name_right = (TextView) convertView.findViewById(R.id.tv_goods_name_right);
            holder.tv_teacher_right = (TextView) convertView.findViewById(R.id.tv_teacher_right);
            holder.tv_read_num_right = (TextView) convertView.findViewById(R.id.tv_read_num_right);
            holder.img_vip_right = convertView.findViewById(R.id.img_vip_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.area_top_space.setVisibility(View.VISIBLE);
        } else {
            holder.area_top_space.setVisibility(View.GONE);
        }


        final CourseCombine comb = mList.get(position);
        holder.tv_goods_name_left.setText(comb.getCourseBeanLeft().getTitle());
        holder.tv_teacher_left.setText(comb.getCourseBeanLeft().getAuthorName());
        holder.tv_read_num_left.setText(comb.getCourseBeanLeft().getPraiseNum() + "");
        ImageManager.getInstance().displayImg(holder.img_goods_left, comb.getCourseBeanLeft().getCoverImg());
        ImageManager.getInstance().displayImg(holder.img_teacher_left, comb.getCourseBeanLeft().getAuthorPic(), R.mipmap.headimg_default_img);
        holder.left_area_root.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", comb.getCourseBeanLeft().getId());
                mContext.startActivity(it);
            }
        });
        if (comb.getCourseBeanLeft().getContentType() == 1) {//视频
            holder.img_can_play_left.setVisibility(View.VISIBLE);
        } else {//图文，音频
            holder.img_can_play_left.setVisibility(View.GONE);
        }
        if (comb.getCourseBeanLeft().getIsTag() == 1) {//显示vip标签
            holder.img_vip_left.setVisibility(View.VISIBLE);
        } else {
            holder.img_vip_left.setVisibility(View.GONE);
        }

        if (comb.getCourseBeanRight() != null) {
            holder.right_area_root.setVisibility(View.VISIBLE);

            holder.tv_goods_name_right.setText(comb.getCourseBeanRight().getTitle());
            holder.tv_teacher_right.setText(comb.getCourseBeanRight().getAuthorName());
            holder.tv_read_num_right.setText(comb.getCourseBeanRight().getPraiseNum() + "");
            ImageManager.getInstance().displayImg(holder.img_goods_right, comb.getCourseBeanRight().getCoverImg());
            ImageManager.getInstance().displayImg(holder.img_teacher_right, comb.getCourseBeanRight().getAuthorPic(), R.mipmap.headimg_default_img);
            holder.right_area_root.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, CourseDetailsActivity.class);
                    it.putExtra("coursePlanId", comb.getCourseBeanRight().getId());
                    mContext.startActivity(it);
                }
            });
            if (comb.getCourseBeanRight().getContentType() == 1) {//视频
                holder.img_can_play_right.setVisibility(View.VISIBLE);
            } else {//图文，音频
                holder.img_can_play_right.setVisibility(View.GONE);
            }
            if (comb.getCourseBeanRight().getIsTag() == 1) {//显示vip标签
                holder.img_vip_right.setVisibility(View.VISIBLE);
            } else {
                holder.img_vip_right.setVisibility(View.GONE);
            }
        } else {
            holder.right_area_root.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_goods_left, img_goods_right, img_teacher_left, img_teacher_right, img_can_play_left, img_can_play_right, img_vip_left, img_vip_right;
        TextView tv_goods_name_left, tv_goods_name_right;
        TextView tv_teacher_left, tv_teacher_right;
        TextView tv_read_num_left, tv_read_num_right;
        View left_area_root, right_area_root, area_top_space, area_bottom_space;
    }

}
