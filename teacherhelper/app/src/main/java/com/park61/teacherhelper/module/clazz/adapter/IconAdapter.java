package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.bean.IconBean;

import java.util.List;

/**
 * Created by chenlie on 2018/5/16.
 */

public class IconAdapter extends BaseAdapter {

    private Context mContext;
    private List<IconBean> datas;

    public IconAdapter(Context c, List<IconBean> list) {
        mContext = c;
        datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public IconBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconHolder holder = null;
        if (convertView == null) {
            holder = new IconHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.park_grid_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.item_img);
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        } else {
            holder = (IconHolder) convertView.getTag();
        }

        IconBean b = datas.get(position);
        if (position == 0) {
            holder.img.setImageResource(R.mipmap.icon_baseinfo);
            holder.name.setText("基础信息");
        } else {
            if (!TextUtils.isEmpty(b.getImgUrl()))
                ImageManager.getInstance().displayImg(holder.img, b.getImgUrl());
            if (!TextUtils.isEmpty(b.getName()))
                holder.name.setText(b.getName());
        }
        return convertView;
    }

    class IconHolder {
        ImageView img;
        TextView name;
    }
}
