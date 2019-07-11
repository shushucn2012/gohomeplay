package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.ClazzChildRelationsActivity;
import com.park61.teacherhelper.module.clazz.bean.TeachGClassChild;
import com.park61.teacherhelper.module.clazz.bean.UserChildKinshipRelation;
import com.park61.teacherhelper.widget.dialog.DoubleDialog;
import com.park61.teacherhelper.widget.listview.MyRItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 班级管理宝贝家长列表Adapter
 * Created by zhangchi on 2017/8/18.
 */

public class ClazzParentList2Adapter extends BaseAdapter {

    private List<UserChildKinshipRelation> dataList;
    private Context mContext;
    private EditListener editListener;

    public ClazzParentList2Adapter(Context _context, List<UserChildKinshipRelation> _list) {
        mContext = _context;
        dataList = _list;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final UserChildKinshipRelation teachGClassChild = dataList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.clazz_child_list_item, null);
            holder = new ViewHolder();
            holder.avatarView = (ImageView) convertView.findViewById(R.id.avatar);
            holder.nameView = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_relations = (TextView) convertView.findViewById(R.id.tv_relations);
            holder.area_item_root = convertView.findViewById(R.id.area_item_root);
            holder.recyclerViewItem = (MyRItem) convertView.findViewById(R.id.scroll_item);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String picUrl = teachGClassChild.getPictureUrl();

        ImageManager.getInstance().displayImg(holder.avatarView, picUrl, R.mipmap.mine_icon);
        holder.nameView.setText(teachGClassChild.getName());
        //获取宝贝家长关系列表
        holder.tv_relations.setText(teachGClassChild.getUserMobile());
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editListener != null)
                    editListener.deleteTask(teachGClassChild.getKinship(), position);
            }
        });
        if (position == dataList.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        //恢复状态
        holder.recyclerViewItem.reset();
        return convertView;
    }

    class ViewHolder {
        ImageView avatarView;
        TextView nameView, tv_relations;
        MyRItem recyclerViewItem;
        TextView tv_delete;
        View area_item_root, bottom_line;
    }

    public interface EditListener {
        void deleteTask(String taskId, int position);
    }

    public void setEditListener(EditListener listener) {
        editListener = listener;
    }
}
