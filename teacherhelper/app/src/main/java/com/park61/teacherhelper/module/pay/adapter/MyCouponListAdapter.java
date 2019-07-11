package com.park61.teacherhelper.module.pay.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.course.ExpertCourseMainActivity;
import com.park61.teacherhelper.module.home.ExpertListActivity;
import com.park61.teacherhelper.module.pay.bean.CouponBean;

import java.util.List;

public class MyCouponListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater factory;
    private List<CouponBean> mlist;
    private int where;//0:choose;1:my
    private int type;//choose：0-可用；1不可用；//my：0-未用，1-已用，2-失效

    public MyCouponListAdapter(Context context, List<CouponBean> list, int where, int type) {
        this.mContext = context;
        this.mlist = list;
        this.where = where;
        this.type = type;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.coupon_listview_item, null);
            holder = new ViewHolder();
            holder.unit_tv = (TextView) convertView.findViewById(R.id.unit_tv);
            holder.amount_tv = (TextView) convertView.findViewById(R.id.amount_tv);
            holder.tv_coupon_title = (TextView) convertView.findViewById(R.id.tv_coupon_title);
            holder.limittime_tv = (TextView) convertView.findViewById(R.id.limittime_tv);
            holder.img_used = (ImageView) convertView.findViewById(R.id.img_used);
            holder.img_past = (ImageView) convertView.findViewById(R.id.img_past);
            holder.area_expand = convertView.findViewById(R.id.area_expand);
            holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            holder.area_remark = convertView.findViewById(R.id.area_remark);
            holder.img_expand = (ImageView) convertView.findViewById(R.id.img_expand);
            holder.img_chk = (ImageView) convertView.findViewById(R.id.img_chk);
            holder.top_space = convertView.findViewById(R.id.top_space);
            holder.img_goto_use = (ImageView) convertView.findViewById(R.id.img_goto_use);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CouponBean item = mlist.get(position);
        holder.amount_tv.setText(item.getShowPrice());
        holder.tv_coupon_title.setText(item.getTitle());
        holder.limittime_tv.setText(item.getShowTime());
        holder.tv_remark.setText(item.getRemarks());
        ViewHolder finalHolder = holder;
        holder.area_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.area_remark.getVisibility() == View.GONE) {//备注隐藏，点击显示
                    //旋转动画：围绕x轴旋转
                    ObjectAnimator animator = ObjectAnimator.ofFloat(finalHolder.img_expand, "rotationX", 0, 180);
                    animator.setDuration(500).start();
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            finalHolder.area_remark.setVisibility(View.VISIBLE);
                        }
                    });
                } else {//备注显示，点击隐藏
                    //旋转动画：围绕x轴旋转
                    ObjectAnimator animator = ObjectAnimator.ofFloat(finalHolder.img_expand, "rotationX", 180, 0);
                    animator.setDuration(500).start();
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            finalHolder.area_remark.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        if (where == 0) {//choose
            if (type == 1) {//1不可用
                holder.img_chk.setVisibility(View.GONE);
                holder.unit_tv.setTextColor(mContext.getResources().getColor(R.color.g8b8b8b));
                holder.amount_tv.setTextColor(mContext.getResources().getColor(R.color.g8b8b8b));
            } else {//0可用
                holder.img_chk.setVisibility(View.VISIBLE);
                holder.unit_tv.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
                holder.amount_tv.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
                if (item.getCheckCoupon() == 1) {
                    holder.img_chk.setImageResource(R.mipmap.xuanze_focus);
                } else {
                    holder.img_chk.setImageResource(R.mipmap.xuanze_default2);
                }
            }
        } else {//my
            holder.img_chk.setVisibility(View.GONE);
            if (type == 0) {
                holder.unit_tv.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
                holder.amount_tv.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
                holder.img_goto_use.setVisibility(View.VISIBLE);
                holder.img_used.setVisibility(View.GONE);
                holder.img_past.setVisibility(View.GONE);
            } else if (type == 1) {
                holder.unit_tv.setTextColor(mContext.getResources().getColor(R.color.g8b8b8b));
                holder.amount_tv.setTextColor(mContext.getResources().getColor(R.color.g8b8b8b));
                holder.img_goto_use.setVisibility(View.GONE);
                holder.img_used.setVisibility(View.VISIBLE);
                holder.img_past.setVisibility(View.GONE);
            } else {
                holder.unit_tv.setTextColor(mContext.getResources().getColor(R.color.g8b8b8b));
                holder.amount_tv.setTextColor(mContext.getResources().getColor(R.color.g8b8b8b));
                holder.img_goto_use.setVisibility(View.GONE);
                holder.img_used.setVisibility(View.GONE);
                holder.img_past.setVisibility(View.VISIBLE);
            }
        }
        if (position == 0) {
            holder.top_space.setVisibility(View.VISIBLE);
        } else {
            holder.top_space.setVisibility(View.GONE);
        }

        if ("9".equals(item.getUseRange())) {//9是集训优惠券 不显示立即使用按钮
            holder.img_goto_use.setVisibility(View.GONE);
        }

        holder.img_goto_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abilityIt = new Intent(mContext, ExpertCourseMainActivity.class);
                abilityIt.putExtra("couponId", item.getCouponId());
                mContext.startActivity(abilityIt);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView amount_tv, tv_coupon_title, limittime_tv, tv_remark, unit_tv;
        ImageView img_used, img_past, img_expand, img_chk, img_goto_use;
        View area_expand, area_remark, top_space;
    }

}
