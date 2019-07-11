package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.activity.bean.ServiceApplyBean;

import java.util.List;

public class ServiceApplyListAdapter extends BaseAdapter {

    private List<ServiceApplyBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ServiceApplyListAdapter(Context _context, List<ServiceApplyBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ServiceApplyBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.item_service_apply_list, null);
            holder = new ViewHolder();
            holder.tv_group_name = convertView.findViewById(R.id.tv_group_name);
            holder.tv_apply_date = convertView.findViewById(R.id.tv_apply_date);
            holder.tv_apply_state = convertView.findViewById(R.id.tv_apply_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ServiceApplyBean serviceApplyBean = mList.get(position);
        holder.tv_group_name.setText(serviceApplyBean.getGroupName());
        holder.tv_apply_date.setText(serviceApplyBean.getCreateDate());
        String stateStr = "";
        if (serviceApplyBean.getStatus() == 0) {
            stateStr = "待审核";
        } else if (serviceApplyBean.getStatus() == 1) {
            stateStr = "审核通过";
        } else if (serviceApplyBean.getStatus() == 2) {
            stateStr = "审核拒绝";
        }
        holder.tv_apply_state.setText(stateStr);
        return convertView;
    }

    class ViewHolder {
        TextView tv_group_name, tv_apply_date, tv_apply_state;
    }
}
