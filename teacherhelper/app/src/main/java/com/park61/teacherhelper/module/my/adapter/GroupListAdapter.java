package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.my.bean.GroupBean;
import com.park61.teacherhelper.module.my.bean.PublishItem;
import com.park61.teacherhelper.module.my.bean.PublishNoticeItem;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.List;

public class GroupListAdapter extends BaseAdapter {

    private List<GroupBean> mList;
    private Context mContext;

    public GroupListAdapter(Context _context, List<GroupBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GroupBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, null);
            holder = new ViewHolder();
            holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            holder.tv_group_addr = (TextView) convertView.findViewById(R.id.tv_group_addr);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupBean groupBean = mList.get(position);
        holder.tv_group_name.setText(groupBean.getSchoolName());
        holder.tv_group_addr.setText("地址：" + groupBean.getAddress());
        return convertView;
    }

    class ViewHolder {
        TextView tv_group_name;
        TextView tv_group_addr;
    }

}
