package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;

import java.util.List;


public class SwitchClazzListAdapter extends BaseAdapter {

    private List<TeachGClass> dataList;
    private Context mContext;

    public SwitchClazzListAdapter(Context _context, List<TeachGClass> _list) {
        mContext = _context;
        dataList = _list;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public TeachGClass getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_switchclazzlistadapter, null, false);
            holder = new ViewHolder();
            holder.tv_class_name = convertView.findViewById(R.id.tv_class_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TeachGClass item = dataList.get(position);
        holder.tv_class_name.setText(item.getName());
        if (item.isChosen()) {
            holder.tv_class_name.setTextColor(mContext.getResources().getColor(R.color.com_orange));
        } else {
            holder.tv_class_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_class_name;
    }
}
