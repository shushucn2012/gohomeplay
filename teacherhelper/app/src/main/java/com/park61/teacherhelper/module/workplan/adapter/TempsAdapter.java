package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.TempTasksActivity;
import com.park61.teacherhelper.module.workplan.bean.TempBean;

import java.util.List;

/**
 * Created by shubei on 2018/4/28.
 */

public class TempsAdapter extends BaseAdapter {

    private Context mContext;
    private List<TempBean> mlist;
    private OnAllcateClickedListener mOnAllcateClickedListener;

    public TempsAdapter(Context context, List<TempBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public TempBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.temps_item, null);
            holder = new ViewHolder();
            holder.space_top = (Space) convertView.findViewById(R.id.space_top);
            holder.area_content = convertView.findViewById(R.id.area_content);
            holder.btn_allocate = (Button) convertView.findViewById(R.id.btn_allocate);
            holder.tv_tempname = (TextView) convertView.findViewById(R.id.tv_tempname);
            holder.tv_tasknum = (TextView) convertView.findViewById(R.id.tv_tasknum);
            holder.tv_allocate_num = (TextView) convertView.findViewById(R.id.tv_allocate_num);
            holder.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TempBean item = mlist.get(position);
        holder.tv_tempname.setText(item.getTitle());
        holder.tv_tasknum.setText(item.getTaskCount() + "");
        holder.tv_allocate_num.setText(item.getClassCount() + "");
        if (!TextUtils.isEmpty(item.getStartDate())) {
            holder.tv_datetime.setText(item.getStartDate() + "-" + item.getEndDate());
        } else {
            holder.tv_datetime.setText("");
        }

        if (position == 0) {
            holder.space_top.setVisibility(View.VISIBLE);
        } else {
            holder.space_top.setVisibility(View.GONE);
        }

        holder.area_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TempTasksActivity.class);
                it.putExtra("tempId", mlist.get(position).getId());
                it.putExtra("tempName", mlist.get(position).getTitle());
                mContext.startActivity(it);
            }
        });
        holder.btn_allocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAllcateClickedListener.onClicked(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        Space space_top;
        View area_content;
        Button btn_allocate;
        TextView tv_tempname, tv_tasknum, tv_allocate_num, tv_datetime;
    }

    public interface OnAllcateClickedListener {
        void onClicked(int position);
    }

    public void setOnAllcateClickedListener(OnAllcateClickedListener listener) {
        this.mOnAllcateClickedListener = listener;
    }
}
