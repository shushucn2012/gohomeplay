package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.activity.bean.GardenProperty;

import java.util.List;

public class GardenPropertyAdapter extends BaseAdapter {

    private List<GardenProperty> mList;
    private Context mContext;

    public GardenPropertyAdapter(Context _context, List<GardenProperty> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GardenProperty getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_garden_property, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GardenProperty gardenProperty = mList.get(position);
        holder.tv_name.setText(gardenProperty.getName());
        if (gardenProperty.isChecked()) {
            holder.tv_name.setBackgroundResource(R.drawable.shape_grant_button);
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.gffffff));
        } else {
            holder.tv_name.setBackgroundResource(R.drawable.rec_999_stroke_trans_solid_corner30);
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.g999999));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
    }

    /**
     * 获取所选item的id，多个逗号拼接
     */
    public String getSelectedItemIds() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isChecked()) {
                sb.append(mList.get(i).getId());
                sb.append(",");
            }
        }
        String str = sb.toString();
        if (!TextUtils.isEmpty(str)) {
            str = str.substring(0, sb.length() - 1);
        }
        return str;
    }
}