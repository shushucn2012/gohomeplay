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
import com.park61.teacherhelper.module.clazz.bean.UserChildKinshipRelation;
import com.park61.teacherhelper.widget.dialog.DoubleDialog;

import java.util.List;

/**
 * 班级管理宝贝家长列表Adapter
 * Created by zhangchi on 2017/8/18.
 */

public class ClazzParentListAdapter extends BaseAdapter {

    private List<UserChildKinshipRelation> dataList;
    private Context mContext;

    public ClazzParentListAdapter(Context _context, List<UserChildKinshipRelation> _list) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.clazz_parent_list_item, null);
            holder = new ViewHolder();
            //holder.avatarView   = (RoundCornerImageView) convertView.findViewById(R.id.avatar);
            holder.titleView    = (TextView) convertView.findViewById(R.id.tv_parent_title);
            holder.nameView     = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mobileView   = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.lay2         = (RelativeLayout) convertView.findViewById(R.id.lay_2);
            holder.callView     = (ImageView) convertView.findViewById(R.id.img_call_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserChildKinshipRelation userChildKinshipRelation = dataList.get(position);

        //String picUrl = userChildKinshipRelation.getPictureUrl();
        //ImageManager.getInstance().displayImg(holder.avatarView, picUrl, R.mipmap.img_default_avatar);
        holder.titleView.setText(userChildKinshipRelation.getRelationConstantName());
        holder.nameView.setText(userChildKinshipRelation.getName());
        holder.mobileView.setText(String.valueOf(userChildKinshipRelation.getUserMobile()));

        //初始化点击事件
        holder.lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取对应的电话号码
                TextView textView = (TextView) view.findViewById(R.id.tv_mobile);
                final String phoneReal = (String) textView.getText();
                //直接调用系统服务拨打电话
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNum));
//                mContext.startActivity(intent);

                final DoubleDialog dDialog = new DoubleDialog(mContext);

                dDialog.showDialog("提示", "确认拨打" + phoneReal + "?", "取消", "确认", null, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneReal));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        dDialog.dismissDialog();
                    }
                });


            }
        });

        return convertView;
    }

    class ViewHolder {
        //RoundCornerImageView avatarView;
        TextView titleView;
        TextView nameView;
        TextView mobileView;
        RelativeLayout lay2;
        ImageView callView;
    }
}
