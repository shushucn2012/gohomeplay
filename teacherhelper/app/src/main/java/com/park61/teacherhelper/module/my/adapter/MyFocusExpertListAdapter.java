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
import com.park61.teacherhelper.module.home.TeachHouseActivity;
import com.park61.teacherhelper.module.my.bean.MyfocusExpertBean;

import java.util.List;

public class MyFocusExpertListAdapter extends BaseAdapter {

    private List<MyfocusExpertBean> mList;
    private Context mContext;
    private OnFocusClickedLsner mOnFocusClickedLsner;

    public MyFocusExpertListAdapter(Context _context, List<MyfocusExpertBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MyfocusExpertBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_funslist_item, null);
            holder = new ViewHolder();
            holder.img_expert = (ImageView) convertView.findViewById(R.id.img_expert);
            holder.tv_expert_name = (TextView) convertView.findViewById(R.id.tv_expert_name);
            holder.tv_funs_num = (TextView) convertView.findViewById(R.id.tv_funs_num);
            holder.tv_expert_focus = (TextView) convertView.findViewById(R.id.tv_expert_focus);
            holder.area_content = convertView.findViewById(R.id.area_content);
            holder.area_expert_flag = convertView.findViewById(R.id.area_expert_flag);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MyfocusExpertBean expertBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_expert, expertBean.getHeadPictureUrl());
        holder.tv_expert_name.setText(expertBean.getName());
        holder.tv_funs_num.setText(expertBean.getDescription());
        if (position == mList.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        holder.tv_expert_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFocusClickedLsner.onFocus(position);
            }
        });
        holder.area_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TeachHouseActivity.class);
                it.putExtra("teachId", expertBean.getId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_expert;
        TextView tv_expert_name;
        TextView tv_funs_num;
        TextView tv_expert_focus;
        View area_content, area_expert_flag;
        View bottom_line;
    }

    public interface OnFocusClickedLsner {
        void onFocus(int position);
    }

    public void setOnFocusClickedLsner(OnFocusClickedLsner lsner) {
        this.mOnFocusClickedLsner = lsner;
    }

}
