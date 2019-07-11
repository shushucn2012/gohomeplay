package com.park61.teacherhelper.module.course.adapter;

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
import com.park61.teacherhelper.module.home.ExpertListActivity;
import com.park61.teacherhelper.module.pay.bean.CouponBean;

import java.util.List;

public class CouponChooseGetListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater factory;
    private List<CouponBean> mlist;

    public CouponChooseGetListAdapter(Context context, List<CouponBean> list) {
        this.mContext = context;
        this.mlist = list;
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
            convertView = factory.inflate(R.layout.coupon_choose_get_item, null);
            holder = new ViewHolder();
            holder.amount_tv = (TextView) convertView.findViewById(R.id.amount_tv);
            holder.tv_coupon_title = (TextView) convertView.findViewById(R.id.tv_coupon_title);
            holder.limittime_tv = (TextView) convertView.findViewById(R.id.limittime_tv);
            holder.tv_got = (TextView) convertView.findViewById(R.id.tv_got);
            holder.tv_get = (TextView) convertView.findViewById(R.id.tv_get);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CouponBean item = mlist.get(position);
        holder.amount_tv.setText(item.getRuleValue2());
        holder.tv_coupon_title.setText(item.getTitle());
        holder.limittime_tv.setText("使用期限：" + item.getLimitStart() + " - " + item.getLimitEnd());
        if(item.getCouponReceiveStatus() == 0){//优惠券领取状态(0:立即领取 1:已领完)
            holder.tv_got.setVisibility(View.GONE);
            holder.tv_get.setVisibility(View.VISIBLE);
        }else {
            holder.tv_got.setVisibility(View.VISIBLE);
            holder.tv_get.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView amount_tv, tv_coupon_title, limittime_tv, tv_get, tv_got;
    }

}
