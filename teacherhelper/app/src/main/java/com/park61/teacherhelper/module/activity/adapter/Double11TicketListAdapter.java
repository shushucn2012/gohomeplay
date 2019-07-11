package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.activity.bean.Double11TicketBean;
import com.park61.teacherhelper.module.home.bean.EvaluateItemInfo;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;

import java.util.List;

/**
 * Created by shubei on 2017/6/12.
 */

public class Double11TicketListAdapter extends BaseAdapter {

    private List<Double11TicketBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public Double11TicketListAdapter(Context _context, List<Double11TicketBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Double11TicketBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.double11_ticketlist_item, null);
            holder = new ViewHolder();
            holder.area_top_space = convertView.findViewById(R.id.area_top_space);
            holder.img_qrcode = (ImageView) convertView.findViewById(R.id.img_qrcode);
            holder.img_state = (ImageView) convertView.findViewById(R.id.img_state);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.area_top_space.setVisibility(View.VISIBLE);
        } else {
            holder.area_top_space.setVisibility(View.GONE);
        }

        Double11TicketBean mDouble11TicketBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_qrcode, mDouble11TicketBean.getQrCodeUrl());
        holder.tv_name.setText(mDouble11TicketBean.getName());
        holder.tv_addr.setText(mDouble11TicketBean.getAddress());
        holder.tv_time.setText(mDouble11TicketBean.getStartTime().substring(0, 10).replace("-", ".") + "-" + mDouble11TicketBean.getEndTime().substring(0, 10).replace("-", "."));

        if (mDouble11TicketBean.getStatusName() == 0) { //待付款
            holder.img_state.setImageResource(R.mipmap.icon_state_daifukuan);
        } else if (mDouble11TicketBean.getStatusName() == 1) {//待签到
            holder.img_state.setImageResource(R.mipmap.icon_state_yifukuan);
        } else if (mDouble11TicketBean.getStatusName() == 2) {//已签到
            holder.img_state.setImageResource(R.mipmap.icon_state_yiqiandao);
        } else if (mDouble11TicketBean.getStatusName() == 3) {//未付款已取消
            holder.img_state.setImageResource(R.mipmap.icon_state_yiquxiao);
        } else if (mDouble11TicketBean.getStatusName() == 4) {//已付款已过期
            holder.img_state.setImageResource(R.mipmap.icon_state_yiguoqi);
        } else if (mDouble11TicketBean.getStatusName() == 5) {//已退款成功已取消
            holder.img_state.setImageResource(R.mipmap.icon_state_yiquxiao);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_qrcode, img_state;
        TextView tv_name, tv_addr, tv_time, tv_comt_content;
        View item_area, area_commt_content, area_top_space;
        ImageView img_face;
    }

}
