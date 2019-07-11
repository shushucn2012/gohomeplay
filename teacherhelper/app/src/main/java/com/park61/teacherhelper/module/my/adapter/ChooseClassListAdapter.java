package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;
import com.park61.teacherhelper.module.course.bean.StudyRecordBean;

import java.util.List;

public class ChooseClassListAdapter extends BaseAdapter {

    private List<TeachGClass> mList;
    private Context mContext;

    public ChooseClassListAdapter(Context _context, List<TeachGClass> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TeachGClass getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_class_list_item, null);
            holder = new ViewHolder();
            holder.img_chk = (ImageView) convertView.findViewById(R.id.img_chk);
            holder.tv_class_name = (TextView) convertView.findViewById(R.id.tv_class_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TeachGClass teachGClass = mList.get(position);
        holder.tv_class_name.setText(teachGClass.getFullName());
        if (teachGClass.isChosen()) {
            holder.img_chk.setImageResource(R.mipmap.icon_xuanzhong_focus);
        } else {
            holder.img_chk.setImageResource(R.mipmap.icon_xuanzhong_default);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_chk;
        TextView tv_class_name;
    }

}
