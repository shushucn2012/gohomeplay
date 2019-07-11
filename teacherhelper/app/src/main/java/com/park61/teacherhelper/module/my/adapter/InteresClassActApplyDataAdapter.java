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
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.my.InterestingClassActApplyListActivity;
import com.park61.teacherhelper.module.my.InterestingClassActDetailsActivity;
import com.park61.teacherhelper.module.my.bean.InterestClassActApplyDataItem;
import com.park61.teacherhelper.module.my.bean.InterestingClassActBean;
import com.park61.teacherhelper.widget.pw.SharePopWin;

import java.util.List;

/**
 * 兴趣班报名数据列表详情
 * Created by shubei on 2018/8/16
 */

public class InteresClassActApplyDataAdapter extends BaseAdapter {

    private Context mContext;
    private List<InterestClassActApplyDataItem> datas;

    public InteresClassActApplyDataAdapter(Context context, List<InterestClassActApplyDataItem> list) {
        mContext = context;
        datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public InterestClassActApplyDataItem getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_interestingclassact_applydata, null);
            holder = new ViewHolder();
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_pay_money = (TextView) convertView.findViewById(R.id.tv_pay_money);
            holder.tv_group_class_name = (TextView) convertView.findViewById(R.id.tv_group_class_name);
            holder.tv_mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InterestClassActApplyDataItem interestingClassActBean = datas.get(position);
        holder.tv_username.setText(interestingClassActBean.getChildName());
        holder.tv_pay_money.setText(interestingClassActBean.getOrderAmount() + "元");
        holder.tv_group_class_name.setText(interestingClassActBean.getClassName());
        holder.tv_mobile.setText(interestingClassActBean.getMobile());
        holder.tv_time.setText(interestingClassActBean.getCreateDate());
        if (position == datas.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_username, tv_pay_money, tv_group_class_name, tv_mobile, tv_time;
        View bottom_line;
    }
}
