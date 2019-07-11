package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.bean.StudentBean;

import java.util.List;

public class FamilySeeAllStuListAdapter extends BaseAdapter {

    private List<StudentBean> mList;
    private Context mContext;

    public FamilySeeAllStuListAdapter(Context _context, List<StudentBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public StudentBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.familyconbook_seeall_stus_item, null);
            holder = new ViewHolder();
            holder.tv_stu_name = convertView.findViewById(R.id.tv_stu_name);
            holder.img_header = convertView.findViewById(R.id.img_header);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StudentBean roleItem = mList.get(position);
        holder.tv_stu_name.setText(roleItem.getName());
        ImageManager.getInstance().displayImg(holder.img_header, roleItem.getPictureUrl(), R.mipmap.headimg_default_img);
        return convertView;
    }

    class ViewHolder {
        TextView tv_stu_name;
        ImageView img_header;
    }

}
