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
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.bean.TeachGClassTeacher;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;
import com.park61.teacherhelper.widget.dialog.DoubleDialog;

import java.util.List;

/**
 * 班级管理教师列表Adapter
 * Created by zhangchi on 2017/8/18.
 */

public class ClazzTeacherListAdapter extends BaseAdapter {

    private List<TeachGClassTeacher> dataList;
    private Context mContext;

    public ClazzTeacherListAdapter(Context _context, List<TeachGClassTeacher> _list) {
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

        TeachGClassTeacher teachGClassTeacher = dataList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.clazz_teacher_list_item, null);
            holder = new ViewHolder();
            holder.avatarView   = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.nameView     = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mobileView   = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.lay2         = (RelativeLayout) convertView.findViewById(R.id.lay_2);
            holder.imgCallView  = (ImageView) convertView.findViewById(R.id.img_call_view);

            //如果是自己，隐藏电话图标
            if ("自己".equals(teachGClassTeacher.getName())) {
                holder.imgCallView.setVisibility(View.INVISIBLE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String picUrl = teachGClassTeacher.getPictureUrl();

        ImageManager.getInstance().displayImg(holder.avatarView, picUrl, R.mipmap.mine_icon);
        holder.nameView.setText(teachGClassTeacher.getName());
        holder.mobileView.setText(String.valueOf(teachGClassTeacher.getUserMobile()));


        //初始化点击事件
        //如果是自己，禁用电话呼出
        if (!"自己".equals(teachGClassTeacher.getName())) {
            holder.lay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取对应的电话号码
                    TextView textView = (TextView) view.findViewById(R.id.tv_mobile);
                    final String phoneReal = (String) textView.getText();
                    //直接调用系统服务拨打电话
//                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNum));
//                    mContext.startActivity(intent);



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
        }



        return convertView;
    }

    class ViewHolder {
        CircleImageView avatarView;
        TextView nameView;
        TextView mobileView;
        RelativeLayout lay2;
        ImageView imgCallView;
    }
}
