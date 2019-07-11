package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.aliyun.vod.common.utils.ToastUtil;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.TempTasksActivity;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.park61.teacherhelper.module.workplan.bean.TempClassBean;

import java.util.List;

/**
 * Created by shubei on 2018/4/28.
 */

public class TempClassesAdapter extends BaseAdapter {

    private Context mContext;
    private List<TempClassBean> mlist;

    public TempClassesAdapter(Context context, List<TempClassBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public TempClassBean getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.temp_classes_item, null);
            holder = new ViewHolder();
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
            holder.tv_class_name = (TextView) convertView.findViewById(R.id.tv_class_name);
            holder.area_content = convertView.findViewById(R.id.area_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TempClassBean item = mlist.get(position);
        holder.tv_class_name.setText(item.getFullName());
        if (item.getIsAssignedTemplate() == 1) {//已分配
            holder.tv_class_name.setTextColor(mContext.getResources().getColor(R.color.g999999));
        } else {
            holder.tv_class_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }
        if (item.isChecked()) {
            holder.img_check.setVisibility(View.VISIBLE);
        } else {
            holder.img_check.setVisibility(View.GONE);
        }
        holder.area_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIsAssignedTemplate() == 1) { //已分配
                    ToastUtil.showToast(mContext, "该班级已分配");
                    return;
                }
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                    /*for (int i = 0; i < mlist.size(); i++) {
                        if (i != position)
                            mlist.get(i).setChecked(false);
                    }*/
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_check;
        TextView tv_class_name;
        View area_content;
    }

}
